package com.huatu.android.bean;

import com.huatu.android.utils.AppConfig;

/**
 * Created by zhouzhu on 2017/8/8.
 */

public class BaseBean<T> {

    public String status;
    public String msg;
    public T data;

    public boolean success() {
        return AppConfig.RESULT_STATUS.equals(status);
    }

}
