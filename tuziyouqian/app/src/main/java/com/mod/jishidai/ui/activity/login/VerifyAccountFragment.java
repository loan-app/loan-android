package com.mod.jishidai.ui.activity.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.lib.core.utils.ToastUtil;
import com.mod.jishidai.R;
import com.mod.jishidai.base.App;
import com.mod.jishidai.base.BaseFragment;
import com.mod.jishidai.widget.ClearEditText;
import com.mod.jishidai.widget.TitleHeaderBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 周竹
 * @file VerifyAccountFragment
 * @brief
 * @date 2018/4/22 下午2:35
 * Copyright (c) 2017
 * All rights reserved.
 */
public class VerifyAccountFragment extends BaseFragment<LoginPresenter, LoginModel> implements LoginContract.View {
    @BindView(R.id.etPhone)
    ClearEditText etPhone;
    @BindView(R.id.btnNext)
    Button btnNext;
    String phone;
    @BindView(R.id.mTitle)
    TitleHeaderBar mTitle;
    @Override
    protected void getBundleExtras(Bundle bundle) {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_verify_account;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);

    }

    @Override
    protected void initView() {
        mTitle.setLeftOnClickListener(view -> _mActivity.onBackPressed());
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable)) {
                    btnNext.setBackgroundResource(R.drawable.radius_solid_cbcbcb_corners_10);
                    btnNext.setEnabled(false);
                    btnNext.setTextColor(App.getResColor(R.color.white));
                } else {
                    btnNext.setBackgroundResource(R.drawable.radius_style_button);
                    btnNext.setEnabled(true);
                    btnNext.setTextColor(App.getResColor(R.color.color_text_primary));
                }
            }
        });
        String phone = App.getInstance().getLoginPhone();
        etPhone.setText(phone);
        if (!TextUtils.isEmpty(phone)) {
            etPhone.setSelection(phone.length());
        }


    }


    @OnClick({R.id.btnNext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                phone = etPhone.getText().toString();
                mPresenter.judgeRegister(phone);
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
    public void jump(int code) {
        if (_mActivity instanceof LoginActivity) {
            LoginActivity activity = (LoginActivity) _mActivity;
            if (code == 0) {
                activity.start(RegisterFragment.newInstance(phone));
            } else if (code == 1) {
                activity.start(LoginFragment.newInstance(phone));

            }
        }
    }

    @Override
    public void showCodeSuccess() {

    }

    @Override
    public void goLogin(String phone, String pwd) {

    }

    @Override
    public void showLoginSuccess() {

    }

}
