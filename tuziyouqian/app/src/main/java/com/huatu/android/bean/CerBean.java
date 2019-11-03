package com.huatu.android.bean;

/**
 * 认证状态
 */
public class CerBean {
  /**
   * realName是否实名认证
   * userDetails是否个人信息认证
   * bindbank绑卡
   * mobile是否运营商手机认证
   * liveness是否人脸识别认证
   */
  public int realName;
  public int userDetails;
  public int bindbank;
  public int mobile;
  public int liveness;
  public int alipay;
}
