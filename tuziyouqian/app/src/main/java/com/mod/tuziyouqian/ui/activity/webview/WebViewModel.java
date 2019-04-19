package com.mod.tuziyouqian.ui.activity.webview;

import com.mod.tuziyouqian.base.App;
import com.mod.tuziyouqian.bean.BaseBean;
import com.mod.tuziyouqian.bean.RealNameBean;
import com.mod.tuziyouqian.http.RxSchedulers;

import io.reactivex.Flowable;

/**
* Created by TMVPHelper on 2018/05/07
*/
public class WebViewModel implements WebViewContract.Model{

    @Override
    public Flowable<BaseBean<RealNameBean>> getRealNameInfo(String token) {
        return App.serverAPI.getRealNameInfo(token).compose(RxSchedulers.<BaseBean<RealNameBean>>io_main());
    }
}