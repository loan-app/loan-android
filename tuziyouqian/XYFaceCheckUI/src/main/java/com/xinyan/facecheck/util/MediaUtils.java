package com.xinyan.facecheck.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;


import com.xinyan.facecheck.lib.util.Loggers;

import java.io.IOException;

/**
 * Created by Vincent on 2019/1/23.
 */
public class MediaUtils {

  Context mContext;
  AudioManager am;
  MediaPlayer player;
  String tips = "";

  public MediaUtils(Context context) {
    this.mContext = context;
    am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
    openSound();
  }

  public void startMedia(int rawId, String currentTips) {
    if (!tips.contains(currentTips)) {// 过滤重复创建播放组件；
      // if (player == null) {//去掉此判断，让其每次都进行创建，解决二次 media 播放非法状态问题
      player = new MediaPlayer();
      // }
      tips = currentTips;
      final int ringerMode = am.getRingerMode();
      switch (ringerMode) {
        case AudioManager.RINGER_MODE_NORMAL:// 普通模式
          playFromRawFile(mContext, rawId);
          Loggers.i("----------------RINGER_MODE_NORMAL");
          break;
        case AudioManager.RINGER_MODE_VIBRATE:// 静音模式
          Loggers.i("----------------RINGER_MODE_VIBRATE");
          break;
        case AudioManager.RINGER_MODE_SILENT:// 震动模式
          Loggers.i("----------------RINGER_MODE_SILENT");
          break;
        default:
          Loggers.i("----------------OTHER");
          break;
      }
    }
  }

  /**
   * 提示音
   * 
   * @param mContext
   */
  private void playFromRawFile(Context mContext, int rawId) {
    try {
      AssetFileDescriptor file = mContext.getResources().openRawResourceFd(rawId);
      try {
        player.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
        if (!player.isPlaying()) {
          player.prepare();
          player.start();
        }
        file.close();
      } catch (IOException e) {
        Loggers.i("player setDataSource is exception:" + e.getMessage());
        player = null;
      }
    } catch (Exception e) {
      Loggers.i("player exception:" + e.getMessage());
    }
  }

  /**
   * 停止播放；
   */
  public void stopMedia() {
    if (player != null && player.isPlaying()) {
      player.stop();
      player.reset();
      player.release();
      player = null;
    }
  }

  /**
   * 关闭声音；
   */
  public void closeSound() {
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
      am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
    } else {
      am.setStreamMute(AudioManager.STREAM_MUSIC, true);
    }
  }

  /**
   * 开启声音；
   */
  public void openSound() {
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {// 适配c8818机型；
      am.setStreamVolume(AudioManager.STREAM_MUSIC, 11, 0);
    } else {
      am.setStreamMute(AudioManager.STREAM_MUSIC, false);
    }
  }

}
