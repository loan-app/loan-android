package com.xinyan.facecheck.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tao_xu on 2019/3/15.
 */

@SuppressLint("SimpleDateFormat")
public class DateUtils {

    public static String PATTERN_YYYYMMddHHMMSS = "yyyyMMddHHmmss";

    public static String getCurrentDateString(String pattern) {
        String date = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            date = format.format(new Date());
        } catch (Exception e) {
            date = "";
        }
        return date;
    }
}
