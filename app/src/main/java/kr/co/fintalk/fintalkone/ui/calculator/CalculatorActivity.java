package kr.co.fintalk.fintalkone.ui.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kr.co.fintalk.fintalkone.R;
import kr.co.fintalk.fintalkone.common.BaseFragmentActivity;
import kr.co.fintalk.fintalkone.ui.calculator.saving.SavingFirstActivity;

/**
 * Created by BeomyongChoi on 6/20/16.
 */

public class CalculatorActivity extends BaseFragmentActivity {
    public final static String ITEM_TITLE = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        TextView titleTextView = (TextView) findViewById(R.id.appbarTitle);
        titleTextView.setText(R.string.calculator_title);

        setListView();
    }

    public Map<String,?> createItem(String title) {
        Map<String,String> item = new HashMap<>();
        item.put(ITEM_TITLE, title);
        return item;
    }

    public void setListView() {
        List<Map<String,?>> saving = new LinkedList<>();
        saving.add(createItem("단기 목돈모으기(적금:월적금액)"));
        saving.add(createItem("단기 목돈모으기(적금:만기금액)"));
        saving.add(createItem("단기 목돈굴리기(예금)"));

        List<Map<String,?>> investment = new LinkedList<>();
        investment.add(createItem("중장기 목돈모으기(월적립액)"));
        investment.add(createItem("중장기 목돈모으기(목표금액)"));
        investment.add(createItem("중장기 목돈투자"));

        List<Map<String,?>> debt = new LinkedList<>();
        debt.add(createItem("만기일시상환"));
        debt.add(createItem("원금균등상환"));
        debt.add(createItem("원리금균등상환"));

        // create our list and custom adapter
        CalculatorListViewAdapter adapter = new CalculatorListViewAdapter(this);

        String[] from = { ITEM_TITLE };
        int[] to = new int[] {R.id.calculatorListSectionTitle};

        adapter.addSection("저축", new SimpleAdapter(this, saving,
                R.layout.listview_row_calculator, from, to));
        adapter.addSection("투자", new SimpleAdapter(this, investment,
                R.layout.listview_row_calculator, from, to));
        adapter.addSection("대출상환", new SimpleAdapter(this, debt,
                R.layout.listview_row_calculator, from, to));

        ListView list = new ListView(this);
        list.setAdapter(adapter);
        this.setContentView(list);
        list.setOnItemClickListener(mItemClickListener);
    }

    AdapterView.OnItemClickListener mItemClickListener =
            new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(CalculatorActivity.this, SavingFirstActivity.class);
                    intent.putExtra("position",position);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            };
}