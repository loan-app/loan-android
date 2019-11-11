package com.huatu.android.ui.fragment.personal;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.lib.core.utils.ToastUtil;
import com.huatu.android.R;
import com.huatu.android.base.App;
import com.huatu.android.base.BaseFragment;
import com.huatu.android.bean.PersonInfoBean;
import com.huatu.android.bean.VersionBean;
import com.huatu.android.ui.activity.login.LoginActivity;
import com.huatu.android.ui.activity.more.MoreActivity;
import com.huatu.android.ui.activity.webview.WebViewActivity;
import com.huatu.android.ui.activity.webview.WebViewFragment;
import com.huatu.android.utils.AppConfig;
import com.huatu.android.utils.UrlFactory;
import com.huatu.android.widget.TitleHeaderBar;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * @author 周竹
 * @file PersonalFragment
 * @brief
 * @date 2018/4/20 下午10:38
 * Copyright (c) 2017
 * All rights reserved.
 */
public class PersonalFragment extends BaseFragment<PersonalPresenter, PersonalModel> implements PersonalContract.View {
    @BindView(R.id.mTitle)
    TitleHeaderBar mTitle;
    @BindView(R.id.register)
    CardView ptRegister;
    @BindView(R.id.ptRecord)
    CardView ptRecord;
    @BindView(R.id.ptBank)
    CardView ptBank;
    @BindView(R.id.money)
    CardView ptMoney;
    @BindView(R.id.logout)
    CardView pLogout;
    @BindView(R.id.tvPhone)
    TextView tvPhone;

    @Override
    protected void getBundleExtras(Bundle bundle) {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_personal;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);

    }

    @Override
    protected void initView() {
        updateInfo();
        mTitle.getLeftViewContainer().setVisibility(View.INVISIBLE);
        mRxManager.on(AppConfig.RXMANAGER_UPDATE, new Consumer<Object>() {

            @Override
            public void accept(Object o) throws Exception {
                updateInfo();
            }
        });
    }

    private void updateInfo() {
        if (App.getInstance().isLogin()) {
            tvPhone.setText(App.getInstance().getLoginPhone());
        } else {
            tvPhone.setText("欢迎登陆");
        }
    }


    @OnClick({R.id.register, R.id.ptRecord, R.id.ptBank, R.id.money, R.id.logout, R.id.tvPhone})
    public void onViewClicked(View view) {
        if (!App.getInstance().isLogin()) {
            startActivity(LoginActivity.class);
            return;
        }
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.register:
                bundle.putString(WebViewFragment.WEB_URL, UrlFactory.getRegisterUrl());
                startActivity(WebViewActivity.class, bundle);
                break;
            case R.id.ptRecord:
                bundle.putString(WebViewFragment.WEB_URL, UrlFactory.getOrderHistoryUrl());
                startActivity(WebViewActivity.class, bundle);
                break;
            case R.id.ptBank:
                bundle.putString(WebViewFragment.WEB_URL, UrlFactory.getBankCardUrl());
                startActivity(WebViewActivity.class, bundle);
                break;
            case R.id.money:
                bundle.putString(WebViewFragment.WEB_URL, UrlFactory.gethelpCenterUrl());
                startActivity(WebViewActivity.class, bundle);
                break;
            case R.id.logout:
                startActivity(MoreActivity.class);
                break;
        }
    }

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

    @Override
    public void showUserInfo(PersonInfoBean infoBean) {
        tvPhone.setText(App.getInstance().getLoginPhone());
    }

    @Override
    public void logOutSuccess() {

    }

    @Override
    public void showUpdateDialog(VersionBean versionBean) {

    }
}
