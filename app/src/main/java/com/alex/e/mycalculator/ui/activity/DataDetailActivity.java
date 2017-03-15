package com.alex.e.mycalculator.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.alex.e.mycalculator.R;
import com.alex.e.mycalculator.base.BaseActivity;
import com.alex.e.mycalculator.ui.fragment.DataDetailFragmentDayItems;
import com.alex.e.mycalculator.ui.fragment.DataDetailFragmentOneItem;

/**
 * Created by Administrator on 2017/3/3 0003.
 */
public class DataDetailActivity extends BaseActivity {

    public static int TYPE_ONE_DAY = 0;
    public static int TYPE_ONE_ITEM = 1;

    public static void launch(Context from, String data, int type) {
        Intent intent = new Intent(from, DataDetailActivity.class);
        intent.putExtra("data", data);
        intent.putExtra("type", type);
        from.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.act_data_detail;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initView() {
        super.initView();

        Intent intent = getIntent();
        int type = intent.getIntExtra("type", TYPE_ONE_ITEM);
        Bundle bundle = new Bundle();
        bundle.putString("data", intent.getStringExtra("data"));
        Fragment fragment;
        if (type == TYPE_ONE_DAY) {
            fragment = new DataDetailFragmentDayItems();
        } else {
            fragment = new DataDetailFragmentOneItem();
        }
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl_content, fragment).commit();

    }
}
