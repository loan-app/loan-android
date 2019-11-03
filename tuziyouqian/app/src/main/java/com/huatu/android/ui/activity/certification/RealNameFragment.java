package com.huatu.android.ui.activity.certification;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.sdk.utils.LogUtil;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.huatu.android.utils.FileUtil;
import com.lib.core.utils.AppUtils;
import com.lib.core.utils.ImageLoaderUtils;
import com.lib.core.utils.LogUtils;
import com.lib.core.utils.PermissionUtils;
import com.lib.core.utils.ToastUtil;
import com.huatu.android.R;
import com.huatu.android.base.BaseFragment;
import com.huatu.android.bean.PersonInfoBean;
import com.huatu.android.bean.RealNameBean;
import com.huatu.android.bean.UploadBean;
import com.huatu.android.utils.UploadManage;
import com.huatu.android.widget.TitleHeaderBar;
import com.huatu.android.widget.dialog.AppUpdateDialog;
import com.huatu.android.widget.dialog.ConfirmDialog;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 周竹
 * @file RealNameFragment
 * @brief
 * @date 2018/4/24 下午1:56
 * Copyright (c) 2017
 * All rights reserved.
 * 终端号1902150652。商户8150728867
 */
public class RealNameFragment extends BaseFragment<CertificatPresenter, CertificatModel> implements CertificatContract.View {
  @BindView(R.id.mTitle)
  TitleHeaderBar mTitle;
  @BindView(R.id.ivCard1)
  ImageView ivCard1;
  @BindView(R.id.ivCard2)
  ImageView ivCard2;
  @BindView(R.id.next)
  Button nextBtn;

  private static final int REQUEST_CODE_CAMERA = 102;
  private int scanType = 0;//0代表扫描正面 1代表反面
  private String idCardFrontPath;//正面身份证扫描路径
  private String idCardBackPath;//反面身份证扫描路径
  private boolean frontHasScan = false;
  private boolean backHasScan = false;

  private String userName;//身份证姓名
  private String userCertNo;//身份证号码
  private String ia;//签发机关
  private String indate;//有效日期
  private String address;//地址
  private String nation;//名族
  private String imgCertFront;//身份证正面url
  private String imgCertBack;//背面url
  String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

  public static RealNameFragment newInstance() {

    Bundle args = new Bundle();

    RealNameFragment fragment = new RealNameFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  protected void getBundleExtras(Bundle bundle) {

  }

  @Override
  protected int getLayoutResource() {
    return R.layout.fragment_realname;
  }

  @Override
  public void initPresenter() {
    mPresenter.setVM(this, mModel);

  }

  @Override
  protected void initView() {
    idCardFrontPath = FileUtil.getSaveFile(_mActivity).getAbsolutePath();
    idCardBackPath = FileUtil.getSaveFile(_mActivity).getAbsolutePath();
    mTitle.setLeftOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        _mActivity.finish();
      }
    });

    mPresenter.getRealNameInfo();
  }

  @OnClick(R.id.next)
  public void next() {
    if (!frontHasScan || !backHasScan) {
      ToastUtil.showShort("请先完善信息！");
      return;
    }
    showConfirmDialog();
  }


  private void showConfirmDialog() {
    ConfirmDialog dialog = new ConfirmDialog(_mActivity);
    String title = "姓名：" + userName + "\n身份证：" + userCertNo;
    dialog.title(title);
    dialog.show();
    dialog.setCanceledOnTouchOutside(true);
    dialog.setOnCallBack(new AppUpdateDialog.OnCallBack() {
      @Override
      public void onCancel() {
        dialog.dismiss();
      }

      @Override
      public void onConfirm() {
        upload();
        dialog.dismiss();
      }
    });
  }

  private void upload() {
    startProgressDialog();
    Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
    options.size = 90;
    Tiny.getInstance().source(idCardFrontPath).asFile().withOptions(options).compress(new FileCallback() {
      @Override
      public void callback(boolean isSuccess, String outfile, Throwable t) {
        if (isSuccess) {
          UploadManage.upload(mRxManager, _mActivity, outfile, new UploadManage.UploadListener() {
            @Override
            public void onUploadSuccess(UploadBean bean) {
              imgCertFront = bean.url;//正面成功后再上传反面
              uploadCertBack(idCardBackPath);
            }

            @Override
            public void onFailed(String code, String msg) {
              stopProgressDialog();
              ToastUtil.showShort(msg);

            }
          });
        } else {
          stopProgressDialog();
          ToastUtil.showShort("图片压缩失败");
        }
      }
    });
  }

  private void uploadCertBack(String idCardBackPath) {
    Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
    options.size = 90;
    Tiny.getInstance().source(idCardBackPath).asFile().withOptions(options).compress(new FileCallback() {
      @Override
      public void callback(boolean isSuccess, String outfile, Throwable t) {
        if (isSuccess) {
          UploadManage.upload(mRxManager, _mActivity, outfile, new UploadManage.UploadListener() {
            @Override
            public void onUploadSuccess(UploadBean bean) {
              imgCertBack = bean.url;//反面上传成功后提交信息
              saveInfo();
            }

            @Override
            public void onFailed(String code, String msg) {
              stopProgressDialog();
              ToastUtil.showShort(msg);

            }
          });
        } else {
          stopProgressDialog();
          ToastUtil.showShort("图片压缩失败");
        }

      }
    });


  }

  private void saveInfo() {
    mPresenter.saveRealNameInfo(userName, userCertNo, ia,
        indate, address, nation, imgCertFront, imgCertBack);
  }


  @OnClick({R.id.ivCard1, R.id.ivCard2})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.ivCard1:
        scanType = 0;
        checkPermission();
        break;
      case R.id.ivCard2:
        scanType = 1;
        checkPermission();
        break;
    }
  }

  private void checkPermission() {

    PermissionUtils.checkMorePermissions(_mActivity, permissions, new PermissionUtils.PermissionCheckCallBack() {
      @Override
      public void onHasPermission() {
        if (AppUtils.isCameraUseable()) {
          startScan();
        } else {
          PermissionUtils.showDialog(_mActivity, "请在权限管理中开启拍照和录像权限", 150);
        }
      }

      @Override
      public void onUserHasAlreadyTurnedDown(String... permission) {
        PermissionUtils.requestMorePermissions(RealNameFragment.this, permissions, 1000);
      }

      @Override
      public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
        PermissionUtils.requestMorePermissions(RealNameFragment.this, permissions, 1000);
      }
    });

  }

  private void startScan() {
    LogUtils.loge("进来了------");
    Intent intent = new Intent(_mActivity, CameraActivity.class);
    intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, scanType == 0 ? idCardFrontPath : idCardBackPath);
    intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
        scanType == 0 ? CameraActivity.CONTENT_TYPE_ID_CARD_FRONT : CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
    startActivityForResult(intent, REQUEST_CODE_CAMERA);

  }

  /**
   * private String userName;//身份证姓名
   * private String userCertNo;//身份证号码
   * private String ia;//签发机关
   * private String indate;//有效日期
   * private String address;//地址
   * private String nation;//名族
   */
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
      String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
      if (contentType.equals(CameraActivity.CONTENT_TYPE_ID_CARD_FRONT)) {
        ImageLoaderUtils.display(_mActivity, ivCard1, idCardFrontPath);
        LogUtils.loge(idCardFrontPath);
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(idCardFrontPath));
        param.setIdCardSide(IDCardParams.ID_CARD_SIDE_FRONT);
        OCR.getInstance(_mActivity).recognizeIDCard(param, new OnResultListener<IDCardResult>() {
          @Override
          public void onResult(IDCardResult result) {
            frontHasScan = true;
            LogUtil.e("AAA", "front success" + result.toString());
            if (result.getName() != null) {
              userName = result.getName().getWords();
            }
            if (result.getIdNumber() != null) {
              userCertNo = result.getIdNumber().getWords();
            }
            if (result.getAddress() != null) {
              address = result.getAddress().getWords();
            }
            if (result.getEthnic() != null) {
              nation = result.getEthnic().getWords();
            }
          }

          @Override
          public void onError(OCRError error) {
            LogUtils.loge(error.getMessage());
          }
        });
      } else if (contentType.equals(CameraActivity.CONTENT_TYPE_ID_CARD_BACK)) {
        LogUtils.loge(idCardBackPath);
        ImageLoaderUtils.display(_mActivity, ivCard2, idCardBackPath);
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(idCardBackPath));
        param.setIdCardSide(IDCardParams.ID_CARD_SIDE_BACK);
        OCR.getInstance(_mActivity).recognizeIDCard(param, new OnResultListener<IDCardResult>() {
          @Override
          public void onResult(IDCardResult result) {
            backHasScan = true;
            LogUtil.e("AAA", "back success" + result.toString());
            if (result.getIssueAuthority() != null) {
              ia = result.getIssueAuthority().getWords();
            }
            if (result.getExpiryDate() != null && result.getSignDate() != null) {
              String singDate = result.getSignDate().getWords();
              String expiryDate = result.getExpiryDate().getWords();
              indate = singDate + "-" + expiryDate;
            }
          }

          @Override
          public void onError(OCRError error) {
            LogUtils.loge(error.getMessage());
          }
        });
      }
    }
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
              startScan();
            } else {
              PermissionUtils.showDialog(_mActivity, "请在权限管理中开启拍照和录像权限", 150);
            }
          }

          @Override
          public void onUserHasAlreadyTurnedDown(String... permission) {
            PermissionUtils.showDialog(_mActivity, "请在权限管理中开启相机权限", 150);
          }

          @Override
          public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
            PermissionUtils.showDialog(_mActivity, "请在权限管理中开启相机权限", 150);
          }
        });

        break;

    }
  }

  @Override
  public void showRealNameInfo(RealNameBean nameBean) {
    if ("1".equals(nameBean.identStatus)) {  //只读，不能做编辑
      ivCard1.setEnabled(false);
      ivCard2.setEnabled(false);
      if (!TextUtils.isEmpty(nameBean.imgCertFront))
        ImageLoaderUtils.display(_mActivity, ivCard1, nameBean.imgCertFront);
      if (!TextUtils.isEmpty(nameBean.imgCertBack))
        ImageLoaderUtils.display(_mActivity, ivCard2, nameBean.imgCertBack);
    } else if ("0".equals(nameBean.identStatus)) {
      ivCard1.setEnabled(true);
      ivCard2.setEnabled(true);
    }
  }

  @Override
  public void saveRealNameSuccess() {
    stopProgressDialog();
    _mActivity.finish();

  }

  @Override
  public void showUserInfo(PersonInfoBean infoBean) {

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
  public void onDestroy() {
    super.onDestroy();
  }

}
