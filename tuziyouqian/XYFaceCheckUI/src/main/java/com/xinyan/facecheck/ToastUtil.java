package com.xinyan.facecheck;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by XY400063 on 2018/7/23.
 */

public class ToastUtil {

    public static Toast mToast = null;

    /**
     * 弹出Toast
     *
     * @param context  上下文对象
     * @param text     提示的文本
     * @param duration 持续时间（0：短；1：长）
     */
    public static void showToast(Context context, String text, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, duration);
        } else {
            mToast.setText(text);
            mToast.setDuration(duration);
        }

        mToast.show();
    }
    /**
     * 弹出Toast
     *
     * @param context  上下文对象
     * @param text     提示的文本
     */
    public static void showToast(Context context, String text) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }

        mToast.show();
    }
    /**
     * 弹出Toast
     *
     * @param context  上下文对象
     * @param text     提示的文本
     * @param duration 持续时间（0：短；1：长）
     * @param gravity  位置（Gravity.CENTER;Gravity.TOP;...）
     */
    public static void showToast(Context context, String text, int duration, int gravity) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, duration);
        } else {
            mToast.setText(text);
            mToast.setDuration(duration);
        }
        mToast.setGravity(gravity, 0, 0);
        mToast.show();
    }

    /**
     * 关闭Toast
     */
    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }

    }
}
