package com.mod.jishidai.ui.activity.webview;

import com.mod.jishidai.base.mvp.BaseModel;
import com.mod.jishidai.base.mvp.BasePresenter;
import com.mod.jishidai.base.mvp.BaseView;
import com.mod.jishidai.bean.BaseBean;
import com.mod.jishidai.bean.RealNameBean;

import io.reactivex.Flowable;

/**
 * @author 周竹
 * @file WebViewContract
 * @brief
 * @date 2018/5/7 下午3:22
 * Copyright (c) 2017
 * All rights reserved.
 */
public interface WebViewContract {

    interface View extends BaseView {
        void showRealNameInfo(String status,RealNameBean nameBean);

    }

    interface Model extends BaseModel {
        Flowable<BaseBean<RealNameBean>> getRealNameInfo(String token);
    }

    abstract static class Presenter extends BasePresenter<Model, View> {
        public abstract void getRealNameInfo(String status);
    }
}