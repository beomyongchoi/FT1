package kr.co.fintalk.fintalkone.ui.debt.detail;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.oooobang.library.OBParse;

import kr.co.fintalk.fintalkone.R;

/**
 * Created by BeomyongChoi on 6/30/16
 */
public class DebtChartMarkerView extends MarkerView {

    OBParse mParse;

    private TextView tvContent;
    private ImageView tvLeftDot;
    private ImageView tvRightDot;

    public DebtChartMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = (TextView) findViewById(R.id.tvContent);
        tvLeftDot = (ImageView) findViewById(R.id.tvLeftDot);
        tvRightDot = (ImageView) findViewById(R.id.tvRightDot);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setText(mParse.addComma(e.getVal()) + "원"); // set the entry-value as the display text
    }

    @Override
    public int getXOffset(float xPosition) {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        // this will center the marker-view horizontally
        if (xPosition > metrics.widthPixels / 2) {
            tvLeftDot.setVisibility(INVISIBLE);
            tvRightDot.setVisibility(VISIBLE);
            return - getWidth() + (tvLeftDot.getWidth() / 2);
        }
        else {
            tvLeftDot.setVisibility(VISIBLE);
            tvRightDot.setVisibility(INVISIBLE);
            return - tvLeftDot.getWidth() / 2;
        }
    }

    @Override
    public int getYOffset(float yPosition) {
        // this will cause the marker-view to be above the selected value
        return - getHeight() + tvLeftDot.getWidth() / 2;
    }
}