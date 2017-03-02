package com.alex.e.mycalculator.mvp.presenter;

import com.alex.e.mycalculator.mvp.contract.MainContract;

/**
 * Created by Administrator on 2017/3/2 0002.
 */
public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;

    public MainPresenter(MainContract.View view) {
        mView = view;
    }
}
