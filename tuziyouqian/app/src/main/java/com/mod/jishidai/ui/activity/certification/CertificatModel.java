package com.mod.jishidai.ui.activity.certification;

import com.mod.jishidai.base.App;
import com.mod.jishidai.bean.BaseBean;
import com.mod.jishidai.bean.PersonInfoBean;
import com.mod.jishidai.bean.RealNameBean;
import com.mod.jishidai.http.RxSchedulers;

import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by TMVPHelper on 2018/04/23
 */
public class CertificatModel implements CertificatContract.Model {

    @Override
    public Flowable<BaseBean<RealNameBean>> getRealNameInfo(String token) {
        return App.serverAPI.getRealNameInfo(token).compose(RxSchedulers.<BaseBean<RealNameBean>>io_main());
    }

    @Override
    public Flowable<BaseBean> saveRealNameInfo(String token, String userName,
                                               String userCertNo, String ia,
                                               String indate, String address,
                                               String nation, String imgCertFront,
                                               String imgCertBack) {
        return App.serverAPI.saveRealNameInfo(token, userName, userCertNo, ia,
                indate, address, nation, imgCertFront, imgCertBack).compose(RxSchedulers.<BaseBean>io_main());
    }

    @Override
    public Flowable<BaseBean<PersonInfoBean>> getUserInfo(String token) {
        return App.serverAPI.getUserInfo(token).compose(RxSchedulers.<BaseBean<PersonInfoBean>>io_main());
    }

    @Override
    public Flowable<BaseBean> saveUserInfo(Map<String, String> map) {
        return App.serverAPI.saveUserInfo(map).compose(RxSchedulers.<BaseBean>io_main());
    }

    @Override
    public Flowable<BaseBean> uploadContact(String token, String addressList) {
        return App.serverAPI.uploadContact(token, addressList).compose(RxSchedulers.<BaseBean>io_main());
    }

/*    @Override
    public Flowable<BaseBean> uploadFaceCheck(String token, String imgFace) {
        return App.serverAPI.uploadFaceCheck(token, imgFace).compose(RxSchedulers.<BaseBean>io_main());
    }*/
}