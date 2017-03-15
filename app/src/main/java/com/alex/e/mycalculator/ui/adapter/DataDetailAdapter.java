package com.alex.e.mycalculator.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alex.e.mycalculator.R;
import com.alex.e.mycalculator.ui.widge.MyHScrollView;

import java.util.List;


public class DataDetailAdapter extends BaseAdapter {
    public List<List<String>> mData;
    private Context context;
    private View top_layout;

    public DataDetailAdapter(Context context, View top_layout) {
        this.context = context;
        this.top_layout = top_layout;
    }

    public synchronized void setData(List<List<String>> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return getMaxItem();
    }

    private int getMaxItem() {
        int num = 0;
        for (int i = 0; i < mData.size(); ++i) {
            if (num < mData.get(i).size())
                num = mData.get(i).size();
        }
        return num;
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LinearLayout.inflate(context, R.layout.item_data_excel, null);
        LinearLayout llMain = (LinearLayout) convertView.findViewById(R.id.ll_content);
        MyHScrollView horizontalScrollView_item_excel_chart = (MyHScrollView) convertView.findViewById(R.id.horizontalScrollView_excel);
        MyHScrollView headSrcrollView = (MyHScrollView) top_layout.findViewById(R.id.horizontalScrollView_excel);
        headSrcrollView.AddScrollViewObserver(new ScrollViewObserverImp(horizontalScrollView_item_excel_chart));
        TextView textView = (TextView) convertView.findViewById(R.id.tv_item_first);
        int index = position + 1;
        textView.setText(index + "");

        for (int i = 0; i < mData.size(); ++i) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_excel_atom, null);
            TextView text = (TextView) view.findViewById(R.id.tv_item);
            text.setText(position < mData.get(i).size() ? mData.get(i).get(position) : "");
            llMain.addView(view);

        }

        return convertView;
    }


    class ScrollViewObserverImp implements MyHScrollView.ScrollViewObserver {
        MyHScrollView mScrollViewArg;

        public ScrollViewObserverImp(MyHScrollView scrollViewar) {
            mScrollViewArg = scrollViewar;
        }

        @Override
        public void onScrollChanged(int l, int t, int oldl, int oldt) {
            mScrollViewArg.smoothScrollTo(l, t);
        }
    }


}
