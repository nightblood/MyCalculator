package com.alex.e.mycalculator.ui.activity;

import android.content.Context;
import android.content.Intent;

import com.alex.e.mycalculator.R;
import com.alex.e.mycalculator.base.BaseActivity;

/**
 * Created by Administrator on 2017/3/2 0002.
 */
public class DatabaseActivity extends BaseActivity {

    public static void launch(Context from) {
        Intent intent = new Intent(from, DatabaseActivity.class);
        from.startActivity(intent);
    }
    @Override
    protected int getContentView() {
        return R.layout.act_base;
    }
}
