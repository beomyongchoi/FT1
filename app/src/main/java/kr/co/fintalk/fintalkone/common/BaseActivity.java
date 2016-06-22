package kr.co.fintalk.fintalkone.common;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

import kr.co.fintalk.fintalkone.R;

/**
 * Created by beomyong on 6/21/16.
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);

        TextView titleTextView = (TextView) findViewById(R.id.appbarTitle);
        titleTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/GodoM.otf"));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
