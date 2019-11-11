package com.lib.core.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.lib.core.R;


/**
 * 构建多种图形的ImageView<BR>
 * 支持画椭圆、圆角、圆形等
 * 支持比例图片 比如正方形、16:9、3:2 等
 * Created by Ivan on 2018/1/16.
 */
@SuppressWarnings("unused") public class UIImageView extends AppCompatImageView {

  /**
   * 默认比例为0 则显示原始宽和高
   */
  private static final float DEFAULT_PROPORTION = 0;

  /**
   * 比例 以宽度为准
   */
  private static final int PROPORTION_BASE_WIDTH = 0;

  /**
   * 比例 以高度为准
   */
  private static final int PROPORTION_BASE_HEIGHT = 1;

  /**
   * 实际比例
   */
  private float mProportion = DEFAULT_PROPORTION;

  /**
   * 比例 基准
   */
  private int mProportionBase = PROPORTION_BASE_WIDTH;

  /**
   * 打印日志的TAG
   */
  public static final String TAG = "UIImageView";

  /**
   * 默认圆角的角度
   */
  private static final int DEFAULT_RADIUS = 0;

  /**
   * 默认边线的宽度
   */
  private static final int DEFAULT_BORDER = 0;

  /**
   * 默认边线的颜色值
   */
  private static final int DEFAULT_BORDER_COLOR = Color.BLACK;

  /**
   * 默认点击阴影的颜色值
   */
  private static final int DEFAULT_SHADOW_COLOR = 0xFFB2B2B2;

  /**
   * 默认前景色颜色为透明输入
   */
  private static final int DEFAULT_FOREGROUND_COLOR = 0x00000000;

  /**
   * Options for scaling the bounds of an image to the bounds of this view.
   */
  private static final ScaleType[] SCALE_TYPE_ARRAY = {
      ScaleType.MATRIX, ScaleType.FIT_XY, ScaleType.FIT_START, ScaleType.FIT_CENTER,
      ScaleType.FIT_END, ScaleType.CENTER, ScaleType.CENTER_CROP, ScaleType.CENTER_INSIDE
  };

  /**
   * 指定UIImageView draw支持的几种类型
   */
  private static final DrawableType[] DRAWABLE_TYPE_ARRAY = {
      DrawableType.NORMAL, DrawableType.OVAL, DrawableType.ROUNDED, DrawableType.CIRCLE,
  };

  /**
   * 圆角的角度
   */
  private int mCornerRadius;

  private int mLeftTopRadius;
  private int mRightTopRadius;
  private int mRightBottomRadius;
  private int mLeftBottomRadius;

  /**
   * 边界线的宽度
   */
  private int mBorderWidth;

  /**
   * 边界线的颜色
   */
  private int mBorderColor;

  /**
   * 背景是否要画圆角的标志位
   */
  private boolean mIsDrawBackground = false;

  /**
   * 点击是否有阴影的标志位
   */
  private boolean mIsClickShadow = false;

  /**
   * 点击阴影颜色值
   */
  private int mShadowColor;

  /**
   * foreground color
   */
  private int mForegroundColor;

  /**
   * ImageView资源图片的Drawable
   */
  private Drawable mDrawable;

  /**
   * ImageView背景图片的Drawable
   */
  private Drawable mBackgroundDrawable;

  /**
   * 缩放类型
   */
  private ScaleType mScaleType;

  /**
   * 画图类型
   */
  private DrawableType mDrawableType = DrawableType.ROUNDED;

  /**
   * paint
   */
  private Paint mPaint = new Paint();

  /**
   * foreground
   */
  private RectF mForegroundRectF = new RectF();

  public UIImageView(Context context) {
    super(context);
    mCornerRadius = DEFAULT_RADIUS;
    mLeftTopRadius = DEFAULT_RADIUS;
    mRightTopRadius = DEFAULT_RADIUS;
    mRightBottomRadius = DEFAULT_RADIUS;
    mLeftBottomRadius = DEFAULT_RADIUS;

    mBorderWidth = DEFAULT_BORDER;
    mBorderColor = DEFAULT_BORDER_COLOR;
    mDrawableType = DrawableType.ROUNDED;
  }

  public UIImageView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public UIImageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    TypedArray attributes =
        context.obtainStyledAttributes(attrs, R.styleable.UIImageView, defStyle, 0);

    //scaleType
    int scaleTypeIndex = attributes.getInt(R.styleable.UIImageView_scaleType, -1);
    if (scaleTypeIndex >= 0) {
      setScaleType(SCALE_TYPE_ARRAY[scaleTypeIndex]);
    }

    //drawableType
    int drawableTypeIndex = attributes.getInt(R.styleable.UIImageView_drawableType, 0);
    if (drawableTypeIndex >= 0) {
      mDrawableType = DRAWABLE_TYPE_ARRAY[drawableTypeIndex];
    }

    //圆角的角度
    mCornerRadius =
        attributes.getDimensionPixelSize(R.styleable.UIImageView_radius, DEFAULT_RADIUS);
    mLeftTopRadius =
        attributes.getDimensionPixelOffset(R.styleable.UIImageView_leftTopRadius, DEFAULT_RADIUS);
    mRightTopRadius =
        attributes.getDimensionPixelOffset(R.styleable.UIImageView_rightTopRadius, DEFAULT_RADIUS);
    mRightBottomRadius =
        attributes.getDimensionPixelOffset(R.styleable.UIImageView_rightBottomRadius,
            DEFAULT_RADIUS);
    mLeftBottomRadius = attributes.getDimensionPixelOffset(R.styleable.UIImageView_leftBottomRadius,
        DEFAULT_RADIUS);

    //边界线的宽度
    mBorderWidth =
        attributes.getDimensionPixelSize(R.styleable.UIImageView_borderWidth, DEFAULT_BORDER);

    // 不允许mCornerRadius 和 mBorderWidth 的值是负数
    if (mCornerRadius < 0) {
      mCornerRadius = DEFAULT_RADIUS;
    }
    //如果四个角的值没有设置，那么就使用通用的radius的值。
    if (DEFAULT_BORDER == mLeftTopRadius || mLeftTopRadius < 0) {
      mLeftTopRadius = mCornerRadius;
    }
    if (DEFAULT_BORDER == mRightTopRadius || mRightTopRadius < 0) {
      mRightTopRadius = mCornerRadius;
    }
    if (DEFAULT_BORDER == mRightBottomRadius || mRightBottomRadius < 0) {
      mRightBottomRadius = mCornerRadius;
    }
    if (DEFAULT_BORDER == mLeftBottomRadius || mLeftBottomRadius < 0) {
      mLeftBottomRadius = mCornerRadius;
    }

    if (mBorderWidth < 0) {
      mBorderWidth = DEFAULT_BORDER;
    }

    //边界线的颜色
    mBorderColor = attributes.getColor(R.styleable.UIImageView_borderColor, DEFAULT_BORDER_COLOR);

    //是否画背景
    mIsDrawBackground = attributes.getBoolean(R.styleable.UIImageView_drawBackground, false);
    // 是否画点击阴影
    mIsClickShadow = attributes.getBoolean(R.styleable.UIImageView_clickShadow, false);
    // 阴影颜色值
    mShadowColor = attributes.getColor(R.styleable.UIImageView_shadowColor, DEFAULT_SHADOW_COLOR);
    // 前景色颜色
    mForegroundColor =
        attributes.getColor(R.styleable.UIImageView_foregroundColor, DEFAULT_FOREGROUND_COLOR);

    //更新生成的新的资源Drawable
    if (mDrawable instanceof CustomDrawable) {
      updateDrawableAttrs(mDrawable);
    }

    //更新新生成的背景资源
    if (mIsDrawBackground) {
      if (!(mBackgroundDrawable instanceof CustomDrawable)) {
        //尝试设置背景图
        setBackgroundDrawable(mBackgroundDrawable);
      }
      if (mBackgroundDrawable instanceof CustomDrawable) {
        updateDrawableAttrs(mBackgroundDrawable);
      }
    }

    mProportion = attributes.getFloat(R.styleable.UIImageView_proportion, DEFAULT_PROPORTION);
    mProportionBase =
        attributes.getInt(R.styleable.UIImageView_proportionBase, PROPORTION_BASE_WIDTH);
    attributes.recycle();
  }

  @SuppressWarnings("SuspiciousNameCombination") @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    if (mProportion == DEFAULT_PROPORTION) {
      return;
    }
    widthMeasureSpec = getMeasuredWidth();
    heightMeasureSpec = getMeasuredHeight();
    if (mProportionBase == PROPORTION_BASE_WIDTH) {
      heightMeasureSpec = (int) (widthMeasureSpec / mProportion);
    } else {
      widthMeasureSpec = (int) (heightMeasureSpec / mProportion);
    }
    setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
  }

  /**
   * 设置比例
   *
   * @param proportion 设置比例
   */
  public void setProportion(float proportion) {
    if (proportion < 0) {
      return;
    }
    if (mProportion != proportion) {
      mProportion = proportion;
      requestLayout();
      invalidate();
    }
  }

  /**
   * 设置比例基准, 以高为标准或者以宽为标准
   *
   * @param base 宽或者高 {@link UIImageView#PROPORTION_BASE_WIDTH} {@link
   * UIImageView#PROPORTION_BASE_HEIGHT}
   */
  public void setProportionBase(int base) {
    if (base != PROPORTION_BASE_HEIGHT && base != PROPORTION_BASE_WIDTH) {
      return;
    }
    if (mProportionBase != base) {
      mProportionBase = base;
      requestLayout();
      invalidate();
    }
  }

  /**
   * 设置drawableType<BR>
   *
   * @param drawableType DrawableType枚举类型
   */
  public void setDrawableType(DrawableType drawableType) {
    mDrawableType = drawableType;
    invalidate();
  }

  /**
   * Controls how the image should be resized or moved to match the size
   * of this ImageView.
   *
   * @param scaleType The desired scaling mode.
   */
  @Override public void setScaleType(ScaleType scaleType) {
    if (scaleType == null) {
      throw new NullPointerException();
    }

    if (mScaleType != scaleType) {
      mScaleType = scaleType;

      switch (scaleType) {
        case CENTER:
        case CENTER_CROP:
        case CENTER_INSIDE:
        case FIT_CENTER:
        case FIT_START:
        case FIT_END:
        case FIT_XY:
          super.setScaleType(scaleType);
          break;
        default:
          super.setScaleType(scaleType);
          break;
      }

      if (mDrawable instanceof CustomDrawable
          && ((CustomDrawable) mDrawable).getScaleType() != scaleType) {
        ((CustomDrawable) mDrawable).setScaleType(scaleType);
      }

      if (mBackgroundDrawable instanceof CustomDrawable
          && ((CustomDrawable) mBackgroundDrawable).getScaleType() != scaleType) {
        ((CustomDrawable) mBackgroundDrawable).setScaleType(scaleType);
      }

      //setWillNotCacheDrawing(true);
      requestLayout();
      //invalidate();
    }
  }

  /**
   * 返回当前UIImageView使用的缩放类型<BR>
   *
   * @return scaleType
   * @see android.widget.ImageView#getScaleType()
   */
  @Override public ScaleType getScaleType() {
    return mScaleType;
  }

  /**
   * 重写setImageDrawable方法<BR>
   * 通过矩阵变换得到想要的drawable
   *
   * @param drawable 原始drawable
   * @see android.widget.ImageView#setImageDrawable(android.graphics.drawable.Drawable)
   */
  @Override public void setImageDrawable(Drawable drawable) {
    if (drawable != null) {
      mDrawable =
          CustomDrawable.fromDrawable(drawable, mLeftTopRadius, mRightTopRadius, mRightBottomRadius,
              mLeftBottomRadius, mBorderWidth, mBorderColor);
      updateDrawableAttrs(mDrawable);
    } else {
      mDrawable = null;
    }
    super.setImageDrawable(mDrawable);
  }

  /**
   * 设置ImageView的位图<BR>
   *
   * @param bm 位图
   * @see android.widget.ImageView#setImageBitmap(android.graphics.Bitmap)
   */
  @Override public void setImageBitmap(Bitmap bm) {
    if (bm != null) {
      mDrawable = new CustomDrawable(bm, mLeftTopRadius, mRightTopRadius, mRightBottomRadius,
          mLeftBottomRadius, mBorderWidth, mBorderColor);
      updateDrawableAttrs(mDrawable);
    } else {
      mDrawable = null;
    }
    super.setImageDrawable(mDrawable);
  }

  /**
   * 更新Drawable的属性<BR>
   *
   * @param drawable drawable
   */
  private void updateDrawableAttrs(Drawable drawable) {
    if (drawable instanceof CustomDrawable) {
      CustomDrawable baseDrawable = (CustomDrawable) drawable;
      baseDrawable.setScaleType(mScaleType);
      baseDrawable.setLeftBottomRadius(mLeftBottomRadius);
      baseDrawable.setLeftTopRadius(mLeftTopRadius);
      baseDrawable.setRightBottomRadius(mRightBottomRadius);
      baseDrawable.setRightTopRadius(mRightTopRadius);
      baseDrawable.setBorderWidth(mBorderWidth);
      baseDrawable.setBorderColor(mBorderColor);
      baseDrawable.setDrawableType(mDrawableType);
    }
  }

  /**
   * 设置背景<BR>
   *
   * @param background 背景Drawable
   * @see android.view.View#setBackgroundDrawable(android.graphics.drawable.Drawable)
   */
  @Override public void setBackgroundDrawable(Drawable background) {
    if (mIsDrawBackground && background != null) {
      mBackgroundDrawable = CustomDrawable.fromDrawable(background, mLeftTopRadius, mRightTopRadius,
          mRightBottomRadius, mLeftBottomRadius, mBorderWidth, mBorderColor);
      updateDrawableAttrs(mBackgroundDrawable);
    } else {
      mBackgroundDrawable = background;
    }
    super.setBackgroundDrawable(mBackgroundDrawable);
  }

  public int getCornerRadius() {
    return mCornerRadius;
  }

  public int getBorder() {
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
  public void setCornerRadius(int radius) {
    if (mCornerRadius == radius || radius < 0) {
      return;
    }

    this.mCornerRadius = radius;
    setLeftBottomRadius(radius);
    setLeftTopRadius(radius);
    setRightTopRadius(radius);
    setRightBottomRadius(radius);
  }

  /**
   * 设置圆角角度<BR>
   *
   * @param radius 角度
   */
  public void setLeftTopRadius(int radius) {
    if (mLeftTopRadius == radius || radius < 0) {
      return;
    }

    this.mLeftTopRadius = radius;
    if (mDrawable instanceof CustomDrawable) {
      ((CustomDrawable) mDrawable).setLeftTopRadius(radius);
    }
    if (mIsDrawBackground && mBackgroundDrawable instanceof CustomDrawable) {
      ((CustomDrawable) mBackgroundDrawable).setLeftTopRadius(radius);
    }
  }

  /**
   * 设置圆角角度<BR>
   *
   * @param radius 角度
   */
  public void setRightTopRadius(int radius) {
    if (mRightTopRadius == radius || radius < 0) {
      return;
    }

    this.mRightTopRadius = radius;
    if (mDrawable instanceof CustomDrawable) {
      ((CustomDrawable) mDrawable).setRightTopRadius(radius);
    }
    if (mIsDrawBackground && mBackgroundDrawable instanceof CustomDrawable) {
      ((CustomDrawable) mBackgroundDrawable).setRightTopRadius(radius);
    }
  }

  /**
   * 设置圆角角度<BR>
   *
   * @param radius 角度
   */
  public void setRightBottomRadius(int radius) {
    if (mRightBottomRadius == radius || radius < 0) {
      return;
    }

    this.mRightBottomRadius = radius;
    if (mDrawable instanceof CustomDrawable) {
      ((CustomDrawable) mDrawable).setRightBottomRadius(radius);
    }
    if (mIsDrawBackground && mBackgroundDrawable instanceof CustomDrawable) {
      ((CustomDrawable) mBackgroundDrawable).setRightBottomRadius(radius);
    }
  }

  /**
   * 设置圆角角度<BR>
   *
   * @param radius 角度
   */
  public void setLeftBottomRadius(int radius) {
    if (mLeftBottomRadius == radius || radius < 0) {
      return;
    }

    this.mLeftBottomRadius = radius;
    if (mDrawable instanceof CustomDrawable) {
      ((CustomDrawable) mDrawable).setLeftBottomRadius(radius);
    }
    if (mIsDrawBackground && mBackgroundDrawable instanceof CustomDrawable) {
      ((CustomDrawable) mBackgroundDrawable).setLeftBottomRadius(radius);
    }
  }

  /**
   * 设置边线宽度<BR>
   *
   * @param width 宽度px
   */
  public void setBorderWidth(int width) {
    if (mBorderWidth == width) {
      return;
    }

    this.mBorderWidth = width;
    if (mDrawable instanceof CustomDrawable) {
      ((CustomDrawable) mDrawable).setBorderWidth(width);
    }
    if (mIsDrawBackground && mBackgroundDrawable instanceof CustomDrawable) {
      ((CustomDrawable) mBackgroundDrawable).setBorderWidth(width);
    }
    invalidate();
  }

  /**
   * 设置边线颜色<BR>
   *
   * @param color 色值
   */
  public void setBorderColor(int color) {
    if (mBorderColor == color) {
      return;
    }

    this.mBorderColor = color;
    if (mDrawable instanceof CustomDrawable) {
      ((CustomDrawable) mDrawable).setBorderColor(color);
    }
    if (mIsDrawBackground && mBackgroundDrawable instanceof CustomDrawable) {
      ((CustomDrawable) mBackgroundDrawable).setBorderColor(color);
    }
    if (mBorderWidth > 0) {
      invalidate();
    }
  }

  @Override public void setImageResource(int resId) {
    super.setImageResource(resId);
    setImageDrawable(getDrawable());
  }

  public boolean isDrawBackground() {
    return mIsDrawBackground;
  }

  /**
   * 设置是否画背景<BR>
   *
   * @param drawBackground 是否画背景的布尔值
   */
  public void setDrawBackground(boolean drawBackground) {
    if (this.mIsDrawBackground == drawBackground) {
      return;
    }

    this.mIsDrawBackground = drawBackground;
    if (drawBackground) {
      if (mBackgroundDrawable instanceof CustomDrawable) {
        updateDrawableAttrs(mBackgroundDrawable);
      } else {
        setBackgroundDrawable(mBackgroundDrawable);
      }
    } else if (mBackgroundDrawable instanceof CustomDrawable) {
      ((CustomDrawable) mBackgroundDrawable).setBorderWidth(0);
      setCornerRadius(0);
    }

    invalidate();
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (mForegroundColor != DEFAULT_FOREGROUND_COLOR) {
      mPaint.setColor(mForegroundColor);
      mForegroundRectF.left = 0;
      mForegroundRectF.right = getWidth();
      mForegroundRectF.top = 0;
      mForegroundRectF.bottom = getHeight();
      canvas.drawRoundRect(mForegroundRectF, mCornerRadius, mCornerRadius, mPaint);
      //TODO gsh 支持不同的角度
    }
  }

  @Override public void setPressed(boolean pressed) {
    super.setPressed(pressed);

    Drawable drawable = getDrawable();
    if (drawable != null && mIsClickShadow) {
      if (pressed) {

        drawable.setColorFilter(mShadowColor, PorterDuff.Mode.MULTIPLY);
        invalidate();
      } else {
        drawable.clearColorFilter();
        invalidate();
      }
    }
  }
}
