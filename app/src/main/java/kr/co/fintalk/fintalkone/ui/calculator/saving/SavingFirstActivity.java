package kr.co.fintalk.fintalkone.ui.calculator.saving;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
 * Created by BeomyongChoi on 6/23/16
 */
public class SavingFirstActivity extends BaseFragmentActivity {
    public final static String ITEM_TITLE = "title";
    public final static String ITEM_CONTENTS = "contents";

    OBParse mParse = new OBParse();

    OBEditText mMonthlyPaymentEditText;
    OBEditText mGoalPeriodEditText;
    OBEditText mInterestRateEditText;

    public int mInterestType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_first);

        TextView titleTextView = (TextView) findViewById(R.id.appbarTitle);
        titleTextView.setText(R.string.saving_goal_amount);

        mMonthlyPaymentEditText = (OBEditText) findViewById(R.id.savingMonthlyPaymentEditText);
        mGoalPeriodEditText = (OBEditText) findViewById(R.id.savingGoalPeriodEditText);
        mInterestRateEditText = (OBEditText) findViewById(R.id.savingInterestRateEditText);
    }

    public void interestTypeOnClick(View view) {
        final CharSequence[] items = new CharSequence[]{"단리", "월복리", "연복리"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.interest_type);
        dialog.setItems(items,new DialogInterface.OnClickListener() {
            // 리스트 선택 시 이벤트
            public void onClick(DialogInterface dialog, int id) {
                TextView interestTypeTextView = (TextView) findViewById(R.id.savingInterestTypeTextView);
                interestTypeTextView.setText(items[id]);
                mInterestType = id;
            }
        });
        dialog.show();
    }

    public void calculateSavingOnClick(View view) {


        String monthlyPaymentString = mMonthlyPaymentEditText.getText().toString();
        String goalPeriodString = mGoalPeriodEditText.getText().toString();
        String interestRateString = mInterestRateEditText.getText().toString();

        double monthlyPayment = mParse.toDouble(monthlyPaymentString.replace("원","").replace(",",""));
        double goalPeriod = mParse.toDouble(goalPeriodString.replace("개월",""));
        double interestRate = mParse.toDouble(interestRateString.replace("%",""));

        double resultPrincipal;
        double resultInterest;

        if(monthlyPaymentString.length() != 0
                && goalPeriodString.length() != 0
                && interestRateString.length() != 0) {
            resultPrincipal = monthlyPayment*goalPeriod;
            resultInterest = calculatorInterest(monthlyPayment,goalPeriod,interestRate,mInterestType);
            setResultHeader(mParse.addComma(resultPrincipal) + "원");
            setListView(resultPrincipal, resultInterest);
        }
        else {
            showToast("모든 항목을 입력하세요", 3);
        }
        showToast(monthlyPaymentString+"",2);
    }

    public Map<String,?> createItem(String title, String contents) {
        Map<String,String> item = new HashMap<>();
        item.put(ITEM_TITLE, title);
        item.put(ITEM_CONTENTS, contents);
        return item;
    }

    public void setResultHeader(String principal) {
        TextView titleTextView = (TextView) findViewById(R.id.resultTitleTextView);
        TextView captionTextView = (TextView) findViewById(R.id.resultCaptionTextView);
        TextView amountTextView = (TextView) findViewById(R.id.resultAmountTextView);

        titleTextView.setText(R.string.resultCalculate);
        captionTextView.setText(R.string.principal);
        amountTextView.setText(principal);
    }

    public void setListView(double resultPrincipal, double taxFreeInterest) {
        double taxGeneralInterest, taxBreaksInterest;
        double taxGeneral, taxBreaks;

        taxGeneralInterest = taxFreeInterest*.846;
        taxBreaksInterest = taxFreeInterest*.905;

        taxGeneral = taxFreeInterest*.154;
        taxBreaks = taxFreeInterest*.095;

        //일반과세
        String taxGeneralAmountString = mParse.addComma(resultPrincipal+taxGeneralInterest) + "원";
        String taxGeneralInterestString = mParse.addComma(taxGeneralInterest) + "원";
        String taxGeneralString = mParse.addComma(taxGeneral) + "원";
        //세금우대
        String taxBreaksAmountString = mParse.addComma(resultPrincipal+taxBreaksInterest) + "원";
        String taxBreaksInterestString = mParse.addComma(taxBreaksInterest) + "원";
        String taxBreaksString = mParse.addComma(taxBreaks) + "원";
        //비과세
        String taxFreeAmountString = mParse.addComma(resultPrincipal+taxFreeInterest) + "원";
        String taxFreeInterestString = mParse.addComma(taxFreeInterest) + "원";

        List<Map<String,?>> taxGeneralList = new LinkedList<>();
        taxGeneralList.add(createItem("만기지급액", taxGeneralAmountString));
        taxGeneralList.add(createItem("세후이자", taxGeneralInterestString));
        taxGeneralList.add(createItem("세금", taxGeneralString));

        List<Map<String,?>> taxBreaksList = new LinkedList<>();
        taxBreaksList.add(createItem("만기지급액", taxBreaksAmountString));
        taxBreaksList.add(createItem("세후이자", taxBreaksInterestString));
        taxBreaksList.add(createItem("세금", taxBreaksString));

        List<Map<String,?>> taxFreeList = new LinkedList<>();
        taxFreeList.add(createItem("만기지급액", taxFreeAmountString));
        taxFreeList.add(createItem("세후이자", taxFreeInterestString));
        taxFreeList.add(createItem("세금", "0원"));

        // create our list and custom adapter
        SavingListViewAdapter adapter = new SavingListViewAdapter(this);

        String[] from = { ITEM_TITLE, ITEM_CONTENTS };
        int[] to = new int[] {R.id.savingResultTitle, R.id.savingResultContents};

        adapter.addSection("일반과세", new SimpleAdapter(this, taxGeneralList,
                R.layout.listview_saving_row, from, to));
        adapter.addSection("세금우대", new SimpleAdapter(this, taxBreaksList,
                R.layout.listview_saving_row, from, to));
        adapter.addSection("비과세", new SimpleAdapter(this, taxFreeList,
                R.layout.listview_saving_row, from, to));

        ListView list = (ListView) findViewById(R.id.savingResultListView);
        list.setAdapter(adapter);
    }
//예금
//    public double savingCalculator(double a, double d, double r) {
//        return a*(1+d*r/1200);
//    }

    public double calculatorInterest(double payment, double period, double rate, int type) {
        rate /= 1200;
        double principal = payment * period;
        double rateWithPrincipal = 1 + rate;
        double rateYearly = Math.pow(rateWithPrincipal,(period + 1.0) / 12);
        double tempRatio = Math.pow(rateWithPrincipal, period) - 1;
        double monthlyPrincipalInterestRate = Math.pow(rateWithPrincipal, 1.0 / 12);

        switch (type) {
            case 0: // 단리
                return principal * (period + 1) / 2 * rate;
            case 1: // 월복리
                return payment * rateWithPrincipal * tempRatio / rate - principal;
            case 2: // 연복리
                return payment * (rateYearly - monthlyPrincipalInterestRate)
                        / (monthlyPrincipalInterestRate - 1) - principal;
            default:// 오리
                return 0;
        }
    }
}