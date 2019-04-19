package com.mod.tuziyouqian.ui.activity.login;

import com.lib.core.utils.MD5Util;
import com.mod.tuziyouqian.base.App;
import com.mod.tuziyouqian.bean.BaseBean;
import com.mod.tuziyouqian.bean.TokenBean;
import com.mod.tuziyouqian.http.RxSubscriber;

/**
 * Created by TMVPHelper on 2018/04/22
 */
public class LoginPresenter extends LoginContract.Presenter {
    /**
     * 验证手机号是否注册
     *
     * @param phone
     */

    @Override
    public void judgeRegister(String phone) {
        mModel.judgeRegister(phone).subscribe(new RxSubscriber<BaseBean>(mContext, mRxManage, true) {

            @Override
            protected void onSuccess(BaseBean baseBean) {
                if (mView != null) {
                    mView.jump(0);//跳转注册
                }

            }

            @Override
            protected void onFailed(String code, String msg) {
                if (mView != null) {
                    if ("2001".equals(code)) {//已注册，跳转登录
                        mView.jump(1);
                    } else {
                        mView.showToast(msg);
                    }
                }
            }
        });
    }

    /**
     * 获取注册验证码
     *
     * @param phone      手机号
     * @param graph_code 图形验证码
     * @param sms_type   事件类型	String	非空	1001-注册，1002-修改密码
     */
    @Override
    public void getVerificationCode(String phone, String graph_code, String sms_type, boolean showDialog) {
        mModel.getVerificationCode(phone, graph_code, sms_type).subscribe(new RxSubscriber<BaseBean>(mContext, mRxManage, showDialog) {
            @Override
            protected void onSuccess(BaseBean stringBaseBean) {
                if (mView != null)
                    mView.showCodeSuccess();
            }

            @Override
            protected void onFailed(String code, String msg) {
                if (mView != null)
                    mView.showToast(msg);
            }
        });
    }

    /**
     * @param phone
     * @param password   MD5值
     * @param phone_code 验证码
     */

    @Override
    public void register(String phone, String password, String phone_code) {
        mModel.register(phone, MD5Util.MD5(password), phone_code).subscribe(new RxSubscriber<BaseBean>(mContext, mRxManage, true) {
            @Override
            protected void onSuccess(BaseBean baseBean) {
                if (mView != null) {
                    mView.goLogin(phone, MD5Util.MD5(password));
                }

            }

            @Override
            protected void onFailed(String code, String msg) {
                if (mView != null) {
                    mView.showToast(msg);
                }

            }
        });

    }

    @Override
    public void login(String phone, String password) {
        mModel.login(phone, password).subscribe(new RxSubscriber<BaseBean<TokenBean>>(mContext, mRxManage, true) {
            @Override
            protected void onSuccess(BaseBean<TokenBean> bean) {
                if (mView != null) {
                    App.getInstance().saveLoginPhone(phone);
                    App.getInstance().saveLoginPassword(password);
                    App.getInstance().saveToken(bean.data.token);
                    App.getInstance().saveUid(bean.data.uid);
                    mView.showLoginSuccess();
                }

            }

            @Override
            protected void onFailed(String code, String msg) {
                if (mView != null) {
                    mView.showToast(msg);
                }

            }
        });
    }

    @Override
    public void updatePwd(String phone, String password, String phone_code) {
        mModel.updatePwd(phone, password, phone_code).subscribe(new RxSubscriber<BaseBean>(mContext, mRxManage, true) {
            @Override
            protected void onSuccess(BaseBean baseBean) {
                if (mView != null) {
                    mView.goLogin(phone, password);
                }

            }

            @Override
            protected void onFailed(String code, String msg) {
                if (mView != null)
                    mView.showToast(msg);

            }
        });
    }

}