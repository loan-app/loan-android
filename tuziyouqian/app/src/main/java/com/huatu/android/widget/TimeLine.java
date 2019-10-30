package com.huatu.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.huatu.android.R;

/**
 * @author 周竹
 * @file TimeLine
 * @brief
 * @date 2018/5/11 下午1:25
 * Copyright (c) 2017
 * All rights reserved.
 */
public class TimeLine extends LinearLayout {
    View viewTop;
    View mPoint;
    View viewBottom;


    public TimeLine(Context context) {
        this(context, null);
    }

    public TimeLine(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeLine(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.time_line_item_layout, this);
        viewTop = findViewById(R.id.viewTop);
        mPoint = findViewById(R.id.mPoint);
        viewBottom = findViewById(R.id.viewBottom);
    }

    public View getTopView() {
        return viewTop;
    }

    public View getPointView() {
        return mPoint;
    }

    public View getBottomView() {
        return viewBottom;
    }
}
