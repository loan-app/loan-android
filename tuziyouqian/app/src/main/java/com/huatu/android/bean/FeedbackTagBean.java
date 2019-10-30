package com.huatu.android.bean;

/**
 * @author 周竹
 * @file FeedbackTagBean
 * @brief
 * @date 2018/4/30 上午11:33
 * Copyright (c) 2017
 * All rights reserved.
 */
public class FeedbackTagBean {
    public String tag;
    public boolean isSelect;

    public FeedbackTagBean(String tag, boolean isSelect) {
        this.tag = tag;
        this.isSelect = isSelect;
    }
}
