package com.mod.tuziyouqian.ui.fragment.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lib.core.utils.ToastUtil;
import com.mod.tuziyouqian.R;
import com.mod.tuziyouqian.base.App;
import com.mod.tuziyouqian.base.BaseFragment;
import com.mod.tuziyouqian.bean.PersonInfoBean;
import com.mod.tuziyouqian.bean.VersionBean;
import com.mod.tuziyouqian.ui.activity.login.LoginActivity;
import com.mod.tuziyouqian.ui.activity.more.MoreActivity;
import com.mod.tuziyouqian.ui.activity.webview.WebViewActivity;
import com.mod.tuziyouqian.ui.activity.webview.WebViewFragment;
import com.mod.tuziyouqian.utils.AppConfig;
import com.mod.tuziyouqian.utils.UrlFactory;
import com.mod.tuziyouqian.widget.PersonItemView;
import com.mod.tuziyouqian.widget.TitleHeaderBar;

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
    @BindView(R.id.ptInfo)
    PersonItemView ptInfo;
    @BindView(R.id.ptRecord)
    PersonItemView ptRecord;
    @BindView(R.id.ptBank)
    PersonItemView ptBank;
    @BindView(R.id.ptHelp)
    PersonItemView ptHelp;
    @BindView(R.id.ptMore)
    PersonItemView ptMore;
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


    @OnClick({R.id.ptInfo, R.id.ptRecord, R.id.ptBank, R.id.ptHelp, R.id.ptMore, R.id.tvPhone})
    public void onViewClicked(View view) {
        if (!App.getInstance().isLogin()) {
            startActivity(LoginActivity.class);
            return;
        }
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.ptInfo:
                bundle.putString(WebViewFragment.WEB_URL, UrlFactory.getCertCenterUrl());
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
            case R.id.ptHelp:
                bundle.putString(WebViewFragment.WEB_URL, UrlFactory.gethelpCenterUrl());
                startActivity(WebViewActivity.class, bundle);
                break;
            case R.id.ptMore:
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
