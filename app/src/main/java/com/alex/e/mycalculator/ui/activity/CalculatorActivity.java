package com.alex.e.mycalculator.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alex.e.mycalculator.R;
import com.alex.e.mycalculator.base.BaseActivity;
import com.alex.e.mycalculator.utils.Calculator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.GridViewWithHeaderAndFooter;

/**
 * Created by Administrator on 2017/3/2 0002.
 */
public class CalculatorActivity extends BaseActivity implements Calculator.OnDataChangedListener {

    @BindView(R.id.gv_content)
    GridViewWithHeaderAndFooter mGvCalculator;
    private CalculatorAdapter mAdapter;
    private final String mGvItems[] =
            {"7", "8", "9", "+",
            "4", "5", "6", "-",
            "1", "2", "3", "*",
            "0", ".", "=", "/"};
    private TextView mTvTemp;
    private TextView mTvResult;
    private TextView mTvChat;
    private ListView mLvData;
    private Calculator mCalculator;
    private List<String> mDataNum = new ArrayList<>();
    private List<String> mDataChar = new ArrayList<>();
    private ListView mLvChar;

    @Override
    protected int getContentView() {
        return R.layout.act_calculator;
    }

    @Override
    protected void initView() {
        super.initView();

        View header = LayoutInflater.from(mContext).inflate(R.layout.item_calculator_header, null);
        mTvTemp = (TextView) header.findViewById(R.id.tv_temp);
        mTvChat = (TextView) header.findViewById(R.id.tv_char);
        mTvResult = (TextView) header.findViewById(R.id.tv_result);
        mLvData = (ListView) header.findViewById(R.id.lv_content_data);
        mLvChar = (ListView) header.findViewById(R.id.lv_content_char);
        mGvCalculator.addHeaderView(header);
        mAdapter = new CalculatorAdapter();
        mGvCalculator.setAdapter(mAdapter);

        mGvCalculator.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String temp = (String) parent.getItemAtPosition(position);
                String temp = mGvItems[position];
                if ("+-*/=.".contains(temp)) {
                    mCalculator.onCharClick(temp);
                } else {
                    mCalculator.onNumClick(temp);
                }
            }
        });
        mLvData.setAdapter(new StringAdapter(mDataNum));
        mLvData.setAdapter(new StringAdapter(mDataChar));
    }

    @Override
    protected void initData() {
        super.initData();
        mCalculator = new Calculator(this);
    }

    public static void launch(Context from) {
        Intent intent = new Intent(from, CalculatorActivity.class);
        from.startActivity(intent);
    }

    @Override
    public void onResultChanged(String res) {
        mTvResult.setText(res);
    }

    @Override
    public void onTempChanged(String temp) {
        mTvTemp.setText(temp);
    }

    private class CalculatorAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mGvItems.length;
        }

        @Override
        public Object getItem(int position) {
            return mGvItems[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_calculator, null);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.tv_item);
            textView.setText(mGvItems[position]);
            return convertView;
        }
    }

    private class StringAdapter extends BaseAdapter {
        private List<String> datas;

        public StringAdapter(List<String> datas) {
            this.datas = datas;
        }
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
