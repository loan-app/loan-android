package com.mod.jishidai.ui.activity.webview;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.lib.core.utils.AppUtils;
import com.lib.core.utils.LogUtils;
import com.lib.core.utils.MD5Util;
import com.lib.core.utils.PermissionUtils;
import com.lib.core.utils.SPUtils;
import com.lib.core.utils.ToastUtil;
import com.mod.jishidai.BuildConfig;
import com.mod.jishidai.R;
import com.mod.jishidai.base.App;
import com.mod.jishidai.base.BaseActivity;
import com.mod.jishidai.base.BaseFragment;
import com.mod.jishidai.bean.JsBean;
import com.mod.jishidai.bean.RealNameBean;
import com.mod.jishidai.ui.activity.certification.CertificatContainActivity;
import com.mod.jishidai.ui.activity.feedback.FeedbackActivity;
import com.mod.jishidai.ui.activity.login.LoginActivity;
import com.mod.jishidai.ui.activity.main.MainActivity;
import com.mod.jishidai.utils.AppConfig;
import com.mod.jishidai.widget.WebHeaderBar;
import com.moxie.client.manager.MoxieCallBack;
import com.moxie.client.manager.MoxieCallBackData;
import com.moxie.client.manager.MoxieContext;
import com.moxie.client.manager.MoxieSDK;
import com.moxie.client.model.MxParam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import io.reactivex.functions.Consumer;


/**
 * @author 周竹
 * @file WebFragment
 * @brief
 * @date 2018/4/20 上午10:10
 * Copyright (c) 2017
 * All rights reserved.
 */
public class WebViewFragment extends BaseFragment<WebViewPresenter, WebViewModel> implements WebViewContract.View {
    AgentWeb mAgentWeb;
    public static final String WEB_URL = "web_url";
    public static final String SHOWBACK = "showBack";
    @BindView(R.id.mTitle)
    WebHeaderBar mTitle;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    WebView mWebView;
    String webUrl;
    boolean showBack;
    int color;
    private PopupMenu mPopupMenu;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    JsBean bean = (JsBean) msg.obj;
                    if ("0".equals(bean.isShowBack)) {
                        mTitle.getLeftViewContainer().setVisibility(View.INVISIBLE);
                    } else if ("1".equals(bean.isShowBack)) {
                        mTitle.getLeftViewContainer().setVisibility(View.VISIBLE);
                    }
                    if ("0".equals(bean.isShowClose)) {
                        mTitle.getIvClose().setVisibility(View.INVISIBLE);
                    } else if ("1".equals(bean.isShowClose)) {
                        mTitle.getIvClose().setVisibility(View.VISIBLE);
                    }
                    if (!TextUtils.isEmpty(bean.title))
                        mTitle.setTitle(bean.title);
                    if ("0".equals(bean.browser)) {
                        mTitle.getRightViewContainer().setVisibility(View.INVISIBLE);
                    } else if ("1".equals(bean.browser)) {
                        mTitle.getRightViewContainer().setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    };
    String device;

    public static WebViewFragment newInstance(String url, boolean showBack) {
        Bundle args = new Bundle();
        args.putString(WEB_URL, url);
        args.putBoolean(SHOWBACK, showBack);
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getBundleExtras(Bundle bundle) {
        webUrl = bundle.getString(WEB_URL);
        showBack = bundle.getBoolean(SHOWBACK, true);
        LogUtils.logi("H5地址====" + webUrl);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_web;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);

    }

    @Override
    protected void initView() {
        mTitle.getRightImageView().setImageResource(R.mipmap.ic_more);
        mTitle.getRightViewContainer().setVisibility(View.INVISIBLE);
        color = ContextCompat.getColor(App.getInstance(), R.color.color_fc5641);
        mWebView = new WebView(getActivity());
        mWebView.addJavascriptInterface(new JsInterface(), "tool");
        mWebView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        mAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent(rootView, -1, rootView.getLayoutParams())//传入AgentWeb的父控件。
                .useDefaultIndicator(color, 1)//
                .setWebChromeClient(mWebChromeClient)//WebChromeClient
                .setWebView(mWebView)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1) //参数1是错误显示的布局，参数2点击刷新控件ID -1表示点击整个布局都刷新， AgentWeb 3.0.0 加入。
                .createAgentWeb()//创建AgentWeb。
                .ready()//设置 WebSettings。
                .go(webUrl); //WebView载入该url地址的页面并显示。
        if (!showBack)
            mTitle.getLeftViewContainer().setVisibility(View.INVISIBLE);
        mTitle.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mAgentWeb.back()) {
                    _mActivity.finish();
                }
            }
        });
        mTitle.setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPoPup(view);
            }
        });
        mTitle.getIvClose().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _mActivity.finish();
            }
        });

        mRxManager.on(AppConfig.RXMANAGER_WEBUPDATE, new Consumer<Object>() {

            @Override
            public void accept(Object o) throws Exception {
                mAgentWeb.getUrlLoader().reload(); // 刷新
            }
        });

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        StringBuffer agent = new StringBuffer(mWebView.getSettings().getUserAgentString());
        mWebView.getSettings().setUserAgentString(agent.append("modfintech/2018 ( ").append(BuildConfig.alias).append(";").append(BuildConfig.FLAVOR).append(";").append(App.getInstance().getVersionCode()).append(";").append(App.getInstance().getVersionName()).append(" ) ").toString());
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    /**
     * 显示更多菜单
     *
     * @param view 菜单依附在该View下面
     */
    private void showPoPup(View view) {
        if (mPopupMenu == null) {
            mPopupMenu = new PopupMenu(this.getActivity(), view);
            mPopupMenu.inflate(R.menu.toolbar_menu);
            mPopupMenu.setOnMenuItemClickListener(mOnMenuItemClickListener);
        }
        mPopupMenu.show();
    }

    /**
     * 菜单事件
     */
    private PopupMenu.OnMenuItemClickListener mOnMenuItemClickListener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {
                case R.id.refresh:
                    if (mAgentWeb != null) {
                        mAgentWeb.getUrlLoader().reload(); // 刷新
                    }
                    return true;
                case R.id.default_browser:
                    if (mAgentWeb != null) {
                        openBrowser(mAgentWeb.getWebCreator().getWebView().getUrl());
                    }
                    return true;
                default:
                    return false;
            }

        }
    };

    /**
     * 打开浏览器
     *
     * @param targetUrl 外部浏览器打开的地址
     */
    private void openBrowser(String targetUrl) {
        if (TextUtils.isEmpty(targetUrl) || targetUrl.startsWith("file://")) {
            ToastUtil.showShort(targetUrl + " 该链接无法使用浏览器打开。");
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri mUri = Uri.parse(targetUrl);
        intent.setData(mUri);
        startActivity(intent);


    }


    protected WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (mTitle != null) {
                mTitle.setTitle(title);
            }
        }
    };

//    @Override
//    public boolean onFragmentKeyDown(int keyCode, KeyEvent event) {
//        return mAgentWeb.handleKeyEvent(keyCode, event);
//    }


    @Override
    public void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroyView();
    }

    @Override
    public void showRealNameInfo(String status, RealNameBean nameBean) {
        if (nameBean == null)
            return;
        if ("8".equals(status)) {
            Date day=new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            String timeMark = df.format(day);
            String apiUser = "8150728867";
            //32位 (将apiUser 、 timeMark 、 apiName 、 taskId 、apiKey5个变量拼装后MD5加密。

            String apiName = "carrier";
            String taskId = nameBean.userCertNo;
            String jumpUrl =BuildConfig.H5_HOST+ "/user/cert_center.html";
            String apiEnc = MD5Util.MD5(apiUser+timeMark+apiName+taskId+"452wmFls6G6OcHWv");
            String url = " https://qz.xinyan.com/h5/"+apiUser+"/"+apiEnc+"/"+timeMark+"/"+apiName+"/"+taskId+"?jumpUrl="+jumpUrl+"&dataNotifyUrl=www.baidu.com&reportNotifyUrl=https://open.xinyan.com";
            Bundle bundle = new Bundle();
            bundle.putString(WebViewFragment.WEB_URL, url);
            startActivity(WebViewActivity.class, bundle);
            LogUtils.loge("运营商--->"+url);
            _mActivity.finish();
//           https://qz.xinyan.com/h5/{apiUser}/{apiEnc}/{timeMark}/{apiName}/{taskId}?jumpUrl={jumpUrl}
        }
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


    public class JsInterface {

        @JavascriptInterface
        public String getToken() {
            return App.getInstance().getToken();
        }

        @JavascriptInterface
        public void goAppStore(){
            try {
                Uri uri = Uri.parse("market://details?id="+ _mActivity.getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @JavascriptInterface
        public void closeAndOpen(String status) {
            Bundle bundle;
            switch (status) {
                case "0":  //0-直接关闭
                    _mActivity.finish();
                    break;
                case "1": //1-登录
                    SPUtils.removeStringData(SPUtils.PASSWORD);
                    SPUtils.removeStringData(SPUtils.TOKEN);
                    startActivity(LoginActivity.class);
                    break;
                case "2"://首页（关闭）
                    startActivity(MainActivity.class);
                    break;
                case "3"://我的（关闭
                    mRxManager.post(AppConfig.RXMANAGER_UPDATE, "");//提交订单后回到我的界面时候通知刷新主页
                    bundle = new Bundle();
                    bundle.putInt("index", 2);
                    startActivity(MainActivity.class, bundle);
                    break;
                case "4"://意见反馈
                    startActivity(FeedbackActivity.class);
                    break;
                case "5"://5-实名认证
                    bundle = new Bundle();
                    bundle.putInt("type", 0);
                    startActivity(CertificatContainActivity.class, bundle);
                    break;
                case "6"://6-活体检测
                    bundle = new Bundle();
                    bundle.putInt("type", 2);
                    startActivity(CertificatContainActivity.class, bundle);
                    break;
                case "7"://7-个人信息
                    bundle = new Bundle();
                    bundle.putInt("type", 1);
                    startActivity(CertificatContainActivity.class, bundle);
                    break;
                case "8"://8-手机认证
                    mPresenter.getRealNameInfo(status);
                    break;
                case "9"://9-支付宝认证
                    mPresenter.getRealNameInfo(status);
                    break;

            }
        }

        @JavascriptInterface
        public void skipPage(String isclose, String url) {
            Bundle bundle = new Bundle();
            bundle.putString(WebViewFragment.WEB_URL, url);
            startActivity(WebViewActivity.class, bundle);
            if ("1".equals(isclose)) {
                _mActivity.finish();
            }
        }

        /**
         * 默认显示返回按钮，隐藏关闭按钮，webview标题从页面中自动解析
         * 参数1---返回按钮，0-隐藏，1-显示
         * 参数2---关闭按钮，0-隐藏，1-显示
         * 参数3---webview标题，空或者不穿不改变
         * 参数4---0-隐藏 1-点击右上角按钮在浏览器中打开当前链接
         *
         * @param isShowBack
         * @param isShowClose
         * @param title
         * @param browser
         */
        @JavascriptInterface
        public void viewSetup(String isShowBack, String isShowClose, String title, String browser) {
            Message msg = handler.obtainMessage();
            msg.what = 1;
            msg.obj = new JsBean(isShowBack, isShowClose, title, browser);
            handler.sendMessage(msg);
        }

        @JavascriptInterface
        public String getDevice() {
            PermissionUtils.checkPermission(_mActivity, Manifest.permission.READ_PHONE_STATE, new PermissionUtils.PermissionCheckCallBack() {
                @Override
                public void onHasPermission() {
                    device = getDeviceStr();
                }

                @Override
                public void onUserHasAlreadyTurnedDown(String... permission) {
                    PermissionUtils.requestPermission(WebViewFragment.this, Manifest.permission.READ_PHONE_STATE, 1001);
                    device = getDeviceStrNull();
                }

                @Override
                public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                    PermissionUtils.requestPermission(WebViewFragment.this, Manifest.permission.READ_PHONE_STATE, 1001);
                    device = getDeviceStrNull();
                }
            });

            return device;
        }

    }

    public String getDeviceStr() {
        StringBuffer buffer = new StringBuffer(BuildConfig.alias).append("|");
        buffer.append(App.getInstance().getPhoneModel()).append("|");
        buffer.append(AppUtils.getRomTotalSize()).append("|");
        buffer.append(App.getInstance().getVersionCode()).append("|");
        buffer.append(App.getInstance().getDeviceId()).append("|");
        buffer.deleteCharAt(buffer.length() - 1);
        return buffer.toString();
    }

    public String getDeviceStrNull() {
        StringBuffer buffer = new StringBuffer(BuildConfig.alias).append("|");
        buffer.append(App.getInstance().getPhoneModel()).append("|");
        buffer.append(AppUtils.getRomTotalSize()).append("|");
        buffer.append(App.getInstance().getVersionCode()).append("|");
        buffer.append("").append("|");
        buffer.deleteCharAt(buffer.length() - 1);
        return buffer.toString();
    }

    @Override
    public boolean onBackPressedSupport() {
        if (mAgentWeb != null && mAgentWeb.getWebCreator().getWebView().canGoBack()) {
            mAgentWeb.back();
            return true;
        } else {
            if (_mActivity instanceof BaseActivity) {
                BaseActivity activity = (BaseActivity) _mActivity;
                activity.onBackPressedSupport();
            } else {
                _mActivity.finish();
            }
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1001:
                PermissionUtils.onRequestPermissionResult(this, Manifest.permission.READ_PHONE_STATE, grantResults, new PermissionUtils.PermissionCheckCallBack() {
                    @Override
                    public void onHasPermission() {
                        device = getDeviceStr();
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDown(String... permission) {
                        PermissionUtils.showDialog(_mActivity, "请在权限管理中开启电话权限", 150);
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                        PermissionUtils.showDialog(_mActivity, "请在权限管理中开启电话权限", 150);
                    }
                });

                break;
        }
    }
}
