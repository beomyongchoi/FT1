package kr.co.fintalk.fintalkone.ui.calculator.saving;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import kr.co.fintalk.fintalkone.R;
import kr.co.fintalk.fintalkone.common.BaseFragmentActivity;

/**
 * Created by BeomyongChoi on 6/23/16
 */
public class SavingFirstActivity extends BaseFragmentActivity {
    public final static String ITEM_TITLE = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_first);

        TextView titleTextView = (TextView) findViewById(R.id.appbarTitle);
        titleTextView.setText(R.string.saving_monthly_amount);
    }


    public void interestTypeOnClick(View view) {
        final CharSequence[] items = new CharSequence[]{"단리", "월복리", "연복리"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
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

}