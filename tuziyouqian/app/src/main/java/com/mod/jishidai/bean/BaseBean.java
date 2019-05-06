package com.mod.jishidai.bean;

import com.mod.jishidai.utils.AppConfig;

/**
 * Created by zhouzhu on 2017/8/8.
 */

public class BaseBean<T> {

    public String status;
    public String message;
    public T data;

    public boolean success() {
        return AppConfig.RESULT_STATUS.equals(status);
    }

}
