package com.xinyan.facecheck;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinyan.bioassay.sdk.R;
import com.xinyan.facecheck.lib.XYFaceCheckSDK;
import com.xinyan.facecheck.lib.bean.XYFaceCheckType;
import com.xinyan.facecheck.lib.interfaces.OnXYPreviewListener;
import com.xinyan.facecheck.lib.util.Loggers;
import com.xinyan.facecheck.util.MediaUtils;
import com.xinyan.facecheck.util.ProgressDialog;

public class FosActivity extends AppCompatActivity {

  private static final String TAG = "FosActivity";

  /**
   * 动作检测成功后得到的图像集合.
   */
  private MediaUtils mMediaUtils;
  private TextView mTipsTv;
  private CheckBox mSoundCb;
  private ImageView mActionIv;
  private LinearLayout mCameraPreviewBg;
  private ImageView mImgBack;
  private AnimationDrawable mAnimationDrawable;
  private BroadcastReceiver receiver;
  private FrameLayout mCameraFrameLayout;
  private ProgressDialog dialog;

  private OnXYPreviewListener mXyActionListener = new OnXYPreviewListener() {

    @Override
    public void onTypeChanged(XYFaceCheckType type) {
      cancelProgressDialog();

      if (type == null) {
        type = XYFaceCheckType.TYPE_FACE_FRAME;
      }
      String tips = type.getDesc();

      switch (type) {
        case TYPE_FACE_FRAME:
          mMediaUtils.startMedia(R.raw.v_move_into_rect, tips);
          break;
        case TYPE_BLINK:
          mMediaUtils.startMedia(R.raw.v_blink, tips);
          imageAnim(R.drawable.anim_eye);
          break;
        case TYPE_SHAKE_LEFT:
          mMediaUtils.startMedia(R.raw.v_to_left, tips);
          imageAnim(R.drawable.anim_left);
          break;
        case TYPE_SHAKE_RIGHT:
          mMediaUtils.startMedia(R.raw.v_to_right, tips);
          imageAnim(R.drawable.anim_right);
          break;
        case TYPE_NOD:
          mMediaUtils.startMedia(R.raw.v_nod, tips);
          imageAnim(R.drawable.anim_nod);
          break;
        case TYPE_OPEN_MOUTH:
          mMediaUtils.startMedia(R.raw.v_open_mouth, tips);
          imageAnim(R.drawable.anim_open_mouth);
          break;
        case TYPE_DARK:
          break;
        case TYPE_BRIGHT:
          break;
        case TYPE_SMALL:
          break;
        case TYPE_LARGE:
          break;
        case TYPE_PASS:
          break;
      }

      mTipsTv.setText(tips);
    }

    @Override
    public void onFailure(String code, String message) {

      Intent intent = new Intent();
      intent.putExtra(KV.KEY_FLAG, false);
      setResult(RESULT_OK, intent);

      finish();
    }

    @Override
    public void onSuccess() {
      Loggers.i(TAG, "onSuccess");

      if (mMediaUtils != null) {
        mMediaUtils.stopMedia();
      }

      stopPreview();

      Intent intent = new Intent();
      intent.putExtra(KV.KEY_FLAG, true);
      setResult(RESULT_OK, intent);

      finish();
    }
  };

  private Handler handler = new Handler();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fo_s);
    initView();
    initListener();
    registerReceiver();
    mMediaUtils = new MediaUtils(this);
    initData();

    mCameraFrameLayout = findViewById(R.id.cameraFrameLayout);
    mCameraPreviewBg.setBackgroundResource(R.color.color_000000);
    initActionChecker();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        mCameraPreviewBg.setBackgroundResource(R.color.color_transparency);
      }
    }, 500);// 适配低版本手机界面延迟问题；

    showProgressDialog();

    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        cancelProgressDialog();
      }
    }, 5000);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    XYFaceCheckSDK.getInstance().cancelFaceCheck();
    if (mMediaUtils != null) {
      mMediaUtils.stopMedia();
    }
    if (receiver != null) {
      unregisterReceiver(receiver);
    }
  }

  private void initView() {
    mCameraPreviewBg = findViewById(R.id.activity_step3_camerapreview_bg);
    mTipsTv = findViewById(R.id.activitiy_step3_tips_tv);
    mSoundCb = findViewById(R.id.activity_step3_sound_cb);
    mActionIv = findViewById(R.id.activity_step_3_iv);
    mImgBack = findViewById(R.id.btn_back);
  }

  /**
   * 异常退出时，停止预览和动画提示；
   */
  private void stopPreview() {
    stopImageAnim();
  }

  private void initListener() {
    mSoundCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
          mMediaUtils.closeSound();
        } else {
          mMediaUtils.openSound();
        }
      }
    });
    mImgBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
  }

  private void initData() {
    Intent intent = getIntent();
    if (intent == null) {
      return;
    }
    boolean isPlaySound = XinYanFaceSDK.getInstance().isPlaySound();
    if (isPlaySound) {
      mSoundCb.setChecked(false);
    } else {
      mSoundCb.setChecked(true);
    }
  }

  private void imageAnim(int drawable) {
    mActionIv.setBackgroundResource(drawable);
    mAnimationDrawable = (AnimationDrawable) mActionIv.getBackground();
    mAnimationDrawable.start();
  }

  private void stopImageAnim() {
    if (mActionIv != null && mAnimationDrawable != null && mAnimationDrawable.isRunning()) {
      mAnimationDrawable.stop();
    }
  }

  private void initActionChecker() {
    mTipsTv.setText(getString(R.string.common_face_init_notice));
    XYFaceCheckSDK.getInstance().startXYFaceCheck(mCameraFrameLayout, mXyActionListener);
  }

  @Override
  protected void onPause() {
    super.onPause();
    try {
      XYFaceCheckSDK.getInstance().cancelFaceCheck();
      if (mMediaUtils != null) {
        mMediaUtils.stopMedia();
      }
    } catch (Exception e) {
      Loggers.e(e.getMessage());
    }
    finish();
  }

  private void registerReceiver() {
    receiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Loggers.d("action:" + action);
        if (Intent.ACTION_SCREEN_OFF.equals(action)
            || Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
          FosActivity.this.finish();
        }
      }
    };
    IntentFilter filter = getCloseFilter();
    registerReceiver(receiver, filter);
  }

  public static IntentFilter getCloseFilter() {
    IntentFilter filter = new IntentFilter();
    filter.addAction(Intent.ACTION_SCREEN_OFF); // 锁屏
    filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS); // Home键
    return filter;
  }

  public void showProgressDialog() {
    if (dialog == null && !isFinishing()) {
      dialog = new ProgressDialog(FosActivity.this);
    }
    if (dialog != null) {
      dialog.setCanceledOnTouchOutside(false);
      dialog.showProgress();
    }
  }

  public void cancelProgressDialog() {
    if (dialog != null && dialog.isShowing() && !isFinishing()) {
      dialog.dismissProgress();
    }
  }
}
