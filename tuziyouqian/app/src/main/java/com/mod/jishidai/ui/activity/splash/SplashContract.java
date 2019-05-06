package com.mod.jishidai.ui.activity.splash;

import com.mod.jishidai.base.mvp.BaseModel;
import com.mod.jishidai.base.mvp.BasePresenter;
import com.mod.jishidai.base.mvp.BaseView;
import com.mod.jishidai.bean.BaseBean;
import com.mod.jishidai.bean.PhoneBean;
import com.mod.jishidai.bean.SplashBean;

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