package com.mod.tuziyouqian.utils;

import android.content.Context;

import com.lib.core.utils.TimeUtil;
import com.mod.tuziyouqian.base.App;
import com.mod.tuziyouqian.bean.BaseBean;
import com.mod.tuziyouqian.bean.UploadBean;
import com.mod.tuziyouqian.http.RxManager;
import com.mod.tuziyouqian.http.RxSchedulers;
import com.mod.tuziyouqian.http.RxSubscriber;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author 周竹
 * @file UploadManage
 * @brief  文件上传统一方法
 * @date 2018/4/24 下午11:50
 * Copyright (c) 2017
 * All rights reserved.
 */
public class UploadManage {
    public static boolean upload(RxManager mRxManager, Context mContext, String filePath, UploadListener listener) {
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), App.getInstance().getToken());
        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", getFileName(filePath), requestFile);
        App.serverAPI.uploadFile(tokenBody, body).compose(RxSchedulers.<BaseBean<UploadBean>>io_main()).subscribe(new RxSubscriber<BaseBean<UploadBean>>(mContext, mRxManager, false) {
            @Override
            protected void onSuccess(BaseBean<UploadBean> bean) {
                if (listener != null)
                    listener.onUploadSuccess(bean.data);

            }

            @Override
            protected void onFailed(String code, String msg) {
                if (listener != null)
                    listener.onFailed(code, msg);

            }
        });

        return false;
    }

    public interface UploadListener {
        void onUploadSuccess(UploadBean bean);

        void onFailed(String code, String msg);

    }

    public interface SaveSaceListener {

        void onUploadSuccess();

        void onFailed(String code, String msg);

    }


    public static String getFileName(String filePath) {
        File file = new File(filePath);
        return file.exists() && !file.isDirectory() && file.canRead() ? getName(file.getName()) : null;
    }

    private static String getName(String fileName) {
        StringBuilder builder = new StringBuilder(TimeUtil.getCurrentDate("yyyyMMddHHmmssSSS"));
        builder.append(getRandNum(10000, 99999));
        builder.append(fileName.substring(fileName.lastIndexOf(".")));

        return builder.toString();
    }

    private static int getRandNum(int min, int max) {
        int randNum = min + (int) (Math.random() * ((max - min) + 1));
        return randNum;
    }



    public static boolean saveSace(RxManager mRxManager, Context mContext, String filePath, SaveSaceListener listener) {
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), App.getInstance().getToken());
        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", getFileName(filePath), requestFile);
        App.serverAPI.saveSace(tokenBody, body).compose(RxSchedulers.<BaseBean>io_main()).subscribe(new RxSubscriber<BaseBean>(mContext, mRxManager, false) {
            @Override
            protected void onSuccess(BaseBean bean) {
                if (listener != null)
                    listener.onUploadSuccess();

            }

            @Override
            protected void onFailed(String code, String msg) {
                if (listener != null)
                    listener.onFailed(code, msg);

            }
        });

        return false;
    }
}
