package com.xinyan.facecheck.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.xinyan.bioassay.sdk.R;


/**
 * Created by Vincent on 2018/4/3.
 */


public class ProgressDialog extends Dialog {
    private ImageView mProgress;
    private AnimationSet animationSet;

    public ProgressDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindowAttr();
        setContentView(R.layout.loading);
        initView();


    }

    private void initView() {
        mProgress = findViewById(R.id.iv_progress);


    }

    public void showAnim() {
        animationSet = new AnimationSet(true);
        RotateAnimation animation_rotate = new RotateAnimation(0, +359, 1, 0.5f, 1
                , 0.5f);
        animation_rotate.setRepeatCount(-1);
        animation_rotate.setStartOffset(0);
        animation_rotate.setDuration(1000);
        LinearInterpolator lir = new LinearInterpolator();
        animationSet.setInterpolator(lir);
        animationSet.addAnimation(animation_rotate);
        mProgress.startAnimation(animationSet);//再重新启动animation动画；
    }

    public void dismissAnim() {
        if (animationSet != null && mProgress != null) {
            mProgress.clearAnimation();
        }

    }

    /**
     * 设置对话框背景透明
     */
    public void setWindowAttr() {
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().setDimAmount(0f);
        setCancelable(false);
    }

    public void showProgress() {
        if (!this.isShowing()) {
            this.show();
            showAnim();

        }
    }

    public void dismissProgress() {
        if (this.isShowing()) {
            this.dismiss();
            dismissAnim();
        }

    }

}
