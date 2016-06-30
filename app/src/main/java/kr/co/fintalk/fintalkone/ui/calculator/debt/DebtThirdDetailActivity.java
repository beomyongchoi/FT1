package kr.co.fintalk.fintalkone.ui.calculator.debt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.oooobang.library.OBParse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kr.co.fintalk.fintalkone.R;
import kr.co.fintalk.fintalkone.common.BaseFragmentActivity;
import kr.co.fintalk.fintalkone.common.FTConstants;

/**
 * Created by BeomyongChoi on 6/29/16
 */
public class DebtThirdDetailActivity extends BaseFragmentActivity {
    OBParse mParse = new OBParse();

    public int mIndex;
    public double mMonthlyRepayment;
    ArrayList<Double> mRemainingDebt;
    ArrayList<Double> mMonthlyInterest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt_third_detail);

        TextView titleTextView = (TextView) findViewById(R.id.appbarTitle);
        titleTextView.setText(R.string.monthly_schedule);

        Intent intent = getIntent();

        mIndex = intent.getExtras().getInt("index");
        mMonthlyRepayment = intent.getExtras().getDouble("monthlyRepayment");
        mRemainingDebt = (ArrayList<Double>) intent.getSerializableExtra("remainingDebt");
        mMonthlyInterest = (ArrayList<Double>) intent.getSerializableExtra("monthlyInterest");

        setListView(0);
    }

    public void indexOnClick(View view) {
        final CharSequence[] items = new CharSequence[mIndex];
        for(int index = 0; index < mIndex; index++) {
            items[index] = index + 1 + "회차";
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.index_selection);
        dialog.setItems(items,new DialogInterface.OnClickListener() {
            // 리스트 선택 시 이벤트
            public void onClick(DialogInterface dialog, int id) {
                TextView indexTextView = (TextView) findViewById(R.id.indexTextView);
                indexTextView.setText(items[id]);
                setListView(id);
            }
        });
        dialog.show();
    }

    public Map<String,?> createItem(String title, String contents) {
        Map<String,String> item = new HashMap<>();
        item.put(FTConstants.ITEM_TITLE, title);
        item.put(FTConstants.ITEM_CONTENTS, contents);
        return item;
    }

    public void setListView(int index) {
        int id = index + 1;
        String payment = mParse.addComma(mMonthlyRepayment) + "원";
        String principalPayment = mParse.addComma(mMonthlyRepayment - mMonthlyInterest.get(index)) + "원";
        String interest = mParse.addComma(mMonthlyInterest.get(index)) + "원";
        String principalSum = mParse.addComma(mMonthlyRepayment * id - sumOfInterest(mMonthlyInterest, id)) + "원";
        String remainingDebt = mParse.addComma(mRemainingDebt.get(index)) + "원";

        List<Map<String,?>> resultList = new LinkedList<>();
        resultList.add(createItem("상환금", payment));
        resultList.add(createItem("납입원금", principalPayment));
        resultList.add(createItem("이자", interest));
        resultList.add(createItem("납입원금합계", principalSum));
        resultList.add(createItem("잔금", remainingDebt));

        // create our list and custom adapter
        DebtDetailListViewAdapter adapter = new DebtDetailListViewAdapter(this);

        String[] from = { FTConstants.ITEM_TITLE, FTConstants.ITEM_CONTENTS };
        int[] to = new int[] {R.id.debtDetailTitle, R.id.debtDetailContents};

        adapter.addSection(id + " 회차", new SimpleAdapter(this, resultList,
                R.layout.listview_debt_detail_row, from, to));

        ListView list = (ListView) findViewById(R.id.debtDetailListView);
        list.setAdapter(adapter);
    }

    public static double sumOfInterest(List<Double> doubles, int id) {
        double sum = 0;
        for(int index = 0; index < id; index++){
            sum += doubles.get(index);
        }
        return sum;
    }
}