package com.mod.jishidai.widget.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.flyco.dialog.widget.base.BaseDialog;
import com.mod.jishidai.R;

/**
 * @author 周竹
 * @file AppUpdateDialog
 * @brief
 * @date 2018/5/8 下午11:32
 * Copyright (c) 2017
 * All rights reserved.
 */
public class AppUpdateDialog extends BaseDialog {
    private String content;
    private String title;
    private TextView tvCancel;
    private TextView tvConfirm;
    private TextView mContent;
    private TextView tvTitle;
    OnCallBack callBack;

    public AppUpdateDialog(Context context) {
        super(context);
    }

    @Override
    public View onCreateView() {
        widthScale(0.8f);
        View view = View.inflate(mContext, R.layout.dialog_app_update_layout, null);
        tvCancel = (TextView) view.findViewById(R.id.tvCancel);
        tvConfirm = (TextView) view.findViewById(R.id.tvConfirm);
        mContent = (TextView) view.findViewById(R.id.mContent);
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
        if (!TextUtils.isEmpty(content))
            mContent.setText(content);
        if (!TextUtils.isEmpty(title))
            tvTitle.setText(title);
    }

    public AppUpdateDialog content(String content) {
        this.content = content;
        return this;
    }

    public AppUpdateDialog title(String title) {
        this.title = title;
        return this;
    }

    public interface OnCallBack {
        void onCancel();

        void onConfirm();
    }

    public void setOnCallBack(OnCallBack callBack) {
        this.callBack = callBack;
    }
}
