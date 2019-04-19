package com.mod.tuziyouqian.ui.activity.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.lib.core.utils.RxHelper;
import com.lib.core.utils.ToastUtil;
import com.mod.tuziyouqian.R;
import com.mod.tuziyouqian.base.App;
import com.mod.tuziyouqian.base.BaseFragment;
import com.mod.tuziyouqian.ui.activity.webview.WebViewActivity;
import com.mod.tuziyouqian.ui.activity.webview.WebViewFragment;
import com.mod.tuziyouqian.utils.AppConfig;
import com.mod.tuziyouqian.utils.UrlFactory;
import com.mod.tuziyouqian.widget.ClearEditText;
import com.mod.tuziyouqian.widget.TitleHeaderBar;
import com.mod.tuziyouqian.widget.dialog.GraphCodeDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 周竹
 * @file RegisterFragment
 * @brief
 * @date 2018/4/22 下午4:20
 * Copyright (c) 2017
 * All rights reserved.
 */
public class RegisterFragment extends BaseFragment<LoginPresenter, LoginModel> implements LoginContract.View, TextWatcher {
    @BindView(R.id.mTitle)
    TitleHeaderBar mTitle;
    @BindView(R.id.etCode)
    ClearEditText etCode;
    @BindView(R.id.btnCode)
    Button btnCode;
    @BindView(R.id.etPwd)
    ClearEditText etPwd;
    @BindView(R.id.btnNext)
    Button btnNext;
    @BindView(R.id.mCheckBox)
    CheckBox mCheckBox;
    GraphCodeDialog dialog;
    String phone;

    public static RegisterFragment newInstance(String phone) {

        Bundle args = new Bundle();
        args.putString("phone", phone);
        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getBundleExtras(Bundle bundle) {
        phone = bundle.getString("phone");

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_register;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected void initView() {
        mTitle.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _mActivity.onBackPressed();
            }
        });
        etCode.addTextChangedListener(this);
        etPwd.addTextChangedListener(this);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (!TextUtils.isEmpty(etPwd.getText()) && !TextUtils.isEmpty(etCode.getText())) {
                        String pw = etPwd.getText().toString();
                        String code = etCode.getText().toString();
                        if (pw.length() > 5 && code.length() == 4) {
                            btnNext.setEnabled(true);
                            btnNext.setTextColor(App.getResColor(R.color.color_text_primary));
                            btnNext.setBackgroundResource(R.drawable.radius_style_button);
                        } else {
                            btnNext.setEnabled(false);
                            btnNext.setTextColor(App.getResColor(R.color.white));
                            btnNext.setBackgroundResource(R.drawable.radius_solid_cbcbcb_corners_10);
                        }
                    } else {
                        btnNext.setEnabled(false);
                        btnNext.setTextColor(App.getResColor(R.color.white));
                        btnNext.setBackgroundResource(R.drawable.radius_solid_cbcbcb_corners_10);
                    }
                } else {
                    btnNext.setEnabled(false);
                    btnNext.setTextColor(App.getResColor(R.color.white));
                    btnNext.setBackgroundResource(R.drawable.radius_solid_cbcbcb_corners_10);
                }

            }
        });

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

    }

    @Override
    public void showCodeSuccess() {
        if (dialog != null)
            dialog.dismiss();
        btnCode.setEnabled(false);
        btnCode.setTextColor(App.getResColor(R.color.color_999999));
        btnCode.setBackgroundResource(R.drawable.radius_solid_f2f2f2_corners_44);
        mRxManager.add(RxHelper.countdown(60, new RxHelper.onCountdownOnClickListener() {
            @Override
            public void onCountdown(long num) {
                btnCode.setText(num + "s后重试");

            }

            @Override
            public void onFinish() {
                btnCode.setText("再次获取");
                btnCode.setEnabled(true);
                btnCode.setTextColor(App.getResColor(R.color.color_text_primary));
                btnCode.setBackgroundResource(R.drawable.radius_style_button);
            }
        }));

    }

    @Override
    public void goLogin(String phone, String pwd) {
        mPresenter.login(phone, pwd);

    }

    @Override
    public void showLoginSuccess() {
        mRxManager.post(AppConfig.RXMANAGER_UPDATE, App.getInstance().getToken());
        mRxManager.post(AppConfig.RXMANAGER_LOGIN, "");
        _mActivity.finish();

    }

    @OnClick({R.id.btnCode, R.id.btnNext, R.id.tvDeal})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnCode:
                dialog = new GraphCodeDialog(_mActivity, phone);
                dialog.show();
                dialog.setOnCallBack(new GraphCodeDialog.OnGraphCodeCallBack() {
                    @Override
                    public void onCancel() {
                        dialog.dismiss();

                    }

                    @Override
                    public void onConfirm(String code) {
                        mPresenter.getVerificationCode(phone, code, "1001", false);
                    }
                });
                break;
            case R.id.btnNext:
                mPresenter.register(phone, etPwd.getText().toString(), etCode.getText().toString());
                break;
            case R.id.tvDeal:
                Bundle bundle = new Bundle();
                bundle.putString(WebViewFragment.WEB_URL, UrlFactory.getDealUrl());
                startActivity(WebViewActivity.class, bundle);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (mCheckBox.isChecked() && !TextUtils.isEmpty(etPwd.getText()) && !TextUtils.isEmpty(etCode.getText())) {
            String pw = etPwd.getText().toString();
            String code = etCode.getText().toString();
            if (pw.length() > 5 && code.length() == 4) {
                btnNext.setEnabled(true);
                btnNext.setTextColor(App.getResColor(R.color.color_text_primary));
                btnNext.setBackgroundResource(R.drawable.radius_style_button);
            } else {
                btnNext.setEnabled(false);
                btnNext.setTextColor(App.getResColor(R.color.white));
                btnNext.setBackgroundResource(R.drawable.radius_solid_cbcbcb_corners_10);
            }
        } else {
            btnNext.setEnabled(false);
            btnNext.setTextColor(App.getResColor(R.color.white));
            btnNext.setBackgroundResource(R.drawable.radius_solid_cbcbcb_corners_10);
        }

    }
}
