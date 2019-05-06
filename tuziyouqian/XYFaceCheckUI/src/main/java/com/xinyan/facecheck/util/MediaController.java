package com.xinyan.facecheck.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.xinyan.bioassay.sdk.R;

/**
 * Created on 2016/11/01 10:45.
 *
 * @author Han Xu
 */
public final class MediaController {

    private MediaPlayer mMediaPlayer = null;

    private MediaController() {
        // Do nothing.
    }

    public static MediaController getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * 释放媒体播放器.
     */
    public void release() {
        if (mMediaPlayer == null) {
            return;
        }
        mMediaPlayer.setOnPreparedListener(null);
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    /**
     * 播放声音.
     */
    public void playNotice(Context context, int motion) {
        switch (motion) {
            case 0://眨眼
                play(context, R.raw.common_notice_blink);
                break;
            case 1://
              //  play(context, R.raw.common_notice_mouth);
                break;
            case 2:
                play(context, R.raw.common_notice_yaw);
                break;
            case 3://
                play(context, R.raw.common_notice_nod);
                break;
            default:
                break;
        }
    }

    private void play(Context context, int soundId) {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        AudioManager audioManager =
                (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                // Do nothing.
            }
        }, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        mMediaPlayer = MediaPlayer.create(context, soundId);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }

    private static class InstanceHolder {
        private static final MediaController INSTANCE = new MediaController();
    }
}