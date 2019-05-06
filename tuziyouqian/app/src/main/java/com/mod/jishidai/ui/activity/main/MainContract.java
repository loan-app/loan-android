package com.mod.jishidai.ui.activity.main;

import com.mod.jishidai.base.mvp.BaseModel;
import com.mod.jishidai.base.mvp.BasePresenter;
import com.mod.jishidai.base.mvp.BaseView;
import com.mod.jishidai.bean.BaseBean;
import com.mod.jishidai.bean.VersionBean;

import java.util.HashMap;

import io.reactivex.Flowable;

/**
 * @author 周竹
 * @file MainContract
 * @brief
 * @date 2018/4/29 下午10:45
 * Copyright (c) 2017
 * All rights reserved.
 */
public interface MainContract {

    interface View extends BaseView {
        void showUpdateDialog(VersionBean versionBean);

    }

    interface Model extends BaseModel {

        Flowable<BaseBean> uploadDevice(HashMap<String, String> map);

        Flowable<BaseBean<VersionBean>> checkVersion(String token);
    }

    abstract static class Presenter extends BasePresenter<Model, View> {
        public abstract void uploadDevice();

        public abstract void checkVersion();

    }
}