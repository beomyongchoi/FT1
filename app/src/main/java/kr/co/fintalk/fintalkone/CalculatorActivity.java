package kr.co.fintalk.fintalkone;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by beomyong on 6/20/16.
 */

public class CalculatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        setContentView(R.layout.activity_calculator);

        TextView titleTextView = (TextView) findViewById(R.id.textview_title);
        titleTextView.setText(R.string.calculator_title);
        
    }

}