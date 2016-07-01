package kr.co.fintalk.fintalkone.ui.calculator.debt.detail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.oooobang.library.OBParse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kr.co.fintalk.fintalkone.R;
import kr.co.fintalk.fintalkone.common.BaseFragmentActivity;
import kr.co.fintalk.fintalkone.common.FTConstants;

/**
 * Created by BeomyongChoi on 6/29/16
 */
public class DebtSecondDetailActivity extends BaseFragmentActivity implements OnChartValueSelectedListener {
    OBParse mParse = new OBParse();

    TextView mIndexTextView;

    public int mIndex;
    public double mMonthlyRepayment;
    ArrayList<Double> mMonthlyInterest;

    private CombinedChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt_second_detail);

        TextView titleTextView = (TextView) findViewById(R.id.appbarTitle);
        titleTextView.setText(R.string.monthly_schedule);

        Intent intent = getIntent();

        mIndex = intent.getExtras().getInt("index");
        mMonthlyRepayment = intent.getExtras().getDouble("monthlyRepayment");
        mMonthlyInterest = (ArrayList<Double>) intent.getSerializableExtra("monthlyInterest");

        mIndexTextView = (TextView) findViewById(R.id.indexTextView);

        mChart = (CombinedChart) findViewById(R.id.chart);
        mChart.setDescription("");
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setTouchEnabled(true);
        mChart.setOnChartValueSelectedListener(this);

        setListView(0);
        setChart();
    }

    public void indexOnClick(View view) {
        final CharSequence[] items = new CharSequence[mIndex];
        for (int index = 0; index < mIndex; index++) {
            items[index] = index + 1 + "회차";
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.index_selection);
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            // 리스트 선택 시 이벤트
            public void onClick(DialogInterface dialog, int id) {
                mIndexTextView.setText(items[id]);
                setListView(id);
            }
        });
        dialog.show();
    }

    public Map<String, ?> createItem(String title, String contents) {
        Map<String, String> item = new HashMap<>();
        item.put(FTConstants.ITEM_TITLE, title);
        item.put(FTConstants.ITEM_CONTENTS, contents);
        return item;
    }

    public void setListView(int index) {
        int id = index + 1;
        String payment = mParse.addComma(mMonthlyRepayment + mMonthlyInterest.get(index)) + "원";
        String principalPayment = mParse.addComma(mMonthlyRepayment) + "원";
        String interest = mParse.addComma(mMonthlyInterest.get(index)) + "원";
        String principalSum = mParse.addComma(mMonthlyRepayment * id) + "원";
        String remainingDebt = mParse.addComma(mMonthlyRepayment * (mIndex - id)) + "원";

        List<Map<String, ?>> resultList = new LinkedList<>();
        resultList.add(createItem("상환금", payment));
        resultList.add(createItem("납입원금", principalPayment));
        resultList.add(createItem("이자", interest));
        resultList.add(createItem("납입원금합계", principalSum));
        resultList.add(createItem("잔금", remainingDebt));

        // create our list and custom adapter
        DebtDetailListViewAdapter adapter = new DebtDetailListViewAdapter(this);

        String[] from = {FTConstants.ITEM_TITLE, FTConstants.ITEM_CONTENTS};
        int[] to = new int[]{R.id.debtDetailTitle, R.id.debtDetailContents};

        adapter.addSection(id + " 회차", new SimpleAdapter(this, resultList,
                R.layout.listview_debt_detail_row, from, to));

        ListView list = (ListView) findViewById(R.id.debtDetailListView);
        list.setAdapter(adapter);
    }

    public void setChart() {
        ArrayList<BarEntry> interestEntries = new ArrayList<>();
        ArrayList<BarEntry> principalEntries = new ArrayList<>();
        ArrayList<Entry> remainingDebtEntries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        for (int index = 0; index < mIndex; index++) {
            interestEntries.add(new BarEntry(mMonthlyInterest.get(index).floatValue(), index));
            principalEntries.add(new BarEntry((float) (mMonthlyRepayment),index));
            remainingDebtEntries.add(new Entry((float) (mMonthlyRepayment * (mIndex - index - 1)),index));
            labels.add(index + 1 + "");
        }

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)
        rightAxis.setValueFormatter(new DebtYAxisValueFormatter());

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMaxValue((float) ((mMonthlyRepayment > mMonthlyInterest.get(0)
                ? mMonthlyRepayment : mMonthlyInterest.get(0)) * 1.5));
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)
        leftAxis.setValueFormatter(new DebtYAxisValueFormatter());

        XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        BarDataSet interestDataSet = new BarDataSet(interestEntries, "이자");
        interestDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        BarDataSet principalDataSet = new BarDataSet(principalEntries, "원금");
        principalDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        LineDataSet remainingDebtDataSet = new LineDataSet(remainingDebtEntries, "잔금");
        remainingDebtDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);

        interestDataSet.setColor(ColorTemplate.VORDIPLOM_COLORS[4]);
        interestDataSet.setDrawValues(false); //숫자표시

        principalDataSet.setColor(ColorTemplate.VORDIPLOM_COLORS[3]);
        principalDataSet.setDrawValues(false);


        remainingDebtDataSet.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        remainingDebtDataSet.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        remainingDebtDataSet.setFillColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        remainingDebtDataSet.setDrawFilled(true);
        remainingDebtDataSet.setDrawCircles(false);
        remainingDebtDataSet.setDrawValues(false);

//        LineDataSet interestDataSet = new LineDataSet(interestEntries, "이자");
//        interestDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
//        LineDataSet principalDataSet = new LineDataSet(principalEntries, "원금");
//        principalDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
//        BarDataSet remainingDebtDataSet = new BarDataSet(remainingDebtEntries, "잔금");
//        remainingDebtDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
//
//        interestDataSet.setColor(ColorTemplate.VORDIPLOM_COLORS[4]);
//        interestDataSet.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[4]);
//        interestDataSet.setFillColor(ColorTemplate.VORDIPLOM_COLORS[4]);
//        interestDataSet.setDrawFilled(true); //선아래로 색상표시
//        interestDataSet.setDrawValues(false); //숫자표시
//        interestDataSet.setDrawCircles(false); //항목에 원
//
//        principalDataSet.setColor(ColorTemplate.VORDIPLOM_COLORS[3]);
//        principalDataSet.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[3]);
//        principalDataSet.setFillColor(ColorTemplate.VORDIPLOM_COLORS[3]);
//        principalDataSet.setDrawFilled(true);
//        principalDataSet.setDrawValues(false);
//        principalDataSet.setDrawCircles(false);
//
//        remainingDebtDataSet.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
//        remainingDebtDataSet.setDrawValues(false);

        mChart.setDrawOrder(new CombinedChart.DrawOrder[] {
                CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.BAR });

        LineData lineData = new LineData();

//        lineData.addDataSet(interestDataSet);
//        lineData.addDataSet(principalDataSet);
        lineData.addDataSet(remainingDebtDataSet);

        BarData barData = new BarData();

        barData.addDataSet(interestDataSet);
        barData.addDataSet(principalDataSet);
//        barData.addDataSet(remainingDebtDataSet);

        CombinedData data = new CombinedData(labels);

        data.setData(lineData);
        data.setData(barData);

        mChart.setData(data); // set the data and list of labels into chart

        DebtChartMarkerView mv = new DebtChartMarkerView (this, R.layout.custom_marker_view_layout);
        mChart.setMarkerView(mv);
    }

    @Override
    public void onNothingSelected(){
        // do stuff
    }
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h){
        int id = (int) e.getXIndex();
        mIndexTextView.setText(id + 1 + "회차");
        setListView(id);
    }
}