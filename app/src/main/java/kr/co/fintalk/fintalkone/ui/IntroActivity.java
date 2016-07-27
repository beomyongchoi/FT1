package kr.co.fintalk.fintalkone.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import kr.co.fintalk.fintalkone.R;

public class IntroActivity extends AppCompatActivity {

    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        mHandler= new Handler();
        mHandler.postDelayed(mRun, 800);
    }

    Runnable mRun = new Runnable(){
        @Override
        public void run(){
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    };

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        mHandler.removeCallbacks(mRun);
    }

}
