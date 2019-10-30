package com.huatu.android.ui.fragment.home;

import android.os.Bundle;

import com.huatu.android.R;
import com.huatu.android.base.BaseFragment;
import com.huatu.android.ui.activity.webview.WebViewFragment;
import com.huatu.android.utils.UrlFactory;

/**
 * @author 周竹
 * @file NewHomeFragment
 * @brief
 * @date 2018/5/6 下午8:14
 * Copyright (c) 2017
 * All rights reserved.
 */
public class NewHomeFragment extends BaseFragment {
    @Override
    protected void getBundleExtras(Bundle bundle) {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_newhome;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        loadRootFragment(R.id.mContainer, WebViewFragment.newInstance(UrlFactory.getHomeUrl(), false));
    }
}
