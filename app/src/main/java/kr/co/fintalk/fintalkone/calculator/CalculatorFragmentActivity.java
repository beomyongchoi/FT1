package kr.co.fintalk.fintalkone.calculator;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import kr.co.fintalk.fintalkone.BaseActivity;
import kr.co.fintalk.fintalkone.R;
import kr.co.fintalk.fintalkone.calculator.saving.SavingFirstFragment;
import kr.co.fintalk.fintalkone.calculator.saving.SavingSecondFragment;

/**
 * Created by beomyong on 6/21/16.
 */
public class CalculatorFragmentActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_fragment);

        TextView titleTextView = (TextView) findViewById(R.id.appbarTitle);
        titleTextView.setText(R.string.calculator_title);
    }

    public void selectFrag(View view){
        Fragment fragment;

        if(view == findViewById(R.id.button2)){
            fragment = new SavingSecondFragment();
        }else {
            fragment = new SavingFirstFragment();
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, fragment);
        fragmentTransaction.commit();
    }

}
