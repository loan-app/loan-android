package com.huatu.android.ui.fragment.personal;

import com.huatu.android.base.App;
import com.huatu.android.bean.BaseBean;
import com.huatu.android.bean.PersonInfoBean;
import com.huatu.android.bean.VersionBean;
import com.huatu.android.http.RxSchedulers;

import io.reactivex.Flowable;

/**
 * Created by TMVPHelper on 2018/04/23
 */
public class PersonalModel implements PersonalContract.Model {

    @Override
    public Flowable<BaseBean<PersonInfoBean>> getUserInfo(String token) {
        return App.serverAPI.getUserInfo(token).compose(RxSchedulers.<BaseBean<PersonInfoBean>>io_main());
    }

    @Override
    public Flowable<BaseBean> logOut(String token) {
        return App.serverAPI.logOut(token).compose(RxSchedulers.<BaseBean>io_main());
    }

    @Override
    public Flowable<BaseBean<VersionBean>> checkVersion(String token) {
        return App.serverAPI.checkVersion(token).compose(RxSchedulers.<BaseBean<VersionBean>>io_main());
    }
}