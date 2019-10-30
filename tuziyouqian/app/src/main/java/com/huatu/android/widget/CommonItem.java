package com.huatu.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huatu.android.R;
import com.huatu.android.base.App;

/**
 * @author 周竹
 * @file PersonItemView
 * @brief
 * @date 2017/10/19 上午10:53
 * Copyright (c) 2017, 米发科技
 * All rights reserved.
 */

public class CommonItem extends RelativeLayout {
    String title;
    String content;
    int titleColor;
    int contentColor;
    boolean isShowLine;
    boolean isShowArrow;
    TextView tvTitle;
    TextView tvContent;
    ImageView ic_next;
    View mLine;

    public CommonItem(Context context) {
        this(context, (AttributeSet) null);
    }

    public CommonItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.common_item);
        title = ta.getString(R.styleable.common_item_common_title);
        content = ta.getString(R.styleable.common_item_common_content);
        titleColor = ta.getColor(R.styleable.common_item_common_titleColor, ContextCompat.getColor(context, R.color.color_666666));
        contentColor = ta.getColor(R.styleable.common_item_common_contentColor, ContextCompat.getColor(context, R.color.color_666666));
        isShowLine = ta.getBoolean(R.styleable.common_item_common_show_line, true);
        isShowArrow = ta.getBoolean(R.styleable.common_item_common_show_arrow, true);
        LayoutInflater.from(context).inflate(R.layout.item_layout_common, this);
        tvTitle = findViewById(R.id.tvTitle);
        tvContent = findViewById(R.id.tvContent);
        ic_next = findViewById(R.id.ic_next);
        mLine = findViewById(R.id.mLine);
        init();
        ta.recycle();


    }

    private void init() {
        tvTitle.setTextColor(titleColor);
        tvTitle.setText(title);
        tvContent.setTextColor(contentColor);
        tvContent.setText(content);
        if (!isShowArrow) {
            ic_next.setVisibility(GONE);
            int right = App.getInstance().getResources().getDimensionPixelOffset(R.dimen.DIMEN_32_0PX);
            RelativeLayout.LayoutParams params = (LayoutParams) tvContent.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.setMargins(0, 0, right, 0);
            tvContent.setLayoutParams(params);
        }
        mLine.setVisibility(isShowLine ? VISIBLE : INVISIBLE);
    }

    public void setContent(CharSequence content) {
        tvContent.setText(content);
    }

    public void setContentColor(int resColor) {
        tvContent.setTextColor(App.getResColor(resColor));
    }

    public void setTitle(CharSequence content) {
        tvTitle.setText(content);
    }

    public void setTitleColor(int resColor) {
        tvTitle.setTextColor(App.getResColor(resColor));
    }


}
