package com.mod.tuziyouqian.ui.activity.webview;

import com.mod.tuziyouqian.base.App;
import com.mod.tuziyouqian.bean.BaseBean;
import com.mod.tuziyouqian.bean.RealNameBean;
import com.mod.tuziyouqian.http.RxSubscriber;

/**
 * Created by TMVPHelper on 2018/05/07
 */
public class WebViewPresenter extends WebViewContract.Presenter {

    @Override
    public void getRealNameInfo(String status) {
        mModel.getRealNameInfo(App.getInstance().getToken()).subscribe(new RxSubscriber<BaseBean<RealNameBean>>(mContext, mRxManage, false) {
            @Override
            protected void onSuccess(BaseBean<RealNameBean> bean) {
                if (mView != null)
                    mView.showRealNameInfo(status, bean.data);

            }

            @Override
            protected void onFailed(String code, String msg) {
                if (mView != null)
                    mView.showToast(msg);

            }
        });
    }
}