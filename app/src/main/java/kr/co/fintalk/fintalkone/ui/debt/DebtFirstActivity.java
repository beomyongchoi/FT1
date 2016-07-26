package kr.co.fintalk.fintalkone.ui.debt;

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
public class DebtFirstActivity extends BaseFragmentActivity {
    Toolbar toolbar;

    OBParse mParse = new OBParse();

    ClearEditText mPrincipalEditText;
    ClearEditText mRepaymentPeriodEditText;
    ClearEditText mInterestRateEditText;

    public double mMonthlyInterest;
    public double mTotalInterest;

    Button mCalculatorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt_first);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView titleTextView = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        titleTextView.setText(R.string.debt_maturity);
        titleTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/GodoM.otf"));

        toolbar.findViewById(R.id.toolbarBackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPrincipalEditText = (ClearEditText) findViewById(R.id.debtPrincipalEditText);
        mRepaymentPeriodEditText = (ClearEditText) findViewById(R.id.debtRepaymentPeriodEditText);
        mInterestRateEditText = (ClearEditText) findViewById(R.id.debtInterestRateEditText);

        mPrincipalEditText.setWon(true);
        mRepaymentPeriodEditText.setPeriod(true);
        mInterestRateEditText.setRate(true);

        mPrincipalEditText.init();
        mRepaymentPeriodEditText.init();
        mInterestRateEditText.init();

        mCalculatorButton = (Button) findViewById(R.id.calculatorButton);

        mInterestRateEditText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(3, 3)});
        mInterestRateEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        mInterestRateEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mCalculatorButton.performClick();
                }
                return true;
            }
        });

        setCardView(0);
    }

    public void calculateDebtOnClick(View view) {
        String principalString = mPrincipalEditText.getText().toString();
        String repaymentPeriodString = mRepaymentPeriodEditText.getText().toString();
        String interestRateString = mInterestRateEditText.getText().toString();

        double principal = mParse.toDouble(principalString.replace("원","").replace(",",""));
        int repaymentPeriod = mParse.toInt(repaymentPeriodString.replace("개월",""));
        double interestRate = mParse.toDouble(interestRateString.replace("%",""));

        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        if(principalString.length() != 0
                && repaymentPeriodString.length() != 0
                && interestRateString.length() != 0) {
            if (repaymentPeriod < 12) {
                showToast(R.string.period_debt_toast, 2);
            } else {
                inputMethodManager.hideSoftInputFromWindow(mInterestRateEditText.getWindowToken(), 0);
                mPrincipalEditText.clearFocus();
                mRepaymentPeriodEditText.clearFocus();
                mInterestRateEditText.clearFocus();
                calculatorInterest(principal, repaymentPeriod, interestRate);
                setCardView(principal);
            }
        }
        else {
            showToast(R.string.all_fields_required_toast, 2);
        }
    }

    public void setCardView(double principal) {
        TextView monthlyInterestTextView = (TextView) findViewById(R.id.monthlyInterestTextView);
        TextView totalInterestTextView = (TextView) findViewById(R.id.totalInterestTextView);
        TextView totalRepaymentTextView = (TextView) findViewById(R.id.totalRepaymentTextView);
        
        String monthlyInterestString = mParse.addComma(mMonthlyInterest) + " 원";
        String totalInterestString = mParse.addComma(mTotalInterest) + " 원";
        String totalRepaymentString = mParse.addComma(principal + mTotalInterest) + " 원";
        
        monthlyInterestTextView.setText(monthlyInterestString);
        totalInterestTextView.setText(totalInterestString);
        totalRepaymentTextView.setText(totalRepaymentString);
    }

    public void calculatorInterest(double principal, int period, double yearlyRate) {
        double monthlyRate = yearlyRate / 1200;

        mMonthlyInterest = principal * monthlyRate;
        mTotalInterest = mMonthlyInterest * period;
    }

}