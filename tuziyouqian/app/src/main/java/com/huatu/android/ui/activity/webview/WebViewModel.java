package com.huatu.android.ui.activity.webview;

import com.huatu.android.base.App;
import com.huatu.android.bean.BaseBean;
import com.huatu.android.bean.RealNameBean;
import com.huatu.android.http.RxSchedulers;

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