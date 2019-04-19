package com.mod.tuziyouqian.ui.activity.splash;

import com.lib.core.utils.JsonUtils;
import com.lib.core.utils.SPUtils;
import com.mod.tuziyouqian.base.App;
import com.mod.tuziyouqian.bean.BaseBean;
import com.mod.tuziyouqian.bean.PhoneBean;
import com.mod.tuziyouqian.bean.SplashBean;
import com.mod.tuziyouqian.http.RxSubscriber;
import com.mod.tuziyouqian.utils.AppConfig;

/**
 * Created by TMVPHelper on 2018/05/12
 */
public class SplashPresenter extends SplashContract.Presenter {


    @Override
    public void getSplash() {
        mModel.getSplash(App.getInstance().getToken()).subscribe(new RxSubscriber<BaseBean<SplashBean>>(mContext, mRxManage, false) {
            @Override
            protected void onSuccess(BaseBean<SplashBean> baseBean) {
                if (mView != null){
                    mView.showSplash(baseBean.data);
                    App.getInstance().saveSplash(baseBean.data);
                }
            }

            @Override
            protected void onFailed(String code, String msg) {

            }
        });
    }


    @Override
    public void searchPhone(String model, String memory) {
        mModel.searchPhone(model, memory).subscribe(new RxSubscriber<BaseBean<PhoneBean>>(mContext, mRxManage, false) {
            @Override
            protected void onSuccess(BaseBean<PhoneBean> baseBean) {
                SPUtils.saveStringData(AppConfig.PHONE_MODEL, JsonUtils.bean2json(baseBean.data));
            }

            @Override
            protected void onFailed(String code, String msg) {

            }
        });
    }
}