package com.mod.jishidai.ui.fragment.personal;

import com.mod.jishidai.base.mvp.BaseModel;
import com.mod.jishidai.base.mvp.BasePresenter;
import com.mod.jishidai.base.mvp.BaseView;
import com.mod.jishidai.bean.BaseBean;
import com.mod.jishidai.bean.PersonInfoBean;
import com.mod.jishidai.bean.VersionBean;

import io.reactivex.Flowable;

/**
 * @author 周竹
 * @file PersonalContract
 * @brief
 * @date 2018/4/23 上午3:16
 * Copyright (c) 2017
 * All rights reserved.
 */
public interface PersonalContract {

    interface View extends BaseView {
        void showUserInfo(PersonInfoBean infoBean);

        void logOutSuccess();

        void showUpdateDialog(VersionBean versionBean);

    }

    interface Model extends BaseModel {
        Flowable<BaseBean<PersonInfoBean>> getUserInfo(String token);

        Flowable<BaseBean> logOut(String token);

        Flowable<BaseBean<VersionBean>> checkVersion(String token);

    }

    abstract static class Presenter extends BasePresenter<Model, View> {
        public abstract void getUserInfo();

        public abstract void logOut();

        public abstract void checkVersion();
    }
}