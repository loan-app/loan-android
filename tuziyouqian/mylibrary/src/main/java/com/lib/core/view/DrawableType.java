package com.lib.core.view;

/**
 * 枚举Drawable的类型<BR>
 * Created by Ivan on 2018/1/16.
 */
public enum DrawableType {

  /**
   * 画普通矩形的Drawable
   */
  NORMAL(0),

  /**
   * 画椭圆形状的Drawable
   */
  OVAL(1),

  /**
   * 画带圆角矩形的Drawable
   */
  ROUNDED(2),

  /**
   * 画圆形的Drawable
   */
  CIRCLE(3);

  private final int mNativeInt;

  DrawableType(int ni) {
    mNativeInt = ni;
  }

  /**
   * 获取类型 int 值
   *
   * @return 类型的 int 值
   */
  public final int getNativeInt() {
    return mNativeInt;
  }
}
