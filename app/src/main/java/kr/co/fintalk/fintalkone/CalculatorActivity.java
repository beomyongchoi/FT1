package kr.co.fintalk.fintalkone;

import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by beomyong on 6/20/16.
 */

public class CalculatorActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        TextView titleTextView = (TextView) findViewById(R.id.textview_title);
        titleTextView.setText(R.string.calculator_title);
    }

}