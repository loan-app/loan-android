package com.mod.jishidai.ui.activity.webview;

import com.mod.jishidai.base.App;
import com.mod.jishidai.bean.BaseBean;
import com.mod.jishidai.bean.RealNameBean;
import com.mod.jishidai.http.RxSchedulers;

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