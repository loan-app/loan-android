package com.huatu.android.ui.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huatu.android.R;
import com.huatu.android.base.App;
import com.huatu.android.bean.OrderBean;
import com.huatu.android.ui.activity.login.LoginActivity;
import com.huatu.android.ui.activity.webview.WebViewActivity;
import com.huatu.android.ui.activity.webview.WebViewFragment;

/**
 * @author 周竹
 * @file ValuationView
 * @brief
 * @date 2018/5/11 上午10:06
 * Copyright (c) 2017
 * All rights reserved.
 */
public class LoanView extends LinearLayout {
    Button tvConfirm;
    TextView tvSum,tvTitle/*,tvDetail*/;
    Context mContext;


    public LoanView(Context context) {
        this(context, null);
    }

    public LoanView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoanView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.loan_view_layout, this);
        tvConfirm = findViewById(R.id.tvConfirm);
        tvSum = findViewById(R.id.tvSum);
        tvTitle = findViewById(R.id.tvTitle);
//        tvDetail = findViewById(R.id.tvDetail);
    }

    public void setData(OrderBean bean) {

        tvConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                toNext(bean);
            }
        });
        tvConfirm.setText(bean.descMid);
        tvSum.setText(bean.amount);
        tvTitle.setText(bean.descTop);
//        tvDetail.setText(bean.descBottom);
    }

    private void toNext(OrderBean bean) {
        if (App.getInstance().isLogin()) {
            Intent intent = new Intent(mContext, WebViewActivity.class);
            intent.putExtra(WebViewFragment.WEB_URL, bean.url);
            mContext.startActivity(intent);
        } else {
            Intent intent = new Intent(mContext, LoginActivity.class);
            mContext.startActivity(intent);
        }
    }
}
