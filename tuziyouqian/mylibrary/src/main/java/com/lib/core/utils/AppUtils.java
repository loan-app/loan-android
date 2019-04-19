package com.lib.core.utils;

import android.hardware.Camera;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import com.lib.core.baseapp.BaseApplication;

import java.io.File;
import java.util.List;

/**
 * @author 周竹
 * @file AppUtils
 * @brief
 * @date 2018/4/24 下午4:38
 * Copyright (c) 2017
 * All rights reserved.
 */
public class AppUtils {

    public static boolean isCameraUseable() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
// setParameters 是针对魅族MX5。MX5通过Camera.open()拿到的Camera对象不为null
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            canUse = false;
        }
        if (mCamera != null) {
            mCamera.release();
        }
        return canUse;
    }

    /**
     * @return //获取手机储存空间 单位G
     */
    public static String getRomTotalSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        int sizeNum = 0;
        String size = Formatter.formatFileSize(BaseApplication.getAppContext(), blockSize * totalBlocks);
        if(size != null && size.contains("吉字节")){
            sizeNum = (int) Double.parseDouble(size.substring(0, size.length() - 3));
        }else{
            sizeNum = (int) Double.parseDouble(size.substring(0, size.length() - 2));
        }
        if (sizeNum <= 4) {
            sizeNum = 4;
        } else if (sizeNum <= 8) {
            sizeNum = 8;
        } else if (sizeNum <= 16) {
            sizeNum = 16;
        } else if (sizeNum <= 32) {
            sizeNum = 32;
        } else if (sizeNum <= 64) {
            sizeNum = 64;
        } else if (sizeNum <= 128) {
            sizeNum = 128;
        } else if (sizeNum <= 256) {
            sizeNum = 256;
        } else if (sizeNum <= 512) {
            sizeNum = 512;
        } else if (sizeNum <= 1024) {
            sizeNum = 1024;
        }
        return sizeNum + "";
    }


    public static int getApproximate(int x, List<Integer> src) {
        if (CollectionUtils.isNullOrEmpty(src)) {
            return -1;
        }
        if (src.size() == 1) {
            return src.get(0);
        }
        int minDifference = Math.abs(src.get(0) - x);
        int minIndex = 0;
        for (int i = 1; i < src.size(); i++) {
            int temp = Math.abs(src.get(i) - x);
            if (temp < minDifference) {
                minIndex = i;
                minDifference = temp;
            }
        }
        return src.get(minIndex);
    }

}
