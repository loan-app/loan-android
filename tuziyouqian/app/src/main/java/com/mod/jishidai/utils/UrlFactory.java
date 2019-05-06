package com.mod.jishidai.utils;

import com.mod.jishidai.BuildConfig;
import com.mod.jishidai.base.App;

/**
 * @author 周竹
 * @file UrlFactory
 * @brief
 * @date 2018/4/11 上午11:12
 * Copyright (c) 2017
 * All rights reserved.
 */
public class UrlFactory {

    private static final String BASE_URL = BuildConfig.API_HOST;
    private static final String H5_URL = BuildConfig.H5_HOST;

    /**
     * 获取图形验证码
     */
    public static String getGraphCodeUrl(String phone) {
        return new StringBuffer(BASE_URL).append("user/user_graph_code?phone=").append(phone).append("&version=").append(App.getInstance().getVersionName()).append("&type=android&alias=").append(BuildConfig.alias).toString();
    }

    /**
     * 用户认证中心
     *
     * @return
     */
    public static String getCertCenterUrl() {
        return new StringBuffer(H5_URL).append("/user/cert_center.html").toString();
    }

    /**
     * 关于我们
     *
     * @return
     */
    public static String getAboutUrl() {
        return new StringBuffer(H5_URL).append("/about.html").toString();
    }

    /**
     * 发现
     *
     * @return
     */
    public static String getFindUrl() {
        return new StringBuffer(H5_URL).append("/find.html").toString();
    }

    /**
     * 首页估价
     *
     * @return
     */
    public static String getHomeUrl() {
        return new StringBuffer(H5_URL).append("/order/phone_condition.html").toString();
    }

    /**
     * 帮助中心
     *
     * @return
     */
    public static String gethelpCenterUrl() {
        return new StringBuffer(H5_URL).append("/help_center.html").toString();
    }

    /**
     * 获取银行卡
     *
     * @return
     */
    public static String getBankCardUrl() {
        return new StringBuffer(H5_URL).append("/user/bank_card.html").toString();
    }

    /**
     * 历史订单
     *
     * @return
     */
    public static String getOrderHistoryUrl() {
        return new StringBuffer(H5_URL).append("/order/store_order_history.html").toString();
    }

    /**
     * 注册服务协议
     *
     * @return
     */
    public static String getDealUrl() {
        return new StringBuffer(H5_URL).append("/agreement/register.html").toString();
    }


    /**
     * 首页
     *
     * @return
     */
    public static String getHomeWebUrl() {
        return new StringBuffer(H5_URL).append("/store_borrowing.html").toString();
    }
}
