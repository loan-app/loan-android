package com.huatu.android.utils;

import android.os.CountDownTimer;

/**
 * @author 周竹
 * @file DownTimer
 * @brief
 * @date 2018/5/14 下午4:19
 * Copyright (c) 2017
 * All rights reserved.
 */
public class DownTimer extends CountDownTimer {
    CountdownClickListener listener;

    public DownTimer(long time, CountdownClickListener listener) {  //到计时秒数
        super(time  * 1000+1050, 1000);
        this.listener = listener;
    }

    @Override
    public void onTick(long l) {
        if (listener != null)
            listener.onCountdown(l / 1000-1);

    }

    @Override
    public void onFinish() {
        if (listener != null)
            listener.onFinish();

    }

    public interface CountdownClickListener {
        void onCountdown(long num);

        void onFinish();
    }
}
