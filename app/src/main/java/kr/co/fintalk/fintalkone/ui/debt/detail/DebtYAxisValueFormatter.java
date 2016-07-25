package kr.co.fintalk.fintalkone.ui.debt.detail;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

import java.text.DecimalFormat;

/**
 * Created by BeomyongChoi on 6/30/16
 */
public class DebtYAxisValueFormatter implements YAxisValueFormatter {

    private DecimalFormat mFormat;

    public DebtYAxisValueFormatter() {
        mFormat = new DecimalFormat("#,###"); // use one decimal
    }

    @Override
    public String getFormattedValue(float value, YAxis yAxis) {
        // write your logic here
        // access the YAxis object to get more information
        if(value >= 100000000) {
            return mFormat.format(value / 100000000) + "억원";
        }
        else if(value >= 10000000) {
            return mFormat.format(value / 10000000) + "천만원";
        }
        else if(value >= 1000000) {
            return mFormat.format(value / 1000000) + "백만원";
        }
        else {
            return mFormat.format(value / 10000) + " 만원"; // e.g. append a dollar-sign
        }
    }
}