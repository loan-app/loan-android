package com.huatu.android.base;

import android.text.TextUtils;

import java.util.HashMap;

/**
 * @author 周竹
 * @file BaseParams
 * @brief
 * @date 2018/2/24 下午2:48
 * Copyright (c) 2017
 * All rights reserved.
 */

public class BaseParams {

    private HashMap<String, Object> mParamsMap = new HashMap<>();

    public BaseParams() {
        addDefaultParams();
    }

    private void addDefaultParams() {
    }

    public void put(String key, String value) {
        if (!TextUtils.isEmpty(key)) {//&& (!TextUtils.isEmpty(value))) {
            mParamsMap.put(key, value);
        }
    }

    public void put(String key, int value) {
        if (!TextUtils.isEmpty(key)) {
            mParamsMap.put(key, String.valueOf(value));
        }
    }

    public void put(String key, long value) {
        if (!TextUtils.isEmpty(key)) {
            mParamsMap.put(key, String.valueOf(value));
        }
    }

    public void put(String key, Object value) {
        if (!TextUtils.isEmpty(key)) {
            mParamsMap.put(key, value);
        }
    }

    public HashMap<String, Object> getParamsMap() {
        return mParamsMap;
    }
}
