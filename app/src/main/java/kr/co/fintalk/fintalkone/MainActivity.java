package kr.co.fintalk.fintalkone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import kr.co.fintalk.fintalkone.calculator.CalculatorActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView titleTextView = (TextView) findViewById(R.id.appbarTitle);
        titleTextView.setText(R.string.main_title);

        ListView listview = (ListView)findViewById(R.id.cardScheduleListView);
        final View header = getLayoutInflater().inflate(R.layout.card_schedule_list_header, null, false);

        CardScheduleListViewAdapter adapter = new CardScheduleListViewAdapter();

        listview.addHeaderView(header) ;
        listview.setAdapter(adapter);

        adapter.addItem("신한카드", "매달 26일", "3,000,000원");
        adapter.addItem("국민카드", "매달 26일", "3,000,000원");
        adapter.addItem("국민카드", "매달 26일", "3,000,000원");
        adapter.addItem("국민카드", "매달 26일", "3,000,000원");
        adapter.addItem("국민카드", "매달 26일", "3,000,000원");
        adapter.addItem("국민카드", "매달 26일", "3,000,000원");
        adapter.addItem("국민카드", "매달 26일", "3,000,000원");
        adapter.addItem("국민카드", "매달 26일", "3,000,000원");
    }

    public void militaryServiceOnClick(View view) {
        Toast.makeText(MainActivity.this, "군인공제회액티비티호출 되겠죠 나중에", Toast.LENGTH_SHORT).show();
    }

    public void calculatorOnClick(View view) {
        Intent intent = new Intent(this, CalculatorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}