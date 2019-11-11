package com.huatu.android.ui.fragment.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.huatu.android.bean.CerBean;
import com.huatu.android.ui.activity.certification.CertificatContainActivity;
import com.huatu.android.ui.activity.certification.OperatorActivity;
import com.lib.core.utils.ToastUtil;
import com.huatu.android.R;
import com.huatu.android.base.App;
import com.huatu.android.base.BaseFragment;
import com.huatu.android.bean.OrderBean;
import com.huatu.android.ui.activity.webview.WebViewActivity;
import com.huatu.android.ui.activity.webview.WebViewFragment;
import com.huatu.android.utils.AppConfig;
import com.huatu.android.widget.TitleHeaderBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

public class HomeFragment extends BaseFragment<HomePresenter, HomeModel> implements HomeContract.View {
  @BindView(R.id.mBanner)
  ImageView mBanner;
  @BindView(R.id.mSmart)
  SmartRefreshLayout mSmart;
  @BindView(R.id.mTitle)
  TitleHeaderBar mTitle;
  List<String> mBannerUrls;
  @BindView(R.id.mContent)

  FrameLayout mContent;

  ProgressView progressView;

  ProgressFailView progressFailView;

  RefundView refundView;

  LoanView loanView;

  @Override
  protected void getBundleExtras(Bundle bundle) {

  }

  @Override
  protected int getLayoutResource() {
    return R.layout.fragment_main;
  }

  @Override
  public void initPresenter() {
    mPresenter.setVM(this, mModel);

  }

  @Override
  protected void initView() {
    mBannerUrls = new ArrayList<>();
    mRxManager.on(AppConfig.RXMANAGER_UPDATE, new Consumer<Object>() {

      @Override
      public void accept(Object o) throws Exception {
        updateHome();
      }
    });
    mTitle.getLeftImageView().setVisibility(View.INVISIBLE);
    mSmart.setOnRefreshListener(new OnRefreshListener() {
      @Override
      public void onRefresh(RefreshLayout refreshlayout) {
        updateHome();

      }
    });
    updateHome();
    initBanner();
  }

  private void updateHome() {
    if (App.getInstance().isLogin()) {
      mPresenter.getOrderStatus();
    } else {
      mPresenter.getLoanData();
    }
  }


  private void initBanner() {
    mBanner.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(WebViewFragment.WEB_URL, "");
        startActivity(WebViewActivity.class, bundle);
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
  public void closeRefresh(boolean success) {
    if (mSmart != null && mSmart.isRefreshing()) {
      mSmart.finishRefresh(success);
    }

  }

  @Override
  public void showLoanData(OrderBean bean) {
    if (loanView == null)
      loanView = new LoanView(_mActivity);
    mContent.removeAllViews();
    mContent.addView(loanView);
    loanView.setData(bean);
    loanView.setLoanViewListener(new LoanView.LoanViewListener() {
      @Override
      public void checkCerState() {
        mPresenter.getCerState();
      }
    });
  }


  @Override
  public void showOrderStatus(OrderBean bean) {
//        bean.orderStatus = "2";
    if (TextUtils.isEmpty(bean.orderStatus))
      return;
    switch (bean.orderStatus) {
      case "0"://0 直接进入评估页面
        mPresenter.getLoanData();
        break;
      case "1"://1 进入流程，无按钮
        if (progressView == null) {
          progressView = new ProgressView(_mActivity);
        }
        progressView.setData(bean, true);
        mContent.removeAllViews();
        mContent.addView(progressView);
        break;
      case "2"://审核失败
        if (progressFailView == null) {
          progressFailView = new ProgressFailView(_mActivity);
        }
        progressFailView.setCallBack(new ProgressView.CallBack() {//审核失败，点击我知道了，继续初始化到评估界面
          @Override
          public void onCallBack() {
            Bundle bundle = new Bundle();
            bundle.putString(WebViewFragment.WEB_URL, bean.url);
            startActivity(WebViewActivity.class, bundle);
          }
        });
        progressFailView.setData(bean);
        mContent.removeAllViews();
        mContent.addView(progressFailView);
        break;
      case "3"://正常还款页面 显示立即回购。跳转到url页面
        if (refundView == null)
          refundView = new RefundView(_mActivity);
        refundView.setData(bean, true);
        mContent.removeAllViews();
        mContent.addView(refundView);
        break;
      case "4"://逾期还款页面 显示立即回购。跳转到url页面
        if (refundView == null)
          refundView = new RefundView(_mActivity);
        refundView.setData(bean, false);
        mContent.removeAllViews();
        mContent.addView(refundView);
        break;
    }

  }

  @Override
  public void onGetCetState(CerBean cerBean) {
    if (cerBean.realName != 2) {
      //去实名认证
      Bundle bundle = new Bundle();
      bundle.putInt("type", 0);
      startActivity(CertificatContainActivity.class, bundle);
      return;
    }
    if (cerBean.userDetails != 2) {
      //去信息完善
      Bundle bundle = new Bundle();
      bundle.putInt("type", 1);
      startActivity(CertificatContainActivity.class, bundle);
      return;
    }
    if (cerBean.liveness != 2) {
      //去扫脸
      Bundle bundle = new Bundle();
      bundle.putInt("type", 2);
      startActivity(CertificatContainActivity.class, bundle);
      return;
    }
    if (cerBean.mobile != 2) {
      startActivity(OperatorActivity.class);
      return;
    }
    //认证完成，去web页面
    startActivity(WebViewActivity.class);
  }
}
