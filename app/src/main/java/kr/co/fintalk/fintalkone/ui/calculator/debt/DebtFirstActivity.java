package kr.co.fintalk.fintalkone.ui.calculator.debt;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

/**
 * Created by BeomyongChoi on 6/28/16
 */
public class DebtFirstActivity extends BaseFragmentActivity {
    public final static String ITEM_TITLE = "title";
    public final static String ITEM_CONTENTS = "contents";

    OBParse mParse = new OBParse();

    OBEditText mPrincipalEditText;
    OBEditText mRepaymentPeriodEditText;
    OBEditText mInterestRateEditText;
    Button mDetailScheduleButton;

    public double mMonthlyInterest;
    public double mTotalInterest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt_first);

        TextView titleTextView = (TextView) findViewById(R.id.appbarTitle);
        titleTextView.setText(R.string.debt_maturity);

        mPrincipalEditText = (OBEditText) findViewById(R.id.debtPrincipalEditText);
        mRepaymentPeriodEditText = (OBEditText) findViewById(R.id.debtRepaymentPeriodEditText);
        mInterestRateEditText = (OBEditText) findViewById(R.id.debtInterestRateEditText);

        mDetailScheduleButton = (Button) findViewById(R.id.detailScheduleButton);
        mDetailScheduleButton.setVisibility(View.INVISIBLE);
    }

    public void calculateDebtOnClick(View view) {
        String principalString = mPrincipalEditText.getText().toString();
        String repaymentPeriodString = mRepaymentPeriodEditText.getText().toString();
        String interestRateString = mInterestRateEditText.getText().toString();

        double principal = mParse.toDouble(principalString.replace("원","").replace(",",""));
        double repaymentPeriod = mParse.toDouble(repaymentPeriodString.replace("개월",""));
        double interestRate = mParse.toDouble(interestRateString.replace("%",""));

        if(principalString.length() != 0
                && repaymentPeriodString.length() != 0
                && interestRateString.length() != 0) {
            calculatorInterest(principal, repaymentPeriod, interestRate);
            setListView(principal);
            mDetailScheduleButton.setVisibility(View.VISIBLE);
        }
        else {
            showToast("모든 항목을 입력하세요", 3);
        }
    }

    public Map<String,?> createItem(String title, String contents) {
        Map<String,String> item = new HashMap<>();
        item.put(ITEM_TITLE, title);
        item.put(ITEM_CONTENTS, contents);
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

        String[] from = { ITEM_TITLE, ITEM_CONTENTS };
        int[] to = new int[] {R.id.debtResultTitle, R.id.debtResultContents};

        adapter.addSection("계산결과", new SimpleAdapter(this, resultList,
                R.layout.listview_debt_row, from, to));

        ListView list = (ListView) findViewById(R.id.debtResultListView);
        list.setAdapter(adapter);
    }

    public void calculatorInterest(double principal, double period, double yearlyRate) {
        double monthlyRate = yearlyRate / 1200;

        mMonthlyInterest = principal * monthlyRate;
        mTotalInterest = mMonthlyInterest * period;
    }

    public void detailScheduleOnClick(View view) {
        showToast("아직 안했어",2);
    }
}