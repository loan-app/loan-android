package com.huatu.android.bean;

/**
 * @author 周竹
 * @file UploadRequestBean
 * @brief
 * @date 2018/4/30 下午4:18
 * Copyright (c) 2017
 * All rights reserved.
 */
public class UploadRequestBean {
    public UploadRequestBean(String url, boolean isComplete, boolean isSuccess) {
        this.url = url;
        this.isComplete = isComplete;
        this.isSuccess = isSuccess;
    }

    public String url;
    public boolean isComplete;//任务是否完成
    public boolean isSuccess;//上传是否成功
}
