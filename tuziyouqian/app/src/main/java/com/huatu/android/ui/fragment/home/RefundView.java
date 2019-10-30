package com.huatu.android.ui.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huatu.android.R;
import com.huatu.android.base.App;
import com.huatu.android.bean.OrderBean;
import com.huatu.android.ui.activity.webview.WebViewActivity;
import com.huatu.android.ui.activity.webview.WebViewFragment;

public class RefundView extends LinearLayout {
    TextView tvSum;
    TextView tvTag;
    TextView tvTime;
    TextView tvNotice;
    TextView tvConfirm;
    TextView textGroup;
    LayoutInflater inflater;
    Context mContext;

    public RefundView(Context context) {
        this(context, null);
    }

    public RefundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefundView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.refund_view_layout, this);
        tvSum = findViewById(R.id.tvSum);
        tvTag = findViewById(R.id.tvTag);
        tvTime = findViewById(R.id.tvTime);
        tvNotice = findViewById(R.id.tvNotice);
        tvConfirm = findViewById(R.id.tvConfirm);
        textGroup = findViewById(R.id.textGroup);
    }

    public void setData(OrderBean data, boolean success) {
        if (data == null)
            return;
        tvConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(WebViewFragment.WEB_URL, data.url);
                mContext.startActivity(intent);
            }
        });
        if(!TextUtils.isEmpty(data.shouldRepay)){
            tvSum.setText(data.shouldRepay+"元");
        }
        if(!TextUtils.isEmpty(data.remainDays)){
            textGroup.setText(data.remainDays+"天");
        }
        tvTime.setText(data.lastRepayTime);
        if (success) {//正常还款
            tvTag.setVisibility(GONE);
            tvSum.setTextColor(App.getResColor(R.color.color_999999));
            textGroup.setTextColor(App.getResColor(R.color.color_999999));
            tvNotice.setText("距还款日还剩");
        } else {//逾期
            tvTag.setVisibility(GONE);
            tvSum.setTextColor(App.getResColor(R.color.color_fc5641));
            textGroup.setTextColor(App.getResColor(R.color.color_fc5641));
            tvNotice.setText("您已逾期");
        }

    }

}
