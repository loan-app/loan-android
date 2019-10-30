package com.huatu.android.bean;

/**
 * @author 周竹
 * @file ContactBean
 * @brief
 * @date 2018/5/14 上午11:02
 * Copyright (c) 2017
 * All rights reserved.
 */
public class ContactBean {

    public ContactBean(String c, String n, String p) {
        this.c = c;
        this.n = n;
        this.p = p;
    }

    public String c;//关系
    public String n;//名字
    public String p;//手机号
}
