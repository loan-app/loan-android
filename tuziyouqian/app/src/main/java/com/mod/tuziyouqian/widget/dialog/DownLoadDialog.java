package com.mod.tuziyouqian.widget.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flyco.dialog.widget.base.BaseDialog;
import com.mod.tuziyouqian.R;

/**
 * @author 周竹
 * @file DownLoadDialog
 * @brief
 * @date 2018/5/9 下午6:06
 * Copyright (c) 2017
 * All rights reserved.
 */
public class DownLoadDialog extends BaseDialog {
    ProgressBar mProgress;
    TextView tvProgress;

    public DownLoadDialog(Context context) {
        super(context);
    }

    @Override
    public View onCreateView() {
        widthScale(0.8f);
        View view = View.inflate(mContext, R.layout.dialog_download_layout, null);
        mProgress = (ProgressBar) view.findViewById(R.id.mProgress);
        tvProgress = (TextView) view.findViewById(R.id.tvProgress);
        return view;
    }

    @Override
    public void setUiBeforShow() {

    }


    public void setProgress(int progress) {
        if (mProgress != null) {
            mProgress.setProgress(progress);
            tvProgress.setText(progress + "%");
        }
    }

}
