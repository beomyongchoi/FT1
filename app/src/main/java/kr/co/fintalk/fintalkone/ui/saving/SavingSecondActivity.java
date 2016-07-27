package kr.co.fintalk.fintalkone.ui.saving;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.oooobang.library.OBParse;

import kr.co.fintalk.fintalkone.R;
import kr.co.fintalk.fintalkone.common.BaseFragmentActivity;
import kr.co.fintalk.fintalkone.common.ClearEditText;
import kr.co.fintalk.fintalkone.common.DecimalDigitsInputFilter;

/**
 * Created by BeomyongChoi on 6/23/16
 */
public class SavingSecondActivity extends BaseFragmentActivity {
    Toolbar toolbar;

    OBParse mParse = new OBParse();

    ClearEditText mGoalAmountEditText;
    ClearEditText mPredictedPeriodEditText;
    ClearEditText mInterestRateEditText;

    Button mCalculatorButton;

    public double mMonthlyPaymentTaxGeneral;
    public double mMonthlyPaymentTaxBreaks;
    public double mMonthlyPaymentTaxFree;

    public int mInterestType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_second);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView titleTextView = (TextView) findViewById(R.id.toolbarTitle);
        titleTextView.setText(R.string.saving_goal_amount);
        titleTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/GodoM.otf"));

        ImageView toolbarBackButton = (ImageView) findViewById(R.id.toolbarBackButton);
        
        toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mGoalAmountEditText = (ClearEditText) findViewById(R.id.savingGoalAmountEditText);
        mPredictedPeriodEditText = (ClearEditText) findViewById(R.id.savingGoalPeriodEditText);
        mInterestRateEditText = (ClearEditText) findViewById(R.id.savingInterestRateEditText);

        mGoalAmountEditText.setWon(true);
        mPredictedPeriodEditText.setPeriod(true);
        mPredictedPeriodEditText.setSaving(true);
        mInterestRateEditText.setRate(true);

        mGoalAmountEditText.init();
        mPredictedPeriodEditText.init();
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

        setResultHeader("0 원");
        setCardView(0, 0, 0 , 0);
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

        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        if(goalAmountString.length() != 0
                && predictedPeriodString.length() != 0
                && interestRateString.length() != 0) {
            inputMethodManager.hideSoftInputFromWindow(mInterestRateEditText.getWindowToken(), 0);
            mGoalAmountEditText.clearFocus();
            mPredictedPeriodEditText.clearFocus();
            mInterestRateEditText.clearFocus();
            calculatorMonthlyPayment(goalAmount, predictedPeriod, interestRate);
            interestTaxGeneral = calculatorInterest(mMonthlyPaymentTaxGeneral, predictedPeriod, interestRate);
            interestTaxBreaks = calculatorInterest(mMonthlyPaymentTaxBreaks, predictedPeriod, interestRate);
            interestTaxFree = calculatorInterest(mMonthlyPaymentTaxFree, predictedPeriod, interestRate);
            setResultHeader(mParse.addComma(goalAmount) + "원");
            setCardView(interestTaxGeneral, interestTaxBreaks, interestTaxFree, predictedPeriod);
        }
        else {
            showToast(R.string.all_fields_required_toast, 2);
        }
    }

    public void setResultHeader(String goalAmount) {
        TextView titleTextView = (TextView) findViewById(R.id.resultTitleTextView);
        TextView captionTextView = (TextView) findViewById(R.id.resultCaptionTextView);
        TextView amountTextView = (TextView) findViewById(R.id.resultAmountTextView);

        titleTextView.setText(R.string.result_calculate);
        captionTextView.setText(R.string.maturity_value);
        amountTextView.setText(goalAmount);
    }

    public void setCardView(double taxGeneralInterest, double taxBreaksInterest, double taxFreeInterest, double period) {
        TextView taxGeneralMonthlyPaymentTextView = (TextView) findViewById(R.id.taxGeneralMonthlyPayment);
        TextView taxGeneralAmountTextView = (TextView) findViewById(R.id.taxGeneralAmount);
        TextView taxGeneralInterestTextView = (TextView) findViewById(R.id.taxGeneralInterest);
        TextView taxGeneralTextView = (TextView) findViewById(R.id.taxGeneral);

        TextView taxBreaksMonthlyPaymentTextView = (TextView) findViewById(R.id.taxBreaksMonthlyPayment);
        TextView taxBreaksAmountTextView = (TextView) findViewById(R.id.taxBreaksAmount);
        TextView taxBreaksInterestTextView = (TextView) findViewById(R.id.taxBreaksInterest);
        TextView taxBreaksTextView = (TextView) findViewById(R.id.taxBreaks);

        TextView taxFreeMonthlyPaymentTextView = (TextView) findViewById(R.id.taxFreeMonthlyPayment);
        TextView taxFreeAmountTextView = (TextView) findViewById(R.id.taxFreeAmount);
        TextView taxFreeInterestTextView = (TextView) findViewById(R.id.taxFreeInterest);
        TextView taxFreeTextView = (TextView) findViewById(R.id.taxFree);

        double taxGeneral, taxBreaks;
        taxGeneral = taxGeneralInterest*.154;
        taxBreaks = taxBreaksInterest*.095;

        taxGeneralInterest = taxGeneralInterest*.846;
        taxBreaksInterest = taxBreaksInterest*.905;

        //일반과세
        String taxGeneralMonthlyPaymentString = mParse.addComma(mMonthlyPaymentTaxGeneral) + " 원";
        String taxGeneralAmountString = mParse.addComma(mMonthlyPaymentTaxGeneral * period) + " 원";
        String taxGeneralInterestString = mParse.addComma(taxGeneralInterest) + " 원";
        String taxGeneralString = mParse.addComma(taxGeneral) + " 원";
        //세금우대
        String taxBreaksMonthlyPaymentString = mParse.addComma(mMonthlyPaymentTaxBreaks) + " 원";
        String taxBreaksAmountString = mParse.addComma(mMonthlyPaymentTaxBreaks * period) + " 원";
        String taxBreaksInterestString = mParse.addComma(taxBreaksInterest) + " 원";
        String taxBreaksString = mParse.addComma(taxBreaks) + " 원";
        //비과세
        String taxFreeMonthlyPaymentString = mParse.addComma(mMonthlyPaymentTaxFree) + " 원";
        String taxFreeAmountString = mParse.addComma(mMonthlyPaymentTaxFree * period) + " 원";
        String taxFreeInterestString = mParse.addComma(taxFreeInterest) + " 원";
        String taxFreeString = "0 원";

        taxGeneralMonthlyPaymentTextView.setText(taxGeneralMonthlyPaymentString);
        taxGeneralAmountTextView.setText(taxGeneralAmountString);
        taxGeneralInterestTextView.setText(taxGeneralInterestString);
        taxGeneralTextView.setText(taxGeneralString);

        taxBreaksMonthlyPaymentTextView.setText(taxBreaksMonthlyPaymentString);
        taxBreaksAmountTextView.setText(taxBreaksAmountString);
        taxBreaksInterestTextView.setText(taxBreaksInterestString);
        taxBreaksTextView.setText(taxBreaksString);

        taxFreeMonthlyPaymentTextView.setText(taxFreeMonthlyPaymentString);
        taxFreeAmountTextView.setText(taxFreeAmountString);
        taxFreeInterestTextView.setText(taxFreeInterestString);
        taxFreeTextView.setText(taxFreeString);
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

        double simpleDivisorTaxFree = paymentCountMonthly * monthlyRate + period;
        double simpleDivisorTaxGeneral = paymentCountMonthly * monthlyRate * taxGeneralRate + period;
        double simpleDivisorTaxBreaks = paymentCountMonthly * monthlyRate * taxBreaksRate + period;

        double monthlyDivisorTaxFree = monthlyRoot * monthlyMultiplier / monthlyRate;
        double monthlyDivisorTaxGeneral = (monthlyDivisorTaxFree - period) * taxGeneralRate + period;
        double monthlyDivisorTaxBreaks = (monthlyDivisorTaxFree - period) * taxBreaksRate + period;

        double yearlyDivisorTaxFree = yearlyMultiplier / (yearlyConstant - 1);
        double yearlyDivisorTaxGeneral = (yearlyDivisorTaxFree - period) * taxGeneralRate + period;
        double yearlyDivisorTaxBreaks = (yearlyDivisorTaxFree - period) * taxBreaksRate + period;

        switch (mInterestType) {
            case 0: // 단리
                mMonthlyPaymentTaxGeneral = goalAmount / simpleDivisorTaxGeneral;
                mMonthlyPaymentTaxBreaks = goalAmount / simpleDivisorTaxBreaks;
                mMonthlyPaymentTaxFree = goalAmount / simpleDivisorTaxFree;
                break;
            case 1: // 월복리
                mMonthlyPaymentTaxGeneral = goalAmount / monthlyDivisorTaxGeneral;
                mMonthlyPaymentTaxBreaks = goalAmount / monthlyDivisorTaxBreaks;
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