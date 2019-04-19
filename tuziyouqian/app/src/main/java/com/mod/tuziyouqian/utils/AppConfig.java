package com.mod.tuziyouqian.utils;

import com.mod.tuziyouqian.base.App;

/**
 * @author 周竹
 * @file AppConfig
 * @brief
 * @date 2017/12/2 下午10:00
 * Copyright (c) 2017, 米发科技
 * All rights reserved.
 */

public class AppConfig {
    public static final String APPTYPE = "android";
    //应用编码方式
    public static final String ENCODING = "utf-8";
    public static final String APP_SECRET = "mizhifa2017licai";
    public static final String SIGN = "sign";
    //请求成功返回状态吗
    public static final String RESULT_STATUS = "2000";
    public static final String TOKEN_OVERDUE = "4002";
    public static final String RXMANAGER_UPDATE = "rxManager_Update";//通知刷新
    public static final String RXMANAGER_LOGIN = "rxManager_Login";//通知登录成功，上传deviceId
    public static final String PHONE_MODEL = "phone_model";
    public static final String LAUNCH_DATA = "LaunchData";//保存启动页轮播图和通知
    public static String FAILURE_ORDERID = "FAILURE_ORDERID";//保存失败订单(用户名加订单id) 审核失败只提醒一次，跟这个字段判断
    public static final String RXMANAGER_WEBUPDATE = "rxManager_WebUpdate";//通知h5刷新

}
