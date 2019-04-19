package com.mod.tuziyouqian.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mod.tuziyouqian.R;


/**
 * @author 周竹
 * @file HomeManageItem
 * @brief
 * @date 2018/2/14 上午3:09
 * Copyright (c) 2017
 * All rights reserved.
 */

public class IconItem extends LinearLayout {
    Drawable icon;
    ImageView iv;
    TextView tvTitle;
    String title;

    public IconItem(Context context) {
        this(context, null);
    }

    public IconItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.IconItem);
        icon = ta.getDrawable(R.styleable.IconItem_IconItem_icon);
        title = ta.getString(R.styleable.IconItem_IconItem_title);
        ta.recycle();
        LayoutInflater.from(context).inflate(R.layout.icon_item_layout, this);
        iv = findViewById(R.id.iv);
        tvTitle = findViewById(R.id.tvTitle);
        if (icon != null)
            iv.setImageDrawable(icon);
        if (!TextUtils.isEmpty(title))
            tvTitle.setText(title);
    }

}
