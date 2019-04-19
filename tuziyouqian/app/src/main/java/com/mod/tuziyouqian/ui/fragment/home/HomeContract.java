package com.mod.tuziyouqian.ui.fragment.home;

import com.mod.tuziyouqian.base.mvp.BaseModel;
import com.mod.tuziyouqian.base.mvp.BasePresenter;
import com.mod.tuziyouqian.base.mvp.BaseView;
import com.mod.tuziyouqian.bean.BalanceBean;
import com.mod.tuziyouqian.bean.BaseBean;
import com.mod.tuziyouqian.bean.LaunchBean;
import com.mod.tuziyouqian.bean.OrderBean;
import com.mod.tuziyouqian.bean.PhoneBean;

import io.reactivex.Flowable;

/**
 * @author 周竹
 * @file HomeContract
 * @brief
 * @date 2018/4/30 下午5:19
 * Copyright (c) 2017
 * All rights reserved.
 */
public interface HomeContract {

    interface View extends BaseView {

        void showLaunchData(LaunchBean bean);

        void showBalanceBeanData(BalanceBean bean);

        void closeRefresh(boolean success);

        void showLoanData(OrderBean bean);

        void showOrderStatus(OrderBean bean);

    }

    interface Model extends BaseModel {

        Flowable<BaseBean<LaunchBean>> getLaunchData();

        Flowable<BaseBean<BalanceBean>> getBalance(String token);

        Flowable<BaseBean<OrderBean>> getLoanData(String token);

        Flowable<BaseBean<OrderBean>> getOrderStatus(String token);

    }

    abstract static class Presenter extends BasePresenter<Model, View> {
        public abstract void getLaunchData();

        public abstract void getBalance();

        public abstract void getLoanData();

        public abstract void getOrderStatus();

    }
}