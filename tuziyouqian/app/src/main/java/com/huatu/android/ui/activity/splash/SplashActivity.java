package com.huatu.android.ui.activity.splash;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.huatu.android.bean.BaseBean;
import com.huatu.android.http.RxSchedulers;
import com.huatu.android.ui.activity.login.LoginActivity;
import com.lib.core.utils.RxHelper;
import com.huatu.android.R;
import com.huatu.android.base.App;
import com.huatu.android.base.BaseActivity;
import com.huatu.android.bean.SplashBean;
import com.huatu.android.ui.activity.main.MainActivity;
import com.huatu.android.ui.activity.webview.WebViewActivity;
import com.huatu.android.ui.activity.webview.WebViewFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class SplashActivity extends BaseActivity<SplashPresenter, SplashModel> implements SplashContract.View {
  @BindView(R.id.tv_skip)
  TextView tv_skip;
  @BindView(R.id.ivImage)
  ImageView ivImage;
  boolean isJump;

  @Override
  protected void getBundleExtras(Bundle extras) {

  }

  @Override
  public int getLayoutId() {
    return R.layout.activity_splash;
  }

  @Override
  public void initPresenter() {
    mPresenter.setVM(this, mModel);

  }

  @Override
  public void initView() {
    mImmersionBar.init();
    //mPresenter.searchPhone(App.getInstance().getPhoneModel(), AppUtils.getRomTotalSize());
    //mPresenter.getSplash();
    tv_skip.setVisibility(View.VISIBLE);
    mRxManager.add(RxHelper.countdown(3, new RxHelper.onCountdownOnClickListener() {
      @Override
      public void onCountdown(long num) {
        tv_skip.setText("跳过 " + num);
      }

      @Override
      public void onFinish() {
        if (!isJump) {
          onViewClicked();
        }
      }
    }));
    App.getInstance().setPhoneStart(true);
    SplashBean splash = App.getInstance().getSplash();
    if (splash != null && splash.startup != null) {
      showImage(splash.startup.imgurl, splash.startup.url);
    } else {
      showImage(null, null);
    }
  }

  @Override
  protected boolean isImmersionBarEnabled() {
    return false;
  }

  @SuppressLint("CheckResult")
  @OnClick(R.id.tv_skip)
  public void onViewClicked() {
    startActivity(MainActivity.class);
    finish();
  }

   /* @Override
    public void showPhoneData(PhoneBean bean) {

    }*/

  @Override
  public void showLoading(String title) {

  }

  @Override
  public void stopLoading() {

  }

  @Override
  public void showToast(String msg) {

  }

   /* @Override
    public void showSplash(SplashBean bean) {
        if(bean!= null && bean.startup!=null) {
            showImage(bean.startup.imgurl, bean.startup.url);
        }
    }*/

  private void showImage(String imgurl, String url) {
    RequestOptions myOptions = new RequestOptions()
        .centerCrop()
        .error(R.mipmap.ic_splash)
        .priority(Priority.HIGH)
        .diskCacheStrategy(DiskCacheStrategy.NONE);

    Glide.with(this)
        .load(imgurl)
        .apply(myOptions)
//                .transition(new DrawableTransitionOptions().crossFade(2000))
//                .thumbnail(Glide.with(context)
//                        .load(path))
        .into(ivImage);
    if (!TextUtils.isEmpty(url)) {
      ivImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          isJump = true;
          Bundle bundle = new Bundle();
          bundle.putString(WebViewFragment.WEB_URL, url);
          bundle.putBoolean("isJumpHome", isJump);
          startActivity(WebViewActivity.class, bundle);
          finish();
        }
      });
    }
  }


}
