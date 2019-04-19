package com.mod.tuziyouqian.ui.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lib.core.utils.AppUtils;
import com.mod.tuziyouqian.R;
import com.mod.tuziyouqian.base.App;
import com.mod.tuziyouqian.bean.PhoneBean;
import com.mod.tuziyouqian.ui.activity.login.LoginActivity;
import com.mod.tuziyouqian.ui.activity.webview.WebViewActivity;
import com.mod.tuziyouqian.ui.activity.webview.WebViewFragment;

/**
 * @author 周竹
 * @file ValuationView
 * @brief
 * @date 2018/5/11 上午10:06
 * Copyright (c) 2017
 * All rights reserved.
 */
public class ValuationView extends LinearLayout {
    TextView tvModel;
//    TextView tvMemory;
    TextView tvMaxPrice;
//    TextView tvValuation;
    TextView tvGetMoney;
    Context mContext;


    public ValuationView(Context context) {
        this(context, null);
    }

    public ValuationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ValuationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.valuation_view_layout, this);
        tvModel = findViewById(R.id.tvModel);
//        tvMemory = findViewById(R.id.tvMemory);
        tvMaxPrice = findViewById(R.id.tvMaxPrice);
//        tvValuation = findViewById(R.id.tvValuation);
        tvGetMoney = findViewById(R.id.tvGetMoney);
    }

    public void setData(PhoneBean bean) {
        tvModel.setText(App.getInstance().getPhoneBrand()  +" "+bean.phoneName +"   "+AppUtils.getRomTotalSize() + "G");
//        tvMemory.setText("内存 " + AppUtils.getRomTotalSize() + "G");
        tvMaxPrice.setText("最高回收价 ￥" + bean.maxPrice);
//        tvValuation.setOnClickListener(new OnClickListener()
//            @Override
//            public void onClick(View view) {
//                toNext(bean);
//            }
//        });
        tvGetMoney.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                toNext(bean);
            }
        });
    }

    private void toNext(PhoneBean bean) {
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
