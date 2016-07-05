package kr.co.fintalk.fintalkone.ui.calculator.investment;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.oooobang.library.OBParse;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kr.co.fintalk.fintalkone.R;
import kr.co.fintalk.fintalkone.common.BaseFragmentActivity;
import kr.co.fintalk.fintalkone.common.ClearEditText;
import kr.co.fintalk.fintalkone.common.DecimalDigitsInputFilter;
import kr.co.fintalk.fintalkone.common.FTConstants;

/**
 * Created by BeomyongChoi on 6/28/16
 */
public class InvestmentSecondActivity extends BaseFragmentActivity {
    OBParse mParse = new OBParse();

    ClearEditText mGoalAmountEditText;
    ClearEditText mGoalPeriodEditText;
    ClearEditText mReturnRateEditText;

    Button mCalculatorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment_second);

        TextView titleTextView = (TextView) findViewById(R.id.appbarTitle);
        titleTextView.setText(R.string.investment_goal_amount);

        mGoalAmountEditText = (ClearEditText) findViewById(R.id.investmentGoalAmountEditText);
        mGoalPeriodEditText = (ClearEditText) findViewById(R.id.investmentGoalPeriodEditText);
        mReturnRateEditText = (ClearEditText) findViewById(R.id.investmentReturnRateEditText);
        
        mGoalAmountEditText.setWon(true);
        mGoalPeriodEditText.setPeriod(true);
        mReturnRateEditText.setRate(true);

        mGoalAmountEditText.init();
        mGoalPeriodEditText.init();
        mReturnRateEditText.init();

        mCalculatorButton = (Button) findViewById(R.id.calculatorButton);

        mReturnRateEditText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(3, 3)});
        mReturnRateEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        mReturnRateEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mCalculatorButton.performClick();
                }
                return true;
            }
        });

        setListView(0, 0, 0);
    }

    public void calculateInvestmentOnClick(View view) {
        String goalAmountString = mGoalAmountEditText.getText().toString();
        String predictedPeriodString = mGoalPeriodEditText.getText().toString();
        String returnRateString = mReturnRateEditText.getText().toString();

        double goalAmount = mParse.toDouble(goalAmountString.replace("원","").replace(",",""));
        double predictedPeriod = mParse.toDouble(predictedPeriodString.replace("개월",""));
        double returnRate = mParse.toDouble(returnRateString.replace("%",""));

        double monthlyPayment;
        double resultPrincipal;
        double resultReturn;

        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        if(goalAmountString.length() != 0
                && predictedPeriodString.length() != 0
                && returnRateString.length() != 0) {
            if (predictedPeriod < 36) {
                showToast(R.string.period_investment_toast, 2);
            }
            else {
                inputMethodManager.hideSoftInputFromWindow(mReturnRateEditText.getWindowToken(), 0);
                mGoalAmountEditText.clearFocus();
                mGoalPeriodEditText.clearFocus();
                mReturnRateEditText.clearFocus();
                monthlyPayment = calculatorMonthlyPayment(goalAmount, predictedPeriod, returnRate);
                resultReturn = calculatorInterest(monthlyPayment, predictedPeriod, returnRate);
                resultPrincipal = monthlyPayment * predictedPeriod;
                setListView(monthlyPayment, resultPrincipal, resultReturn);
            }
        }
        else {
            showToast(R.string.all_fields_required_toast, 2);
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