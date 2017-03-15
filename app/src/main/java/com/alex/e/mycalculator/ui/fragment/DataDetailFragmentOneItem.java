package com.alex.e.mycalculator.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alex.e.mycalculator.R;
import com.alex.e.mycalculator.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/3/6 0006.
 */
public class DataDetailFragmentOneItem extends BaseFragment {

    @BindView(R.id.lv_main)
    ListView mLvMain;
    private List<String> mData = new ArrayList<>();

    @Override
    protected void initData() {
        Bundle bundle = getArguments();

        String temp = "";
        String data = bundle.getString("data");
        for (int i = 0; i < data.length(); ++i) {
            char c = data.charAt(i);
            switch (c) {
                case '+':
                case '-':
                case '*':
                case '/':
                    mData.add(temp);
                    temp = c + "";
                    break;
                default:
                    temp += c;
                    break;
            }
        }
        if (!TextUtils.isEmpty(temp))
            mData.add(temp);
    }

    @Override
    protected void initView() {
        mLvMain.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mData.size();
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
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_data_detail, null);
                }
                TextView id = (TextView) convertView.findViewById(R.id.tv_id);
                TextView ch = (TextView) convertView.findViewById(R.id.tv_char);
                TextView num = (TextView) convertView.findViewById(R.id.tv_num);
                id.setText((position + 1) + "");
                num.setText(mData.get(position));
                return convertView;
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.frag_data_detail_one_item;
    }
}
