package kr.co.fintalk.fintalkone.calculator;

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
import kr.co.fintalk.fintalkone.common.BaseActivity;

/**
 * Created by beomyong on 6/20/16.
 */

public class CalculatorActivity extends BaseActivity {
    public final static String ITEM_TITLE = "title";
    public final static String ITEM_ID = "0";

    public Map<String,?> createItem(String title, String id) {
        Map<String,String> item = new HashMap<String,String>();
        item.put(ITEM_TITLE, title);
        item.put(ITEM_ID, id);
        return item;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        TextView titleTextView = (TextView) findViewById(R.id.appbarTitle);
        titleTextView.setText(R.string.calculator_title);

        List<Map<String,?>> saving = new LinkedList<Map<String,?>>();
        saving.add(createItem("단기 목돈모으기(적금:월적금액)", "0"));
        saving.add(createItem("단기 목돈모으기(적금:만기금액)", "1"));
        saving.add(createItem("단기 목돈굴리기(예금)", "2"));

        List<Map<String,?>> investment = new LinkedList<Map<String,?>>();
        investment.add(createItem("중장기 목돈모으기(월적립액)", "3"));
        investment.add(createItem("중장기 목돈모으기(목표금액)", "4"));
        investment.add(createItem("중장기 목돈투자", "5"));

        List<Map<String,?>> debt = new LinkedList<Map<String,?>>();
        debt.add(createItem("만기일시상환", "7"));
        debt.add(createItem("원금균등상환", "8"));
        debt.add(createItem("원리금균등상환", "9"));

        // create our list and custom adapter
        CalculatorListViewAdapter adapter = new CalculatorListViewAdapter(this);

        adapter.addSection("저축", new SimpleAdapter(this, saving,
                R.layout.calculator_list_item, new String[]{ ITEM_TITLE }, new int[] { R.id.calculatorListSectionTitle }));
        adapter.addSection("투자", new SimpleAdapter(this, investment,
                R.layout.calculator_list_item, new String[]{ ITEM_TITLE }, new int[] { R.id.calculatorListSectionTitle }));
        adapter.addSection("대출상환", new SimpleAdapter(this, debt,
                R.layout.calculator_list_item, new String[]{ ITEM_TITLE }, new int[] { R.id.calculatorListSectionTitle }));

        ListView list = new ListView(this);
        list.setAdapter(adapter);
        this.setContentView(list);
        list.setOnItemClickListener(mItemClickListener);
    }

    AdapterView.OnItemClickListener mItemClickListener =
            new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(CalculatorActivity.this, CalculatorFragmentActivity.class);
                    intent.putExtra("position",position);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            };
}