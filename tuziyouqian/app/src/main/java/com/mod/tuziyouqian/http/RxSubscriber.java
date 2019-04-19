package com.mod.tuziyouqian.http;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.lib.core.R;
import com.lib.core.baseapp.BaseApplication;
import com.lib.core.dialog.LoadingDialog;
import com.lib.core.utils.NetWorkUtils;
import com.mod.tuziyouqian.base.App;
import com.mod.tuziyouqian.bean.BaseBean;
import com.mod.tuziyouqian.ui.activity.login.LoginActivity;
import com.mod.tuziyouqian.ui.activity.main.MainActivity;
import com.mod.tuziyouqian.utils.AppConfig;

import io.reactivex.subscribers.DisposableSubscriber;


/********************使用例子********************/
/*_apiService.login(mobile, verifyCode)
        .//省略
        .subscribe(new RxSubscriber<User user>(mContext,false) {
@Override
public void _onNext(User user) {
        // 处理user
        }

@Override
public void _onError(String msg) {
        ToastUtil.showShort(mActivity, msg);
        });*/
public abstract class RxSubscriber<T> extends DisposableSubscriber<T> {

    private Context mContext;
    private String msg;
    private boolean showDialog = true;

    /**
     * 是否显示浮动dialog
     */
    public void showDialog() {
        this.showDialog = true;
    }

    public void hideDialog() {
        this.showDialog = true;
    }

    public RxSubscriber(Context context, RxManager manager, String msg, boolean showDialog) {
        if (manager != null)
            manager.add(this);
        this.mContext = context;
        this.msg = msg;
        this.showDialog = showDialog;
    }

    public RxSubscriber(Context context, RxManager manager) {
        this(context, manager, BaseApplication.getAppContext().getString(R.string.loading), true);
    }

    public RxSubscriber(Context context, RxManager manager, boolean showDialog) {
        this(context, manager, BaseApplication.getAppContext().getString(R.string.loading), showDialog);
    }

    @Override
    public void onComplete() {
        if (showDialog && LoadingDialog.isShow())
            LoadingDialog.cancelDialogForLoading();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (NetWorkUtils.isNetConnected(BaseApplication.getAppContext()) && showDialog) {
            try {
                LoadingDialog.showDialogForLoading((Activity) mContext, msg, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onNext(T t) {
        if (showDialog && LoadingDialog.isShow()) {
            LoadingDialog.cancelDialogForLoading();
        }
        if (t != null && t instanceof BaseBean) {
            BaseBean baseBean = (BaseBean) t;
            if (baseBean.success()) {
                onSuccess(t);
            } else {
                onFailed(baseBean.status, baseBean.message);
                if (AppConfig.TOKEN_OVERDUE.equals(baseBean.status)) {
                    App.logOut();
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                    App.getInstance().finishOther(MainActivity.class, LoginActivity.class);
                }
            }
        } else {
            onSuccess(t);
        }
    }


    @Override
    public void onError(Throwable e) {
        if (showDialog && LoadingDialog.isShow())
            LoadingDialog.cancelDialogForLoading();
        e.printStackTrace();
        //网络
        if (!NetWorkUtils.isNetConnected(BaseApplication.getAppContext())) {
            onFailed("-1", BaseApplication.getAppContext().getString(R.string.no_net));
        } else {   //其它
            onFailed("-1", BaseApplication.getAppContext().getString(R.string.net_error));
        }
    }

    protected abstract void onSuccess(T t);

    protected abstract void onFailed(String code, String msg);

}
