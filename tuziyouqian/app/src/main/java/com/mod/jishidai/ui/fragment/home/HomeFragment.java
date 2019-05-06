package com.mod.jishidai.ui.fragment.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.lib.core.utils.CollectionUtils;
import com.lib.core.utils.ToastUtil;
import com.lib.core.view.MarqueeView;
import com.mod.jishidai.R;
import com.mod.jishidai.base.App;
import com.mod.jishidai.base.BaseFragment;
import com.mod.jishidai.bean.BalanceBean;
import com.mod.jishidai.bean.BannerBean;
import com.mod.jishidai.bean.LaunchBean;
import com.mod.jishidai.bean.NoticeBean;
import com.mod.jishidai.bean.OrderBean;
import com.mod.jishidai.ui.activity.webview.WebViewActivity;
import com.mod.jishidai.ui.activity.webview.WebViewFragment;
import com.mod.jishidai.utils.AppConfig;
import com.mod.jishidai.utils.GlideImageLoader;
import com.mod.jishidai.widget.TitleHeaderBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

public class HomeFragment extends BaseFragment<HomePresenter, HomeModel> implements HomeContract.View {
    @BindView(R.id.mBanner)
    Banner mBanner;
    //    @BindView(R.id.ivBanner)
//    ImageView ivBanner;
    @BindView(R.id.mMarquee)
    MarqueeView mMarquee;
    @BindView(R.id.llMarquee)
    LinearLayout llMarquee;
    List<String> notices;
    @BindView(R.id.mSmart)
    SmartRefreshLayout mSmart;
    @BindView(R.id.mTitle)
    TitleHeaderBar mTitle;
    LaunchBean launchBean;
    List<String> mBannerUrls;
    //    @BindView(R.id.tvSum)
//    TextView tvSum;
//    List<Integer> allowSum = new ArrayList<>();
//    @BindView(R.id.mRulerView)
//    RulerView mRulerView;
//    int minLimit;
//    int maxLimit;
    @BindView(R.id.mContent)
    FrameLayout mContent;
/*    ValuationView valuationView;//估值view*/
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
                mPresenter.getLaunchData();
                updateHome();

            }
        });
        mPresenter.getLaunchData();
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
        mBanner.setIndicatorGravity(BannerConfig.RIGHT).setBannerAnimation(Transformer.Default).setImageLoader(new GlideImageLoader());
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (launchBean != null && !CollectionUtils.isNullOrEmpty(launchBean.banner) && position < launchBean.banner.size()) {
                    BannerBean bannerBean = launchBean.banner.get(position);
                    if (!TextUtils.isEmpty(bannerBean.bannerUrl)) {
                        Bundle bundle = new Bundle();
                        bundle.putString(WebViewFragment.WEB_URL, bannerBean.bannerUrl);
                        startActivity(WebViewActivity.class, bundle);
                    }
                }

            }
        });

        mBanner.setImages(mBannerUrls).start();
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
    public void showLaunchData(LaunchBean bean) {
        launchBean = bean;
        if (bean != null) {
            if (!CollectionUtils.isNullOrEmpty(bean.banner)) {
                mBannerUrls.clear();
                for (BannerBean banner : bean.banner) {
                    mBannerUrls.add(banner.bannerImgurl);
                }
                if (!CollectionUtils.isNullOrEmpty(mBannerUrls)) {
                    mBanner.setImages(mBannerUrls).start();
                }
            }
            if (!CollectionUtils.isNullOrEmpty(bean.notice)) {
                notices = new ArrayList<>();
                for (NoticeBean notice : bean.notice) {
                    notices.add(notice.noticeTitle);
                }
            }
            if (!CollectionUtils.isNullOrEmpty(notices)) {
                if (llMarquee.getVisibility() == View.GONE)
                    llMarquee.setVisibility(View.VISIBLE);
                mMarquee.startWithList(notices, R.anim.anim_bottom_in, R.anim.anim_top_out);
            } else {
                llMarquee.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void showBalanceBeanData(BalanceBean bean) {
//        if (bean != null) {
//            try {
//                if (!TextUtils.isEmpty(bean.limit_money)) {
//                    String[] limit = bean.limit_money.split("\\|");
//                    if (limit != null && limit.length > 1) {
//                        minLimit = Integer.parseInt(limit[0]);
//                        maxLimit = Integer.parseInt(limit[1]);
//                        if (minLimit % 100 == 0)
//                            mRulerView.setSelPosition(minLimit / 100);
//                    }
//                    if (!TextUtils.isEmpty(bean.limit_money_allow)) {
//                        String[] allowArr = bean.limit_money_allow.split("\\|");
//                        if (allowArr != null && allowArr.length > 0) {
//                            allowSum.clear();
//                            for (String allow : allowArr) {
//                                allowSum.add(Integer.parseInt(allow));
//                            }
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
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
    }


    @Override
    public void showOrderStatus(OrderBean bean) {
//        bean.orderStatus = "2";
        if (TextUtils.isEmpty(bean.orderStatus))
            return;
        switch (bean.orderStatus) {
            case "0"://0 直接进入评估页面
//                mPresenter.searchPhone(App.getInstance().getPhoneModel(), AppUtils.getRomTotalSize());
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
/*                if (!TextUtils.isEmpty(SPUtils.getStringData(App.getInstance().getLoginPhone() + AppConfig.FAILURE_ORDERID + bean.orderId, null))) {
                    mPresenter.searchPhone();
                } else {*/
                    if (progressFailView == null) {
                        progressFailView = new ProgressFailView(_mActivity);
                    }
                    progressFailView.setCallBack(new ProgressView.CallBack() {//审核失败，点击我知道了，继续初始化到评估界面
                        @Override
                        public void onCallBack() {
                            Bundle bundle = new Bundle();
                            bundle.putString(WebViewFragment.WEB_URL, bean.url);
                            startActivity(WebViewActivity.class, bundle);
                            /*saveOrderId(bean);*/
                        }
                    });
                    progressFailView.setData(bean);
                    mContent.removeAllViews();
                    mContent.addView(progressFailView);
               /* }*/
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

/*    private void saveOrderId(OrderBean bean) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SPUtils.saveStringData(App.getInstance().getLoginPhone() + AppConfig.FAILURE_ORDERID + bean.orderId, bean.orderId);
                searchPhone();
            }
        }, 1300);

    }*/

    @Override
    public void onStart() {
        super.onStart();
        if (!CollectionUtils.isNullOrEmpty(mBannerUrls))
            mBanner.startAutoPlay();
        if (!CollectionUtils.isNullOrEmpty(notices))
            mMarquee.startFlipping();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!CollectionUtils.isNullOrEmpty(mBannerUrls))
            mBanner.stopAutoPlay();
        if (!CollectionUtils.isNullOrEmpty(notices))
            mMarquee.stopFlipping();
    }

}
