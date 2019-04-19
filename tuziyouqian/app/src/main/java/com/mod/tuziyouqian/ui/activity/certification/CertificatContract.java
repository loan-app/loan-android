package com.mod.tuziyouqian.ui.activity.certification;

import com.mod.tuziyouqian.base.mvp.BaseModel;
import com.mod.tuziyouqian.base.mvp.BasePresenter;
import com.mod.tuziyouqian.base.mvp.BaseView;
import com.mod.tuziyouqian.bean.BaseBean;
import com.mod.tuziyouqian.bean.PersonInfoBean;
import com.mod.tuziyouqian.bean.RealNameBean;

import java.util.Map;

import io.reactivex.Flowable;

/**
 * @author 周竹
 * @file CertificatContract
 * @brief
 * @date 2018/4/23 下午11:23
 * Copyright (c) 2017
 * All rights reserved.
 */
public interface CertificatContract {

    interface View extends BaseView {
        void showRealNameInfo(RealNameBean nameBean);

        void saveRealNameSuccess();

        void showUserInfo(PersonInfoBean infoBean);

/*        void showFaceCheckSuccess();*/
    }

    interface Model extends BaseModel {
        Flowable<BaseBean<RealNameBean>> getRealNameInfo(String token);

        Flowable<BaseBean> saveRealNameInfo(String token, String userName,
                                            String userCertNo, String ia,
                                            String indate, String address,
                                            String nation, String imgCertFront,
                                            String imgCertBack);

        Flowable<BaseBean<PersonInfoBean>> getUserInfo(String token);

        Flowable<BaseBean> saveUserInfo(Map<String, String> map);

        Flowable<BaseBean> uploadContact(String token, String addressList);

/*        Flowable<BaseBean> uploadFaceCheck(String token, String imgFace);*/

    }

    abstract static class Presenter extends BasePresenter<Model, View> {

        public abstract void getRealNameInfo();

        public abstract void saveRealNameInfo(String userName,
                                              String userCertNo, String ia,
                                              String indate, String address,
                                              String nation, String imgCertFront,
                                              String imgCertBack);

        public abstract void getUserInfo();

        public abstract void saveUserInfo(Map<String, String> map);

        public abstract void uploadContact(String addressList);

/*
        public abstract void uploadFaceCheck(String imgFace);
*/


    }
}