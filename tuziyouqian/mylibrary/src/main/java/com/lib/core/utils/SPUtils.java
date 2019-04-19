package com.lib.core.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.lib.core.baseapp.BaseApplication;


/**
 * 对SharedPreference文件中的各种类型的数据进行存取操作
 */
public class SPUtils {

    private static String CONFIG = "config";
    private static SharedPreferences sharedPreferences;
    public static final String PHONE = "userPhone";
    public static final String PASSWORD = "userPassword";
    public static final String TOKEN = "userToken";
    public static final String DEVICEID = "deviceId";
    public static final String USER_UD = "user_ud";
    public static final String PHONE_SPLASH = "phone_splash";
    public static final String PHONE_START = "phone_start";

    /**
     * 删除key键的值
     *
     * @param key
     */
    public static void removeStringData(String key) {
        if (sharedPreferences == null) {
            sharedPreferences = BaseApplication.getAppContext().getSharedPreferences(CONFIG,
                    Context.MODE_PRIVATE);
        }

        sharedPreferences.edit().remove(key).commit();
    }

    // 存
    public static synchronized void saveStringData(String key,
                                                   String value) {
        if (sharedPreferences == null) {
            sharedPreferences = BaseApplication.getAppContext().getSharedPreferences(CONFIG,
                    Context.MODE_PRIVATE);
        }

        sharedPreferences.edit().putString(key, value).commit();
    }


    // 存boolean
    public static void saveBooleanData(String key,
                                       boolean value) {
        if (sharedPreferences == null) {
            sharedPreferences = BaseApplication.getAppContext().getSharedPreferences(CONFIG,
                    Context.MODE_PRIVATE);
        }

        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    // 取boolean
    public static boolean getBooleanData(String key,
                                         boolean defValue) {
        if (sharedPreferences == null) {
            sharedPreferences = BaseApplication.getAppContext().getSharedPreferences(CONFIG,
                    Context.MODE_PRIVATE);
        }
        return sharedPreferences.getBoolean(key, defValue);
    }

    // 取
    public static synchronized String getStringData(
            String key, String defValue) {
        if (sharedPreferences == null) {
            sharedPreferences = BaseApplication.getAppContext().getSharedPreferences(CONFIG,
                    Context.MODE_PRIVATE);
        }
        return sharedPreferences.getString(key, defValue);
    }

    // 删
    public static boolean deleteStringData(String key) {
        if (sharedPreferences == null) {
            sharedPreferences = BaseApplication.getAppContext().getSharedPreferences(CONFIG,
                    Context.MODE_PRIVATE);
        }
        return sharedPreferences.edit().remove(key).commit();
    }

    /**
     * 删除boolean值
     *
     * @param string
     */
    public static void removeBooleanData(
            String string) {
        if (sharedPreferences == null) {
            sharedPreferences = BaseApplication.getAppContext().getSharedPreferences(CONFIG,
                    Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().remove(string).commit();
    }


}