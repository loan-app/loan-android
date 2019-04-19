package com.mod.tuziyouqian.ui.activity.certification;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.baidu.ocr.ui.camera.CameraNativeHelper;
import com.baidu.ocr.ui.camera.CameraView;
import com.lib.core.utils.AppUtils;
import com.lib.core.utils.ImageLoaderUtils;
import com.lib.core.utils.LogUtils;
import com.lib.core.utils.PermissionUtils;
import com.lib.core.utils.ToastUtil;
import com.mod.tuziyouqian.R;
import com.mod.tuziyouqian.base.App;
import com.mod.tuziyouqian.base.BaseFragment;
import com.mod.tuziyouqian.bean.PersonInfoBean;
import com.mod.tuziyouqian.bean.RealNameBean;
import com.mod.tuziyouqian.bean.UploadBean;
import com.mod.tuziyouqian.utils.FileUtil;
import com.mod.tuziyouqian.utils.UploadManage;
import com.mod.tuziyouqian.widget.TitleHeaderBar;
import com.mod.tuziyouqian.widget.dialog.AppUpdateDialog;
import com.mod.tuziyouqian.widget.dialog.ConfirmDialog;
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
 */
public class RealNameFragment extends BaseFragment<CertificatPresenter, CertificatModel> implements CertificatContract.View {
/*    @Override
    public void showFaceCheckSuccess() {

    }*/

    @BindView(R.id.mTitle)
    TitleHeaderBar mTitle;
    @BindView(R.id.ivCard1)
    ImageView ivCard1;
    @BindView(R.id.ivCard2)
    ImageView ivCard2;
    @BindView(R.id.tvName)
    EditText tvName;
    @BindView(R.id.tvCard)
    EditText tvCard;
    private static final int REQUEST_CODE_CAMERA = 102;
    private int scanType = 0;//0代表扫描正面 1代表反面
    private String idCardFrontPath;//正面身份证扫描路径
    private String idCardBackPath;//反面身份证扫描路径
    private String userName;//身份证姓名
    private String userCertNo;//身份证号码
    private String ia;//签发机关
    private String indate;//有效日期
    private String address;//地址
    private String nation;//名族
    private String imgCertFront;//身份证正面url
    private String imgCertBack;//背面url

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
        mTitle.getRightViewContainer().setVisibility(View.INVISIBLE);
        mTitle.setCustomizedRightString("提交");
        mTitle.setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(idCardFrontPath) || TextUtils.isEmpty(idCardBackPath) || TextUtils.isEmpty(tvCard.getText()) || TextUtils.isEmpty(tvName.getText())) {
                    ToastUtil.showShort("请先完善信息！");
                    return;
                }
                showConfirmDialog();
            }
        });
        mTitle.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _mActivity.finish();
            }
        });

        mPresenter.getRealNameInfo();
        OCR.getInstance(_mActivity).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
            }
        }, App.getInstance());

        CameraNativeHelper.init(_mActivity, OCR.getInstance(_mActivity).getLicense(),
                new CameraNativeHelper.CameraNativeInitCallback() {
                    @Override
                    public void onError(int errorCode, Throwable e) {
                        String msg;
                        switch (errorCode) {
                            case CameraView.NATIVE_SOLOAD_FAIL:
                                msg = "加载so失败，请确保apk中存在ui部分的so";
                                break;
                            case CameraView.NATIVE_AUTH_FAIL:
                                msg = "授权本地质量控制token获取失败";
                                break;
                            case CameraView.NATIVE_INIT_FAIL:
                                msg = "本地质量控制";
                                break;
                            default:
                                msg = String.valueOf(errorCode);
                        }
                    }
                });
    }

    private void showConfirmDialog(){
        ConfirmDialog dialog = new ConfirmDialog(_mActivity);
        String title = "姓名："+ tvName.getText().toString()+"\n身份证："+ tvCard.getText().toString();
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
        mPresenter.saveRealNameInfo(tvName.getText().toString()/*userName, userCertNo*/, tvCard.getText().toString(),ia,
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
        PermissionUtils.checkPermission(_mActivity, Manifest.permission.CAMERA, new PermissionUtils.PermissionCheckCallBack() {
            @Override
            public void onHasPermission() {
                if (AppUtils.isCameraUseable()) {
                    scanIdCard();//扫描身份证
                } else {
                    PermissionUtils.showDialog(_mActivity, "请在权限管理中开启拍照和录像权限", 150);
                }
            }

            @Override
            public void onUserHasAlreadyTurnedDown(String... permission) {
                PermissionUtils.requestPermission(RealNameFragment.this, Manifest.permission.CAMERA, 1000);
            }

            @Override
            public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                PermissionUtils.requestPermission(RealNameFragment.this, Manifest.permission.CAMERA, 1000);
            }
        });

    }

    private void scanIdCard() {
        Intent intent = new Intent(_mActivity, CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(App.getInstance()).getAbsolutePath());
        intent.putExtra(CameraActivity.KEY_NATIVE_TOKEN,
                OCR.getInstance(_mActivity).getLicense());
        intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE,
                true);
        // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
        // 请手动使用CameraNativeHelper初始化和释放模型
        // 推荐这样做，可以避免一些activity切换导致的不必要的异常
        intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL,
                true);
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, scanType == 0 ? CameraActivity.CONTENT_TYPE_ID_CARD_FRONT : CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                String filePath = data.getStringExtra(CameraActivity.KEY_OUTPUT_FILE_PATH);
                if (!TextUtils.isEmpty(contentType)) {
                    if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath);
                    } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath);
                    }
                }
            }
        }
    }

    private void recIDCard(String idCardSide, String filePath) {
        File imageFile = new File(filePath);
        IDCardParams param = new IDCardParams();
        param.setImageFile(imageFile);
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(20);

        OCR.getInstance(_mActivity).recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                try {
                    if (IDCardParams.ID_CARD_SIDE_FRONT.equals(idCardSide)) {//正面识别成功
                        idCardFrontPath = filePath;
                        ImageLoaderUtils.display(_mActivity, ivCard1, imageFile);
                        userName = result.getName().toString();
                        userCertNo = result.getIdNumber().toString();
                        address = result.getAddress().toString();
                        nation = result.getEthnic().toString();
                        tvName.setText(userName);
                        tvCard.setText(userCertNo);
                    } else if (IDCardParams.ID_CARD_SIDE_BACK.equals(idCardSide)) {//反面识别成功
                        idCardBackPath = filePath;
                        ImageLoaderUtils.display(_mActivity, ivCard2, imageFile);
                        ia = result.getIssueAuthority().toString();
                        indate = result.getSignDate().toString() + "-" + result.getExpiryDate().toString();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(OCRError error) {
                LogUtils.logd(error.getMessage());
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000:
                PermissionUtils.onRequestPermissionResult(this, Manifest.permission.CAMERA, grantResults, new PermissionUtils.PermissionCheckCallBack() {
                    @Override
                    public void onHasPermission() {
                        if (AppUtils.isCameraUseable()) {
                            scanIdCard();//扫描身份证
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
            tvName.setEnabled(false);
            tvCard.setEnabled(false);
            if (!TextUtils.isEmpty(nameBean.imgCertFront))
                ImageLoaderUtils.display(_mActivity, ivCard1, nameBean.imgCertFront);
            if (!TextUtils.isEmpty(nameBean.imgCertBack))
                ImageLoaderUtils.display(_mActivity, ivCard2, nameBean.imgCertBack);
            mTitle.getRightViewContainer().setVisibility(View.INVISIBLE);
            tvName.setText(nameBean.userName);
            tvCard.setText(nameBean.userCertNo);
        } else if ("0".equals(nameBean.identStatus)) {
            ivCard1.setEnabled(true);
            ivCard2.setEnabled(true);
            tvName.setEnabled(true);
            tvCard.setEnabled(true);
            mTitle.getRightViewContainer().setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void saveRealNameSuccess() {
        stopProgressDialog();
//        ToastUtil.showShort("提交成功");
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
        // 释放本地质量控制模型
        CameraNativeHelper.release();
        super.onDestroy();
        // 释放内存资源
        OCR.getInstance(_mActivity).release();
    }
}
