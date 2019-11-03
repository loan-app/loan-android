package com.huatu.android.ui.activity.main;

import com.huatu.android.base.App;
import com.huatu.android.bean.BaseBean;
import com.huatu.android.bean.CerBean;
import com.huatu.android.http.RxSchedulers;

import io.reactivex.Flowable;

/**
 * Created by TMVPHelper on 2018/04/29
 */
public class MainModel implements MainContract.Model {


    /*@Override
    public Flowable<BaseBean> uploadDevice(HashMap<String, String> map) {
        return App.serverAPI.uploadDevice(map).compose(RxSchedulers.<BaseBean>io_main());
    }

    @Override
    public Flowable<BaseBean<VersionBean>> checkVersion(String token) {
        return App.serverAPI.checkVersion(token).compose(RxSchedulers.<BaseBean<VersionBean>>io_main());
    }
*/
}