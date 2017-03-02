package com.alex.e.mycalculator.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/2 0002.
 */
public abstract class BaseActivity<P extends BasePresenter> extends Activity {

    protected P mPresenter;
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        mContext = this;
        initView();
        initData();
    }



    protected void initData() {

    }

    protected void initView() {

    }

    protected abstract int getContentView();
}
