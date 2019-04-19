package com.mod.tuziyouqian.ui.activity.splash;

import com.mod.tuziyouqian.base.App;
import com.mod.tuziyouqian.bean.BaseBean;
import com.mod.tuziyouqian.bean.PhoneBean;
import com.mod.tuziyouqian.bean.SplashBean;
import com.mod.tuziyouqian.http.RxSchedulers;

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