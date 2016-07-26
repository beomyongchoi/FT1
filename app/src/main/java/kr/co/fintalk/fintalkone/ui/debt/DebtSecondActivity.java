package kr.co.fintalk.fintalkone.ui.debt;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import kr.co.fintalk.fintalkone.R;
import kr.co.fintalk.fintalkone.common.BaseFragmentActivity;
import kr.co.fintalk.fintalkone.common.ClearEditText;
import kr.co.fintalk.fintalkone.common.DecimalDigitsInputFilter;
import kr.co.fintalk.fintalkone.ui.debt.detail.DebtSecondDetailActivity;

/**
 * Created by BeomyongChoi on 6/28/16
 */
public class DebtSecondActivity extends BaseFragmentActivity {
    Toolbar toolbar;

    OBParse mParse = new OBParse();

    ClearEditText mPrincipalEditText;
    ClearEditText mRepaymentPeriodEditText;
    ClearEditText mInterestRateEditText;

    Button mCalculatorButton;
    Button mDetailScheduleButton;

    public int mPeriod;

    public double mPrincipal;
    public double mMonthlyRepayment;
    ArrayList<Double> mMonthlyInterest = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt_second);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView titleTextView = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        titleTextView.setText(R.string.debt_divide_balance);
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

        mDetailScheduleButton = (Button) findViewById(R.id.detailScheduleButton);
        mDetailScheduleButton.setVisibility(View.INVISIBLE);

        setCardView(0);
    }

    public void calculateDebtOnClick(View view) {
        String principalString = mPrincipalEditText.getText().toString().replace("원","").replace(",","");
        String repaymentPeriodString = mRepaymentPeriodEditText.getText().toString().replace("개월","");
        String interestRateString = mInterestRateEditText.getText().toString().replace("%","");

        mPrincipal = mParse.toDouble(principalString);
        mPeriod = mParse.toInt(repaymentPeriodString);
        double interestRate = mParse.toDouble(interestRateString);

        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        if(principalString.length() != 0
                && repaymentPeriodString.length() != 0
                && interestRateString.length() != 0) {
            if (mPeriod < 12) {
                showToast(R.string.period_debt_toast, 2);
            } else {
                inputMethodManager.hideSoftInputFromWindow(mInterestRateEditText.getWindowToken(), 0);
                mPrincipalEditText.clearFocus();
                mRepaymentPeriodEditText.clearFocus();
                mInterestRateEditText.clearFocus();
                mMonthlyRepayment = mPrincipal / mPeriod;
                calculatorInterest(mPrincipal, interestRate);
                setCardView(mPrincipal);
                mDetailScheduleButton.setVisibility(View.VISIBLE);
            }
        }
        else {
            showToast(R.string.all_fields_required_toast, 2);
        }
    }

    public void setCardView(double principal) {
        TextView monthlyRepaymentTextView = (TextView) findViewById(R.id.monthlyRepaymentTextView);
        TextView totalInterestTextView = (TextView) findViewById(R.id.totalInterestTextView);
        TextView totalRepaymentTextView = (TextView) findViewById(R.id.totalRepaymentTextView);

        double totalInterest = sumOfTotalInterest(mMonthlyInterest);

        String monthlyRepaymentString = mParse.addComma(mMonthlyRepayment) + " 원";
        String totalInterestString = mParse.addComma(totalInterest) + " 원";
        String totalRepaymentString = mParse.addComma(principal + totalInterest) + " 원";

        monthlyRepaymentTextView.setText(monthlyRepaymentString);
        totalInterestTextView.setText(totalInterestString);
        totalRepaymentTextView.setText(totalRepaymentString);
    }

    public void calculatorInterest(double principal, double yearlyRate) {
        double monthlyRate = yearlyRate / 1200;
        mMonthlyInterest.clear();
        for(int index = 0; index < mPeriod; index++) {
            mMonthlyInterest.add(index, principal * monthlyRate);
            principal -= mMonthlyRepayment;
        }
    }

    public void detailScheduleOnClick(View view) {
        Intent intent = new Intent(DebtSecondActivity.this, DebtSecondDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("index", mPeriod);
        intent.putExtra("principal", mPrincipal);
        intent.putExtra("monthlyRepayment", mMonthlyRepayment);
        intent.putExtra("monthlyInterest", mMonthlyInterest);
        startActivity(intent);
    }

    public static double sumOfTotalInterest(List<Double> doubles) {
        int length = doubles.size();
        if (length == 0) return 0;
        if (length == 1) return doubles.get(0);
        return sumOfTotalInterest(doubles.subList(0, length/2))
                + sumOfTotalInterest(doubles.subList(length/2, length));
    }
}