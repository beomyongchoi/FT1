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
public class InvestmentThirdActivity extends BaseFragmentActivity {
    Toolbar toolbar;

    OBParse mParse = new OBParse();

    ClearEditText mDepositAmountEditText;
    ClearEditText mDepositPeriodEditText;
    ClearEditText mReturnRateEditText;

    Button mCalculatorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment_third);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView titleTextView = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        titleTextView.setText(R.string.investment_deposit);
        titleTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/GodoM.otf"));

        toolbar.findViewById(R.id.toolbarBackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDepositAmountEditText = (ClearEditText) findViewById(R.id.investmentDepositAmountEditText);
        mDepositPeriodEditText = (ClearEditText) findViewById(R.id.investmentDepositPeriodEditText);
        mReturnRateEditText = (ClearEditText) findViewById(R.id.investmentReturnRateEditText);

        mDepositAmountEditText.setWon(true);
        mDepositPeriodEditText.setPeriod(true);
        mReturnRateEditText.setRate(true);

        mDepositAmountEditText.init();
        mDepositPeriodEditText.init();
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

        setListView(0, 0);
    }

    public void calculateInvestmentOnClick(View view) {
        String depositAmountString = mDepositAmountEditText.getText().toString();
        String depositPeriodString = mDepositPeriodEditText.getText().toString();
        String returnRateString = mReturnRateEditText.getText().toString();

        double depositAmount = mParse.toDouble(depositAmountString.replace("원","").replace(",",""));
        double depositPeriod = mParse.toDouble(depositPeriodString.replace("개월",""));
        double returnRate = mParse.toDouble(returnRateString.replace("%",""));

        double resultInterest;

        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        if(depositAmountString.length() != 0
                && depositPeriodString.length() != 0
                && returnRateString.length() != 0) {
            if (depositPeriod < 36) {
                showToast(R.string.period_investment_toast, 2);
            }
            else {
                inputMethodManager.hideSoftInputFromWindow(mReturnRateEditText.getWindowToken(), 0);
                mDepositAmountEditText.clearFocus();
                mDepositPeriodEditText.clearFocus();
                mReturnRateEditText.clearFocus();
                resultInterest = calculatorInterest(depositAmount, depositPeriod, returnRate);
                setListView(depositAmount, resultInterest);
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

    public void setListView(double principal, double interest) {
        String resultAmountString = mParse.addComma(principal + interest) + "원";
        String resultReturnString = mParse.addComma(interest) + "원";

        List<Map<String,?>> resultList = new LinkedList<>();
        resultList.add(createItem("최종 금액", resultAmountString));
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