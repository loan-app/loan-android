/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.huatu.android.utils;

import android.content.Context;

import com.lib.core.utils.TimeUtil;

import java.io.File;

public class FileUtil {
  public static File getSaveFile(Context context) {
    File file = new File(context.getFilesDir(), getName());
    return file;
  }

  private static String getName() {
    StringBuilder builder = new StringBuilder(TimeUtil.getCurrentDate("yyyyMMddHHmmssSSS"));
    builder.append(".jpg");
    return builder.toString();
  }

  private static int getRandNum(int min, int max) {
    int randNum = min + (int) (Math.random() * ((max - min) + 1));
    return randNum;
  }

  public static long getFileSize(String filePath) {
    File file = new File(filePath);
    if (file.isFile() && file.exists()) {
      return file.length() / 1024;
    }
    return 0;
  }
}
