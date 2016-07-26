package kr.co.fintalk.fintalkone.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import kr.co.fintalk.fintalkone.R;
import kr.co.fintalk.fintalkone.ui.debt.DebtFirstActivity;
import kr.co.fintalk.fintalkone.ui.debt.DebtSecondActivity;
import kr.co.fintalk.fintalkone.ui.debt.DebtThirdActivity;
import kr.co.fintalk.fintalkone.ui.investment.InvestmentFirstActivity;
import kr.co.fintalk.fintalkone.ui.investment.InvestmentSecondActivity;
import kr.co.fintalk.fintalkone.ui.investment.InvestmentThirdActivity;
import kr.co.fintalk.fintalkone.ui.saving.SavingFirstActivity;
import kr.co.fintalk.fintalkone.ui.saving.SavingSecondActivity;
import kr.co.fintalk.fintalkone.ui.saving.SavingThirdActivity;

/**
 * Created by BeomyongChoi on 7/22/16
 */
public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.MainListViewHolder>{
    public static class MainListViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;
        private TextView mHeaderTextView;

        private LinearLayout mFirstMenu;
        private LinearLayout mSecondMenu;
        private LinearLayout mThirdMenu;

        private TextView mFirstMenuTextView;
        private TextView mSecondMenuTextView;
        private TextView mThirdMenuTextView;

        MainListViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mHeaderTextView = (TextView) itemView.findViewById(R.id.header);

            mFirstMenu = (LinearLayout) itemView.findViewById(R.id.firstMenu);
            mSecondMenu = (LinearLayout) itemView.findViewById(R.id.secondMenu);
            mThirdMenu = (LinearLayout) itemView.findViewById(R.id.thirdMenu);

            mFirstMenuTextView = (TextView) itemView.findViewById(R.id.firstMenuTextView);
            mSecondMenuTextView = (TextView) itemView.findViewById(R.id.secondMenuTextView);
            mThirdMenuTextView = (TextView) itemView.findViewById(R.id.thirdMenuTextView);

            mFirstMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    switch (getAdapterPosition()) {

                        case 0:
                            intent = new Intent(mContext, SavingFirstActivity.class);
                            break;
                        case 1:
                            intent = new Intent(mContext, InvestmentFirstActivity.class);
                            break;
                        case 2:
                            intent = new Intent(mContext, DebtFirstActivity.class);
                            break;
                        default:
                            intent = new Intent(mContext, SavingFirstActivity.class);
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    mContext.startActivity(intent);
                }
            });

            mSecondMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    switch (getAdapterPosition()) {

                        case 0:
                            intent = new Intent(mContext, SavingSecondActivity.class);
                            break;
                        case 1:
                            intent = new Intent(mContext, InvestmentSecondActivity.class);
                            break;
                        case 2:
                            intent = new Intent(mContext, DebtSecondActivity.class);
                            break;
                        default:
                            intent = new Intent(mContext, SavingSecondActivity.class);
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    mContext.startActivity(intent);
                }
            });

            mThirdMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    switch (getAdapterPosition()) {

                        case 0:
                            intent = new Intent(mContext, SavingThirdActivity.class);
                            break;
                        case 1:
                            intent = new Intent(mContext, InvestmentThirdActivity.class);
                            break;
                        case 2:
                            intent = new Intent(mContext, DebtThirdActivity.class);
                            break;
                        default:
                            intent = new Intent(mContext, SavingThirdActivity.class);
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    private List<MainList> mainLists;

    MainRecyclerViewAdapter(List<MainList> mainLists){
        this.mainLists = mainLists;
    }

    @Override
    public int getItemCount() {
        return mainLists.size();
    }

    @Override
    public MainListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_main, viewGroup, false);
        MainListViewHolder mainListViewHolder = new MainListViewHolder(v);
        return mainListViewHolder;
    }

    @Override
    public void onBindViewHolder(MainListViewHolder mainListViewHolder, int position) {
        mainListViewHolder.mHeaderTextView.setCompoundDrawablesWithIntrinsicBounds(mainLists.get(position).getHeaderIcon(), 0, 0, 0);
        mainListViewHolder.mHeaderTextView.setText(mainLists.get(position).getHeader());
        mainListViewHolder.mFirstMenuTextView.setText(mainLists.get(position).getFirstMenu());
        mainListViewHolder.mSecondMenuTextView.setText(mainLists.get(position).getSecondMenu());
        mainListViewHolder.mThirdMenuTextView.setText(mainLists.get(position).getThirdMenu());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}