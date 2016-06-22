package kr.co.fintalk.fintalkone.calculator;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import kr.co.fintalk.fintalkone.R;
import kr.co.fintalk.fintalkone.calculator.saving.SavingFirstFragment;
import kr.co.fintalk.fintalkone.calculator.saving.SavingSecondFragment;
import kr.co.fintalk.fintalkone.calculator.saving.SavingThirdFragment;
import kr.co.fintalk.fintalkone.common.BaseActivity;

/**
 * Created by beomyong on 6/21/16.
 */
public class CalculatorFragmentActivity extends BaseActivity {
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
        fragmentTransaction.replace(R.id.calculatorFragments, fragment);
        fragmentTransaction.commit();
    }

}
