package com.huatu.android.ui.fragment.personal;

import com.huatu.android.base.mvp.BaseModel;
import com.huatu.android.base.mvp.BasePresenter;
import com.huatu.android.base.mvp.BaseView;
import com.huatu.android.bean.BaseBean;
import com.huatu.android.bean.PersonInfoBean;
import com.huatu.android.bean.VersionBean;

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