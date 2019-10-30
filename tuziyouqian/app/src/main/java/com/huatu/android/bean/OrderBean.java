package com.huatu.android.bean;

import java.util.List;

/**
 * @author 周竹
 * @file OrderBean
 * @brief
 * @date 2018/5/11 上午9:40
 * Copyright (c) 2017
 * All rights reserved.
 */
public class OrderBean {
    public String orderStatus;
    public String orderId;
    public String url;
    public String shouldRepay;
    public String lastRepayTime;
    public String remainDays;
    public List<LoanBeforeBean> loanBeforeList;
    public String amount;
    public String descTop;
    public String descMid;
    public String descBottom;
}
