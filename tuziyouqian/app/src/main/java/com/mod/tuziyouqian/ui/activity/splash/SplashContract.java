package com.mod.tuziyouqian.ui.activity.splash;

import com.mod.tuziyouqian.base.mvp.BaseModel;
import com.mod.tuziyouqian.base.mvp.BasePresenter;
import com.mod.tuziyouqian.base.mvp.BaseView;
import com.mod.tuziyouqian.bean.BaseBean;
import com.mod.tuziyouqian.bean.PhoneBean;
import com.mod.tuziyouqian.bean.SplashBean;

import io.reactivex.Flowable;

/**
 * @author 周竹
 * @file SplashContract
 * @brief
 * @date 2018/5/12 下午1:31
 * Copyright (c) 2017
 * All rights reserved.
 */
public interface SplashContract {

    interface View extends BaseView {
        void showPhoneData(PhoneBean bean);
        void showSplash(SplashBean bean);
    }

    interface Model extends BaseModel {
        Flowable<BaseBean<PhoneBean>> searchPhone(String model, String memory);
        Flowable<BaseBean<SplashBean>> getSplash(String token);
    }

    abstract static class Presenter extends BasePresenter<Model, View> {
        public abstract void getSplash();

        public abstract void searchPhone(String model, String memory);
    }
}