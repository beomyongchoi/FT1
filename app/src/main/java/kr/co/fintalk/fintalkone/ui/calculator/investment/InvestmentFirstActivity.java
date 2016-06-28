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

/**
 * Created by BeomyongChoi on 6/28/16
 */
public class InvestmentFirstActivity extends BaseFragmentActivity {
    public final static String ITEM_TITLE = "title";
    public final static String ITEM_CONTENTS = "contents";

    OBParse mParse = new OBParse();

    OBEditText mMonthlyPaymentEditText;
    OBEditText mGoalPeriodEditText;
    OBEditText mReturnRateEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment_first);

        TextView titleTextView = (TextView) findViewById(R.id.appbarTitle);
        titleTextView.setText(R.string.investment_monthly_payment);

        mMonthlyPaymentEditText = (OBEditText) findViewById(R.id.investmentMonthlyPaymentEditText);
        mGoalPeriodEditText = (OBEditText) findViewById(R.id.investmentGoalPeriodEditText);
        mReturnRateEditText = (OBEditText) findViewById(R.id.investmentReturnRateEditText);
    }

    public void calculateInvestmentOnClick(View view) {
        String monthlyPaymentString = mMonthlyPaymentEditText.getText().toString();
        String goalPeriodString = mGoalPeriodEditText.getText().toString();
        String returnRateString = mReturnRateEditText.getText().toString();

        double monthlyPayment = mParse.toDouble(monthlyPaymentString.replace("원","").replace(",",""));
        double goalPeriod = mParse.toDouble(goalPeriodString.replace("개월",""));
        double returnRate = mParse.toDouble(returnRateString.replace("%",""));

        double resultPrincipal;
        double resultInterest;

        if(monthlyPaymentString.length() != 0
                && goalPeriodString.length() != 0
                && returnRateString.length() != 0) {
            resultPrincipal = monthlyPayment * goalPeriod;
            resultInterest = calculatorInterest(monthlyPayment, goalPeriod, returnRate);
            setListView(resultPrincipal, resultInterest);
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

    public void setListView(double principal, double interest) {
        String resultAmountString = mParse.addComma(principal + interest) + "원";
        String principalString = mParse.addComma(principal) + "원";
        String resultReturnString = mParse.addComma(interest) + "원";

        List<Map<String,?>> resultList = new LinkedList<>();
        resultList.add(createItem("최종 금액", resultAmountString));
        resultList.add(createItem("원금", principalString));
        resultList.add(createItem("총 수익액", resultReturnString));

        // create our list and custom adapter
        InvestmentListViewAdapter adapter = new InvestmentListViewAdapter(this);

        String[] from = { ITEM_TITLE, ITEM_CONTENTS };
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