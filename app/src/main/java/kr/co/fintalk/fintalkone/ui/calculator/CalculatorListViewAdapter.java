package kr.co.fintalk.fintalkone.ui.calculator;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.util.LinkedHashMap;
import java.util.Map;

import kr.co.fintalk.fintalkone.R;

/**
 * Created by BeomyongChoi on 6/21/16.
 */
public class CalculatorListViewAdapter extends BaseAdapter {

//    /**
//     * ListView Row Resource.
//     */
//    private static int mRowResource = 0;
//
//    /**
//     * Context Object.
//     */
//    private Context mContext = null;
//
//    /**
//     * ListView Items.
//     */
//    private ArrayList<CalculatorDataSet> mDataList = new ArrayList<>();
//
//    /**
//     * Constructors.
//     * @param context Context Object.
//     * @param resourceId ListView Row Resource ID.
//     * @param itemList 아이템 리스트.
//     */
//    public CalculatorListViewAdapter(Context context, int resourceId,
//                               ArrayList<CalculatorDataSet> itemList) {
//        mContext = context;
//        mRowResource = resourceId;
//        mDataList = itemList;
//    }
//
//    @Override
//    public void notifyDataSetChanged() {
//        super.notifyDataSetChanged();
//    }
//
//    /*
//     * ListView에서 각 행(row)을 화면에 표시하기 전 호출됨.
//     */
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//		/*
//		 * 데이터를 가져온다.
//		 */
//        CalculatorDataSet dataSet = mDataList.get(position);
//
//		/*
//		 * View 재사용을 위하여 Wrapper Class에 View 적용.
//		 */
//        ViewWrapper viewWrapper;
//        if (convertView == null) {
//            convertView = View.inflate(mContext, mRowResource, null);
//            viewWrapper = new ViewWrapper(convertView);
//            convertView.setTag(viewWrapper);
//        } else {
//            viewWrapper = (ViewWrapper) convertView.getTag();
//        }
//
//		/*
//		 * 데이터 바인딩.
//		 */
//        if (dataSet != null) {
//
//            if (dataSet.isGroup()) {
//                viewWrapper.getHeaderLayout().setVisibility(View.VISIBLE);
//                viewWrapper.getBodyLayout().setVisibility(View.GONE);
//
//                //섹션
//                viewWrapper.getSectionTextView().setText(dataSet.getSectionTitle());
//            } else {
//                viewWrapper.getHeaderLayout().setVisibility(View.GONE);
//                viewWrapper.getBodyLayout().setVisibility(View.VISIBLE);
//
//                //메뉴
//                viewWrapper.getTitleTextView().setText(dataSet.getMenuTitle());
//            }
//        }
//
//        return convertView;
//    }
//
//    @Override
//    public int getCount() {
//        if (mDataList != null) {
//            return mDataList.size();
//        } else {
//            return 0;
//        }
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return mDataList.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    /**
//     * Convert View Wrapper.
//     */
//    private class ViewWrapper {
//        private View mConvertView;
//        private ViewGroup mHeaderLayout;
//        private ViewGroup mBodyLayout;
//        private TextView mSectionTextView;
//        private TextView mTitleTextView;
//
//        /**
//         * Constructors.
//         * @param convertView
//         */
//        public ViewWrapper(View convertView) {
//            mConvertView = convertView;
//        }
//
//        /**
//         * Header Layout.
//         * @return
//         */
//        public ViewGroup getHeaderLayout() {
//            if (mHeaderLayout == null) {
//                mHeaderLayout = (ViewGroup) mConvertView.findViewById(R.id.layoutHeader);
//            }
//            return mHeaderLayout;
//        }
//
//        /**
//         * Body Layout.
//         * @return
//         */
//        public ViewGroup getBodyLayout() {
//            if (mBodyLayout == null) {
//                mBodyLayout = (ViewGroup) mConvertView.findViewById(R.id.layoutBody);
//            }
//            return mBodyLayout;
//        }
//
//        /**
//         * 섹션.
//         * @return
//         */
//        public TextView getSectionTextView() {
//            if (mSectionTextView == null) {
//                mSectionTextView = (TextView) mConvertView.findViewById(R.id.sectionTextView);
//            }
//            return mSectionTextView;
//        }
//
//        /**
//         * 타이틀.
//         * @return
//         */
//        public TextView getTitleTextView() {
//            if (mTitleTextView == null) {
//                mTitleTextView = (TextView) mConvertView.findViewById(R.id.titleTextView);
//            }
//            return mTitleTextView;
//        }
//    }
//}
    public final Map<String,Adapter> sections = new LinkedHashMap<String,Adapter>();
    public final ArrayAdapter<String> headers;
    public final static int TYPE_SECTION_HEADER = 0;

    public CalculatorListViewAdapter(Context context) {
        headers = new ArrayAdapter<String>(context, R.layout.calculator_list_header);
    }

    public void addSection(String section, Adapter adapter) {
        this.headers.add(section);
        this.sections.put(section, adapter);
    }

    public Object getItem(int position) {
        for(Object section : this.sections.keySet()) {
            Adapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;

            // check if position inside this section
            if(position == 0) return section;
            if(position < size) return adapter.getItem(position - 1);

            // otherwise jump into next section
            position -= size;
        }
        return null;
    }

    public int getCount() {
        // total together all sections, plus one for each section header
        int total = 0;
        for(Adapter adapter : this.sections.values())
            total += adapter.getCount() + 1;
        return total;
    }

    public int getViewTypeCount() {
        // assume that headers count as one, then total all sections
        int total = 1;
        for(Adapter adapter : this.sections.values())
            total += adapter.getViewTypeCount();
        return total;
    }

    public int getItemViewType(int position) {
        int type = 1;
        for(Object section : this.sections.keySet()) {
            Adapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;

            // check if position inside this section
            if(position == 0) return TYPE_SECTION_HEADER;
            if(position < size) return type + adapter.getItemViewType(position - 1);

            // otherwise jump into next section
            position -= size;
            type += adapter.getViewTypeCount();
        }
        return -1;
    }

    public boolean areAllItemsSelectable() {
        return false;
    }

    public boolean isEnabled(int position) {
        return (getItemViewType(position) != TYPE_SECTION_HEADER);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int sectionNum = 0;
        for(Object section : this.sections.keySet()) {
            Adapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;

            // check if position inside this section
            if(position == 0) return headers.getView(sectionNum, convertView, parent);
            if(position < size) return adapter.getView(position - 1, convertView, parent);

            // otherwise jump into next section
            position -= size;
            sectionNum++;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}