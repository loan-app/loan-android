package com.mod.jishidai.ui.activity.certification;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.lib.core.utils.AppUtils;
import com.lib.core.utils.LogUtils;
import com.lib.core.utils.MD5Util;
import com.lib.core.utils.PermissionUtils;
import com.lib.core.utils.ToastUtil;
import com.mod.jishidai.R;
import com.mod.jishidai.base.BaseFragment;
import com.mod.jishidai.bean.PersonInfoBean;
import com.mod.jishidai.bean.RealNameBean;
import com.mod.jishidai.utils.AppConfig;
import com.mod.jishidai.utils.UploadManage;
import com.mod.jishidai.widget.TitleHeaderBar;
import com.xinyan.facecheck.XinYanFaceSDK;
import com.xinyan.facecheck.lib.bean.XYResultInfo;
import com.xinyan.facecheck.lib.interfaces.OnXYFaceCheckListener;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

public class FaceScanFragment extends BaseFragment<CertificatPresenter, CertificatModel> implements CertificatContract.View, OnXYFaceCheckListener {

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
        //设置商户回调地址
        XinYanFaceSDK.getInstance().setBackUrl("");
        //设置是否采集设备指纹信息
        XinYanFaceSDK.getInstance().setCollectDeviceInfo(true);
        XinYanFaceSDK.getInstance().setOnXYFaceCheckListener(this);
    }

    private void commit(Bitmap bitmap) {
        startProgressDialog();
        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
        options.size = 40;
        Tiny.getInstance().source(bitmap).asFile().withOptions(options).compress(new FileCallback() {
            @Override
            public void callback(boolean isSuccess, String outfile, Throwable t) {
                if (isSuccess && !TextUtils.isEmpty(outfile)) {
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
            @Override
            public void onUploadSuccess() {
                Glide.with(_mActivity)
                        .load(outfile)
                        .into(ivScan);
                ivScan.setEnabled(false);
                stopProgressDialog();
                ToastUtil.showShort("认证成功！");
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


    private void takePhoto() {
/**
 * 开始检测
 * @param activity 当前活动视图
 * @param transId 商户订单号
 * @param showInputView 是否展示输入身份证信息的界面
 * @param inputEditing 是否可以编辑身份证信息
 * @param idName 身份证姓名
 * @param idNo 身份证号码
 */
        LogUtils.loge("时间戳---》" + MD5Util.MD5(String.valueOf(System.currentTimeMillis())) + "----" + String.valueOf(System.currentTimeMillis()));
        XinYanFaceSDK.getInstance().start(_mActivity, MD5Util.MD5(String.valueOf(System.currentTimeMillis()))+String.valueOf(System.currentTimeMillis()), false, false, "沈东鑫", "362321199301014039");
    }





    @Override
    public void onCallBack(XYResultInfo xyResultInfo) {
        if (xyResultInfo != null&&xyResultInfo.getImg().length>0) {
            commit(BitmapFactory.decodeByteArray(xyResultInfo.getImg(), 0, xyResultInfo.getImg().length));
        }
    }
}
