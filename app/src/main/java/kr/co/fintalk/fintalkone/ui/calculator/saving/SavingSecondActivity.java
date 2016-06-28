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
public class SavingSecondActivity extends BaseFragmentActivity {
    public final static String ITEM_TITLE = "title";
    public final static String ITEM_CONTENTS = "contents";

    OBParse mParse = new OBParse();

    OBEditText mGoalAmountEditText;
    OBEditText mPredictedPeriodEditText;
    OBEditText mInterestRateEditText;

    public double mMonthlyPaymentTaxGeneral;
    public double mMonthlyPaymentTaxBreaks;
    public double mMonthlyPaymentTaxFree;

    public int mInterestType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_second);

        TextView titleTextView = (TextView) findViewById(R.id.appbarTitle);
        titleTextView.setText(R.string.saving_goal_amount);

        mGoalAmountEditText = (OBEditText) findViewById(R.id.savingGoalAmountEditText);
        mPredictedPeriodEditText = (OBEditText) findViewById(R.id.savingPredictedPeriodEditText);
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


        String goalAmountString = mGoalAmountEditText.getText().toString();
        String predictedPeriodString = mPredictedPeriodEditText.getText().toString();
        String interestRateString = mInterestRateEditText.getText().toString();

        double goalAmount = mParse.toDouble(goalAmountString.replace("원","").replace(",",""));
        double predictedPeriod = mParse.toDouble(predictedPeriodString.replace("개월",""));
        double interestRate = mParse.toDouble(interestRateString.replace("%",""));

        double interestTaxGeneral, interestTaxBreaks, interestTaxFree;

        if(goalAmountString.length() != 0
                && predictedPeriodString.length() != 0
                && interestRateString.length() != 0) {
            calculatorMonthlyPayment(goalAmount, predictedPeriod, interestRate);
            interestTaxGeneral = calculatorInterest(mMonthlyPaymentTaxGeneral, predictedPeriod, interestRate);
            interestTaxBreaks = calculatorInterest(mMonthlyPaymentTaxBreaks, predictedPeriod, interestRate);
            interestTaxFree = calculatorInterest(mMonthlyPaymentTaxFree, predictedPeriod, interestRate);
            setListView(interestTaxGeneral, interestTaxBreaks, interestTaxFree, predictedPeriod);
            setResultHeader(mParse.addComma(goalAmount) + "원");

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

    public void setResultHeader(String goalAmount) {
        TextView titleTextView = (TextView) findViewById(R.id.resultTitleTextView);
        TextView captionTextView = (TextView) findViewById(R.id.resultCaptionTextView);
        TextView amountTextView = (TextView) findViewById(R.id.resultAmountTextView);

        titleTextView.setText(R.string.result_calculate);
        captionTextView.setText(R.string.maturity_value);
        amountTextView.setText(goalAmount);
    }

    public void setListView(double taxGeneralInterest, double taxBreaksInterest, double taxFreeInterest, double period) {
        double taxGeneral, taxBreaks;
        taxGeneral = taxGeneralInterest*.154;
        taxBreaks = taxBreaksInterest*.095;

        taxGeneralInterest = taxGeneralInterest*.846;
        taxBreaksInterest = taxBreaksInterest*.905;

        //일반과세
        String taxGeneralMonthlyPaymentString = mParse.addComma(mMonthlyPaymentTaxGeneral) + "원";
        String taxGeneralAmountString = mParse.addComma(mMonthlyPaymentTaxGeneral * period) + "원";
        String taxGeneralInterestString = mParse.addComma(taxGeneralInterest) + "원";
        String taxGeneralString = mParse.addComma(taxGeneral) + "원";
        //세금우대
        String taxBreaksMonthlyPaymentString = mParse.addComma(mMonthlyPaymentTaxBreaks) + "원";
        String taxBreaksAmountString = mParse.addComma(mMonthlyPaymentTaxBreaks * period) + "원";
        String taxBreaksInterestString = mParse.addComma(taxBreaksInterest) + "원";
        String taxBreaksString = mParse.addComma(taxBreaks) + "원";
        //비과세
        String taxFreeMonthlyPaymentString = mParse.addComma(mMonthlyPaymentTaxFree) + "원";
        String taxFreeAmountString = mParse.addComma(mMonthlyPaymentTaxFree * period) + "원";
        String taxFreeInterestString = mParse.addComma(taxFreeInterest) + "원";

        List<Map<String,?>> taxGeneralList = new LinkedList<>();
        taxGeneralList.add(createItem("매월적금액", taxGeneralMonthlyPaymentString));
        taxGeneralList.add(createItem("원금", taxGeneralAmountString));
        taxGeneralList.add(createItem("세후이자", taxGeneralInterestString));
        taxGeneralList.add(createItem("세금", taxGeneralString));

        List<Map<String,?>> taxBreaksList = new LinkedList<>();
        taxBreaksList.add(createItem("매월적금액", taxBreaksMonthlyPaymentString));
        taxBreaksList.add(createItem("원금", taxBreaksAmountString));
        taxBreaksList.add(createItem("세후이자", taxBreaksInterestString));
        taxBreaksList.add(createItem("세금", taxBreaksString));

        List<Map<String,?>> taxFreeList = new LinkedList<>();
        taxFreeList.add(createItem("매월적금액", taxFreeMonthlyPaymentString));
        taxFreeList.add(createItem("원금", taxFreeAmountString));
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

    public void calculatorMonthlyPayment(double goalAmount, double period, double yearlyRate) {
        yearlyRate /= 100;
        double monthlyRate = yearlyRate / 12;

        double taxGeneralRate = 0.846;
        double taxBreaksRate = 0.905;

        double yearlyPeriod = (period + 1) / 12;
        double paymentCountMonthly = period * (period + 1) / 2;

        double monthlyRoot = 1 + monthlyRate;
        double yearlyRoot = 1 + yearlyRate;

        double yearlyConstant = Math.pow(yearlyRoot, 1.0 / 12);

        double monthlyMultiplier = Math.pow(monthlyRoot, period) - 1;
        double yearlyMultiplier = Math.pow(yearlyRoot, yearlyPeriod) - yearlyConstant;


        double simpleDivisorTaxGeneral = paymentCountMonthly * monthlyRate * taxGeneralRate + period;
        double simpleDivisorTaxBreaks = paymentCountMonthly * monthlyRate * taxBreaksRate + period;
        double simpleDivisorTaxFree = paymentCountMonthly * monthlyRate + period;

        double monthlyDivisorTaxFree = monthlyRoot * monthlyMultiplier / monthlyRate;
        double monthlyDivisorTaxGeneral = (monthlyDivisorTaxFree - period) * taxGeneralRate + period;
        double monthlyDivisorTaxBreaks = (monthlyDivisorTaxFree - period) * taxBreaksRate + period;

        double yearlyDivisorTaxFree = yearlyMultiplier / (yearlyConstant - 1);
        double yearlyDivisorTaxGeneral = (yearlyDivisorTaxFree - period) * taxGeneralRate + period;
        double yearlyDivisorTaxBreaks = (yearlyDivisorTaxFree - period) * taxBreaksRate + period;

        switch (mInterestType) {
            case 0: // 단리
                mMonthlyPaymentTaxGeneral = goalAmount/ simpleDivisorTaxGeneral;
                mMonthlyPaymentTaxBreaks = goalAmount / simpleDivisorTaxBreaks;
                mMonthlyPaymentTaxFree = goalAmount / simpleDivisorTaxFree;
                break;
            case 1: // 월복리
                mMonthlyPaymentTaxGeneral = goalAmount /monthlyDivisorTaxGeneral;
                mMonthlyPaymentTaxBreaks = goalAmount /monthlyDivisorTaxBreaks;
                mMonthlyPaymentTaxFree = goalAmount / monthlyDivisorTaxFree;
                break;
            case 2: // 연복리
                mMonthlyPaymentTaxGeneral = goalAmount / yearlyDivisorTaxGeneral;
                mMonthlyPaymentTaxBreaks = goalAmount / yearlyDivisorTaxBreaks;
                mMonthlyPaymentTaxFree = goalAmount / yearlyDivisorTaxFree;
                break;
            default:// 오리
                mMonthlyPaymentTaxGeneral = goalAmount;
                mMonthlyPaymentTaxBreaks = goalAmount;
                mMonthlyPaymentTaxFree = goalAmount;
                break;
        }
    }

    public double calculatorInterest(double payment, double period, double yearlyRate) {
        yearlyRate /= 100;
        double monthlyRate = yearlyRate / 12;
        double yearlyPeriod = (period + 1) / 12;
        double paymentCountMonthly = period * (period + 1) / 2;

        double monthlyRoot = 1 + monthlyRate;
        double yearlyRoot = 1 + yearlyRate;
        double principal = payment * period;

        double yearlyConstant = Math.pow(yearlyRoot, 1.0 / 12);

        double simpleMultiplier = paymentCountMonthly * monthlyRate;
        double monthlyMultiplier = Math.pow(monthlyRoot, period) - 1;
        double yearlyMultiplier = Math.pow(yearlyRoot, yearlyPeriod) - yearlyConstant;


        switch (mInterestType) {
            case 0: // 단리
                return payment * simpleMultiplier;
            case 1: // 월복리
                return payment * monthlyRoot * monthlyMultiplier / monthlyRate - principal;
            case 2: // 연복리
                return payment * yearlyMultiplier / (yearlyConstant - 1) - principal;
            default:// 오리
                return 0;
        }
    }
}