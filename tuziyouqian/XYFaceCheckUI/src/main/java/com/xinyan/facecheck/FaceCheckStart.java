package com.xinyan.facecheck;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xinyan.bioassay.sdk.R;
import com.xinyan.facecheck.lib.XYFaceCheckSDK;
import com.xinyan.facecheck.lib.bean.XYResultInfo;
import com.xinyan.facecheck.lib.interfaces.OnXYCheckListener;
import com.xinyan.facecheck.lib.interfaces.OnXYFaceCheckListener;
import com.xinyan.facecheck.lib.util.ErrorCode;
import com.xinyan.facecheck.lib.util.Loggers;
import com.xinyan.facecheck.lib.util.Utils;
import com.xinyan.facecheck.util.FullDate;
import com.xinyan.facecheck.util.PermissionUtil;
import com.xinyan.facecheck.util.ProgressDialog;
import com.xinyan.facecheck.util.UiUtil;

public class FaceCheckStart extends AppCompatActivity {

  private static final String TAG = "FaceCheckStart";

  private static final int CODE_CHECK = 0;

  private XYResultInfo faceCheckInfo;
  private BroadcastReceiver mReceiver;
  public static String ACTION_CLOSE = "com.xinyan.closeActivty";
  private boolean canEdit = false;

  private ProgressDialog dialog;
  private TextView starFaceSDK;
  private String idName;
  private String idNo;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_face_check_start);
    canEdit = getIntent().getBooleanExtra("canEdit", false);
    starFaceSDK = findViewById(R.id.startCheck);
    starFaceSDK.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        if (UiUtil.isFastClick()) {
          return;
        }

        if (PermissionUtil.checkPermission(FaceCheckStart.this, PermissionUtil.needPermissions)) {
            if (PermissionUtil.isCameraUseable()) {
                start();
            } else {
                ToastUtil.showToast(FaceCheckStart.this, "权限检测失败，请检查应用权限设置");
            }
        } else {
          PermissionUtil.requestPermission(FaceCheckStart.this, PermissionUtil.needPermissions);// 权限申请；
        }
      }
    });
    LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
    IntentFilter filter = new IntentFilter();
    filter.addAction(ACTION_CLOSE);

    findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        handleData();
      }
    });

    mReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        if (ACTION_CLOSE.equals(intent.getAction())) {
          if (!isFinishing()) {
            FaceCheckStart.this.finish();
          }
        }
      }
    };
    broadcastManager.registerReceiver(mReceiver, filter);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == PermissionUtil.PERMISSON_REQUESTCODE_FREE) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        start();
      } else {
        Toast.makeText(FaceCheckStart.this, R.string.txt_error_permission, Toast.LENGTH_SHORT)
            .show();
      }
    }
  }

  public void start() {

    showProgressDialog();

    idName = getIntent().getStringExtra(KV.KEY_NAME);
    idNo = getIntent().getStringExtra(KV.KEY_ID);
    String transId = getIntent().getStringExtra(KV.KEY_TRANSID);

    XYFaceCheckSDK.getInstance().checkSDK(this, transId, idName, idNo, new OnXYCheckListener() {
      @Override
      public void onChecked(String errorCode, String errorMsg) {
        cancelProgressDialog();

        if (ErrorCode.CODE_00000.getErrorCode().equals(errorCode)) {
          Intent intent = new Intent(FaceCheckStart.this, FosActivity.class);
          FaceCheckStart.this.startActivityForResult(intent, CODE_CHECK);
        } else {
          // 弹窗
          String message = errorMsg;
          if ("C9999".equals(errorCode)) {
            message = "请允许接受协议";
          }
          Toast.makeText(FaceCheckStart.this, message, Toast.LENGTH_SHORT).show();
        }
      }
    });
    XYFaceCheckSDK.getInstance().setOnXYFaceCheckListener(new OnXYFaceCheckListener() {

      @Override
      public void onCallBack(XYResultInfo returnData) {
        cancelProgressDialog();

        Intent intent = new Intent(FaceCheckStart.this, FaceCheckResultActivity.class);
        intent.putExtra(KV.KEY_DATA, returnData);
        startActivity(intent);

        finish();
      }
    });
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {

      handleData();
    }
    return super.onKeyDown(keyCode, event);
  }

  private void handleData() {
    faceCheckInfo = new XYResultInfo();
    faceCheckInfo.setTransId(XinYanFaceSDK.getInstance().getTransId());
    faceCheckInfo.setErrorCode("C0000");
    faceCheckInfo.setSuccess(false);
    faceCheckInfo.setFee("N");
    faceCheckInfo.setScore("0");
    faceCheckInfo.setErrorMsg("用户取消");
    faceCheckInfo.setIdName(idName);
    faceCheckInfo.setIdNo(idNo);
    if (!canEdit) {
      LocalBroadcastManager.getInstance(FaceCheckStart.this)
              .sendBroadcast(new Intent().setAction(FaceCheckStart.ACTION_CLOSE));

      XinYanFaceSDK.getInstance().faceCheckReturnData(faceCheckInfo);
    }
    this.finish();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Loggers.i(TAG, "onActivityResult: data=" + data);

    if (requestCode == CODE_CHECK) {
      if (resultCode == RESULT_OK) {

        if (data != null) {
          if (data.getBooleanExtra(KV.KEY_FLAG, false)) {
            setStartUnEnabled();
            showProgressDialog();
          } else {
            int errorCount = FullDate.getErrorcount() + 1;
            FullDate.setErrorcount(errorCount);
            if (errorCount >= 3) {
              FullDate.setErrorcount(0);
              faceCheckInfo = new XYResultInfo();
              faceCheckInfo.setErrorCode(ErrorCode.CODE_C0001.getErrorCode());
              faceCheckInfo.setTransId(XinYanFaceSDK.getInstance().getTransId());
              faceCheckInfo.setSuccess(false);
              faceCheckInfo.setFee("N");
              faceCheckInfo.setScore("0");
              faceCheckInfo.setErrorMsg(ErrorCode.CODE_C0001.getErrorMsg());
              faceCheckInfo.setIdName(idName);
              faceCheckInfo.setIdNo(idNo);
              XinYanFaceSDK.getInstance().faceCheckReturnData(faceCheckInfo);

              ToastUtil.showToast(FaceCheckStart.this, ErrorCode.CODE_C0001.getErrorMsg());

              LocalBroadcastManager.getInstance(FaceCheckStart.this)
                  .sendBroadcast(new Intent().setAction(FaceCheckStart.ACTION_CLOSE));
              FaceCheckStart.this.finish();
            } else {
              ToastUtil.showToast(FaceCheckStart.this, ErrorCode.CODE_C0003.getErrorMsg());
            }
          }
        }
      }
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
  }

  private void setStartUnEnabled() {
    starFaceSDK.setEnabled(false);
    starFaceSDK.setText(getText(R.string.txt_face_matching));
  }

  public void showProgressDialog() {
    if (dialog == null && !isFinishing()) {
      dialog = new ProgressDialog(FaceCheckStart.this);
    }
    dialog.showProgress();
  }

  public void cancelProgressDialog() {
    if (dialog != null && dialog.isShowing()) {
      dialog.dismissProgress();
    }
  }
}
