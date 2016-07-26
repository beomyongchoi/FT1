package kr.co.fintalk.fintalkone.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kr.co.fintalk.fintalkone.R;
import kr.co.fintalk.fintalkone.common.BaseFragmentActivity;

/**
 * Created by BeomyongChoi on 6/20/16.
 */

public class MainActivity extends BaseFragmentActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<MainList> mainLists;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView titleTextView = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        titleTextView.setText(R.string.calculator_title);
        titleTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/GodoM.otf"));

        ImageView toolbarBackButtonImageView = (ImageView) toolbar.findViewById(R.id.toolbarBackButton);
        toolbarBackButtonImageView.setVisibility(View.INVISIBLE);

        initializeData();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MainRecyclerViewAdapter(mainLists);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initializeData(){
        mainLists = new ArrayList<>();
        mainLists.add(new MainList(R.string.saving_title, R.drawable.icon_saving, R.string.saving_monthly_payment, R.string.saving_goal_amount, R.string.saving_deposit));
        mainLists.add(new MainList(R.string.investment_title, R.drawable.icon_investment, R.string.investment_monthly_payment, R.string.investment_goal_amount, R.string.investment_deposit));
        mainLists.add(new MainList(R.string.debt_title, R.drawable.icon_debt, R.string.debt_maturity, R.string.debt_divide_balance, R.string.debt_divide_monthly));
    }
}