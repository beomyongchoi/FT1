package kr.co.fintalk.fintalkone.ui.calculator.debt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.oooobang.library.OBEditText;
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
 * Created by BeomyongChoi on 6/28/16
 */
public class DebtThirdActivity extends BaseFragmentActivity {
    OBParse mParse = new OBParse();

    OBEditText mPrincipalEditText;
    OBEditText mRepaymentPeriodEditText;
    OBEditText mInterestRateEditText;
    Button mDetailScheduleButton;

    public int mPeriod;

    public double mMonthlyRepayment;
    ArrayList<Double> mRemainingDebt = new ArrayList<>();
    ArrayList<Double> mMonthlyInterest = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt_third);

        TextView titleTextView = (TextView) findViewById(R.id.appbarTitle);
        titleTextView.setText(R.string.debt_divide_monthly);

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
        mPeriod = mParse.toInt(repaymentPeriodString.replace("개월",""));
        double interestRate = mParse.toDouble(interestRateString.replace("%",""));

        if(principalString.length() != 0
                && repaymentPeriodString.length() != 0
                && interestRateString.length() != 0) {
            mMonthlyRepayment = calculatorMonthlyPayment(principal, interestRate);
            calculatorInterest(principal, interestRate);
            setListView(principal);
            mDetailScheduleButton.setVisibility(View.VISIBLE);
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
        double totalInterest = sumOfTotalInterest(mMonthlyInterest);
        String monthlyRepaymentString = mParse.addComma(mMonthlyRepayment) + "원";
        String totalInterestString = mParse.addComma(totalInterest) + "원";
        String totalAmountString = mParse.addComma(principal + totalInterest) + "원";

        List<Map<String,?>> resultList = new LinkedList<>();
        resultList.add(createItem("월 상환금", monthlyRepaymentString));
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
    
    public double calculatorMonthlyPayment(double principal, double rate){
        rate /= 1200;
        double multiplier = Math.pow((1 + rate), mPeriod);
        return principal * rate * multiplier / (multiplier - 1);
    }

    public void calculatorInterest(double principal, double rate) {
        rate /= 1200;
        mMonthlyInterest.add(0, principal * rate);
        mRemainingDebt.add(0, principal - mMonthlyRepayment + mMonthlyInterest.get(0));

        for(int index = 1; index < mPeriod; index++) {
            mMonthlyInterest.add(index, mRemainingDebt.get(index - 1) * rate);
            mRemainingDebt.add(index, mRemainingDebt.get(index - 1)
                    - mMonthlyRepayment + mMonthlyInterest.get(index));
        }
    }

    public void detailScheduleOnClick(View view) {
        Intent intent = new Intent(DebtThirdActivity.this, DebtThirdDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("index", mPeriod);
        intent.putExtra("monthlyRepayment", mMonthlyRepayment);
        intent.putExtra("remainingDebt", mRemainingDebt);
        intent.putExtra("monthlyInterest", mMonthlyInterest);
        startActivity(intent);
    }


    public static double sumOfTotalInterest(List<Double> doubles) {
        int len = doubles.size();
        if (len == 0) return 0;
        if (len == 1) return doubles.get(0);
        return sumOfTotalInterest(doubles.subList(0, len/2)) + sumOfTotalInterest(doubles.subList(len/2, len));
    }
}