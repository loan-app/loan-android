package com.huatu.android.ui.activity.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lib.core.utils.MD5Util;
import com.lib.core.utils.ToastUtil;
import com.huatu.android.R;
import com.huatu.android.base.App;
import com.huatu.android.base.BaseFragment;
import com.huatu.android.utils.AppConfig;
import com.huatu.android.widget.ClearEditText;
import com.huatu.android.widget.TitleHeaderBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 周竹
 * @file LoginFragment
 * @brief
 * @date 2018/4/23 上午2:34
 * Copyright (c) 2017
 * All rights reserved.
 */
public class LoginFragment extends BaseFragment<LoginPresenter, LoginModel> implements LoginContract.View {
    String phone;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.etPwd)
    ClearEditText etPwd;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.tvForgetPwd)
    TextView tvForgetPwd;
    @BindView(R.id.mTitle)
    TitleHeaderBar mTitle;

    public static LoginFragment newInstance(String phone) {

        Bundle args = new Bundle();
        args.putString("phone", phone);
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getBundleExtras(Bundle bundle) {
        phone = bundle.getString("phone");

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_login;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);

    }

    @Override
    protected void initView() {
        mTitle.setLeftOnClickListener(view -> _mActivity.onBackPressed());
        tvPhone.setText(phone);
        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable.toString())) {
                    btnLogin.setEnabled(true);
                    btnLogin.setTextColor(App.getResColor(R.color.color_text_primary));
                    btnLogin.setBackgroundResource(R.drawable.radius_style_button);
                } else {
                    btnLogin.setEnabled(false);
                    btnLogin.setTextColor(App.getResColor(R.color.white));
                    btnLogin.setBackgroundResource(R.drawable.radius_solid_cbcbcb_corners_10);
                }

            }
        });
    }

    @OnClick({R.id.btnLogin, R.id.tvForgetPwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                mPresenter.login(phone, MD5Util.MD5(etPwd.getText().toString()));
                break;
            case R.id.tvForgetPwd:
                if (_mActivity instanceof LoginActivity) {
                    LoginActivity activity = (LoginActivity) _mActivity;
                    activity.start(FindPwdFragment.newInstance(phone));
                }
                break;
        }
    }

    @Override
    public void jump(int code) {

    }

    @Override
    public void showCodeSuccess() {

    }

    @Override
    public void goLogin(String phone, String pwd) {

    }

    @Override
    public void showLoginSuccess() {
        mRxManager.post(AppConfig.RXMANAGER_UPDATE, "");
        mRxManager.post(AppConfig.RXMANAGER_LOGIN, "");
        mRxManager.post(AppConfig.RXMANAGER_WEBUPDATE, "");
        _mActivity.finish();

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
}
