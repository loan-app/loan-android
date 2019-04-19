package com.mod.tuziyouqian.ui.fragment.found;

import android.os.Bundle;

import com.mod.tuziyouqian.R;
import com.mod.tuziyouqian.base.BaseFragment;
import com.mod.tuziyouqian.ui.activity.webview.WebViewFragment;
import com.mod.tuziyouqian.utils.UrlFactory;

/**
 * @author 周竹
 * @file FoundFragment
 * @brief
 * @date 2018/4/20 下午10:37
 * Copyright (c) 2017
 * All rights reserved.
 */
public class FoundFragment extends BaseFragment {
    @Override
    protected void getBundleExtras(Bundle bundle) {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_found;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        loadRootFragment(R.id.mContainer, WebViewFragment.newInstance(UrlFactory.gethelpCenterUrl(), false));
    }
}
