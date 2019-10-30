package com.huatu.android.ui.activity.login;

import com.huatu.android.base.App;
import com.huatu.android.bean.BaseBean;
import com.huatu.android.bean.TokenBean;
import com.huatu.android.http.RxSchedulers;
import com.umeng.analytics.AnalyticsConfig;

import io.reactivex.Flowable;

/**
 * Created by TMVPHelper on 2018/04/22
 */
public class LoginModel implements LoginContract.Model {

    @Override
    public Flowable<BaseBean> judgeRegister(String phone) {
        return App.serverAPI.judgeRegister(phone).compose(RxSchedulers.<BaseBean>io_main());
    }

    @Override
    public Flowable<BaseBean> getVerificationCode(String phone, String graph_code, String sms_type) {
        return App.serverAPI.getVerificationCode(phone, graph_code, sms_type).compose(RxSchedulers.<BaseBean>io_main());
    }

    @Override
    public Flowable<BaseBean> register(String phone, String password, String phone_code) {
        return App.serverAPI.register(phone, password, phone_code, AnalyticsConfig.getChannel(App.getInstance())).compose(RxSchedulers.<BaseBean>io_main());
    }

    @Override
    public Flowable<BaseBean<TokenBean>> login(String phone, String password) {
        return App.serverAPI.login(phone, password).compose(RxSchedulers.<BaseBean<TokenBean>>io_main());
    }

    @Override
    public Flowable<BaseBean> updatePwd(String token, String password, String phone_code) {
        return App.serverAPI.updatePwd(token, password, phone_code).compose(RxSchedulers.<BaseBean>io_main());
    }
}