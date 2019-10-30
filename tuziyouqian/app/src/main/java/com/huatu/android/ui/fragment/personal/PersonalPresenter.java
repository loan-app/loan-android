package com.huatu.android.ui.fragment.personal;

import com.huatu.android.base.App;
import com.huatu.android.bean.BaseBean;
import com.huatu.android.bean.PersonInfoBean;
import com.huatu.android.bean.VersionBean;
import com.huatu.android.http.RxSubscriber;

/**
 * Created by TMVPHelper on 2018/04/23
 */
public class PersonalPresenter extends PersonalContract.Presenter {

    @Override
    public void getUserInfo() {
        mModel.getUserInfo(App.getInstance().getToken()).subscribe(new RxSubscriber<BaseBean<PersonInfoBean>>(mContext, mRxManage, false) {
            @Override
            protected void onSuccess(BaseBean<PersonInfoBean> user) {
                if (mView != null)
                    mView.showUserInfo(user.data);

            }

            @Override
            protected void onFailed(String code, String msg) {
                if (mView != null)
                    mView.showToast(msg);

            }
        });

    }

    @Override
    public void logOut() {
        mModel.logOut(App.getInstance().getToken()).subscribe(new RxSubscriber<BaseBean>(mContext, mRxManage, true) {
            @Override
            protected void onSuccess(BaseBean baseBean) {
                if (mView != null)
                    mView.logOutSuccess();

            }

            @Override
            protected void onFailed(String code, String msg) {
                if (mView != null)
                    mView.logOutSuccess();
            }
        });

    }

    @Override
    public void checkVersion() {
        mModel.checkVersion(App.getInstance().getToken()).subscribe(new RxSubscriber<BaseBean<VersionBean>>(mContext, mRxManage, true) {
            @Override
            protected void onSuccess(BaseBean<VersionBean> bean) {
                if (mView != null) {
                    if (bean != null && bean.data != null) {
                        if (bean.data.versionCode > Integer.valueOf(App.getInstance().getVersionCode())) {
                            mView.showUpdateDialog(bean.data);
                        } else if (bean.data.versionCode == Integer.valueOf(App.getInstance().getVersionCode())) {
                            mView.showToast("当前已是最新版本");

                        }
                    }

                }

            }

            @Override
            protected void onFailed(String code, String msg) {

            }
        });
    }
}