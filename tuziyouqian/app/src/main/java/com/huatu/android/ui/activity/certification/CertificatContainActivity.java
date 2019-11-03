package com.huatu.android.ui.activity.certification;

import android.os.Bundle;
import android.view.View;

import com.huatu.android.R;
import com.huatu.android.base.BaseActivity;
import com.huatu.android.widget.TitleHeaderBar;

import butterknife.BindView;

/**
 * @author 周竹
 * @file CertificatContainActivity
 * @brief
 * @date 2018/4/24 下午1:37
 * Copyright (c) 2017
 * All rights reserved.
 */
public class CertificatContainActivity extends BaseActivity {

  int type;
  private boolean hasGotToken = false;


  @Override
  protected void getBundleExtras(Bundle bundle) {
    type = bundle.getInt("type");
  }

  @Override
  public int getLayoutId() {
    return R.layout.activity_certification_contain;
  }

  @Override
  public void initPresenter() {

  }

  @Override
  public void initView() {
    switch (type) {
      case 0://实名认证
        loadRootFragment(R.id.mContainer, RealNameFragment.newInstance());
        break;
      case 1://个人信息
        loadRootFragment(R.id.mContainer, PersonalInfoFragment.newInstance());
        break;
      case 2://人脸识别
        loadRootFragment(R.id.mContainer, FaceScanFragment.newInstance());
        break;
    }

  }
}
