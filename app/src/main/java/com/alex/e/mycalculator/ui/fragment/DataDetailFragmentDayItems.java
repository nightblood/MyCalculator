package com.alex.e.mycalculator.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
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
import com.alex.e.mycalculator.utils.Calculator;
import com.alex.e.mycalculator.utils.SPUtils;
import com.alex.e.mycalculator.utils.SizeUtils;
import com.alex.e.mycalculator.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/3/6 0006.
 */
public class DataDetailFragmentDayItems extends BaseFragment {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    LinearLayout mLlRes;
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
        Bundle bundle = getArguments();
        mDate = bundle.getString("data");
        mTvTitle.setText(mDate + TimeUtils.getWeek(mDate + " 00:00:00"));
        SPUtils dataSP = new SPUtils(mDate);
        String data = dataSP.getString("data_list");
        if (TextUtils.isEmpty(data))
            return;
        String[] strings = data.split("@");
        for (int i = 0; i < strings.length; ++i) {
            List<String> nums = getNums(strings[i]);
            mData.add(nums);

            TextView textView = new TextView(getContext());
            textView.setText((i + 1) + "");
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SizeUtils.dp2px(50), SizeUtils.dp2px(20));
            params.gravity = Gravity.CENTER;
            textView.setLayoutParams(params);
            textView.setTextColor(Color.BLACK);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.bg_data_detail);
            mLlTopLine.addView(textView);

//            TextView res = new TextView(getContext());
            String tempRes = caculate(strings[i]);
            mResData.add(tempRes);
//            res.setText(tempRes);
//            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(SizeUtils.dp2px(50), SizeUtils.dp2px(40));
//            params1.gravity = Gravity.CENTER;
//            res.setLayoutParams(params1);
//            res.setTextColor(Color.RED);
//            res.setGravity(Gravity.CENTER);
//            res.setBackgroundResource(R.drawable.bg_data_detail);
//            mLlRes.addView(res);
        }

//        mLvMain.addFooterView(mLlRes);
        mAdapter = new DataDetailAdapter(getContext(), mLlTopLayout);
        mAdapter.setData(mData, mResData);
        mLvMain.setAdapter(mAdapter);

    }

    private List<String> mResData = new ArrayList<>();

    private String caculate(String data) {
        String arg2 = "";
        String arg1 = "";
        char oper = ' ';
        char c;
        for (int i = 0; i < data.length(); ++i) {
            c = data.charAt(i);
            switch (c) {
                case '/':
                case '*':
                case '-':
                case '+':
                    if (TextUtils.isEmpty(arg1)) {
                        arg1 = arg2;
                        oper = c;
                    } else {
                        arg1 = Calculator.calculate(arg1, arg2, oper + "");
                        oper = c;
                    }
                    arg2 = "";
                    break;
                default:
                    arg2 += c;
                    break;
            }
        }
        if (!TextUtils.isEmpty(arg2) && oper != ' ')
            arg1 = Calculator.calculate(arg1, arg2, oper + "");

        return arg1;
    }

    private List<String> getNums(String data) {
        ArrayList<String> res = new ArrayList<>();
        String temp = "";
        char c = ' ';
        for (int i = 0; i < data.length(); ++i) {
            c = data.charAt(i);
            switch (c) {
                case '/':
                case '*':
                case '-':
                case '+':
                    res.add(temp);
                    temp = c + " ";
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
        mLlRes = new LinearLayout(getContext());
        mLlRes.setOrientation(LinearLayout.HORIZONTAL);
        TextView desc = new TextView(getContext());
        desc.setText("结果");
        desc.setTextColor(Color.RED);
        desc.setGravity(Gravity.CENTER);
        desc.setLayoutParams(new LinearLayout.LayoutParams(SizeUtils.dp2px(30), SizeUtils.dp2px(40)));
        desc.setBackgroundResource(R.drawable.bg_data_detail);
        mLlRes.addView(desc);
    }

    @Override
    protected int getContentView() {
        return R.layout.frag_data_detail_day_item;
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
