package kr.co.fintalk.fintalkone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by beomyong on 6/20/16.
 */
public class CardScheduleListViewAdapter extends BaseAdapter{
    private ArrayList<CardScheduleListViewItem> cardScheduleList = new ArrayList<CardScheduleListViewItem>();

    public CardScheduleListViewAdapter() {
    }

    @Override
    public int getCount() {
        return cardScheduleList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.card_schedule_list_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView nameTextView = (TextView) convertView.findViewById(R.id.cardNameTextView);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.cardDateTextView);
        TextView spentTextView = (TextView) convertView.findViewById(R.id.cardSpentTextView);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        CardScheduleListViewItem listViewItem = cardScheduleList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        nameTextView.setText(listViewItem.getCardName());
        dateTextView.setText(listViewItem.getCardDate());
        spentTextView.setText(listViewItem.getCardSpent());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return cardScheduleList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String name, String date, String spent) {
        CardScheduleListViewItem item = new CardScheduleListViewItem();

        item.setCardName(name);
        item.setCardDate(date);
        item.setCardSpent(spent);

        cardScheduleList.add(item);
    }
}

