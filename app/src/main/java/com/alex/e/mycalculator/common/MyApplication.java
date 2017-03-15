package com.alex.e.mycalculator.common;

import android.support.multidex.MultiDexApplication;

import com.alex.e.mycalculator.utils.Utils;

/**
 * Created by Administrator on 2017/3/2 0002.
 */
public class MyApplication extends MultiDexApplication {

    private static MyApplication mApp;
    public static MyApplication getInstance() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        Utils.init(this);
    }
}
