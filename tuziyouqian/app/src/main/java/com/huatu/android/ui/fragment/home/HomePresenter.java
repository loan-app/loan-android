package com.huatu.android.ui.fragment.home;

import com.huatu.android.base.App;
import com.huatu.android.bean.BaseBean;
import com.huatu.android.bean.OrderBean;
import com.huatu.android.http.RxSubscriber;

/**
 * Created by TMVPHelper on 2018/04/30
 */
public class HomePresenter extends HomeContract.Presenter {

    /*@Override
    public void getLaunchData() {
        String jsonLaunchData = SPUtils.getStringData(AppConfig.LAUNCH_DATA, null);
        if (!TextUtils.isEmpty(jsonLaunchData)) {
            LaunchBean bean = JsonUtils.json2Bean(jsonLaunchData, LaunchBean.class);
            mView.showLaunchData(bean);
        }
        mModel.getLaunchData().subscribe(new RxSubscriber<BaseBean<LaunchBean>>(mContext, mRxManage, false) {
            @Override
            protected void onSuccess(BaseBean<LaunchBean> launchBeanBaseBean) {
                if (mView != null) {
                    SPUtils.saveStringData(AppConfig.LAUNCH_DATA, JsonUtils.bean2json(launchBeanBaseBean.data));
                    mView.closeRefresh(true);
                    mView.showLaunchData(launchBeanBaseBean.data);
                }

            }

            @Override
            protected void onFailed(String code, String msg) {
                if (mView != null) {
                    mView.closeRefresh(false);
                    mView.showToast(msg);
                }

            }
        });

    }*/

    /*@Override
    public void getBalance() {
        mModel.getBalance(App.getInstance().getToken()).subscribe(new RxSubscriber<BaseBean<BalanceBean>>(mContext, mRxManage, false) {
            @Override
            protected void onSuccess(BaseBean<BalanceBean> balanceBeanBaseBean) {
                if (mView != null) {
                    mView.closeRefresh(true);
                    mView.showBalanceBeanData(balanceBeanBaseBean.data);
                }

            }

            @Override
            protected void onFailed(String code, String msg) {
                if (mView != null) {
                    mView.closeRefresh(false);
                    mView.showToast(msg);
                }

            }
        });
    }*/

    @Override
    public void getLoanData() {
        mModel.getLoanData(App.getInstance().getToken()).subscribe(new RxSubscriber<BaseBean<OrderBean>>(mContext, mRxManage, false) {
            @Override
            protected void onSuccess(BaseBean<OrderBean> bean) {
                if (mView != null) {
                    mView.closeRefresh(true);
                    mView.showLoanData(bean.data);
                }

            }

            @Override
            protected void onFailed(String code, String msg) {
                if (mView != null) {
                    mView.closeRefresh(false);
                    mView.showToast(msg);
                }
            }
        });

    }

    @Override
    public void getOrderStatus() {
        mModel.getOrderStatus(App.getInstance().getToken()).subscribe(new RxSubscriber<BaseBean<OrderBean>>(mContext, mRxManage, false) {
            @Override
            protected void onSuccess(BaseBean<OrderBean> orderBeanBaseBean) {
                if (mView != null) {
                    mView.closeRefresh(true);
                    mView.showOrderStatus(orderBeanBaseBean.data);
                }
            }

            @Override
            protected void onFailed(String code, String msg) {
                if (mView != null) {
                    mView.closeRefresh(false);
                    mView.showToast(msg);
                }
            }
        });

    }
}