package com.mod.tuziyouqian.ui.fragment.home;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mod.tuziyouqian.R;
import com.mod.tuziyouqian.base.App;
import com.mod.tuziyouqian.bean.LoanBeforeBean;
import com.mod.tuziyouqian.widget.TimeLine;

import java.util.List;

/**
 * @author 周竹
 * @file EventAdapter
 * @brief
 * @date 2018/5/11 上午11:04
 * Copyright (c) 2017
 * All rights reserved.
 */
public class EventAdapter extends BaseQuickAdapter<LoanBeforeBean, BaseViewHolder> {
    private boolean success;

    public EventAdapter(@Nullable List<LoanBeforeBean> data) {
        super(R.layout.adapter_event_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LoanBeforeBean item) {
        int position = mData.indexOf(item);
        TimeLine mTimeLine = helper.getView(R.id.mTimeLine);
        TextView tvEvent = helper.getView(R.id.tvEvent);
        TextView tvEventTime = helper.getView(R.id.tvEventTime);
        TextView tvEventDescribe = helper.getView(R.id.tvEventDescribe);
        tvEvent.setText(item.event);
        tvEventTime.setText(item.eventTime);
        tvEventDescribe.setText(item.eventDescribe);
        if (position == 0) {
            mTimeLine.getTopView().setVisibility(View.INVISIBLE);
            mTimeLine.getBottomView().setVisibility(View.VISIBLE);
            mTimeLine.getPointView().setBackgroundResource(R.drawable.radius_round_d8d8d8);
            tvEvent.setTextColor(App.getResColor(R.color.color_999999));
            tvEventDescribe.setTextColor(App.getResColor(R.color.color_cdcdcd));
        } else if (position == mData.size() - 1) {
            mTimeLine.getTopView().setVisibility(View.VISIBLE);
            mTimeLine.getBottomView().setVisibility(View.INVISIBLE);
            if (success) {
                mTimeLine.getPointView().setBackgroundResource(R.drawable.radius_round_7ed321);
                tvEvent.setTextColor(App.getResColor(R.color.color_333333));
                tvEventDescribe.setTextColor(App.getResColor(R.color.color_666666));
            } else {
                mTimeLine.getPointView().setBackgroundResource(R.drawable.radius_round_fd664b);
                tvEvent.setTextColor(App.getResColor(R.color.color_fd664b));
                tvEventDescribe.setTextColor(App.getResColor(R.color.color_fd664b));
            }
        } else {
            mTimeLine.getTopView().setVisibility(View.VISIBLE);
            mTimeLine.getBottomView().setVisibility(View.VISIBLE);
            mTimeLine.getPointView().setBackgroundResource(R.drawable.radius_round_d8d8d8);
            tvEvent.setTextColor(App.getResColor(R.color.color_999999));
            tvEventDescribe.setTextColor(App.getResColor(R.color.color_cdcdcd));
        }

    }

    public void setShowSuccess(boolean success) {
        this.success = success;
    }
}
