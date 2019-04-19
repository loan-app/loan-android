package com.mod.tuziyouqian.ui.activity.login;

import android.os.Bundle;
import android.view.WindowManager;

import com.mod.tuziyouqian.R;
import com.mod.tuziyouqian.base.BaseActivity;

/**
 * @author 周竹
 * @file LoginActivity
 * @brief
 * @date 2018/4/21 上午10:08
 * Copyright (c) 2017
 * All rights reserved.
 */
public class LoginActivity extends BaseActivity {
    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initPresenter() {


    }

    @Override
    public void initView() {
        mImmersionBar.keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE).init();
        loadRootFragment(R.id.mContainer, new VerifyAccountFragment());
    }
}
