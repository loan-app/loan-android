package com.mod.jishidai.ui.fragment.home;

import android.os.Bundle;

import com.mod.jishidai.R;
import com.mod.jishidai.base.BaseFragment;
import com.mod.jishidai.ui.activity.webview.WebViewFragment;
import com.mod.jishidai.utils.UrlFactory;

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
