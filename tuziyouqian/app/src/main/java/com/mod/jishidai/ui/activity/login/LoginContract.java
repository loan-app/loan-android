package com.mod.jishidai.ui.activity.login;

import com.mod.jishidai.base.mvp.BaseModel;
import com.mod.jishidai.base.mvp.BasePresenter;
import com.mod.jishidai.base.mvp.BaseView;
import com.mod.jishidai.bean.BaseBean;
import com.mod.jishidai.bean.TokenBean;

import io.reactivex.Flowable;

/**
 * @author 周竹
 * @file LoginContract
 * @brief
 * @date 2018/4/22 下午3:16
 * Copyright (c) 2017
 * All rights reserved.
 */
public interface LoginContract {

    interface View extends BaseView {
        void jump(int code);

        void showCodeSuccess();

        void goLogin(String phone, String pwd);

        void showLoginSuccess();
    }

    interface Model extends BaseModel {
        Flowable<BaseBean> judgeRegister(String phone);

        Flowable<BaseBean> getVerificationCode(String phone, String graph_code, String sms_type);

        Flowable<BaseBean> register(String phone, String password, String phone_code);

        Flowable<BaseBean<TokenBean>> login(String phone, String password);

        Flowable<BaseBean> updatePwd(String phone, String password, String phone_code);
    }

    abstract static class Presenter extends BasePresenter<Model, View> {
        public abstract void judgeRegister(String phone);

        public abstract void getVerificationCode(String phone, String graph_code, String sms_type, boolean showDialog);

        public abstract void register(String phone, String password, String phone_code);

        public abstract void login(String phone, String password);

        public abstract void updatePwd(String phone, String password, String phone_code);
    }
}