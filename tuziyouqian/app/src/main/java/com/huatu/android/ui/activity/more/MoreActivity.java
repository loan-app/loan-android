package com.huatu.android.ui.activity.more;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lib.core.utils.PermissionUtils;
import com.lib.core.utils.ToastUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.huatu.android.BuildConfig;
import com.huatu.android.R;
import com.huatu.android.base.App;
import com.huatu.android.base.BaseActivity;
import com.huatu.android.bean.PersonInfoBean;
import com.huatu.android.bean.VersionBean;
import com.huatu.android.ui.activity.feedback.FeedbackActivity;
import com.huatu.android.ui.activity.login.LoginActivity;
import com.huatu.android.ui.activity.webview.WebViewActivity;
import com.huatu.android.ui.activity.webview.WebViewFragment;
import com.huatu.android.ui.fragment.personal.PersonalContract;
import com.huatu.android.ui.fragment.personal.PersonalModel;
import com.huatu.android.ui.fragment.personal.PersonalPresenter;
import com.huatu.android.utils.AppConfig;
import com.huatu.android.utils.AppUtils;
import com.huatu.android.utils.UrlFactory;
import com.huatu.android.widget.PersonItemView;
import com.huatu.android.widget.TitleHeaderBar;
import com.huatu.android.widget.dialog.AppUpdateDialog;
import com.huatu.android.widget.dialog.DownLoadDialog;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 周竹
 * @file MoreActivity
 * @brief
 * @date 2018/4/23 下午5:02
 * Copyright (c) 2017
 * All rights reserved.
 */
public class MoreActivity extends BaseActivity<PersonalPresenter, PersonalModel> implements PersonalContract.View {
    @BindView(R.id.mTitle)
    TitleHeaderBar mTitle;
    @BindView(R.id.piAboutUs)
    PersonItemView piAboutUs;
    @BindView(R.id.piFeedBack)
    PersonItemView piFeedBack;
    @BindView(R.id.piUpdate)
    PersonItemView piUpdate;
    @BindView(R.id.btnLogOut)
    TextView btnLogOut;
    VersionBean mVersionBean;
    int downLoadId;
    DownLoadDialog downLoadDialog;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_more;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);

    }

    @Override
    public void initView() {
        mTitle.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    @OnClick({R.id.piAboutUs, R.id.piFeedBack, R.id.piUpdate, R.id.btnLogOut})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.piAboutUs:
                Bundle bundle = new Bundle();
                bundle.putString(WebViewFragment.WEB_URL, UrlFactory.getAboutUrl());
//                bundle.putString(WebViewFragment.WEB_URL,new StringBuffer(BuildConfig.H5_HOST).append("/test.html").toString());
                startActivity(WebViewActivity.class, bundle);
                break;
            case R.id.piFeedBack:
                startActivity(FeedbackActivity.class);
                break;
            case R.id.piUpdate:
                mPresenter.checkVersion();
                break;
            case R.id.btnLogOut:
                mPresenter.logOut();

                break;
        }
    }

    @Override
    public void showUserInfo(PersonInfoBean infoBean) {

    }

    @Override
    public void logOutSuccess() {
        App.logOut();
        mRxManager.post(AppConfig.RXMANAGER_UPDATE, "");//通知刷新个人信息
        startActivity(LoginActivity.class);
        finish();

    }

    @Override
    public void showUpdateDialog(VersionBean bean) {
        mVersionBean = bean;
        AppUpdateDialog updateDialog = new AppUpdateDialog(this);
        String updateContent = bean.versionContent;
        if (!TextUtils.isEmpty(updateContent)) {
            String content = parseContent(updateContent);
            updateDialog.content(content);
        }
        String title = "发现新版本V" + bean.versionName;
        updateDialog.title(title);
        updateDialog.show();
        updateDialog.setCanceledOnTouchOutside(false);
        if ("1".equals(mVersionBean.versionForce)) {//强制更新
            updateDialog.setCancelable(false);//不响应返回键
        }
        updateDialog.setOnCallBack(new AppUpdateDialog.OnCallBack() {
            @Override
            public void onCancel() {
                updateDialog.dismiss();
                if ("1".equals(mVersionBean.versionForce)) {//强制更新
                    AppUtils.appExit(MoreActivity.this, false);
                }


            }

            @Override
            public void onConfirm() {
                updateDialog.dismiss();
                checkPermissions();

            }
        });
    }

    private void checkPermissions() {
        PermissionUtils.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionUtils.PermissionCheckCallBack() {
            @Override
            public void onHasPermission() {
                downLoad(mVersionBean);
            }

            @Override
            public void onUserHasAlreadyTurnedDown(String... permission) {
                PermissionUtils.requestPermission(MoreActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, 1000);
            }

            @Override
            public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                PermissionUtils.requestPermission(MoreActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, 1000);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1000:
                PermissionUtils.onRequestPermissionResult(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, grantResults, new PermissionUtils.PermissionCheckCallBack() {
                    @Override
                    public void onHasPermission() {
                        downLoad(mVersionBean);
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDown(String... permission) {
                        PermissionUtils.showDialog(MoreActivity.this, "请在权限管理中开启存储权限", 150);
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                        PermissionUtils.showDialog(MoreActivity.this, "请在权限管理中开启存储权限", 150);
                    }
                });

                break;
        }
    }

    private void downLoad(VersionBean bean) {
        String path = App.getInstance().getExternalFilesDir(null).getAbsoluteFile().getPath() + File.separator + BuildConfig.alias + bean.versionName + ".apk";
        FileDownloader.setup(App.getInstance());
        downLoadId = FileDownloader.getImpl().create(bean.versionUrl)
                .setPath(path)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        showProgress((int) ((double) soFarBytes / totalBytes * 100));
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
//                        if (downLoadDialog != null && downLoadDialog.isShowing()) {
//                            downLoadDialog.dismiss();
//                            downLoadDialog = null;
//                        }
                        showProgress(100);
                        installAPK(task.getTargetFilePath());
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                }).start();
    }

    private void showProgress(int process) {
        if (downLoadDialog == null) {
            downLoadDialog = new DownLoadDialog(mContext);
            downLoadDialog.show();
            downLoadDialog.setCanceledOnTouchOutside(false);
            if (mVersionBean != null) {
                if ("1".equals(mVersionBean.versionForce)) {
                    downLoadDialog.setCancelable(false);
                }
            }
            downLoadDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    FileDownloader.getImpl().pause(downLoadId);
                }
            });
        }
        downLoadDialog.setProgress(process);
    }

    public void installAPK(String file) {
        try {
            if (downLoadDialog != null && mVersionBean != null && "0".equals(mVersionBean.versionForce)) {
                downLoadDialog.dismiss();
            }
            File apkFile = new File(file);
            if (!apkFile.exists()) {
                return;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= 24) {
                Uri apkUri = FileProvider.getUriForFile(App.getInstance(), App.getInstance().getPackageName() + ".FileProvider", apkFile);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            }
            mContext.startActivity(intent);
        } catch (Exception e) {
            ToastUtil.showShort("安装失败");

        }

    }

    private String parseContent(String updateContent) {
        StringBuilder builder = new StringBuilder();
        String[] newStrs = updateContent.split("\\|");
        if (newStrs != null && newStrs.length > 0) {
            for (int i = 0; i < newStrs.length; i++) {
                newStrs[i] = i + 1 + "." + newStrs[i] + "\n";
                builder = builder.append(newStrs[i]);
            }
        }
        return builder.toString();
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
}
