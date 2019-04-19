package com.mod.tuziyouqian.ui.activity.certification;

import android.app.Activity;

import com.mod.tuziyouqian.base.App;
import com.mod.tuziyouqian.bean.BaseBean;
import com.mod.tuziyouqian.bean.PersonInfoBean;
import com.mod.tuziyouqian.bean.RealNameBean;
import com.mod.tuziyouqian.http.RxSubscriber;

import java.util.Map;

/**
 * Created by TMVPHelper on 2018/04/23
 */
public class CertificatPresenter extends CertificatContract.Presenter {

    @Override
    public void getRealNameInfo() {
        mModel.getRealNameInfo(App.getInstance().getToken()).subscribe(new RxSubscriber<BaseBean<RealNameBean>>(mContext, mRxManage, true) {
            @Override
            protected void onSuccess(BaseBean<RealNameBean> bean) {
                if (mView != null)
                    mView.showRealNameInfo(bean.data);

            }

            @Override
            protected void onFailed(String code, String msg) {
                if (mView != null)
                    mView.showToast(msg);

            }
        });

    }

    @Override
    public void saveRealNameInfo(String userName, String userCertNo, String ia,
                                 String indate, String address,
                                 String nation, String imgCertFront,
                                 String imgCertBack) {
        mModel.saveRealNameInfo(App.getInstance().getToken(), userName, userCertNo, ia,
                indate, address, nation, imgCertFront, imgCertBack).subscribe(new RxSubscriber<BaseBean>(mContext, mRxManage, false) {
            @Override
            protected void onSuccess(BaseBean baseBean) {
                if (mView != null) {
                    mView.saveRealNameSuccess();
                }

            }

            @Override
            protected void onFailed(String code, String msg) {
                if (mView != null) {
                    mView.stopLoading();
                    mView.showToast(msg);
                }
            }
        });

    }

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
    public void saveUserInfo(Map<String, String> map) {

        mModel.saveUserInfo(map).subscribe(new RxSubscriber<BaseBean>(mContext, mRxManage, true) {
            @Override
            protected void onSuccess(BaseBean baseBean) {
                if (mView != null) {
//                    mView.showToast("提交成功");
                    if (mContext instanceof Activity) {
                        ((Activity) mContext).finish();
                    }
                }

            }

            @Override
            protected void onFailed(String code, String msg) {
                if (mView != null)
                    mView.showToast(msg);

            }
        });

    }

    @Override
    public void uploadContact(String addressList) {
        mModel.uploadContact(App.getInstance().getToken(), addressList).subscribe(new RxSubscriber<BaseBean>(mContext, mRxManage, false) {
            @Override
            protected void onSuccess(BaseBean baseBean) {

            }

            @Override
            protected void onFailed(String code, String msg) {

            }
        });
    }

/*    @Override
    public void uploadFaceCheck(String imgFace) {
        mModel.uploadFaceCheck(App.getInstance().getToken(), imgFace).subscribe(new RxSubscriber<BaseBean>(mContext, mRxManage, false) {
            @Override
            protected void onSuccess(BaseBean baseBean) {
                if (mView != null) {
                    mView.showFaceCheckSuccess();
                }

            }

            @Override
            protected void onFailed(String code, String msg) {
                if (mView != null)
                    mView.showToast(msg);

            }
        });

    }*/
}