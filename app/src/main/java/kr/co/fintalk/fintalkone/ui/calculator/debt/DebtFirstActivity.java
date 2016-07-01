package kr.co.fintalk.fintalkone.ui.calculator.debt;

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
public class DebtFirstActivity extends BaseFragmentActivity {
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

        TextView titleTextView = (TextView) findViewById(R.id.appbarTitle);
        titleTextView.setText(R.string.debt_maturity);

        mPrincipalEditText = (ClearEditText) findViewById(R.id.debtPrincipalEditText);
        mRepaymentPeriodEditText = (ClearEditText) findViewById(R.id.debtRepaymentPeriodEditText);
        mInterestRateEditText = (ClearEditText) findViewById(R.id.debtInterestRateEditText);

        mPrincipalEditText.setWon(true);
        mRepaymentPeriodEditText.setPeriod(true);
        mInterestRateEditText.setRate(true);

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
            if (repaymentPeriod < 36) {
                showToast(R.string.period_toast, 2);
            } else {
                inputMethodManager.hideSoftInputFromWindow(mInterestRateEditText.getWindowToken(), 0);
                mPrincipalEditText.clearFocus();
                mRepaymentPeriodEditText.clearFocus();
                mInterestRateEditText.clearFocus();
                calculatorInterest(principal, repaymentPeriod, interestRate);
                setListView(principal);
            }
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

    public void setListView(double principal) {
        String monthlyInterestString = mParse.addComma(mMonthlyInterest) + "원";
        String totalInterestString = mParse.addComma(mTotalInterest) + "원";
        String totalAmountString = mParse.addComma(principal + mTotalInterest) + "원";

        List<Map<String,?>> resultList = new LinkedList<>();
        resultList.add(createItem("월 납입이자", monthlyInterestString));
        resultList.add(createItem("총 이자", totalInterestString));
        resultList.add(createItem("총 상환금액", totalAmountString));

        // create our list and custom adapter
        DebtListViewAdapter adapter = new DebtListViewAdapter(this);

        String[] from = { FTConstants.ITEM_TITLE, FTConstants.ITEM_CONTENTS };
        int[] to = new int[] {R.id.debtResultTitle, R.id.debtResultContents};

        adapter.addSection("계산결과", new SimpleAdapter(this, resultList,
                R.layout.listview_debt_row, from, to));

        ListView list = (ListView) findViewById(R.id.debtResultListView);
        list.setAdapter(adapter);
    }

    public void calculatorInterest(double principal, int period, double yearlyRate) {
        double monthlyRate = yearlyRate / 1200;

        mMonthlyInterest = principal * monthlyRate;
        mTotalInterest = mMonthlyInterest * period;
    }

}