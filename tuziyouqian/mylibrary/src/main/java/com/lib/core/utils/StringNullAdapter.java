package com.lib.core.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * @author 周竹
 * @file StringAdapter
 * @brief
 * @date 2018/4/17 下午1:54
 * Copyright (c) 2017
 * All rights reserved.
 */
public class StringNullAdapter extends TypeAdapter<String> {
    @Override
    public void write(JsonWriter out, String value) throws IOException {
        try {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.value(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String read(JsonReader in) throws IOException {
        try {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return "";//原先是返回Null，这里改为返回空字符串
            }
            return in.nextString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
