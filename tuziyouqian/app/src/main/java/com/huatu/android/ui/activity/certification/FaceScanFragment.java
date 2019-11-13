package com.huatu.android.ui.activity.certification;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.baidu.ocr.ui.camera.CameraActivity;
import com.bumptech.glide.Glide;
import com.huatu.android.bean.UploadBean;
import com.huatu.android.utils.FileUtil;
import com.lib.core.utils.AppUtils;
import com.lib.core.utils.ImageLoaderUtils;
import com.lib.core.utils.PermissionUtils;
import com.lib.core.utils.ToastUtil;
import com.huatu.android.R;
import com.huatu.android.base.BaseFragment;
import com.huatu.android.bean.PersonInfoBean;
import com.huatu.android.bean.RealNameBean;
import com.huatu.android.utils.UploadManage;
import com.huatu.android.widget.TitleHeaderBar;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import butterknife.BindView;
import butterknife.OnClick;

public class FaceScanFragment extends BaseFragment<CertificatPresenter, CertificatModel> implements CertificatContract.View {

  @BindView(R.id.mTitle)
  TitleHeaderBar mTitle;
  @BindView(R.id.ivScan)
  ImageView ivScan;
  static final int CODE_TAKE_PHOTO = 1000;//相机
  @BindView(R.id.rlExample)
  RelativeLayout rlExample;
  String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

  public static FaceScanFragment newInstance() {

    Bundle args = new Bundle();

    FaceScanFragment fragment = new FaceScanFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  protected void getBundleExtras(Bundle bundle) {

  }

  @Override
  protected int getLayoutResource() {
    return R.layout.fragment_face_scan;
  }

  @Override
  public void initPresenter() {
    mPresenter.setVM(this, mModel);

  }

  @Override
  protected void initView() {
    mPresenter.getRealNameInfo();
    ivScan.setEnabled(true);
    mTitle.getRightViewContainer().setVisibility(View.INVISIBLE);
    mTitle.setLeftOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        _mActivity.finish();

      }
    });
  }

  private void commit() {
    startProgressDialog();
    Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
    options.size = 90;
    Tiny.getInstance().source(filePath).asFile().withOptions(options).compress(new FileCallback() {
      @Override
      public void callback(boolean isSuccess, String outfile, Throwable t) {
        if (isSuccess) {
          upload(outfile);
        } else {
          stopProgressDialog();
          ToastUtil.showShort("图片压缩失败");
        }

      }
    });
  }

  private void upload(String outfile) {
    UploadManage.saveSace(mRxManager, _mActivity, outfile, new UploadManage.SaveSaceListener() {
      @Override public void onUploadSuccess() {
        Glide.with(_mActivity)
            .load(outfile)
            .into(ivScan);
        ivScan.setEnabled(false);
        stopProgressDialog();
        ToastUtil.showShort("认证成功！");
        startActivity(OperatorActivity.class);
        _mActivity.finish();
      }

      @Override
      public void onFailed(String code, String msg) {
        stopProgressDialog();
        ToastUtil.showShort(msg);
      }
    });
  }

  @OnClick(R.id.ivScan)
  public void onViewClicked() {
    PermissionUtils.checkMorePermissions(_mActivity, permissions, new PermissionUtils.PermissionCheckCallBack() {
      @Override
      public void onHasPermission() {
        if (AppUtils.isCameraUseable()) {
          takePhoto();
        } else {
          PermissionUtils.showDialog(_mActivity, "请在权限管理中开启拍照和录像权限", 150);
        }

      }

      @Override
      public void onUserHasAlreadyTurnedDown(String... permission) {
        PermissionUtils.requestMorePermissions(FaceScanFragment.this, permissions, 1000);
      }

      @Override
      public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
        PermissionUtils.requestMorePermissions(FaceScanFragment.this, permissions, 1000);
      }
    });
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode) {
      case 1000:
        PermissionUtils.onRequestMorePermissionsResult(_mActivity, permissions, new PermissionUtils.PermissionCheckCallBack() {
          @Override
          public void onHasPermission() {
            if (AppUtils.isCameraUseable()) {
              takePhoto();
            } else {
              PermissionUtils.showDialog(_mActivity, "请在权限管理中开启拍照和录像权限", 150);
            }
          }

          @Override
          public void onUserHasAlreadyTurnedDown(String... permission) {
          }

          @Override
          public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
            PermissionUtils.showDialog(_mActivity, "请在权限管理中开启拍照和录像权限", 150);
          }
        });

        break;
    }
  }


  @Override
  public void showRealNameInfo(RealNameBean nameBean) {
    if (nameBean != null) {
      if ("0".equals(nameBean.liveStatus)) {
        ivScan.setEnabled(true);
        mTitle.getRightViewContainer().setVisibility(View.VISIBLE);
        rlExample.setVisibility(View.VISIBLE);
      } else if ("1".equals(nameBean.liveStatus)) {
        if (!TextUtils.isEmpty(nameBean.imgFace)) {
          Glide.with(_mActivity)
              .load(nameBean.imgFace)
              .into(ivScan);
        }
        ivScan.setEnabled(false);
//                mTitle.getRightViewContainer().setVisibility(View.INVISIBLE);
//                rlExample.setVisibility(View.INVISIBLE);
      }
    }

  }

  @Override
  public void saveRealNameSuccess() {

  }

  @Override
  public void showUserInfo(PersonInfoBean infoBean) {

  }

  @Override
  public void saveUserInfoSuccess() {

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

  private String filePath;


  private void takePhoto() {
    //filePath = FileUtil.getSaveFile(_mActivity).getAbsolutePath();
    /*Intent intent = new Intent(_mActivity, CameraActivity.class);
    intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, filePath);
    intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
        CameraActivity.CONTENT_TYPE_GENERAL);
    intent.putExtra(CameraActivity.FACING, 1);
    startActivityForResult(intent, CODE_TAKE_PHOTO);*/
    Intent intent = new Intent(_mActivity, Camera2Activity.class);
    startActivityForResult(intent, CODE_TAKE_PHOTO);

  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
      filePath = data.getStringExtra("picturePath");
      ImageLoaderUtils.display(_mActivity, ivScan, filePath);
      commit();
    }
  }

}
