package com.huatu.android.widget.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.flyco.dialog.widget.base.BaseDialog;
import com.lib.core.utils.ImageLoaderUtils;
import com.lib.core.utils.ToastUtil;
import com.huatu.android.R;
import com.huatu.android.utils.UrlFactory;
import com.huatu.android.widget.ClearEditText;

/**
 * @author 周竹
 * @file GraphCodeDialog
 * @brief
 * @date 2018/4/22 下午9:49
 * Copyright (c) 2017
 * All rights reserved.
 */
public class GraphCodeDialog extends BaseDialog {
    ImageView ivGraph;
    ClearEditText etCode;
    Button btnConfirm;
    OnGraphCodeCallBack callBack;
    String phone;
    String url;

    public GraphCodeDialog(Context context, String phone) {
        super(context, true);
        this.phone = phone;
        url = UrlFactory.getGraphCodeUrl(phone);
    }

    @Override
    public View onCreateView() {
        widthScale(0.72f);
        View inflate = View.inflate(mContext, R.layout.dialog_graph_code, null);
        ivGraph = inflate.findViewById(R.id.ivGraph);
        etCode = inflate.findViewById(R.id.etCode);
        btnConfirm = inflate.findViewById(R.id.btnConfirm);
        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        ImageLoaderUtils.displaysNoCache(mContext, ivGraph, url);
        ivGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageLoaderUtils.displaysNoCache(mContext, ivGraph, url);
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callBack != null) {
                    String code = etCode.getText().toString();
                    if (!TextUtils.isEmpty(code) && code.length() > 3) {
                        callBack.onConfirm(code);
                    } else {
                        ToastUtil.showShort("验证码应为四位");
                    }
                }
            }
        });


    }

    public interface OnGraphCodeCallBack {
        void onCancel();

        void onConfirm(String code);
    }

    public void setOnCallBack(OnGraphCodeCallBack callBack) {
        this.callBack = callBack;
    }
}
