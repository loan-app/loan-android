package com.huatu.android.ui.activity.certification;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lib.core.utils.ToastUtil;
import com.huatu.android.BuildConfig;
import com.huatu.android.R;
import com.huatu.android.base.App;
import com.huatu.android.base.BaseActivity;
import com.huatu.android.bean.PersonInfoBean;
import com.huatu.android.bean.RealNameBean;
import com.huatu.android.widget.PersonItemView;
import com.huatu.android.widget.TitleHeaderBar;
import com.moxie.client.manager.MoxieCallBack;
import com.moxie.client.manager.MoxieCallBackData;
import com.moxie.client.manager.MoxieContext;
import com.moxie.client.manager.MoxieSDK;
import com.moxie.client.model.MxParam;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 认证中心
 */
public class CertificationActivity extends BaseActivity<CertificatPresenter, CertificatModel> implements CertificatContract.View {


    @BindView(R.id.mTitle)
    TitleHeaderBar mTitle;
    @BindView(R.id.piRealName)
    PersonItemView piRealName;
    @BindView(R.id.piPersonalInfo)
    PersonItemView piPersonalInfo;
    @BindView(R.id.piPhone)
    PersonItemView piPhone;
    @BindView(R.id.piAliPay)
    PersonItemView piAliPay;
    @BindView(R.id.piFace)
    PersonItemView piFace;
    @BindView(R.id.btnNext)
    Button btnNext;
    String mUserId = App.getInstance().getUid();
    String mApiKey = BuildConfig.moxie_apikey;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_certification;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);

    }

    @Override
    public void initView() {
        mTitle.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @OnClick({R.id.piRealName, R.id.piPersonalInfo, R.id.piPhone, R.id.piAliPay, R.id.piFace, R.id.btnNext})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.piRealName:
                bundle.putInt("type", 0);
                startActivity(CertificatContainActivity.class, bundle);
                break;
            case R.id.piPersonalInfo:
                bundle.putInt("type", 1);
                startActivity(CertificatContainActivity.class, bundle);
                break;
            case R.id.piPhone:

                MxParam mxParam1 = new MxParam();
                mxParam1.setUserId(mUserId);
                mxParam1.setApiKey(mApiKey);
                mxParam1.setFunction(MxParam.PARAM_FUNCTION_CARRIER);
                MoxieSDK.getInstance().start(this, mxParam1, new MoxieCallBack() {
                    @Override
                    public boolean callback(MoxieContext moxieContext, MoxieCallBackData moxieCallBackData) {
                        String s = "成功";
                        return super.callback(moxieContext, moxieCallBackData);
                    }
                });
                break;
            case R.id.piAliPay:
                MxParam mxParam = new MxParam();
                mxParam.setUserId(mUserId);
                mxParam.setApiKey(mApiKey);
                mxParam.setFunction(MxParam.PARAM_FUNCTION_ALIPAY);
                MoxieSDK.getInstance().start(this, mxParam, new MoxieCallBack() {
                    @Override
                    public boolean callback(MoxieContext moxieContext, MoxieCallBackData moxieCallBackData) {
                        String s = "成功";
                        return super.callback(moxieContext, moxieCallBackData);
                    }
                });
                break;
            case R.id.piFace:
                bundle.putInt("type", 2);
                startActivity(CertificatContainActivity.class, bundle);
                break;
            case R.id.btnNext:
                break;
        }
    }

    @Override
    public void showRealNameInfo(RealNameBean nameBean) {
//        if (TextUtils.isEmpty(nameBean.userName)) {
//            piRealName.setContentColor(R.color.color_fd664b);
//            piRealName.setContent("未认证");
//        } else {
//            piRealName.setContentColor(R.color.color_999999);
//            piRealName.setContent("认证成功");
//        }

    }

    @Override
    public void saveRealNameSuccess() {

    }

    @Override
    public void showUserInfo(PersonInfoBean infoBean) {

    }

    @Override
    public void saveUserInfoSuccess() {

    }
/*
    @Override
    public void showFaceCheckSuccess() {

    }*/

    @Override
    public void showLoading(String title) {
        startProgressDialog(title);

    }

    @Override
    public void stopLoading() {
        stopProgressDialog();

    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showShort(msg);

    }


}
