package com.xinyan.facecheck.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 70883 on 2017/6/28.
 */
public class PermissionUtil {
    /**
     * 需要进行检测的权限数组
     */
    public static String[] needPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    protected static final int PERMISSON_REQUESTCODE = 0;
    public static final int PERMISSON_REQUESTCODE_FREE = 1;//权限请求码，当外部需要实时申请权限的时候使用；
    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;
    public String[] mString;

    /**
     * 自定义检查权限；
     *
     * @param permissions
     * @return
     */
    public static boolean checkPermission(Context context,
                                          String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(context, permissions);
        if (null != needRequestPermissonList
                && needRequestPermissonList.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 自定义申请权限；
     */
    public static void requestPermission(Context context, String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(context, permissions);
        if (null != needRequestPermissonList
                && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions((Activity) context,
                    needRequestPermissonList.toArray(
                            new String[needRequestPermissonList.size()]),
                    PERMISSON_REQUESTCODE_FREE);
        }
    }


    /**
     * @param
     * @since 2.5.0
     */
    public static void checkPermissions(Context context, String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(context, permissions);
        if (null != needRequestPermissonList
                && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions((Activity) context,
                    needRequestPermissonList.toArray(
                            new String[needRequestPermissonList.size()]),
                    PERMISSON_REQUESTCODE);
        }
    }


    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    public static List<String> findDeniedPermissions(Context context, String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(context,
                    perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    (Activity) context, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    public static boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否有摄像头权限
     */
    public static boolean isCameraUseable() {
        boolean canUse = false;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            canUse = false;
        }
        if (mCamera != null) {
            canUse = true;
            mCamera.release();
        }
        Log.e("xinyan", canUse + "");
        return canUse;
    }
}
