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
/**
 * Created by BeomyongChoi on 6/23/16
 */
public class SavingFirstActivity extends BaseFragmentActivity {
    Toolbar toolbar;

    OBParse mParse = new OBParse();

    ClearEditText mMonthlyPaymentEditText;
    ClearEditText mGoalPeriodEditText;
    ClearEditText mInterestRateEditText;

    Button mCalculatorButton;

    public int mInterestType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_first);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView titleTextView = (TextView) findViewById(R.id.toolbarTitle);
        titleTextView.setText(R.string.saving_monthly_payment);
        titleTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/GodoM.otf"));

        ImageView toolbarBackButton = (ImageView) findViewById(R.id.toolbarBackButton);

        toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mMonthlyPaymentEditText = (ClearEditText) findViewById(R.id.savingMonthlyPaymentEditText);
        mGoalPeriodEditText = (ClearEditText) findViewById(R.id.savingGoalPeriodEditText);
        mInterestRateEditText = (ClearEditText) findViewById(R.id.savingInterestRateEditText);

        mMonthlyPaymentEditText.setWon(true);
        mGoalPeriodEditText.setPeriod(true);
        mGoalPeriodEditText.setSaving(true);
        mInterestRateEditText.setRate(true);

        mMonthlyPaymentEditText.init();
        mGoalPeriodEditText.init();
        mInterestRateEditText.init();

        mCalculatorButton = (Button) findViewById(R.id.calculatorButton);

        mInterestRateEditText.setFilters(new InputFilter[]{new kr.co.fintalk.fintalkone.common.DecimalDigitsInputFilter(3, 3)});
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
//
//        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
//
//        mShowFabButton = (FloatingActionButton) findViewById(R.id.fab);
//        mShowFabButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                calculateSavingOnClick(view);
//            }
//        });

        setResultHeader("0 원");
        setCardView(0, 0);
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
        String monthlyPaymentString = mMonthlyPaymentEditText.getText().toString().replace("원","").replace(",","");
        String goalPeriodString = mGoalPeriodEditText.getText().toString().replace("개월","");
        String interestRateString = mInterestRateEditText.getText().toString().replace("%","");

        double monthlyPayment = mParse.toDouble(monthlyPaymentString);
        double goalPeriod = mParse.toDouble(goalPeriodString);
        double interestRate = mParse.toDouble(interestRateString);

        double resultPrincipal;
        double resultInterest;

        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        if(monthlyPaymentString.length() != 0
                && goalPeriodString.length() != 0
                && interestRateString.length() != 0) {
            inputMethodManager.hideSoftInputFromWindow(mInterestRateEditText.getWindowToken(), 0);
            mMonthlyPaymentEditText.clearFocus();
            mGoalPeriodEditText.clearFocus();
            mInterestRateEditText.clearFocus();
            resultPrincipal = monthlyPayment * goalPeriod;
            resultInterest = calculatorInterest(monthlyPayment, goalPeriod, interestRate);
            setResultHeader(mParse.addComma(resultPrincipal) + "원");
            setCardView(resultPrincipal, resultInterest);
        }
        else {
            showToast(R.string.all_fields_required_toast, 2);
        }
    }

    public void setResultHeader(String principal) {
        TextView titleTextView = (TextView) findViewById(R.id.resultTitleTextView);
        TextView captionTextView = (TextView) findViewById(R.id.resultCaptionTextView);
        TextView amountTextView = (TextView) findViewById(R.id.resultAmountTextView);

        titleTextView.setText(R.string.result_calculate);
        captionTextView.setText(R.string.principal);
        amountTextView.setText(principal);
    }

    public void setCardView(double resultPrincipal, double taxFreeInterest) {
        TextView taxGeneralAmountTextView = (TextView) findViewById(R.id.taxGeneralAmount);
        TextView taxGeneralInterestTextView = (TextView) findViewById(R.id.taxGeneralInterest);
        TextView taxGeneralTextView = (TextView) findViewById(R.id.taxGeneral);

        TextView taxBreaksAmountTextView = (TextView) findViewById(R.id.taxBreaksAmount);
        TextView taxBreaksInterestTextView = (TextView) findViewById(R.id.taxBreaksInterest);
        TextView taxBreaksTextView = (TextView) findViewById(R.id.taxBreaks);

        TextView taxFreeAmountTextView = (TextView) findViewById(R.id.taxFreeAmount);
        TextView taxFreeInterestTextView = (TextView) findViewById(R.id.taxFreeInterest);
        TextView taxFreeTextView = (TextView) findViewById(R.id.taxFree);

        double taxGeneralInterest, taxBreaksInterest;
        double taxGeneral, taxBreaks;

        taxGeneralInterest = taxFreeInterest*.846;
        taxBreaksInterest = taxFreeInterest*.905;

        taxGeneral = taxFreeInterest*.154;
        taxBreaks = taxFreeInterest*.095;

        //일반과세
        String taxGeneralAmountString = mParse.addComma(resultPrincipal + taxGeneralInterest) + " 원";
        String taxGeneralInterestString = mParse.addComma(taxGeneralInterest) + " 원";
        String taxGeneralString = mParse.addComma(taxGeneral) + " 원";
        //세금우대
        String taxBreaksAmountString = mParse.addComma(resultPrincipal + taxBreaksInterest) + " 원";
        String taxBreaksInterestString = mParse.addComma(taxBreaksInterest) + " 원";
        String taxBreaksString = mParse.addComma(taxBreaks) + " 원";
        //비과세
        String taxFreeAmountString = mParse.addComma(resultPrincipal + taxFreeInterest) + " 원";
        String taxFreeInterestString = mParse.addComma(taxFreeInterest) + " 원";
        String taxFreeString = "0 원";

        taxGeneralAmountTextView.setText(taxGeneralAmountString);
        taxGeneralInterestTextView.setText(taxGeneralInterestString);
        taxGeneralTextView.setText(taxGeneralString);

        taxBreaksAmountTextView.setText(taxBreaksAmountString);
        taxBreaksInterestTextView.setText(taxBreaksInterestString);
        taxBreaksTextView.setText(taxBreaksString);

        taxFreeAmountTextView.setText(taxFreeAmountString);
        taxFreeInterestTextView.setText(taxFreeInterestString);
        taxFreeTextView.setText(taxFreeString);
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