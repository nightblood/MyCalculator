package com.alex.e.mycalculator.ui.fragment;

import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alex.e.mycalculator.R;
import com.alex.e.mycalculator.base.BaseFragment;
import com.alex.e.mycalculator.ui.adapter.DataDetailAdapter;
import com.alex.e.mycalculator.ui.widge.MyHScrollView;
import com.alex.e.mycalculator.utils.SPUtils;
import com.alex.e.mycalculator.utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/3/6 0006.
 */
public class DataDetailFragmentDayItems extends BaseFragment {


    @BindView(R.id.horizontalScrollView_excel)
    MyHScrollView mHScrollView;
    @BindView(R.id.ll_top_line)
    LinearLayout mLlTopLine;
    @BindView(R.id.listView)
    ListView mLvMain;
    @BindView(R.id.toplayout)
    LinearLayout mLlTopLayout;

    private String mDate;
    private List<List<String>> mData = new ArrayList<>();
    private DataDetailAdapter mAdapter;

    @Override
    protected void initData() {
        SPUtils dataSP = new SPUtils("DataList");
        String data = dataSP.getString(mDate);
        if (TextUtils.isEmpty(data))
            return;
        String[] strings = data.split("@");
        for (int i = 0; i < strings.length; ++i) {
            List<String> nums = getNums(strings[i]);
            mData.add(nums);

            TextView textView = new TextView(getContext());
            textView.setText((i + 1) + "");
            textView.setLayoutParams(new LinearLayout.LayoutParams(SizeUtils.dp2px(80), SizeUtils.dp2px(40)));
            mLlTopLine.addView(textView);
        }

        mAdapter = new DataDetailAdapter(getContext(), mLlTopLayout);
        mLvMain.setAdapter(mAdapter);

    }

    private List<String> getNums(String data) {
        ArrayList<String> res = new ArrayList<>();
        String temp = "";
        for (int i = 0; i < data.length(); ++i) {
            char c = data.charAt(i);
            switch (c) {
                case '/':
                case '*':
                case '-':
                case '+':
                    res.add(c + " " +temp);
                    break;
                default:
                    temp += c;
                    break;
            }
        }
        if (!TextUtils.isEmpty(temp))
            res.add(temp);
        return res;
    }

    @Override
    protected void initView() {
        mLvMain.setOnTouchListener(onTouchListener);
        mLvMain.setOnItemClickListener(onItemClickListener);
    }

    @Override
    protected int getContentView() {
        return R.layout.frag_data_detail_one_item;
    }

    public static boolean isTouchItem = false;
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            if (isTouchItem == false)
                return;

            Toast.makeText(getContext(), "你点击的是:" + position + "这个下标", Toast.LENGTH_LONG).show();

        }

    };

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (v.getId()) {
                case R.id.toplayout:
                    mHScrollView.onTouchEvent(event);
                    return false;
                case R.id.listView:
                    mHScrollView.onTouchEvent(event);
                    return false;

            }
            return false;
        }

    };
}
