package kr.co.fintalk.fintalkone.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        private Context context;
        public TextView header;
        public TextView firstMenu;
        public TextView secondMenu;
        public TextView thirdMenu;

        MainListViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            header = (TextView)itemView.findViewById(R.id.header);
            firstMenu = (TextView)itemView.findViewById(R.id.firstMenu);
            secondMenu = (TextView)itemView.findViewById(R.id.secondMenu);
            thirdMenu = (TextView)itemView.findViewById(R.id.thirdMenu);

            firstMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    switch (getAdapterPosition()) {

                        case 0:
                            intent = new Intent(context, SavingFirstActivity.class);
                            break;
                        case 1:
                            intent = new Intent(context, InvestmentFirstActivity.class);
                            break;
                        case 2:
                            intent = new Intent(context, DebtFirstActivity.class);
                            break;
                        default:
                            intent = new Intent(context, SavingFirstActivity.class);
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(intent);
                }
            });

            secondMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    switch (getAdapterPosition()) {

                        case 0:
                            intent = new Intent(context, SavingSecondActivity.class);
                            break;
                        case 1:
                            intent = new Intent(context, InvestmentSecondActivity.class);
                            break;
                        case 2:
                            intent = new Intent(context, DebtSecondActivity.class);
                            break;
                        default:
                            intent = new Intent(context, SavingSecondActivity.class);
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(intent);
                }
            });

            thirdMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    switch (getAdapterPosition()) {

                        case 0:
                            intent = new Intent(context, SavingThirdActivity.class);
                            break;
                        case 1:
                            intent = new Intent(context, InvestmentThirdActivity.class);
                            break;
                        case 2:
                            intent = new Intent(context, DebtThirdActivity.class);
                            break;
                        default:
                            intent = new Intent(context, SavingThirdActivity.class);
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(intent);
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
        mainListViewHolder.header.setCompoundDrawablesWithIntrinsicBounds(mainLists.get(position).getHeaderIcon(), 0, 0, 0);
        mainListViewHolder.header.setText(mainLists.get(position).getHeader());
        mainListViewHolder.firstMenu.setText(mainLists.get(position).getFirstMenu());
        mainListViewHolder.secondMenu.setText(mainLists.get(position).getSecondMenu());
        mainListViewHolder.thirdMenu.setText(mainLists.get(position).getThirdMenu());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}