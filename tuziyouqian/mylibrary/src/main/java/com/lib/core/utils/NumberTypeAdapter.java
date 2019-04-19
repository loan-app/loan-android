package com.lib.core.utils;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * @author 周竹
 * @file NumberTypeAdapter
 * @brief
 * @date 2018/4/17 下午1:56
 * Copyright (c) 2017
 * All rights reserved.
 */
public class NumberTypeAdapter extends TypeAdapter<Number> {
    @Override
    public void write(JsonWriter out, Number value) throws IOException {
        out.value(value);
    }

    @Override
    public Number read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return 0;
        }
        try {
            double i = in.nextDouble();//当成double来读取
            return (int) i;//强制转为int
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }
    }
}
