package com.huatu.android.widget.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.flyco.dialog.widget.base.BaseDialog;
import com.huatu.android.R;

/**
 * @author 周竹
 * @file NormalTitleAlert
 * @brief
 * @date 2018/4/30 下午4:53
 * Copyright (c) 2017
 * All rights reserved.
 */
public class NormalTitleAlert extends BaseDialog {
    private TextView mContent;
    private TextView mTitle;
    private TextView tvCancel;
    private String content;
    private String title;

    public NormalTitleAlert(Context context) {
        super(context);
    }

    @Override
    public View onCreateView() {
        widthScale(0.8f);
        View view = View.inflate(mContext, R.layout.layout_normal_title_alert, null);
        mContent = (TextView) view.findViewById(R.id.mContent);
        mTitle = (TextView) view.findViewById(R.id.mTitle);
        tvCancel = (TextView) view.findViewById(R.id.tvCancel);
        return view;
    }

    @Override
    public void setUiBeforShow() {
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mContent.setText(content);
        mTitle.setText(title);
    }

    public NormalTitleAlert content(String content) {
        this.content = content;
        return this;
    }

    public NormalTitleAlert title(String title) {
        this.title = title;
        return this;
    }
}
