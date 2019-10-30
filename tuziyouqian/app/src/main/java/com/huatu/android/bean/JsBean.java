package com.huatu.android.bean;

/**
 * @author 周竹
 * @file JsBean
 * @brief
 * @date 2018/5/6 下午6:02
 * Copyright (c) 2017
 * All rights reserved.
 */
public class JsBean {
    public JsBean(String isShowBack, String isShowClose, String title, String browser) {
        this.isShowBack = isShowBack;
        this.isShowClose = isShowClose;
        this.title = title;
        this.browser = browser;
    }

    public String isShowBack;
    public String isShowClose;
    public String title;
    public String browser;

}
