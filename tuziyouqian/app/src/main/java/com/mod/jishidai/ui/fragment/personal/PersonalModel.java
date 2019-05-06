package com.mod.jishidai.ui.fragment.personal;

import com.mod.jishidai.base.App;
import com.mod.jishidai.bean.BaseBean;
import com.mod.jishidai.bean.PersonInfoBean;
import com.mod.jishidai.bean.VersionBean;
import com.mod.jishidai.http.RxSchedulers;

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