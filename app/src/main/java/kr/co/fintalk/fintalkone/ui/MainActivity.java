package kr.co.fintalk.fintalkone.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.fintalk.fintalkone.R;
import kr.co.fintalk.fintalkone.common.model.CardDataSet;
import kr.co.fintalk.fintalkone.ui.calculator.CalculatorActivity;
import kr.co.fintalk.fintalkone.common.BaseFragmentActivity;

/**
 * Created by BeomyongChoi on 6/20/16.
 */
public class MainActivity extends BaseFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView titleTextView = (TextView) findViewById(R.id.appbarTitle);
        titleTextView.setText(R.string.main_title);

        ImageView appbarButtonImageView = (ImageView) findViewById(R.id.appbarButton);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //단말기 OS버전이 롤리팝 아래일 때
            appbarButtonImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_white_24dp));
        }
        else{
            //롤리팝 이상
            appbarButtonImageView.setImageDrawable(getDrawable(R.drawable.ic_menu_white_24dp));
        }


        updateListView();
    }

    public void militaryServiceOnClick(View view) {
        showToast("군인공제회 나와라!",2);
    }

    public void calculatorOnClick(View view) {
        Intent intent = new Intent(this, CalculatorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    /**
     * 리스트 뷰 갱신.
     * - ListView Inflate.
     * - Adapter Set.
     */
    private void updateListView() {
        CardDataSet dataSet;
        ArrayList<CardDataSet> dataList = new ArrayList<>();
        String[] cardNames = getResources().getStringArray(R.array.cardNames);
        String[] cardDates = getResources().getStringArray(R.array.cardDates);
        String[] cardSpents = getResources().getStringArray(R.array.cardSpents);
        for (int i = 0; i < cardNames.length; i++) {
            dataSet = new CardDataSet();
            dataSet.setCardName(cardNames[i]);
            dataSet.setCardDate(cardDates[i]);
            dataSet.setCardSpent(cardSpents[i]);
            dataList.add(dataSet);
        }
        CardListViewAdapter adapter = new CardListViewAdapter(
                this,
                R.layout.listview_card_row,
                dataList);
        ListView cardScheduleListView = (ListView) findViewById(R.id.cardScheduleListView);
        final View header = getLayoutInflater().inflate(R.layout.listview_card_header, null, false);
        cardScheduleListView.addHeaderView(header) ;
        cardScheduleListView.setAdapter(adapter);
    }
}