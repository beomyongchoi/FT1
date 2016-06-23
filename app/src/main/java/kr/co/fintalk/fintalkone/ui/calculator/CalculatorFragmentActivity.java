package kr.co.fintalk.fintalkone.ui.calculator;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import kr.co.fintalk.fintalkone.R;
import kr.co.fintalk.fintalkone.common.BaseFragmentActivity;
import kr.co.fintalk.fintalkone.common.CurrencyTextWatcher;
import kr.co.fintalk.fintalkone.ui.calculator.saving.SavingFirstFragment;
import kr.co.fintalk.fintalkone.ui.calculator.saving.SavingSecondFragment;
import kr.co.fintalk.fintalkone.ui.calculator.saving.SavingThirdFragment;

/**
 * Created by BeomyongChoi on 6/21/16.
 */
public class CalculatorFragmentActivity extends BaseFragmentActivity {
    int position = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_fragment);

        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");

        TextView titleTextView = (TextView) findViewById(R.id.appbarTitle);
        selectedFragment(titleTextView);
    }

    public void selectedFragment(TextView titleTextView){
        Fragment fragment = null;

        switch (position) {
            case 1:
                titleTextView.setText(R.string.saving_monthly_amount);
                fragment = new SavingFirstFragment();
                break;
            case 2:
                titleTextView.setText(R.string.saving_total_amount);
                fragment = new SavingSecondFragment();
                break;
            case 3:
                titleTextView.setText(R.string.saving_deposit);
                fragment = new SavingThirdFragment();
                break;
            case 5:
                titleTextView.setText(R.string.investment_monthly_amount);
                break;
            case 6:
                titleTextView.setText(R.string.investment_total_amount);
                break;
            case 7:
                titleTextView.setText(R.string.investment_deposit);
                break;
            case 9:
                titleTextView.setText(R.string.debt_maturity);
                break;
            case 10:
                titleTextView.setText(R.string.debt_divide_balance);
                break;
            case 11:
                titleTextView.setText(R.string.debt_divide_monthly);
                break;
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.calculatorInputFragment, fragment);
        fragmentTransaction.commit();
    }

    public void interestTypeOnClick(View view) {
        final CharSequence[] items = new CharSequence[]{"단리", "월복리", "연복리"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(CalculatorFragmentActivity.this);
        dialog.setTitle(R.string.interest_type);
        dialog.setItems(items,new DialogInterface.OnClickListener() {
            // 리스트 선택 시 이벤트
            public void onClick(DialogInterface dialog, int id) {
                TextView interestTypeTextView = (TextView) findViewById(R.id.firstSavingInterestTypeTextView);
                interestTypeTextView.setText(items[id]);
            }
        });
        dialog.show();
    }

    public static String addComma(Editable editable) {
        try {
            NumberFormat numberFormat = new DecimalFormat("#,###");
            return numberFormat.format(editable);
        } catch (NumberFormatException ex) {
            return String.valueOf(editable);
        } catch (Exception ex) {
            return String.valueOf(editable);
        }
    }

}
