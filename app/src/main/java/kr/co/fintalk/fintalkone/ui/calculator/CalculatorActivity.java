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
import kr.co.fintalk.fintalkone.ui.calculator.debt.DebtFirstActivity;
import kr.co.fintalk.fintalkone.ui.calculator.debt.DebtSecondActivity;
import kr.co.fintalk.fintalkone.ui.calculator.debt.DebtThirdActivity;
import kr.co.fintalk.fintalkone.ui.calculator.investment.InvestmentFirstActivity;
import kr.co.fintalk.fintalkone.ui.calculator.investment.InvestmentSecondActivity;
import kr.co.fintalk.fintalkone.ui.calculator.investment.InvestmentThirdActivity;
import kr.co.fintalk.fintalkone.ui.calculator.saving.SavingFirstActivity;
import kr.co.fintalk.fintalkone.ui.calculator.saving.SavingSecondActivity;
import kr.co.fintalk.fintalkone.ui.calculator.saving.SavingThirdActivity;

/**
 * Created by BeomyongChoi on 6/20/16.
 */

public class CalculatorActivity extends BaseFragmentActivity {
    public final static String ITEM_TITLE = "title";

    private CalculatorListViewAdapter mAdapter;
    private List<Map<String,?>> saving, investment, debt;

    private String[] from = { ITEM_TITLE };
    private int[] to = new int[] {R.id.calculatorListSectionTitle};

    public Map<String,?> createItem(String title) {
        Map<String,String> item = new HashMap<>();
        item.put(ITEM_TITLE, title);
        return item;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        TextView titleTextView = (TextView) findViewById(R.id.appbarTitle);
        titleTextView.setText(R.string.calculator_title);

        updateListView();
    }

    private void updateListView() {
        saving = new LinkedList<>();
        saving.add(createItem("단기 목돈모으기(적금:월적금액)"));
        saving.add(createItem("단기 목돈모으기(적금:만기금액)"));
        saving.add(createItem("단기 목돈굴리기(예금)"));

        investment = new LinkedList<>();
        investment.add(createItem("중장기 목돈모으기(월적립액)"));
        investment.add(createItem("중장기 목돈모으기(목표금액)"));
        investment.add(createItem("중장기 목돈투자"));

        debt = new LinkedList<>();
        debt.add(createItem("만기일시상환"));
        debt.add(createItem("원금균등상환"));
        debt.add(createItem("원리금균등상환"));

        // create our list and custom adapter
        mAdapter = new CalculatorListViewAdapter(this);

        mAdapter.addSection("저축", new SimpleAdapter(this, saving,
                R.layout.listview_calculator_row, from, to));
        mAdapter.addSection("투자", new SimpleAdapter(this, investment,
                R.layout.listview_calculator_row, from, to));
        mAdapter.addSection("대출상환", new SimpleAdapter(this, debt,
                R.layout.listview_calculator_row, from, to));

        ListView list = (ListView) findViewById(R.id.calculatorListView);
        list.setAdapter(mAdapter);
        list.setOnItemClickListener(mItemClickListener);
    }

    AdapterView.OnItemClickListener mItemClickListener =
            new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent;
                    switch (position) {
                        case 1:
                            intent = new Intent(CalculatorActivity.this, SavingFirstActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            break;
                        case 2:
                            intent = new Intent(CalculatorActivity.this, SavingSecondActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            break;
                        case 3:
                            intent = new Intent(CalculatorActivity.this, SavingThirdActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            break;
                        case 5:
                            intent = new Intent(CalculatorActivity.this, InvestmentFirstActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            break;
                        case 6:
                            intent = new Intent(CalculatorActivity.this, InvestmentSecondActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            break;
                        case 7:
                            intent = new Intent(CalculatorActivity.this, InvestmentThirdActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            break;
                        case 9:
                            intent = new Intent(CalculatorActivity.this, DebtFirstActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            break;
                        case 10:
                            intent = new Intent(CalculatorActivity.this, DebtSecondActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            break;
                        case 11:
                            intent = new Intent(CalculatorActivity.this, DebtThirdActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            break;
                        default:
                            intent = new Intent(CalculatorActivity.this, SavingFirstActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            break;
                    }
                }
            };
}