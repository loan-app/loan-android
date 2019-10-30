package com.huatu.android.ui.activity.webview;

import android.app.Activity;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import com.huatu.android.R;
import com.huatu.android.base.BaseActivity;
import com.huatu.android.ui.activity.main.MainActivity;
import com.huatu.android.utils.AppConfig;

public class WebViewActivity extends BaseActivity {
    String url;
    WebViewFragment mWebFragment;
    boolean isJumpHome;


    @Override
    protected void getBundleExtras(Bundle bundle) {
        url = bundle.getString(WebViewFragment.WEB_URL);
        isJumpHome = bundle.getBoolean("isJumpHome");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mWebFragment = WebViewFragment.newInstance(url,true);
        loadRootFragment(R.id.rootView, mWebFragment);

    }

    @Override
    public void finish() {
        if(isJumpHome){
            startActivity(MainActivity.class);
        }
        mRxManager.post(AppConfig.RXMANAGER_UPDATE, "");//通知刷新主页
        hideInput(WebViewActivity.this);
        super.finish();
    }

    /**
     * 关闭软键盘
     */
    public static void hideInput(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
