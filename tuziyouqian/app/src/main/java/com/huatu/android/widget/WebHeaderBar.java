package com.huatu.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lib.core.utils.DisplayUtil;
import com.huatu.android.R;


/**
 * @author 周竹
 * @version 1.0
 * @file TitleHeaderBar.java
 * @brief 标题栏
 * @date 2015/10/13
 * Copyright (c) 2015
 * All rights reserved.
 */
public class WebHeaderBar extends RelativeLayout {

    private TextView mRightTextView;
    private TextView mLeftTextView;

    private TextView mCenterTitleTextView;
    private ImageView mLeftReturnImageView;
    private ImageView ivClose;
    private ImageView mRightImageView;
    private RelativeLayout mLeftViewContainer;
    private RelativeLayout mRightViewContainer;
    private RelativeLayout mCenterViewContainer;
    private RelativeLayout title;
    private View mDivider;
    private boolean show_divider;
    private String mTitle;
    int titleBgColor;

    public WebHeaderBar(Context context) {
        this(context, (AttributeSet) null);
    }

    public WebHeaderBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WebHeaderBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleHeaderBar);
        mTitle = ta.getString(R.styleable.TitleHeaderBar_title_text);
        titleBgColor = ta.getColor(R.styleable.TitleHeaderBar_title_bgColor, ContextCompat.getColor(context, R.color.white));
        show_divider = ta.getBoolean(R.styleable.TitleHeaderBar_show_divider, true);
        LayoutInflater.from(context).inflate(this.getHeaderViewLayoutId(), this);
        this.mLeftViewContainer = this.findViewById(R.id.rl_title_bar_left);
        this.title = this.findViewById(R.id.title);
        this.mCenterViewContainer = this.findViewById(R.id.rl_title_bar_center);
        this.mRightViewContainer = this.findViewById(R.id.rl_title_bar_right);
        this.mLeftReturnImageView = this.findViewById(R.id.iv_title_bar_left);
        this.ivClose = this.findViewById(R.id.ivClose);
        this.mRightImageView = this.findViewById(R.id.iv_title_bar_right);
        this.mCenterTitleTextView = this.findViewById(R.id.tv_title_bar_title);
        this.mRightTextView = this.findViewById(R.id.tv_title_bar_right);
        this.mLeftTextView = this.findViewById(R.id.tv_title_bar_left);
        this.mDivider = this.findViewById(R.id.mDivider);
        title.setBackgroundColor(titleBgColor);
        setBackgroundColor(titleBgColor);
        this.mCenterTitleTextView.setText(mTitle);
        mDivider.setVisibility(show_divider ? VISIBLE : GONE);
        setPadding(0, DisplayUtil.getStatusBarHeight(getContext()), 0, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int defaultHeight = getContext().getResources().getDimensionPixelSize(R.dimen.DIMEN_88_0PX);
        //加上状态栏的高度
        int statusBarHeight = DisplayUtil.getStatusBarHeight(getContext());
        defaultHeight += statusBarHeight;
        setMeasuredDimension(width, defaultHeight);
    }


    protected int getHeaderViewLayoutId() {
        return R.layout.web_bar_layout;
    }

    public ImageView getLeftImageView() {
        return this.mLeftReturnImageView;
    }

    public TextView getTitleTextView() {
        return this.mCenterTitleTextView;
    }

    public void setTitle(String title) {
        this.mTitle = title;
        this.mCenterTitleTextView.setText(title);
    }

    public void setTitleTextColor(int color) {
        this.mCenterTitleTextView.setTextColor(color);
    }

    public void setTitleTextSize(int dimens) {
        this.mCenterTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(dimens));
    }

    public String getTitle() {
        return this.mTitle;
    }

    private LayoutParams makeLayoutParams(View view) {
        ViewGroup.LayoutParams lpOld = view.getLayoutParams();
        LayoutParams lp = null;
        if (lpOld == null) {
            lp = new LayoutParams(-2, -1);
        } else {
            lp = new LayoutParams(lpOld.width, lpOld.height);
        }
        return lp;
    }

    public void setCustomizedLeftView(View view) {
        this.mLeftReturnImageView.setVisibility(View.GONE);
        this.getLeftViewContainer().setVisibility(View.VISIBLE);
        LayoutParams lp = this.makeLayoutParams(view);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.getLeftViewContainer().addView(view, lp);
    }

    /**
     * @param view
     * @brief 移除左边客制化的View
     */
    public void removeCustomizedLeftView(View view) {
        if (view != null && this.mLeftReturnImageView != view) {
            this.getLeftViewContainer().removeView(view);
            this.mLeftReturnImageView.setVisibility(View.VISIBLE);
        }
    }

    public void setCustomizedLeftView(int layoutId) {
        View view = inflate(this.getContext(), layoutId, (ViewGroup) null);
        this.setCustomizedLeftView(view);
    }

    public void setCustomizedCenterView(View view) {
        this.mCenterTitleTextView.setVisibility(View.GONE);
        LayoutParams lp = this.makeLayoutParams(view);
        lp.addRule(13);
        this.getCenterViewContainer().addView(view, lp);
    }

    public void setCustomizedCenterView(int layoutId) {
        View view = inflate(this.getContext(), layoutId, (ViewGroup) null);
        this.setCustomizedCenterView(view);
    }

    public void setCustomizedRightView(View view) {
        LayoutParams lp = this.makeLayoutParams(view);
        lp.addRule(15);
        lp.addRule(11);
        this.getRightViewContainer().addView(view, lp);
    }

    /**
     * @param str
     * @brief 设置右边字符串
     */
    public void setCustomizedRightString(String str) {
        mRightViewContainer.setVisibility(VISIBLE);
        this.mRightTextView.setVisibility(View.VISIBLE);
        this.mRightTextView.setText(str);
    }

    /**
     * @param color
     * @brief 设置右边字符串颜色
     */
    public void setCustomizedRightStringColor(int color) {
        this.mRightTextView.setTextColor(color);
    }

    /**
     * @param str
     * @brief 设置左边字符串
     */
    public void setCustomizedLeftString(String str) {
        this.mLeftTextView.setVisibility(View.VISIBLE);
        this.mLeftTextView.setText(str);
    }

    /**
     * @param color
     * @brief 设置左边字符串颜色
     */
    public void setCustomizedleftStringColor(int color) {
        this.mLeftTextView.setTextColor(color);
    }

    /**
     * @param resId 图片资源ID
     * @brief 通过resID直接设置图片
     */
    public void setCustomizedRightView(int resId) {
        this.mRightTextView.setVisibility(View.INVISIBLE);
        this.mRightImageView.setVisibility(View.VISIBLE);
        this.mRightImageView.setImageResource(resId);
    }

    /**
     * @param resId 图片资源ID
     * @brief 通过resID直接设置图片
     */
    public void setRightViewAndStr(int resId, String str) {
        this.mRightTextView.setVisibility(View.VISIBLE);
        this.mRightImageView.setVisibility(View.VISIBLE);
        this.mRightImageView.setImageResource(resId);
        this.mRightTextView.setText(str);
    }

    /**
     * @param resId 图片资源ID
     * @brief 通过resID直接设置图片
     */
    public void setleftViewAndStr(int resId, String str) {
        this.mLeftTextView.setVisibility(View.VISIBLE);
        this.mLeftReturnImageView.setVisibility(View.VISIBLE);
        this.mLeftReturnImageView.setImageResource(resId);
        this.mLeftTextView.setText(str);
    }

    public RelativeLayout getLeftViewContainer() {
        return this.mLeftViewContainer;
    }

    public RelativeLayout getCenterViewContainer() {
        return this.mCenterViewContainer;
    }

    public RelativeLayout getRightViewContainer() {
        return this.mRightViewContainer;
    }

    public void setLeftOnClickListener(OnClickListener l) {
        this.mLeftViewContainer.setOnClickListener(l);
    }

    public void setCenterOnClickListener(OnClickListener l) {
        this.mCenterViewContainer.setOnClickListener(l);
    }

    public void setRightOnClickListener(OnClickListener l) {
        this.mRightViewContainer.setOnClickListener(l);
    }


    /**
     * @param color
     * @brief 设置自定义背景
     */
    public void setCustomBackgroundColor(int color) {
        this.titleBgColor = color;
        setBackgroundColor(color);
    }


    public ImageView getRightImageView() {
        return mRightImageView;
    }

    public ImageView getIvClose() {
        return ivClose;
    }

    public TextView getRightTextVeiw() {
        return mRightTextView;
    }


}
