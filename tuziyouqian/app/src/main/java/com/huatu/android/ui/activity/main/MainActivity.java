package com.huatu.android.ui.activity.main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.lib.core.utils.PermissionUtils;
import com.lib.core.utils.ToastUtil;
import com.lib.core.view.TabFragmentHost;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.huatu.android.BuildConfig;
import com.huatu.android.R;
import com.huatu.android.base.App;
import com.huatu.android.base.BaseActivity;
import com.huatu.android.bean.SplashBean;
import com.huatu.android.bean.VersionBean;
import com.huatu.android.ui.fragment.home.HomeFragment;
import com.huatu.android.ui.fragment.personal.PersonalFragment;
import com.huatu.android.utils.AppConfig;
import com.huatu.android.utils.AppUtils;
import com.huatu.android.widget.dialog.DownLoadDialog;
import com.huatu.android.widget.dialog.MainDialog;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity<MainPresenter, MainModel> implements MainContract.View {


  @BindView(R.id.mContainer)
  FrameLayout mContainer;
  @BindView(R.id.mTabHost)
  TabFragmentHost mTabHost;
  String[] tables = {App.getResText(R.string.home_page),
      App.getResText(R.string.mine)};
  Class[] fragments = {HomeFragment.class,
      PersonalFragment.class};
  ArrayList<Integer> drawables = new ArrayList<>();
  int selectPosition = 0;
  VersionBean mVersionBean;
  int downLoadId;
  DownLoadDialog downLoadDialog;

  @Override
  protected void getBundleExtras(Bundle extras) {

  }

  @Override
  public int getLayoutId() {
    return R.layout.activity_main;
  }

  @Override
  public void initPresenter() {
    mPresenter.setVM(this, mModel);

  }

  @Override
  public void initView() {
    //mPresenter.checkVersion();
    drawables.add(R.drawable.tabbar_home_selector);
    /*drawables.add(R.drawable.tabbar_found_selector);*/
    drawables.add(R.drawable.tabbar_mine_selector);
    mTabHost.setup(this, getSupportFragmentManager(), R.id.mContainer);
    mTabHost.getTabWidget().setDividerDrawable(R.drawable.transparent);
    for (int i = 0; i < fragments.length; i++) {
      TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tables[i])
          .setIndicator(getTabItemView(i));
      Bundle args = new Bundle();
      args.putInt(fragments[i].getSimpleName(), i);
      mTabHost.addTab(tabSpec, fragments[i], args);
    }

    mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
      @Override
      public void onTabChanged(String tabId) {
        mRxManager.post(AppConfig.RXMANAGER_UPDATE, "");
        if (tables[0].equals(tabId)) {
          selectPosition = 0;
        } else if (tables[1].equals(tabId)) {
          selectPosition = 1;
        } /*else if (tables[2].equals(tabId)) {
                    selectPosition = 2;
                }*/

      }
    });
    mRxManager.on(AppConfig.RXMANAGER_LOGIN, new Consumer<Object>() {
      @Override
      public void accept(Object o) throws Exception {
        //mPresenter.uploadDevice();
      }
    });
    PermissionUtils.checkPermission(this, Manifest.permission.READ_PHONE_STATE, new PermissionUtils.PermissionCheckCallBack() {
      @Override
      public void onHasPermission() {
        App.getInstance().getDeviceId();
//                mPresenter.uploadDevice();
      }

      @Override
      public void onUserHasAlreadyTurnedDown(String... permission) {
        PermissionUtils.requestPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE, 1001);
      }

      @Override
      public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
        PermissionUtils.requestPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE, 1001);
      }
    });


  }

  private View getTabItemView(int index) {
    View view = View.inflate(this, R.layout.tab_item_view, null);
    ImageView mIcon = (ImageView) view.findViewById(R.id.mIcon);
    TextView mTv = (TextView) view.findViewById(R.id.mTv);
    mIcon.setImageResource(drawables.get(index));
    mTv.setText(tables[index]);
    return view;
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
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    int index = intent.getIntExtra("index", 0);
    if (index < /*3*/ 2 && index >= 0) {
      mTabHost.setCurrentTab(index);
    }
  }

  long startTime = 0;

  @Override
  public void onBackPressedSupport() {
    long currentTime = System.currentTimeMillis();
    if ((currentTime - startTime) >= 2000) {
      ToastUtil.showShort(R.string.app_exit);
      startTime = currentTime;
    } else {
      AppUtils.appExit(this, true);
    }
  }

    /*@Override
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
                    AppUtils.appExit(MainActivity.this, false);
                }


            }

            @Override
            public void onConfirm() {
                updateDialog.dismiss();
                checkPermissions();

            }
        });
    }*/

  private void checkPermissions() {
    PermissionUtils.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionUtils.PermissionCheckCallBack() {
      @Override
      public void onHasPermission() {
        downLoad(mVersionBean);
      }

      @Override
      public void onUserHasAlreadyTurnedDown(String... permission) {
        PermissionUtils.requestPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, 1000);
      }

      @Override
      public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
        PermissionUtils.requestPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, 1000);
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
            PermissionUtils.showDialog(MainActivity.this, "请在权限管理中开启存储权限", 150);
          }

          @Override
          public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
            PermissionUtils.showDialog(MainActivity.this, "请在权限管理中开启存储权限", 150);
          }
        });

        break;
      case 1001:
        PermissionUtils.onRequestPermissionResult(this, Manifest.permission.READ_PHONE_STATE, grantResults, new PermissionUtils.PermissionCheckCallBack() {
          @Override
          public void onHasPermission() {
            App.getInstance().getDeviceId();
//                        mPresenter.uploadDevice();
          }

          @Override
          public void onUserHasAlreadyTurnedDown(String... permission) {
            PermissionUtils.showDialog(MainActivity.this, "请在权限管理中开启电话权限", 150);
          }

          @Override
          public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
            PermissionUtils.showDialog(MainActivity.this, "请在权限管理中开启电话权限", 150);
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

  @Override
  protected void onResume() {
    super.onResume();
    if (!isActive) {
      mRxManager.post(AppConfig.RXMANAGER_UPDATE, "");
      isActive = true;
    }

    if (App.getInstance().isPhoneStart()) {
      App.getInstance().setPhoneStart(false);
      SplashBean splash = App.getInstance().getSplash();
      if (splash != null && splash.home != null) {
        MainDialog mainDialog = new MainDialog(this, splash.home.imgurl, splash.home.url);
        mainDialog.setCanceledOnTouchOutside(false);
        mainDialog.show();
      }
    }
  }
}
