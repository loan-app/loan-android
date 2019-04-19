package com.mod.tuziyouqian.ui.activity.certification;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.lib.core.utils.AppUtils;
import com.lib.core.utils.PermissionUtils;
import com.lib.core.utils.ToastUtil;
import com.mod.tuziyouqian.R;
import com.mod.tuziyouqian.base.BaseFragment;
import com.mod.tuziyouqian.bean.PersonInfoBean;
import com.mod.tuziyouqian.bean.RealNameBean;
import com.mod.tuziyouqian.utils.UploadManage;
import com.mod.tuziyouqian.widget.TitleHeaderBar;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import java.io.IOException;

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


/*        mTitle.setCustomizedRightString("提交");
        mTitle.setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap == null) {
                    ToastUtil.showShort("请先拍照！");
                    return;
                }
                commit();
            }
        });*/
        /*// 根据需求添加活体动作
        App.livenessList.clear();
        App.livenessList.add(LivenessTypeEnum.Eye);
        App.livenessList.add(LivenessTypeEnum.Mouth);
        App.livenessList.add(LivenessTypeEnum.HeadUp);
        App.livenessList.add(LivenessTypeEnum.HeadDown);
        App.livenessList.add(LivenessTypeEnum.HeadLeft);
        App.livenessList.add(LivenessTypeEnum.HeadRight);
        App.livenessList.add(LivenessTypeEnum.HeadLeftOrRight);
        FaceSDKManager.getInstance().initialize(_mActivity, "zhaocaihou-face-android", "idl-license.face-android");*/

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
//                _mActivity.finish();
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
        PermissionUtils.checkPermission(_mActivity, Manifest.permission.CAMERA, new PermissionUtils.PermissionCheckCallBack() {
            @Override
            public void onHasPermission() {
                if (AppUtils.isCameraUseable()) {
                   /* setFaceConfig();
                    startActivityForResult(FaceLivenessExpActivity.class, 1000);*/
                    takePhoto();
                } else {
                    PermissionUtils.showDialog(_mActivity, "请在权限管理中开启拍照和录像权限", 150);
                }

            }

            @Override
            public void onUserHasAlreadyTurnedDown(String... permission) {
                PermissionUtils.requestPermission(FaceScanFragment.this, Manifest.permission.CAMERA, 1000);
            }

            @Override
            public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                PermissionUtils.requestPermission(FaceScanFragment.this, Manifest.permission.CAMERA, 1000);
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
                            /*setFaceConfig();
                            startActivityForResult(FaceLivenessExpActivity.class, 1000);*/
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK || resultCode == CODE_TAKE_PHOTO) {
            switch (requestCode) {
                case 1000:
                    if (data != null) {
                        String path = data.getStringExtra("picturePath");
                        if (!TextUtils.isEmpty(path)) {
                            Bitmap bitmap = BitmapFactory.decodeFile(path);
                            int degree = readPictureDegree(path);
                            if (degree != 0) {
                                bitmap = rotaingImageView(degree, bitmap);
                            }
                            commit(bitmap);
                        }
                    }

                    break;
            }
        }
    }

  /*    private static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64Utils.decode(base64Data, Base64Utils.NO_WRAP);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

 private void setFaceConfig() {
        FaceConfig config = FaceSDKManager.getInstance().getFaceConfig();
        // SDK初始化已经设置完默认参数（推荐参数），您也根据实际需求进行数值调整
        config.setLivenessTypeList(App.livenessList);
        config.setLivenessRandom(false);
        config.setBlurnessValue(FaceEnvironment.VALUE_BLURNESS);
        config.setBrightnessValue(FaceEnvironment.VALUE_BRIGHTNESS);
        config.setCropFaceValue(FaceEnvironment.VALUE_CROP_FACE_SIZE);
        config.setHeadPitchValue(FaceEnvironment.VALUE_HEAD_PITCH);
        config.setHeadRollValue(FaceEnvironment.VALUE_HEAD_ROLL);
        config.setHeadYawValue(FaceEnvironment.VALUE_HEAD_YAW);
        config.setMinFaceSize(FaceEnvironment.VALUE_MIN_FACE_SIZE);
        config.setNotFaceValue(0.1f);
        config.setOcclusionValue(FaceEnvironment.VALUE_OCCLUSION);
        config.setCheckFaceQuality(true);
        config.setLivenessRandomCount(2);
        config.setFaceDecodeNumberOfThreads(2);

        FaceSDKManager.getInstance().setFaceConfig(config);
    }*/

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
                mTitle.getRightViewContainer().setVisibility(View.INVISIBLE);
                rlExample.setVisibility(View.INVISIBLE);
            }
        }

    }

    @Override
    public void saveRealNameSuccess() {

    }

    @Override
    public void showUserInfo(PersonInfoBean infoBean) {

    }

/*    @Override
    public void showFaceCheckSuccess() {
        stopProgressDialog();
//        ToastUtil.showShort("提交成功");
        _mActivity.finish();

    }*/

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
        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        if (Build.VERSION.SDK_INT >= 24) {
            photoUri = get24MediaFileUri();
        } else {
            photoUri = getMediaFileUri();
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, CODE_TAKE_PHOTO);*/
        startActivityForResult(Camera2Activity.class, 1000);
    }


    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    private int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            // 计算旋转角度
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    degree = 0;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    /*
     * 旋转图片
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    private Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    private void setPictureDegreeZero(String path) {
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            //修正图片的旋转角度，设置其不旋转。这里也可以设置其旋转的角度，可以传值过去，
            //例如旋转90度，传值ExifInterface.ORIENTATION_ROTATE_90，需要将这个值转换为String类型的
            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "no");
            exifInterface.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
