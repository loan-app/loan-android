package com.huatu.android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.huatu.android.R;

import io.reactivex.annotations.Nullable;

/**
 * @author 周竹
 * @file PointDividerView
 * @brief
 * @date 2018/5/10 下午10:56
 * Copyright (c) 2017
 * All rights reserved.
 */
public class PointDividerView extends View {

    // 创建画笔
    private Paint mPaint; // 画笔
    private float radius; // 圆的半径
    private float dividerWidth; // 圆的间距
    private int mColor = Color.parseColor("#FC5641"); // 圆点的颜色
    private Context mContext;

    public PointDividerView(Context context) {
        super(context);
        init(context, null);
    }

    public PointDividerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PointDividerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 限死高度为space_10(实际是10dp)，如果要需要动态设置，可以通过attrs参数获取设置。
        setMeasuredDimension(widthMeasureSpec, (int) mContext.getResources().getDimension(R.dimen.DIMEN_5_0PX));
    }

    protected void init(Context context, @Nullable AttributeSet attrs) {
        mContext = context;
        mPaint = new Paint();
        radius =  mContext.getResources().getDimension(R.dimen.DIMEN_2_0PX);
        dividerWidth = mContext.getResources().getDimension(R.dimen.DIMEN_10_0PX);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);// 设置颜色
        int measuredHeight = getMeasuredHeight() / 2; // 高度居中
        int measuredWidth = getMeasuredWidth();
        // int maxCount = measuredWidth / (dividerWidth + radius * 2);
        for (float i = radius; i < measuredWidth; ) {
            canvas.drawCircle(i, measuredHeight, radius, mPaint);// 小圆
            i += dividerWidth;
        }

    }

}
