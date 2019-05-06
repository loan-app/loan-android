package com.xinyan.facecheck;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.xinyan.facecheck.lib.XYFaceCheckSDK;
import com.xinyan.facecheck.lib.bean.XYResultInfo;
import com.xinyan.facecheck.lib.interfaces.OnXYFaceCheckListener;
import com.xinyan.facecheck.lib.util.ErrorCode;
import com.xinyan.facecheck.lib.util.IdCardUtils;
import com.xinyan.facecheck.lib.util.IdNameUtils;
import com.xinyan.facecheck.lib.util.RegexUtils;


/**
 * Created by qunwang_xu on 2018/11/1.
 */
public class XinYanFaceSDK {

  private static XinYanFaceSDK xyFaceSDK = null;

  private String memberId;
  private String terminalId;
  private boolean playSound;
  private String license;
  private String transId;

  private XYResultInfo faceCheckReturnData;
  private OnXYFaceCheckListener onXYFaceCheckListener;

  private Handler handler = new Handler(Looper.myLooper()) {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);

      switch (msg.what) {
        case 1:
          if (onXYFaceCheckListener != null) {
            onXYFaceCheckListener.onCallBack(faceCheckReturnData);
          }
          break;
      }
    }
  };
  private String idNo;
  private String idName;

  private XinYanFaceSDK() {}

  public static XinYanFaceSDK getInstance() {
    if (xyFaceSDK == null) {
      synchronized (XinYanFaceSDK.class) {
        if (xyFaceSDK == null) {
          xyFaceSDK = new XinYanFaceSDK();
        }
      }
    }
    return xyFaceSDK;
  }

  /**
   *
   * @param context 上下文
   * @param memberId 商户号
   * @param terminalId 终端号
   * @param license 商户license信息
   * @param playSound 是否播放活体检测声音
   */
  public void init(Context context, String memberId, String terminalId, String license,
      boolean playSound) {
    this.memberId = memberId;
    this.terminalId = terminalId;
    this.license = license;
    this.playSound = playSound;

    XYFaceCheckSDK.getInstance().init(context, memberId, terminalId, license);
  }

  /**
   * 设置是否采集设备指纹信息
   * 
   * @param collectDeviceInfo 是否采集
   */
  public void setCollectDeviceInfo(boolean collectDeviceInfo) {
    XYFaceCheckSDK.getInstance().setCollectDeviceInfo(collectDeviceInfo);
  }

  /**
   * 设置商户回调地址
   * 
   * @param backUrl 回调地址
   */
  public void setBackUrl(String backUrl) {
    XYFaceCheckSDK.getInstance().setBackUrl(backUrl);
  }

  /**
   * 设置是否Debug模式
   * 
   * @param debug 是否Debug模式
   */
  public void setDebug(boolean debug) {
    XYFaceCheckSDK.getInstance().setDebug(debug);
  }

  public boolean isPlaySound() {
    return playSound;
  }

  public String getTransId() {
    return transId;
  }

  /**
   * 回调接口
   * 
   * @param faceCheckListener 接口
   */
  public void setOnXYFaceCheckListener(OnXYFaceCheckListener faceCheckListener) {
    this.onXYFaceCheckListener = faceCheckListener;
  }

  /**
   * 开始检测
   * 
   * @param activity 当前活动视图
   * @param transId 商户订单号
   * @param showInputView 是否展示输入身份证信息的界面
   * @param inputEditing 是否可以编辑身份证信息
   * @param idName 身份证姓名
   * @param idNo 身份证号码
   */
  public void start(Activity activity, String transId, boolean showInputView, boolean inputEditing,
      String idName, String idNo) {
    this.transId = transId;

    if (loginCheck(activity, transId, memberId, terminalId, license)
        && checkIdName(idNo, idName, showInputView, inputEditing)) {

      if (showInputView) {
        Intent nextIntent = new Intent(activity, FaceCheckIDActivity.class);
        nextIntent.putExtra("canEdit", inputEditing);
        nextIntent.putExtra(KV.KEY_NAME, idName);
        nextIntent.putExtra(KV.KEY_ID, idNo);
        nextIntent.putExtra(KV.KEY_TRANSID, transId);
        activity.startActivity(nextIntent);
      } else {
        Intent nextIntent = new Intent(activity, FaceCheckStart.class);
        nextIntent.putExtra(KV.KEY_NAME, idName);
        nextIntent.putExtra(KV.KEY_ID, idNo);
        nextIntent.putExtra(KV.KEY_TRANSID, transId);
        activity.startActivity(nextIntent);
      }
    }
  }

  /**
   * 录入校验
   */
  private boolean loginCheck(Activity activity, String transId, String memberId,
      String terminalId, String license) {
    if (onXYFaceCheckListener == null) {
      return false;
    }
    if (activity == null) {
      errorMessage();
      return false;
    }
    if (TextUtils.isEmpty(memberId)) {
      errorMessage(ErrorCode.CODE_C0006.getErrorCode(), ErrorCode.CODE_C0006.getErrorMsg());
      return false;
    }

    if (TextUtils.isEmpty(terminalId)) {
      errorMessage(ErrorCode.CODE_C0007.getErrorCode(), ErrorCode.CODE_C0007.getErrorMsg());
      return false;
    }

    if (TextUtils.isEmpty(license)) {
      errorMessage(ErrorCode.CODE_C0008.getErrorCode(), ErrorCode.CODE_C0008.getErrorMsg());
      return false;
    }

    if (TextUtils.isEmpty(transId)) {
      errorMessage(ErrorCode.CODE_C0009.getErrorCode(), ErrorCode.CODE_C0009.getErrorMsg());
      return false;
    }

    return true;
  }

  private boolean checkIdName(String idNo, String idName, boolean isShows, boolean isEdit) {
    this.idNo = idNo;
    this.idName = idName;

    if (onXYFaceCheckListener == null) {
      return false;
    }
    if (!isShows || (isShows && !isEdit)) {

      if (!IdNameUtils.isValidatedName(idName)) {
        errorMessage(ErrorCode.CODE_C0011.getErrorCode(), ErrorCode.CODE_C0011.getErrorMsg());
        return false;
      }

      if (TextUtils.isEmpty(idNo) || !RegexUtils.isIDCard15(idNo)
          || !IdCardUtils.isValidatedAllIdcard(idNo)) {
        errorMessage(ErrorCode.CODE_C0010.getErrorCode(), ErrorCode.CODE_C0010.getErrorMsg());
        return false;
      }
    }
    return true;
  }

  private void errorMessage() {
    errorMessage(ErrorCode.CODE_C0002.getErrorCode(), ErrorCode.CODE_C0002.getErrorMsg());
  }

  private void errorMessage(String code, String message) {
    XYResultInfo faceCheckInfo = new XYResultInfo();
    faceCheckInfo.setErrorCode(code);
    faceCheckInfo.setFee("N");
    faceCheckInfo.setSuccess(false);
    faceCheckInfo.setScore("0");
    faceCheckInfo.setErrorMsg(message);
    faceCheckInfo.setTransId(transId);
    faceCheckInfo.setIdNo(idNo);
    faceCheckInfo.setIdName(idName);

    faceCheckReturnData(faceCheckInfo);
  }

  public void faceCheckReturnData(XYResultInfo returnData) {
    faceCheckReturnData = returnData;

    handler.sendEmptyMessage(1);
  }

  public void setEnv(String env) {
    XYFaceCheckSDK.getInstance().setEnv(env);
  }
}
