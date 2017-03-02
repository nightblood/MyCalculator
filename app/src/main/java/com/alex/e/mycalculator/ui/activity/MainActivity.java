package com.alex.e.mycalculator.ui.activity;


import android.view.View;
import android.widget.Button;

import com.alex.e.mycalculator.R;
import com.alex.e.mycalculator.base.BaseActivity;
import com.alex.e.mycalculator.mvp.contract.MainContract;
import com.alex.e.mycalculator.mvp.presenter.MainPresenter;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements MainContract.View, View.OnClickListener {

    @BindView(R.id.btn_calculator) Button mBtnCalculator;
    @BindView(R.id.btn_database) Button mBtnDatabase;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter = new MainPresenter(this);
        mBtnCalculator.setOnClickListener(this);
        mBtnDatabase.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.btn_database):
                DatabaseActivity.launch(mContext);
                break;
            case R.id.btn_calculator:
                CalculatorActivity.launch(mContext);
                break;
        }
    }
}
