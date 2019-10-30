package com.huatu.android.ui.fragment.found;

import android.os.Bundle;

import com.huatu.android.R;
import com.huatu.android.base.BaseFragment;
import com.huatu.android.ui.activity.webview.WebViewFragment;
import com.huatu.android.utils.UrlFactory;

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
