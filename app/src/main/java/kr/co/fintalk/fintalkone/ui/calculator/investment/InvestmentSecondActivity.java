package kr.co.fintalk.fintalkone.ui.calculator.investment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.oooobang.library.OBEditText;
import com.oooobang.library.OBParse;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kr.co.fintalk.fintalkone.R;
import kr.co.fintalk.fintalkone.common.BaseFragmentActivity;
import kr.co.fintalk.fintalkone.common.FTConstants;

/**
 * Created by BeomyongChoi on 6/28/16
 */
public class InvestmentSecondActivity extends BaseFragmentActivity {
    OBParse mParse = new OBParse();

    OBEditText mMonthlyPaymentEditText;
    OBEditText mGoalPeriodEditText;
    OBEditText mReturnRateEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment_second);

        TextView titleTextView = (TextView) findViewById(R.id.appbarTitle);
        titleTextView.setText(R.string.investment_goal_amount);

        mMonthlyPaymentEditText = (OBEditText) findViewById(R.id.investmentGoalAmountEditText);
        mGoalPeriodEditText = (OBEditText) findViewById(R.id.investmentGoalPeriodEditText);
        mReturnRateEditText = (OBEditText) findViewById(R.id.investmentReturnRateEditText);
    }

    public void calculateInvestmentOnClick(View view) {
        String goalAmountString = mMonthlyPaymentEditText.getText().toString();
        String predictedPeriodString = mGoalPeriodEditText.getText().toString();
        String returnRateString = mReturnRateEditText.getText().toString();

        double goalAmount = mParse.toDouble(goalAmountString.replace("원","").replace(",",""));
        double predictedPeriod = mParse.toDouble(predictedPeriodString.replace("개월",""));
        double returnRate = mParse.toDouble(returnRateString.replace("%",""));

        double monthlyPayment;
        double resultPrincipal;
        double resultReturn;

        if(goalAmountString.length() != 0
                && predictedPeriodString.length() != 0
                && returnRateString.length() != 0) {
            monthlyPayment = calculatorMonthlyPayment(goalAmount, predictedPeriod, returnRate);
            resultReturn = calculatorInterest(monthlyPayment, predictedPeriod, returnRate);
            resultPrincipal = monthlyPayment * predictedPeriod;
            setListView(monthlyPayment, resultPrincipal, resultReturn);
        }
        else {
            showToast(R.string.toast_text, 2);
        }
    }

    public Map<String,?> createItem(String title, String contents) {
        Map<String,String> item = new HashMap<>();
        item.put(FTConstants.ITEM_TITLE, title);
        item.put(FTConstants.ITEM_CONTENTS, contents);
        return item;
    }

    public void setListView(double monthlyPayment, double principal, double resultReturn) {
        String monthlyPaymentString = mParse.addComma(monthlyPayment) + "원";
        String principalString = mParse.addComma(principal) + "원";
        String resultReturnString = mParse.addComma(resultReturn) + "원";

        List<Map<String,?>> resultList = new LinkedList<>();
        resultList.add(createItem("월 납입액", monthlyPaymentString));
        resultList.add(createItem("총 납입액", principalString));
        resultList.add(createItem("총 수익액", resultReturnString));

        // create our list and custom adapter
        InvestmentListViewAdapter adapter = new InvestmentListViewAdapter(this);

        String[] from = { FTConstants.ITEM_TITLE, FTConstants.ITEM_CONTENTS };
        int[] to = new int[] {R.id.investmentResultTitle, R.id.investmentResultContents};

        adapter.addSection("계산결과", new SimpleAdapter(this, resultList,
                R.layout.listview_investment_row, from, to));

        ListView list = (ListView) findViewById(R.id.investmentResultListView);
        list.setAdapter(adapter);
    }

    public double calculatorMonthlyPayment(double goalAmount, double period, double yearlyRate) {
        yearlyRate /= 100;

        double yearlyPeriod = (period + 1) / 12;
        double yearlyRoot = 1 + yearlyRate;
        double yearlyConstant = Math.pow(yearlyRoot, 1.0 / 12);
        double yearlyMultiplier = Math.pow(yearlyRoot, yearlyPeriod) - yearlyConstant;

        double yearlyDivisor = yearlyMultiplier / (yearlyConstant - 1);

        return goalAmount / yearlyDivisor;
    }

    public double calculatorInterest(double payment, double period, double yearlyRate) {
        yearlyRate /= 100;
        double yearlyPeriod = (period + 1) / 12;

        double yearlyRoot = 1 + yearlyRate;
        double principal = payment * period;

        double yearlyConstant = Math.pow(yearlyRoot, 1.0 / 12);
        double yearlyMultiplier = Math.pow(yearlyRoot, yearlyPeriod) - yearlyConstant;

        return payment * yearlyMultiplier / (yearlyConstant - 1) - principal;
    }
}