package com.mod.tuziyouqian.ui.activity.main;

import com.mod.tuziyouqian.base.App;
import com.mod.tuziyouqian.bean.BaseBean;
import com.mod.tuziyouqian.bean.VersionBean;
import com.mod.tuziyouqian.http.RxSchedulers;

import java.util.HashMap;

import io.reactivex.Flowable;

/**
 * Created by TMVPHelper on 2018/04/29
 */
public class MainModel implements MainContract.Model {

    @Override
    public Flowable<BaseBean> uploadDevice(HashMap<String, String> map) {
        return App.serverAPI.uploadDevice(map).compose(RxSchedulers.<BaseBean>io_main());
    }

    @Override
    public Flowable<BaseBean<VersionBean>> checkVersion(String token) {
        return App.serverAPI.checkVersion(token).compose(RxSchedulers.<BaseBean<VersionBean>>io_main());
    }

}