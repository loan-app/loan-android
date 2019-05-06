package com.mod.jishidai.ui.fragment.home;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mod.jishidai.R;
import com.mod.jishidai.bean.OrderBean;

/**
 * @author 周竹
 * @file ProgressView
 * @brief
 * @date 2018/5/11 下午1:41
 * Copyright (c) 2017
 * All rights reserved.
 */
public class ProgressView extends RelativeLayout {
    RecyclerView mRecycler;
    EventAdapter adapter;
    TextView tvDetail;
    Context mContext;
    CallBack callBack;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.progress_layout, this);
        mRecycler = findViewById(R.id.mRecycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(context));
        tvDetail = findViewById(R.id.tvDetail);
    }

    public void setData(OrderBean orderBean, boolean success) {
        tvDetail.setVisibility(!success ? VISIBLE : GONE);
        tvDetail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callBack != null)
                    callBack.onCallBack();
            }
        });
        if (adapter == null) {
            adapter = new EventAdapter(orderBean.loanBeforeList);
            adapter.setShowSuccess(success);
            mRecycler.setAdapter(adapter);
        } else {
            adapter.setShowSuccess(success);
            adapter.setNewData(orderBean.loanBeforeList);
        }
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;

    }

    public interface CallBack {
        void onCallBack();
    }
}
