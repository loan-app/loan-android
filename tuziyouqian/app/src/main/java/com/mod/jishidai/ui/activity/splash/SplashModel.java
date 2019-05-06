package com.mod.jishidai.ui.activity.splash;

import com.mod.jishidai.base.App;
import com.mod.jishidai.bean.BaseBean;
import com.mod.jishidai.bean.PhoneBean;
import com.mod.jishidai.bean.SplashBean;
import com.mod.jishidai.http.RxSchedulers;

import io.reactivex.Flowable;

/**
* Created by TMVPHelper on 2018/05/12
*/
public class SplashModel implements SplashContract.Model{

    @Override
    public Flowable<BaseBean<PhoneBean>> searchPhone(String model, String memory) {
        return App.serverAPI.searchPhone(model, memory).compose(RxSchedulers.<BaseBean<PhoneBean>>io_main());
    }

    @Override
    public Flowable<BaseBean<SplashBean>> getSplash(String token) {
        return App.serverAPI.SplashBean(token).compose(RxSchedulers.<BaseBean<SplashBean>>io_main());
    }
}