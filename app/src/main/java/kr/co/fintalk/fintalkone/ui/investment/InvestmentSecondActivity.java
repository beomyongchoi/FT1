package kr.co.fintalk.fintalkone.ui.investment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.oooobang.library.OBParse;

import kr.co.fintalk.fintalkone.R;
import kr.co.fintalk.fintalkone.common.BaseFragmentActivity;
import kr.co.fintalk.fintalkone.common.ClearEditText;
import kr.co.fintalk.fintalkone.common.DecimalDigitsInputFilter;

/**
 * Created by BeomyongChoi on 6/28/16
 */
public class InvestmentSecondActivity extends BaseFragmentActivity {
    Toolbar toolbar;

    OBParse mParse = new OBParse();

    ClearEditText mGoalAmountEditText;
    ClearEditText mGoalPeriodEditText;
    ClearEditText mReturnRateEditText;

    Button mCalculatorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment_second);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView titleTextView = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        titleTextView.setText(R.string.investment_goal_amount);
        titleTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/GodoM.otf"));

        toolbar.findViewById(R.id.toolbarBackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

        setCardView(0, 0, 0);
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
                setCardView(monthlyPayment, resultPrincipal, resultReturn);
            }
        }
        else {
            showToast(R.string.all_fields_required_toast, 2);
        }
    }

    public void setCardView(double monthlyPayment, double principal, double resultReturn) {
        TextView monthlyPaymentTextView = (TextView) findViewById(R.id.monthlyPaymentTextView);
        TextView principalTextView = (TextView) findViewById(R.id.principalTextView);
        TextView resultReturnTextView = (TextView) findViewById(R.id.resultReturnTextView);

        String monthlyPaymentString = mParse.addComma(monthlyPayment) + "원";
        String principalString = mParse.addComma(principal) + "원";
        String resultReturnString = mParse.addComma(resultReturn) + "원";

        monthlyPaymentTextView.setText(monthlyPaymentString);
        principalTextView.setText(principalString);
        resultReturnTextView.setText(resultReturnString);
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