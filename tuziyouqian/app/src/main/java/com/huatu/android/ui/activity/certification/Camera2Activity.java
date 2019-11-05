package com.huatu.android.ui.activity.certification;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.widget.ImageView;

import com.google.android.cameraview.CameraView;
import com.huatu.android.R;
import com.huatu.android.base.BaseActivity;
import com.huatu.android.utils.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.OnClick;

public class Camera2Activity extends BaseActivity {

    @BindView(R.id.mCamera)
    CameraView mCameraView;
    @BindView(R.id.iv_takePhoto)
    ImageView ivTakePhoto;
    private Handler mBackgroundHandler;

    @Override
    protected void getBundleExtras(Bundle bundle) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_camera2;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        if (mCameraView != null) {
            mCameraView.addCallback(mCallback);
        }
    }

    private CameraView.Callback mCallback = new CameraView.Callback() {
        @Override
        public void onCameraOpened(CameraView cameraView) {
            super.onCameraOpened(cameraView);
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            super.onCameraClosed(cameraView);
        }

        @Override
        public void onPictureTaken(CameraView cameraView, byte[] data) {
            getBackgroundHandler().post(new Runnable() {
                @Override
                public void run() {
                    File file = FileUtil.getSaveFile(Camera2Activity.this);
                    OutputStream os = null;
                    try {
                        os = new FileOutputStream(file);
                        os.write(data);
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (os != null) {
                            try {
                                os.close();
                                Intent intent = new Intent();
                                intent.putExtra("picturePath", file.getAbsolutePath());
                                setResult(RESULT_OK, intent);
                                finish();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }

    };

    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraView.start();
    }

    @Override
    protected void onPause() {
        mCameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler.getLooper().quitSafely();
            } else {
                mBackgroundHandler.getLooper().quit();
            }
            mBackgroundHandler = null;
        }
    }


    @OnClick(R.id.iv_takePhoto)
    public void onViewClicked() {
        if (mCameraView != null) {
            mCameraView.takePicture();
        }
    }
}
