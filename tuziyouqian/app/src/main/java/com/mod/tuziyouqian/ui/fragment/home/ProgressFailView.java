package com.mod.tuziyouqian.ui.fragment.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mod.tuziyouqian.R;
import com.mod.tuziyouqian.bean.OrderBean;

public class ProgressFailView extends RelativeLayout {
    TextView tvEventDescribe;
    TextView tvDetail;
    Context mContext;
    ProgressView.CallBack callBack;

    public ProgressFailView(Context context) {
        this(context, null);
    }

    public ProgressFailView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressFailView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.progress_fail_layout, this);
        tvEventDescribe = findViewById(R.id.tvEventDescribe);
        tvDetail = findViewById(R.id.tvDetail);
    }

    public void setData(OrderBean orderBean) {
        tvDetail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callBack != null)
                    callBack.onCallBack();
            }
        });
        if (orderBean.loanBeforeList.size() == 2) {
            tvEventDescribe.setText(orderBean.loanBeforeList.get(1).eventDescribe);
        }
    }

    public void setCallBack(ProgressView.CallBack callBack) {
        this.callBack = callBack;
    }
}
