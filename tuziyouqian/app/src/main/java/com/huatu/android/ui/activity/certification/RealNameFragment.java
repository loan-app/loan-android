package com.huatu.android.ui.activity.certification;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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
import com.huatu.android.utils.AppConfig;
import com.huatu.android.utils.UploadManage;
import com.huatu.android.widget.TitleHeaderBar;
import com.huatu.android.widget.dialog.AppUpdateDialog;
import com.huatu.android.widget.dialog.ConfirmDialog;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import ocr.android.xinyan.com.xinyan_android_ocr_sdk.Interface.OnOcrResultListener;
import ocr.android.xinyan.com.xinyan_android_ocr_sdk.XinyanOCRSDK;
import ocr.android.xinyan.com.xinyan_android_ocr_sdk.entity.XinyanOcrBankCardCallBackData;
import ocr.android.xinyan.com.xinyan_android_ocr_sdk.entity.XinyanOcrIdCardCallBackData;

/**
 * @author 周竹
 * @file RealNameFragment
 * @brief
 * @date 2018/4/24 下午1:56
 * Copyright (c) 2017
 * All rights reserved.
 * 终端号1902150652。商户8150728867
 */
public class RealNameFragment extends BaseFragment<CertificatPresenter, CertificatModel> implements CertificatContract.View, OnOcrResultListener {
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
    private Bitmap idCardFrontPath;//正面身份证扫描路径
    private Bitmap idCardBackPath;//反面身份证扫描路径
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
        XinyanOCRSDK.getInstents().setOnOcrResultListener(this);

        mTitle.getRightViewContainer().setVisibility(View.INVISIBLE);
        mTitle.setCustomizedRightString("提交");
        mTitle.setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(idCardFrontPath.toString()) || TextUtils.isEmpty(idCardBackPath.toString()) || TextUtils.isEmpty(tvCard.getText()) || TextUtils.isEmpty(tvName.getText())) {
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
    }


    private void showConfirmDialog() {
        ConfirmDialog dialog = new ConfirmDialog(_mActivity);
        String title = "姓名：" + tvName.getText().toString() + "\n身份证：" + tvCard.getText().toString();
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

    private void uploadCertBack(Bitmap idCardBackPath) {
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
        mPresenter.saveRealNameInfo(tvName.getText().toString()/*userName, userCertNo*/, tvCard.getText().toString(), ia,
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
//                    scanIdCard();//扫描身份证\
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
        /**
         * 启动OCR SDK
         * @param activity 当前活动窗口
         * @param xyOCRFunction  产品类型 身份证03302 银行卡03303
         * @param xyOCRTransactionID  商户订单号
         * @param xyOCRBackUrl   商户回调地址
         */
        XinyanOCRSDK.getInstents().startScan(_mActivity, "03302", AppConfig.MEMBER_ID, "");
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
//                            scanIdCard();//扫描身份证
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
        super.onDestroy();
    }

    @Override
    public void onSuccessCallBack(XinyanOcrIdCardCallBackData.IdCardInfo idCardInfo, XinyanOcrBankCardCallBackData.BankCardInfo bankCardInfo, String s, String s1) {
        /**
         * @param idCardInfo 身份证结果信息
         * @param bankCardInfo 银行卡结果信息
         * @param resultcode 结果码
         * @param resultMsg 结果信息
         */
        LogUtils.loge(idCardInfo.getIdcard_address()
                + "\n" + idCardInfo.getIdcard_name()
                + "\n" + idCardInfo.getIdcard_number()
                + "\n" + idCardInfo.getIdcard_authority()
                + "\n" + idCardInfo.getIdcard_gender()
                + "\n" + idCardInfo.getValid_date()
                + "\n" + idCardInfo.getIdcard_birthday()
                + "\n" + idCardInfo.getIdcard_nation()
                + "\n" + idCardInfo.getIdcard_back_image());

        idCardFrontPath = idCardInfo.getIdcard_front_image();//正面身份证扫描路径
        LogUtils.loge("黑森森---"+idCardFrontPath);
        idCardBackPath =idCardInfo.getIdcard_back_image();//反面身份证扫描路径
        LogUtils.loge("黑森森---"+idCardBackPath);
        userName = idCardInfo.getIdcard_name();//身份证姓名
        userCertNo = idCardInfo.getIdcard_number();//身份证号码
        ia = idCardInfo.getIdcard_authority();//签发机关
        indate = idCardInfo.getValid_date();//有效日期
        address = idCardInfo.getIdcard_address();//地址
        nation = idCardInfo.getIdcard_nation();//名族


        ToastUtil.showShort(s1);
        //姓名
        tvName.setText(idCardInfo.getIdcard_name());
        //身份证号
        tvCard.setText(idCardInfo.getIdcard_number());
        //身份证正面
        ivCard1.setImageBitmap(idCardInfo.getIdcard_front_image());
        //省份证背面
        ivCard2.setImageBitmap(idCardInfo.getIdcard_back_image());

    }

    @Override
    public void onFailedCallBack(String s, String s1) {
        /**
         * 失败、异常回调
         * @param resultcode
         * @param resultMsg
         */
        LogUtils.loge("s----" + s);
        LogUtils.loge("s1----" + s1);
    }

    /**
     * 把batmap 转file
     *
     * @param bitmap
     * @param filepath
     */
    public static File saveBitmapFile(Bitmap bitmap, String filepath) {
        File file = new File(filepath);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
