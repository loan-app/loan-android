package com.huatu.android.widget.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.flyco.dialog.widget.base.BaseDialog;
import com.huatu.android.R;

/**
 * @author 周竹
 * @file AppUpdateDialog
 * @brief
 * @date 2018/5/8 下午11:32
 * Copyright (c) 2017
 * All rights reserved.
 */
public class ConfirmDialog extends BaseDialog {
    private String content;
    private String title;
    private TextView tvCancel;
    private TextView tvConfirm;
    private TextView tvTitle;
    AppUpdateDialog.OnCallBack callBack;

    public ConfirmDialog(Context context) {
        super(context);
    }

    @Override
    public View onCreateView() {
        widthScale(0.8f);
        View view = View.inflate(mContext, R.layout.dialog_confirm_layout, null);
        tvCancel = (TextView) view.findViewById(R.id.tvCancel);
        tvConfirm = (TextView) view.findViewById(R.id.tvConfirm);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null)
                    callBack.onCancel();

            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null)
                    callBack.onConfirm();

            }
        });
        return view;
    }

    @Override
    public void setUiBeforShow() {
        if (!TextUtils.isEmpty(title))
            tvTitle.setText(title);
    }

    public ConfirmDialog content(String content) {
        this.content = content;
        return this;
    }

    public ConfirmDialog title(String title) {
        this.title = title;
        return this;
    }

    public void setOnCallBack(AppUpdateDialog.OnCallBack callBack) {
        this.callBack = callBack;
    }
}
