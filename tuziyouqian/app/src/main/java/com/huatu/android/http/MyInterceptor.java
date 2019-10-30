package com.huatu.android.http;

import com.lib.core.utils.LogUtils;
import com.huatu.android.BuildConfig;
import com.huatu.android.base.App;
import com.huatu.android.utils.AppConfig;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author 周竹
 * @file MyInterceptor
 * @brief
 * @date 2017/12/2 下午9:18
 * Copyright (c) 2017, 米发科技
 * All rights reserved.
 */

public class MyInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (request.method().equals("GET")) {
            request = addGetParams(request);
        } else if (request.method().equals("POST")) {
            request = addPostParams(request);
        }
        if (LogUtils.mDebug) {
            LogUtils.loge("OkHttpTag请求地址:" + URLDecoder.decode(request.url().toString(), "UTF-8"));
            Response response = chain.proceed(request);
            MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            LogUtils.loge("OkHttpTag:请求结果:" + content);
            return response.newBuilder()
                    .body(ResponseBody.create(mediaType, content))
                    .build();
        } else {
            return chain.proceed(request);
        }
    }


    private Request addPostParams(Request request) throws UnsupportedEncodingException {
        if (request.body() instanceof FormBody) {
            FormBody.Builder newBuilder = new FormBody.Builder();
            StringBuilder paramsBuilder = new StringBuilder();
//            String digest = null;
            FormBody formBody = (FormBody) request.body();
            TreeMap<String, Object> paramsMap = new TreeMap<>();
            //把公共参数加入map中会自动按键值的字母排序
            paramsMap.put("version", App.getInstance().getVersionName());
            paramsMap.put("type", AppConfig.APPTYPE);
            paramsMap.put("alias", BuildConfig.alias);

            //遍历先加入的参数，加入map排序
            for (int i = 0; i < formBody.size(); i++) {
                paramsMap.put(formBody.name(i), formBody.value(i));
            }
            Set<Map.Entry<String, Object>> entries = paramsMap.entrySet();
            Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                paramsBuilder.append(next.getKey());
                paramsBuilder.append("=");
                Object value = next.getValue();
                if (value != null) {
                    newBuilder.add(next.getKey(), next.getValue().toString());
                    paramsBuilder.append(value + "&");

                }
            }
            //删除最后一个"&"
            paramsBuilder.deleteCharAt(paramsBuilder.length() - 1);
//            paramsBuilder.append(AppConfig.APP_SECRET);
            LogUtils.loge("OkHttpTag:加密前排序:" + paramsBuilder.toString());
//            digest = MD5Util.MD5(paramsBuilder.toString());
//            newBuilder.add(AppConfig.SIGN, digest);

            formBody = newBuilder.build();
            request = request.newBuilder().post(formBody).build();

        }
        return request;
    }

    private Request addGetParams(Request request) {
        //添加公共参数
        HttpUrl httpUrl = request.url()
                .newBuilder()
                .addQueryParameter("version", App.getInstance().getVersionName())
                .addQueryParameter("type", AppConfig.APPTYPE)
                .addQueryParameter("alias", BuildConfig.alias)
                .build();

//        //添加签名
//        Set<String> nameSet = httpUrl.queryParameterNames();
//        ArrayList<String> nameList = new ArrayList<>();
//        nameList.addAll(nameSet);
//        Collections.sort(nameList);
//
//        StringBuilder buffer = new StringBuilder();
//        for (int i = 0; i < nameList.size(); i++) {
//            buffer.append("&").append(nameList.get(i)).append("=").append(httpUrl.queryParameterValues(nameList.get(i)) != null &&
//                    httpUrl.queryParameterValues(nameList.get(i)).size() > 0 ? httpUrl.queryParameterValues(nameList.get(i)).get(0) : "");
//        }
//        httpUrl = httpUrl.newBuilder()
//                .addQueryParameter("sign", MD5Util.MD5(buffer.toString()))
//                .build();
        request = request.newBuilder().url(httpUrl).build();
        return request;
    }

}
