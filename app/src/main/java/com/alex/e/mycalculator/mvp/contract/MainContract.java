package com.alex.e.mycalculator.mvp.contract;

import com.alex.e.mycalculator.base.BasePresenter;
import com.alex.e.mycalculator.base.BaseView;

/**
 * Created by Administrator on 2017/3/2 0002.
 */
public interface MainContract {

    interface View extends BaseView {

    }

    interface Presenter<V extends BaseView> extends BasePresenter <View> {

    }
}
