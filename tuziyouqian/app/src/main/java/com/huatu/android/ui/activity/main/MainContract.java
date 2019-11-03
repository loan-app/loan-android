package com.huatu.android.ui.activity.main;

import com.huatu.android.base.mvp.BaseModel;
import com.huatu.android.base.mvp.BasePresenter;
import com.huatu.android.base.mvp.BaseView;
import com.huatu.android.bean.BaseBean;
import com.huatu.android.bean.CerBean;

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
       // void showUpdateDialog(VersionBean versionBean);


    }

    interface Model extends BaseModel {


       // Flowable<BaseBean> uploadDevice(HashMap<String, String> map);

       // Flowable<BaseBean<VersionBean>> checkVersion(String token);
    }

    abstract static class Presenter extends BasePresenter<Model, View> {
        //public abstract void uploadDevice();

       // public abstract void checkVersion();


    }
}