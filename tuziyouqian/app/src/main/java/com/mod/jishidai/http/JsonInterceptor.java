package com.mod.jishidai.http;

import com.lib.core.utils.LogUtils;
import com.mod.jishidai.base.App;

import java.io.IOException;
import java.net.URLDecoder;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * @author 周竹
 * @file MyInterceptor
 * @brief
 * @date 2017/12/2 下午9:18
 * Copyright (c) 2017, 米发科技
 * All rights reserved.
 */

public class JsonInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (request.method().equals("GET")) {
            request = addGetParams(request);
        } else if (request.method().equals("POST")) {
            request = addPostParams(request);
        }
        if (LogUtils.mDebug) {
            LogUtils.logi("OkHttpTag请求地址:" + URLDecoder.decode(request.url().toString(), "UTF-8"));
            Response response = chain.proceed(request);
            MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            LogUtils.logi("OkHttpTag:请求结果:" + content);
            return response.newBuilder()
                    .body(ResponseBody.create(mediaType, content))
                    .build();
        }
        return chain.proceed(request);


    }

    private Request addGetParams(Request request) {
        HttpUrl httpUrl = request.url()
                .newBuilder()
                .addQueryParameter("device-udid", App.getInstance().getVersionName())
                .addQueryParameter("device-client", "android")
                .addQueryParameter("device-code", "6015")
                .addQueryParameter("api-version", "1")
                .build();
        return request.newBuilder().url(httpUrl).build();
    }

    private Request addPostParams(Request request) {
        Request.Builder requestBuilder = request.newBuilder();
        request = requestBuilder
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .addHeader("device-udid", "a46d663d675d4858ea7d0a21c2de06e9")
                .addHeader("device-client", "android")
                .addHeader("device-code", "6015")
                .addHeader("api-version", "1")
                .post(RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                        bodyToString(request.body())))//关键部分，设置requestBody的编码格式为json
                .build();
        return request;
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null) {
                copy.writeTo(buffer);
            } else {
                return "";
            }
            String body = buffer.readUtf8();
            if (LogUtils.mDebug) {
                LogUtils.logi("OkHttpTag请求参数:" + body);
            }
            return body;
        } catch (final IOException e) {
            return "did not work";
        }
    }
}

