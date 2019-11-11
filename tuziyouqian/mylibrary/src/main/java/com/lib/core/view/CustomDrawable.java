package com.lib.core.view;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView.ScaleType;

/**
 * 自定义的 Drawable <BR>
 * 支持 Normal, Oval, Rounded, Circle
 *
 * Created by Ivan on 2018/1/16.
 */
@SuppressWarnings("unused") public class CustomDrawable extends Drawable {

  private static final String TAG = "CustomDrawable";

  /**
   * 像素计算 误差
   */
  private static final float DEVIATION = 0.5f;

  /**
   * 整个可画区域的矩形
   * 坐标点为 单精度浮点型
   */
  private final RectF mBoundsRect = new RectF();

  /**
   * Drawable的矩形(图片内容显示的区域)
   */
  private final RectF mDrawableRect = new RectF();

  /**
   * 边线的矩形
   */
  private final RectF mBorderRect = new RectF();

  /**
   * 着色器的矩阵
   */
  private final Matrix mShaderMatrix = new Matrix();

  /**
   * 要显示的Bitmap的矩形
   */
  private final RectF mBitmapRect = new RectF();

  /**
   * 位图的画笔
   */
  private final Paint mBitmapPaint;

  /**
   * 渲染图像,mBitmapPaint画笔的着色器
   */
  private final BitmapShader mBitmapShader;

  /**
   * 位图的宽度
   */
  private final int mBitmapWidth;

  /**
   * 位图的高度
   */
  private final int mBitmapHeight;

  /**
   * 边线的画笔
   */
  private final Paint mBorderPaint;

  /**
   * 图片圆角的角度
   */
  private float mCornerRadius;
  private float mLeftTopRadius;
  private float mRightTopRadius;
  private float mRightBottomRadius;
  private float mLeftBottomRadius;

  private Path mBorderPath;
  private Path mDrawablePath;
  /**
   * 边线的宽度
   */
  private int mBorderWidth;

  /**
   * 边线的颜色值
   */
  private int mBorderColor;

  /**
   * 当前Drawable的缩放方式
   */
  private ScaleType mScaleType = ScaleType.FIT_XY;

  /**
   * 当前要画图形的类型
   */
  private DrawableType mDrawableType = DrawableType.ROUNDED;

  /**
   * 将Drawable对象包装成自定义的Drawable对象(CustomDrawable)<BR>
   * 提供带边界线的Drawable
   *
   * @param bitmap bitmap 对象实例
   * @param leftTopRadius 左上角角度
   * @param rightTopRadius 右上角角度
   * @param rightBottomRadius 右下角角度
   * @param leftBottomRadius 左下角角度
   * @param border 边线的宽度
   * @param borderColor 边线的颜色
   */
  CustomDrawable(Bitmap bitmap, float leftTopRadius, float rightTopRadius, float rightBottomRadius,
                 float leftBottomRadius, int border, int borderColor) {
    this.mLeftTopRadius = leftTopRadius;
    this.mRightTopRadius = rightTopRadius;
    this.mRightBottomRadius = rightBottomRadius;
    this.mLeftBottomRadius = leftBottomRadius;

    mBorderWidth = border;
    mBorderColor = borderColor;

    //获取位图的宽度
    mBitmapWidth = bitmap.getWidth();
    //获取位图的高度
    mBitmapHeight = bitmap.getHeight();
    //设置位图矩形的位置
    mBitmapRect.set(0, 0, mBitmapWidth, mBitmapHeight);

    //创建BitmapShader对象,当绘制的区域比Bitmap大的时候，可以指定X、Y的取值类型，
    mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    //设置着色器的变换矩阵
    mBitmapShader.setLocalMatrix(mShaderMatrix);

    //创建一个位图画笔对象
    mBitmapPaint = new Paint();
    //设置抗锯齿
    mBitmapPaint.setAntiAlias(true);
    //设置着色器
    mBitmapPaint.setShader(mBitmapShader);

    //创建一个边界的画笔
    mBorderPaint = new Paint();
    //设置抗锯齿
    mBorderPaint.setAntiAlias(true);
    //设置画笔颜色
    mBorderPaint.setColor(mBorderColor);
    //设置画笔宽度
    mBorderPaint.setStrokeWidth(border);
    mBorderPath = new Path();
    mDrawablePath = new Path();
  }

  /**
   * 设置缩放方式<BR>
   *
   * @param scaleType 缩放方式的枚举类型
   */
  void setScaleType(ScaleType scaleType) {
    if (scaleType == null) {
      scaleType = ScaleType.FIT_XY;
    }
    if (mScaleType != scaleType) {
      mScaleType = scaleType;
      setMatrix();
    }
  }

  /**
   * 获取ScaleType<BR>
   *
   * @return 返回ScaleType类型的枚举值
   */
  ScaleType getScaleType() {
    return mScaleType;
  }

  /**
   * 设置DrawableType<BR>
   *
   * @param drawableType DrawableType类型的枚举值
   */
  void setDrawableType(DrawableType drawableType) {
    if (drawableType == null) {
      drawableType = DrawableType.ROUNDED;
    }
    if (mDrawableType != drawableType) {
      mDrawableType = drawableType;
    }
  }

  /**
   * 获取ScaleType<BR>
   *
   * @return 返回DrawableType类型的枚举值
   */
  protected DrawableType getDrawableType() {
    return mDrawableType;
  }

  /**
   * 根据不同的ScaleType对图片进行不同的矩阵变换<BR>
   */
  @SuppressWarnings("SuspiciousNameCombination") private void setMatrix() {
    //根据整个可画区域大小来设置边界矩形的大小
    mBorderRect.set(mBoundsRect);
    //设置Drawable矩形内画图显示区域的坐标
    mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width() - mBorderWidth,
        mBorderRect.height() - mBorderWidth);

    float scale;
    float dx;
    float dy;

    switch (mScaleType) {
      case CENTER:
        mBorderRect.set(mBoundsRect);
        mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width() - mBorderWidth,
            mBorderRect.height() - mBorderWidth);

        mShaderMatrix.set(null);
        mShaderMatrix.setTranslate(
            (int) ((mDrawableRect.width() - mBitmapWidth) * DEVIATION + DEVIATION),
            (int) ((mDrawableRect.height() - mBitmapHeight) * DEVIATION + DEVIATION));
        break;
      case CENTER_CROP:
        mBorderRect.set(mBoundsRect);
        mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width() - mBorderWidth,
            mBorderRect.height() - mBorderWidth);

        mShaderMatrix.set(null);

        dx = 0;
        dy = 0;

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
          scale = mDrawableRect.height() / (float) mBitmapHeight;
          dx = (mDrawableRect.width() - mBitmapWidth * scale) * DEVIATION;
        } else {
          scale = mDrawableRect.width() / (float) mBitmapWidth;
          dy = (mDrawableRect.height() - mBitmapHeight * scale) * DEVIATION;
        }
        //矩阵缩放
        mShaderMatrix.setScale(scale, scale);
        //平移到中心点
        mShaderMatrix.postTranslate((int) (dx + DEVIATION) + mBorderWidth,
            (int) (dy + DEVIATION) + mBorderWidth);
        break;
      case CENTER_INSIDE:
        mShaderMatrix.set(null);

        if (mBitmapWidth <= mBoundsRect.width() && mBitmapHeight <= mBoundsRect.height()) {
          scale = 1.0f;
        } else {
          scale = Math.min(mBoundsRect.width() / (float) mBitmapWidth,
              mBoundsRect.height() / (float) mBitmapHeight);
        }

        dx = (int) ((mBoundsRect.width() - mBitmapWidth * scale) * DEVIATION + DEVIATION);
        dy = (int) ((mBoundsRect.height() - mBitmapHeight * scale) * DEVIATION + DEVIATION);

        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate(dx, dy);

        mBorderRect.set(mBitmapRect);
        mShaderMatrix.mapRect(mBorderRect);
        mDrawableRect.set(mBorderRect.left + mBorderWidth, mBorderRect.top + mBorderWidth,
            mBorderRect.right - mBorderWidth, mBorderRect.bottom - mBorderWidth);
        mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect, Matrix.ScaleToFit.FILL);
        break;
      case FIT_CENTER:
        mBorderRect.set(mBitmapRect);
        mShaderMatrix.setRectToRect(mBitmapRect, mBoundsRect, Matrix.ScaleToFit.CENTER);
        mShaderMatrix.mapRect(mBorderRect);
        mDrawableRect.set(mBorderRect.left + mBorderWidth, mBorderRect.top + mBorderWidth,
            mBorderRect.right - mBorderWidth, mBorderRect.bottom - mBorderWidth);
        mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect, Matrix.ScaleToFit.FILL);
        break;
      case FIT_END:
        mBorderRect.set(mBitmapRect);
        mShaderMatrix.setRectToRect(mBitmapRect, mBoundsRect, Matrix.ScaleToFit.END);
        mShaderMatrix.mapRect(mBorderRect);
        mDrawableRect.set(mBorderRect.left + mBorderWidth, mBorderRect.top + mBorderWidth,
            mBorderRect.right - mBorderWidth, mBorderRect.bottom - mBorderWidth);
        mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect, Matrix.ScaleToFit.FILL);
        break;
      case FIT_START:
        mBorderRect.set(mBitmapRect);
        mShaderMatrix.setRectToRect(mBitmapRect, mBoundsRect, Matrix.ScaleToFit.START);
        mShaderMatrix.mapRect(mBorderRect);
        mDrawableRect.set(mBorderRect.left + mBorderWidth, mBorderRect.top + mBorderWidth,
            mBorderRect.right - mBorderWidth, mBorderRect.bottom - mBorderWidth);
        mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect, Matrix.ScaleToFit.FILL);
        break;
      case FIT_XY:
      default:
        mBorderRect.set(mBoundsRect);
        mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width() - mBorderWidth,
            mBorderRect.height() - mBorderWidth);
        mShaderMatrix.set(null);
        mShaderMatrix.setRectToRect(mBitmapRect, mDrawableRect, Matrix.ScaleToFit.FILL);
        break;
    }
    mBitmapShader.setLocalMatrix(mShaderMatrix);
    // 圆角矩形的时候 才会用到 path 去渲染
    if (mDrawableType == DrawableType.ROUNDED) {
      mBorderPath.reset();
      mDrawablePath.reset();
      if (mBorderWidth > 0) {
        float boarderWidth = mBoundsRect.width();
        float borderHeight = mBoundsRect.height();
        mBorderPath.moveTo(mLeftTopRadius, 0);
        mBorderPath.lineTo(boarderWidth - mRightTopRadius, 0);
        mBorderPath.quadTo(boarderWidth, 0, boarderWidth, mRightTopRadius);
        mBorderPath.lineTo(boarderWidth, borderHeight - mRightBottomRadius);
        mBorderPath.quadTo(boarderWidth, borderHeight, boarderWidth - mRightBottomRadius,
            borderHeight);
        mBorderPath.lineTo(mLeftBottomRadius, borderHeight);
        mBorderPath.quadTo(0, borderHeight, 0, borderHeight - mLeftBottomRadius);
        mBorderPath.lineTo(0, mLeftTopRadius);
        mBorderPath.quadTo(0, 0, mLeftTopRadius, 0);

        float width = mBoundsRect.width() - 2 * mBorderWidth;
        float height = mBoundsRect.height() - 2 * mBorderWidth;
        mDrawablePath.moveTo(mLeftTopRadius + mBorderWidth, mBorderWidth);
        mDrawablePath.lineTo(width - mRightTopRadius, mBorderWidth);
        mDrawablePath.quadTo(width + mBorderWidth, mBorderWidth, width + mBorderWidth,
            mRightTopRadius + mBorderWidth);
        mDrawablePath.lineTo(width + mBorderWidth, height - mRightBottomRadius + mBorderWidth);
        mDrawablePath.quadTo(width + mBorderWidth, height + mBorderWidth,
            width - mRightBottomRadius + mBorderWidth, height + mBorderWidth);
        mDrawablePath.lineTo(mLeftBottomRadius + mBorderWidth, height + mBorderWidth);
        mDrawablePath.quadTo(mBorderWidth, height + mBorderWidth, mBorderWidth,
            height - mLeftBottomRadius);
        mDrawablePath.lineTo(mBorderWidth, mLeftTopRadius + mBorderWidth);
        mDrawablePath.quadTo(mBorderWidth, mBorderWidth, mLeftTopRadius + mBorderWidth,
            mBorderWidth);
      } else {
        float width = mBoundsRect.width();
        float height = mBoundsRect.height();
        mDrawablePath.moveTo(mLeftTopRadius, 0);
        mDrawablePath.lineTo(width - mRightTopRadius, 0);
        mDrawablePath.quadTo(width, 0, width, mRightTopRadius);
        mDrawablePath.lineTo(width, height - mRightBottomRadius);
        mDrawablePath.quadTo(width, height, width - mRightBottomRadius, height);
        mDrawablePath.lineTo(mLeftBottomRadius, height);
        mDrawablePath.quadTo(0, height, 0, height - mLeftBottomRadius);
        mDrawablePath.lineTo(0, mLeftTopRadius);
        mDrawablePath.quadTo(0, 0, mLeftTopRadius, 0);
      }
    }
  }

  @Override protected void onBoundsChange(Rect bounds) {
    super.onBoundsChange(bounds);
    mBoundsRect.set(bounds);
    setMatrix();
  }

  /**
   * 子类重写该方法<BR>
   *
   * @param canvas 画布
   * @see android.graphics.drawable.Drawable#draw(android.graphics.Canvas)
   */
  @Override public void draw(@NonNull Canvas canvas) {
    switch (mDrawableType) {
      case NORMAL:
        drawNormalDrawable(canvas);
        break;
      case OVAL:
        drawOvalDrawable(canvas);
        break;
      case ROUNDED:
        drawRoundedDrawable(canvas);
        break;
      case CIRCLE:
        drawCircleDrawable(canvas);
        break;
      default:
        drawNormalDrawable(canvas);
        break;
    }
  }

  @Override public int getOpacity() {
    return PixelFormat.TRANSLUCENT;
  }

  @Override public void setAlpha(int alpha) {
    mBitmapPaint.setAlpha(alpha);
  }

  @Override public void setColorFilter(ColorFilter cf) {
    mBitmapPaint.setColorFilter(cf);
  }

  @Override public int getIntrinsicWidth() {
    return mBitmapWidth;
  }

  @Override public int getIntrinsicHeight() {
    return mBitmapHeight;
  }

  /**
   * Drawable对象转成Bitmap对象<BR>
   *
   * @param drawable Drawable对象
   * @return 返回bitmap对象
   */
  private static Bitmap drawableToBitmap(Drawable drawable) {
    if (drawable instanceof BitmapDrawable) {
      return ((BitmapDrawable) drawable).getBitmap();
    }

    Bitmap bitmap;
    int width = drawable.getIntrinsicWidth();
    int height = drawable.getIntrinsicHeight();
    if (width > 0 && height > 0) {
      bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
      Canvas canvas = new Canvas(bitmap);
      drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
      drawable.draw(canvas);
    } else {
      bitmap = null;
    }

    return bitmap;
  }

  /**
   * 将Drawable对象包装成自定义的Drawable对象(CustomDrawable)<BR>
   * 提供不带边界线的Drawable
   *
   * @param drawable Drawable 对象实例
   * @param leftTopRadius 左上角角度
   * @param rightTopRadius 右上角角度
   * @param rightBottomRadius 右下角角度
   * @param leftBottomRadius 左下角角度
   * @return 返回值
   */
  public static Drawable fromDrawable(Drawable drawable, float leftTopRadius, float rightTopRadius,
                                      float rightBottomRadius, float leftBottomRadius) {
    return fromDrawable(drawable, leftTopRadius, rightTopRadius, rightBottomRadius,
        leftBottomRadius, 0, 0);
  }

  /**
   * 将Drawable对象包装成自定义的Drawable对象(CustomDrawable)<BR>
   * 提供带边界线的Drawable
   *
   * @param drawable Drawable 对象实例
   * @param leftTopRadius 左上角角度
   * @param rightTopRadius 右上角角度
   * @param rightBottomRadius 右下角角度
   * @param leftBottomRadius 左下角角度
   * @param border 边线的宽度
   * @param borderColor 边线的颜色
   * @return 返回值
   */
  static Drawable fromDrawable(Drawable drawable, float leftTopRadius, float rightTopRadius,
                               float rightBottomRadius, float leftBottomRadius, int border, int borderColor) {
    if (drawable != null) {
      if (drawable instanceof TransitionDrawable) {
        TransitionDrawable td = (TransitionDrawable) drawable;
        int num = td.getNumberOfLayers();

        Drawable[] drawableList = new Drawable[num];
        for (int i = 0; i < num; i++) {
          Drawable d = td.getDrawable(i);
          if (d instanceof ColorDrawable) {
            drawableList[i] = d;
          } else {
            drawableList[i] = new CustomDrawable(drawableToBitmap(td.getDrawable(i)), leftTopRadius,
                rightTopRadius, rightBottomRadius, leftBottomRadius, border, borderColor);
          }
        }
        return new TransitionDrawable(drawableList);
      }

      Bitmap bm = drawableToBitmap(drawable);
      if (bm != null) {
        return new CustomDrawable(bm, leftTopRadius, rightTopRadius, rightBottomRadius,
            leftBottomRadius, border, borderColor);
      } else {
        Log.w(TAG, "Failed to create bitmap from drawable!");
      }
    }
    return drawable;
  }

  public int getBorderWidth() {
    return mBorderWidth;
  }

  public int getBorderColor() {
    return mBorderColor;
  }

  /**
   * 设置圆角角度<BR>
   *
   * @param radius 角度
   */
  public void setLeftTopRadius(int radius) {
    this.mLeftTopRadius = radius;
  }

  /**
   * 设置圆角角度<BR>
   *
   * @param radius 角度
   */
  public void setRightTopRadius(int radius) {
    this.mRightTopRadius = radius;
  }

  /**
   * 设置圆角角度<BR>
   *
   * @param radius 角度
   */
  public void setRightBottomRadius(int radius) {
    this.mRightBottomRadius = radius;
  }

  /**
   * 设置圆角角度<BR>
   *
   * @param radius 角度
   */
  public void setLeftBottomRadius(int radius) {
    this.mLeftBottomRadius = radius;
  }

  /**
   * 设置边界线的宽度<BR>
   *
   * @param width 宽度 单位px
   */
  public void setBorderWidth(int width) {
    this.mBorderWidth = width;
    mBorderPaint.setStrokeWidth(mBorderWidth);
  }

  /**
   * 设置边界颜色<BR>
   *
   * @param color 色值
   */
  void setBorderColor(int color) {
    this.mBorderColor = color;
    mBorderPaint.setColor(color);
  }

  /**
   * 画圆角矩形<BR>
   *
   * @param canvas 画布
   */
  private void drawRoundedDrawable(Canvas canvas) {
    if (mBorderWidth > 0) {
      canvas.drawPath(mBorderPath, mBorderPaint);
    }
    canvas.drawPath(mDrawablePath, mBitmapPaint);
  }

  /**
   * 画普通矩形<BR>
   *
   * @param canvas 画布
   */
  private void drawNormalDrawable(Canvas canvas) {
    if (mBorderWidth > 0) {
      canvas.drawRect(mBorderRect, mBorderPaint);
      canvas.drawRect(mDrawableRect, mBitmapPaint);
    } else {
      canvas.drawRect(mDrawableRect, mBitmapPaint);
    }
  }

  /**
   * 画圆形<BR>
   *
   * @param canvas 画布
   */
  private void drawCircleDrawable(Canvas canvas) {
    float drawableWidth = mDrawableRect.width();
    float drawableHeight = mDrawableRect.height();
    float cx = mDrawableRect.left + drawableWidth / 2;
    float cy = mDrawableRect.top + drawableHeight / 2;
    float radius = Math.min(drawableWidth / 2, drawableHeight / 2);
    if (mBorderWidth > 0) {
      float borderRadius = radius + mBorderWidth;
      canvas.drawCircle(cx, cy, borderRadius, mBorderPaint);
      canvas.drawCircle(cx, cy, radius, mBitmapPaint);
    } else {
      canvas.drawCircle(cx, cy, radius, mBitmapPaint);
    }
  }

  /**
   * 画椭圆形<BR>
   *
   * @param canvas 画布
   */
  private void drawOvalDrawable(Canvas canvas) {
    if (mBorderWidth > 0) {
      canvas.drawOval(mBorderRect, mBorderPaint);
      canvas.drawOval(mDrawableRect, mBitmapPaint);
    } else {
      canvas.drawOval(mDrawableRect, mBitmapPaint);
    }
  }
}
