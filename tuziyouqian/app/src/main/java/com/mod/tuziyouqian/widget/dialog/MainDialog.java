package com.mod.tuziyouqian.widget.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.flyco.dialog.widget.base.BaseDialog;
import com.mod.tuziyouqian.R;
import com.mod.tuziyouqian.ui.activity.webview.WebViewActivity;
import com.mod.tuziyouqian.ui.activity.webview.WebViewFragment;

/**
 * @author 周竹
 * @file AppUpdateDialog
 * @brief
 * @date 2018/5/8 下午11:32
 * Copyright (c) 2017
 * All rights reserved.
 */
public class MainDialog extends BaseDialog {
    private ImageView ivImage;
    private String imgurl;
    private String url;

    public MainDialog(Context context, String imgurl, String url) {
        super(context);
        this.imgurl = imgurl;
        this.url = url;
    }

    @Override
    public View onCreateView() {
        View view = View.inflate(mContext, R.layout.dialog_main, null);
        ivImage = (ImageView) view.findViewById(R.id.ivImage);
        (view.findViewById(R.id.ivDelete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainDialog.this.dismiss();
            }
        });
        if (!TextUtils.isEmpty(imgurl)) {
            Glide.with(mContext)
                    .load(imgurl)
                    .into(ivImage);
        }
        if (!TextUtils.isEmpty(url)) {
            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(url)) {
                        MainDialog.this.dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putString(WebViewFragment.WEB_URL, url);
                        Intent intent = new Intent();
                        intent.setClass(mContext, WebViewActivity.class);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
        return view;
    }

    @Override
    public void setUiBeforShow() {

    }

}
