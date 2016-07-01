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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
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
public class DebtThirdDetailActivity extends BaseFragmentActivity implements OnChartValueSelectedListener {
    OBParse mParse = new OBParse();

    TextView mIndexTextView;

    public int mIndex;
    public double mMonthlyRepayment;
    ArrayList<Double> mRemainingDebt;
    ArrayList<Double> mMonthlyInterest;

//    private CombinedChart mChart;
    LineChart mChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt_third_detail);

        TextView titleTextView = (TextView) findViewById(R.id.appbarTitle);
        titleTextView.setText(R.string.monthly_schedule);

        Intent intent = getIntent();

        mIndex = intent.getExtras().getInt("index");
        mMonthlyRepayment = intent.getExtras().getDouble("monthlyRepayment");
        mRemainingDebt = (ArrayList<Double>) intent.getSerializableExtra("remainingDebt");
        mMonthlyInterest = (ArrayList<Double>) intent.getSerializableExtra("monthlyInterest");

        mIndexTextView = (TextView) findViewById(R.id.indexTextView);

//        mChart = (CombinedChart) findViewById(R.id.chart);
        mChart = (LineChart) findViewById(R.id.chart);
//        mChart.setDrawGridBackground(false);
//        mChart.setDrawBarShadow(false);
        mChart.setTouchEnabled(true);
        mChart.setOnChartValueSelectedListener(this);
        setListView(0);
        setChart();
    }

    public void indexOnClick(View view) {
        final CharSequence[] items = new CharSequence[mIndex];
        for(int index = 0; index < mIndex; index++) {
            items[index] = index + 1 + "회차";
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.index_selection);
        dialog.setItems(items,new DialogInterface.OnClickListener() {
            // 리스트 선택 시 이벤트
            public void onClick(DialogInterface dialog, int id) {
                mIndexTextView.setText(items[id]);
                setListView(id);
            }
        });
        dialog.show();
    }

    public Map<String,?> createItem(String title, String contents) {
        Map<String,String> item = new HashMap<>();
        item.put(FTConstants.ITEM_TITLE, title);
        item.put(FTConstants.ITEM_CONTENTS, contents);
        return item;
    }

    public void setListView(int index) {
        int id = index + 1;
        String payment = mParse.addComma(mMonthlyRepayment) + "원";
        String principalPayment = mParse.addComma(mMonthlyRepayment - mMonthlyInterest.get(index)) + "원";
        String interest = mParse.addComma(mMonthlyInterest.get(index)) + "원";
        String principalSum = mParse.addComma(mMonthlyRepayment * id - sumOfInterest(mMonthlyInterest, id)) + "원";
        String remainingDebt = mParse.addComma(mRemainingDebt.get(index)) + "원";

        List<Map<String,?>> resultList = new LinkedList<>();
        resultList.add(createItem("상환금", payment));
        resultList.add(createItem("납입원금", principalPayment));
        resultList.add(createItem("이자", interest));
        resultList.add(createItem("납입원금합계", principalSum));
        resultList.add(createItem("잔금", remainingDebt));

        // create our list and custom adapter
        DebtDetailListViewAdapter adapter = new DebtDetailListViewAdapter(this);

        String[] from = { FTConstants.ITEM_TITLE, FTConstants.ITEM_CONTENTS };
        int[] to = new int[] {R.id.debtDetailTitle, R.id.debtDetailContents};

        adapter.addSection(id + " 회차", new SimpleAdapter(this, resultList,
                R.layout.listview_debt_detail_row, from, to));

        ListView list = (ListView) findViewById(R.id.debtDetailListView);
        list.setAdapter(adapter);
    }

    public static double sumOfInterest(List<Double> doubles, int id) {
        double sum = 0;
        for(int index = 0; index < id; index++){
            sum += doubles.get(index);
        }
        return sum;
    }


    public void setChart() {
        ArrayList<Entry> interestEntries = new ArrayList<>();
        ArrayList<Entry> principalEntries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        for (int index = 0; index < mIndex; index++) {
            interestEntries.add(new Entry(mMonthlyInterest.get(index).floatValue(), index));
            principalEntries.add(new Entry((float) (mMonthlyRepayment - mMonthlyInterest.get(index)),index));
            labels.add(index + 1 + "");
        }

        LineDataSet interestDataSet = new LineDataSet(interestEntries, "이자");
        interestDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        LineDataSet principalDataSet = new LineDataSet(principalEntries, "원금");
        principalDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        interestDataSet.setColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        interestDataSet.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        interestDataSet.setFillColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        interestDataSet.setDrawFilled(true); //선아래로 색상표시
        interestDataSet.setDrawValues(false); //숫자표시
        interestDataSet.setDrawCircles(false); //항목에 원
        principalDataSet.setColor(ColorTemplate.VORDIPLOM_COLORS[4]);
        principalDataSet.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[4]);
        principalDataSet.setFillColor(ColorTemplate.VORDIPLOM_COLORS[4]);
        principalDataSet.setDrawFilled(true);
        principalDataSet.setDrawValues(false);
        principalDataSet.setDrawCircles(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);

        YAxis left = mChart.getAxisLeft();
        left.setDrawLabels(true); // no axis labels
        left.setDrawAxisLine(false); // no axis line
        left.setDrawGridLines(false); // no grid lines
        left.setDrawZeroLine(true); // draw a zero line
        mChart.getAxisRight().setEnabled(false); // no right axis

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(interestDataSet);
        dataSets.add(principalDataSet);

        LineData data = new LineData(labels, dataSets);

        mChart.setData(data); // set the data and list of labels into chart
        mChart.setDescription("");

        DebtChartMarkerView mv = new DebtChartMarkerView (this, R.layout.custom_marker_view_layout);
        mChart.setMarkerView(mv);
    }

    @Override
    public void onNothingSelected(){
        // do stuff
    }
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h){
        Log.i("I clicked on", String.valueOf(e.getXIndex()) );
        int id = (int) e.getXIndex();
        mIndexTextView.setText(id + 1 + "회차");
        setListView(id);
    }
}