package com.alex.e.mycalculator.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alex.e.mycalculator.R;
import com.alex.e.mycalculator.base.BaseActivity;
import com.alex.e.mycalculator.utils.Calculator;
import com.alex.e.mycalculator.utils.ScreenUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.GridViewWithHeaderAndFooter;

/**
 * Created by Administrator on 2017/3/2 0002.
 */
public class CalculatorActivity extends BaseActivity implements Calculator.OnDataChangedListener, View.OnClickListener {

    @BindView(R.id.lv_char_data) ListView mLvCharData;
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
    private Calculator mCalculator;
    private List<String> mDataNum = new ArrayList<>();
    private List<String> mDataChar = new ArrayList<>();
    private TextView mTvSave;
    private TextView mTvClear;
    private TextView mTvDel;
    private StringAdapter mAdapterCharNum;

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
//        mLvData = (ListView) header.findViewById(R.id.lv_content_data);
//        mLvChar = (ListView) header.findViewById(R.id.lv_content_char);
        mTvClear = (TextView) header.findViewById(R.id.tv_clear);
        mTvDel = (TextView) header.findViewById(R.id.tv_delete);
        mTvSave = (TextView) header.findViewById(R.id.tv_save);
        mGvCalculator.addHeaderView(header);
        mAdapter = new CalculatorAdapter();
        mGvCalculator.setAdapter(mAdapter);

        mTvClear.setOnClickListener(this);
        mTvDel.setOnClickListener(this);
        mTvSave.setOnClickListener(this);

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
        mLvCharData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final int pos = position;
                new AlertDialog.Builder(mContext)
                        .setMessage("是否要删除？？？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                onItemRemoved(pos);
                                YoYo.with(Techniques.FadeOut)
                                        .duration(700)
                                        .playOn(view);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        mCalculator = new Calculator(this);
        mDataNum = mCalculator.getDataNum();
        mDataChar = mCalculator.getDataChar();
        mAdapterCharNum = new StringAdapter();
        mLvCharData.setAdapter(mAdapterCharNum);
    }

    public static void launch(Context from) {
        Intent intent = new Intent(from, CalculatorActivity.class);
        from.startActivity(intent);
    }

    private void onItemRemoved(int pos) {
        if (pos == 0) {
            mDataChar.remove(pos);
        } else {
            mDataChar.remove(pos - 1);
        }
        mDataNum.remove(pos);

//        mCalculator.removeItem(pos);
        mAdapterCharNum.notifyDataSetChanged();
    }

    @Override
    public void onResultChanged(String res) {
        mTvResult.setText(res);
    }

    @Override
    public void onTempChanged(String temp) {
        mTvTemp.setText(temp);
    }

    @Override
    public void onCharChanged(String ch) {
        mTvChat.setText(ch);
    }

    @Override
    public void onListDataChanged(String ch, String num) {
//        if (!TextUtils.isEmpty(ch)) {
//            mDataChar.add(ch);
//        }
//        if (!TextUtils.isEmpty(num)){
//            mDataNum.add(num);
//        }
        mLvCharData.smoothScrollToPosition(mLvCharData.getCount() - 1);
        mAdapterCharNum.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_clear:
                mCalculator.onClearClick();
                mDataNum.clear();
                mDataChar.clear();
                mAdapterCharNum.notifyDataSetChanged();
                break;
            case R.id.tv_save:
                if (mCalculator.saveData()) {
                    Toast.makeText(mContext, "保存成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "保存失败！！！", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.tv_delete:
                mCalculator.onDelClick();
                break;
        }
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
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) textView.getLayoutParams();
            params.height = (ScreenUtils.getScreenWidth() - 10) / 4;
            textView.setLayoutParams(params);
            textView.setText(mGvItems[position]);
            return convertView;
        }
    }

    private class StringAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDataNum.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataNum.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_char_and_num, null);
            }
            TextView ch = (TextView) convertView.findViewById(R.id.tv_char);
            TextView num = (TextView) convertView.findViewById(R.id.tv_num);
            TextView id = (TextView) convertView.findViewById(R.id.tv_id);

            id.setText((position + 1) + "");
            if (position == 0) {
                ch.setText("");
            } else {
                ch.setText(mDataChar.get(position - 1));
            }
            num.setText(mDataNum.get(position));
            return convertView;
        }
    }
}
