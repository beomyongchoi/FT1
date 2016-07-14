package kr.co.fintalk.fintalkone.ui.calculator.debt.detail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
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

    private LineChart mChart;
    private ProgressBar mProgressBar;

    public int mColorInterest;
    public int mColorPrincipal;
    public int mColorBalance;

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

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setMax(mIndex - 1);

        mChart = (LineChart) findViewById(R.id.chart);
        mChart.setDescription("");
        mChart.setDrawGridBackground(false);
        mChart.setTouchEnabled(true);
        mChart.setScaleEnabled(false);
        mChart.setOnChartValueSelectedListener(this);

        mColorInterest = ContextCompat.getColor(this, R.color.chartInterest);
        mColorPrincipal = ContextCompat.getColor(this, R.color.chartPrincipal);
        mColorBalance = ContextCompat.getColor(this, R.color.chartBalance);

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
        ArrayList<Entry> interestEntries = new ArrayList<>();
        ArrayList<Entry> principalEntries = new ArrayList<>();
        ArrayList<Entry> remainingDebtEntries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        for (int index = 0; index < mIndex; index++) {
            interestEntries.add(new Entry(mMonthlyInterest.get(index).floatValue(), index));
            principalEntries.add(new Entry((float) (mMonthlyRepayment),index));
            remainingDebtEntries.add(new Entry((float) (mMonthlyRepayment * (mIndex - index - 1)),index));
            labels.add(index + 1 + "");
        }

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)
        rightAxis.setValueFormatter(new DebtYAxisValueFormatter());
        rightAxis.setShowOnlyMinMax(true);
        rightAxis.setYOffset(5f);
        rightAxis.setTextColor(Color.GRAY);
        rightAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMaxValue((float) ((mMonthlyRepayment > mMonthlyInterest.get(0)
                ? mMonthlyRepayment : mMonthlyInterest.get(0)) * 1.5));
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)
        leftAxis.setValueFormatter(new DebtYAxisValueFormatter());
        leftAxis.setShowOnlyMinMax(true);
        leftAxis.setYOffset(5f);
        leftAxis.setTextColor(Color.GRAY);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);
        xAxis.setDrawAxisLine(false);

        LineDataSet interestDataSet = new LineDataSet(interestEntries, "이자");
        interestDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        LineDataSet principalDataSet = new LineDataSet(principalEntries, "원금");
        principalDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        LineDataSet remainingDebtDataSet = new LineDataSet(remainingDebtEntries, "잔금");
        remainingDebtDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);

        interestDataSet.setColor(mColorInterest);
        interestDataSet.setDrawCircles(false);
        interestDataSet.setDrawValues(false);
        interestDataSet.setHighLightColor(mColorInterest);
        interestDataSet.setDrawHorizontalHighlightIndicator(false);

        principalDataSet.setColor(mColorPrincipal);
        principalDataSet.setDrawCircles(false);
        principalDataSet.setDrawValues(false);
        principalDataSet.setHighLightColor(mColorPrincipal);
        principalDataSet.setDrawHorizontalHighlightIndicator(false);

        remainingDebtDataSet.setColor(mColorBalance);
        remainingDebtDataSet.setDrawCircles(false);
        remainingDebtDataSet.setDrawValues(false);
        remainingDebtDataSet.setHighLightColor(mColorBalance);
        remainingDebtDataSet.setDrawHorizontalHighlightIndicator(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(remainingDebtDataSet);
        dataSets.add(principalDataSet);
        dataSets.add(interestDataSet);

        LineData data = new LineData(labels, dataSets);

        TextView leftYAxisLabelTextView = (TextView) findViewById(R.id.leftYAxisTextView);
        TextView rightYAxisLabelTextView = (TextView) findViewById(R.id.rightYAxisTextView);

        String leftYAxisLabel = "원금+이자";
        String rightYAxisLabel = "잔금";
        SpannableStringBuilder leftBuilder = new SpannableStringBuilder(leftYAxisLabel);
        leftBuilder.setSpan(new ForegroundColorSpan(mColorPrincipal), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        leftBuilder.setSpan(new ForegroundColorSpan(mColorInterest), 3, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableStringBuilder rightBuilder = new SpannableStringBuilder(rightYAxisLabel);
        rightBuilder.setSpan(new ForegroundColorSpan(mColorBalance), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        leftYAxisLabelTextView.append(leftBuilder);
        rightYAxisLabelTextView.append(rightBuilder);

        mChart.setData(data); // set the data and list of labels into chart
        mChart.getLegend().setEnabled(false);
        mChart.invalidate();

        DebtChartMarkerView mv = new DebtChartMarkerView (this, R.layout.text_marker_view_layout);
        mChart.setMarkerView(mv);
    }

    @Override
    public void onNothingSelected(){
        mProgressBar.setProgress(0);
    }
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h){
        Drawable tempDrawable;
        int id = (int) e.getXIndex();
        mIndexTextView.setText(id + 1 + "회차");
        setListView(id);
        switch (dataSetIndex) {
            case 0:
                tempDrawable = ContextCompat.getDrawable(DebtSecondDetailActivity.this, R.drawable.progress_chart_balance);
                break;
            case 1:
                tempDrawable = ContextCompat.getDrawable(DebtSecondDetailActivity.this, R.drawable.progress_chart_principal);
                break;
            case 2:
                tempDrawable = ContextCompat.getDrawable(DebtSecondDetailActivity.this, R.drawable.progress_chart_interest);
                break;
            default:
                tempDrawable = ContextCompat.getDrawable(DebtSecondDetailActivity.this, R.drawable.progress_chart_principal);
                break;
        }
        mProgressBar.setProgressDrawable(tempDrawable);
        mProgressBar.setProgress(id);
    }
}