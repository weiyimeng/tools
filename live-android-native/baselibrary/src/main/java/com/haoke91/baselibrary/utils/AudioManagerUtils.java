package com.haoke91.baselibrary.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.blankj.utilcode.util.Utils;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/6/21 下午7:15
 * 修改人：weiyimeng
 * 修改时间：2018/6/21 下午7:15
 * 修改备注：
 */
public class AudioManagerUtils {
    private static AudioManagerUtils instance;
    
    public static AudioManagerUtils getInstance() {
        if (instance == null) {
            synchronized (AudioManagerUtils.class) {
                if (instance == null) {
                    instance = new AudioManagerUtils();
                }
            }
        }
        return instance;
    }
    
    private AudioManagerUtils() {
        audioManager = (AudioManager) Utils.getApp().getSystemService(Context.AUDIO_SERVICE);
    }
    
    AudioManager audioManager;
    
    public void requestFocus() {
        // if (mFocusChangeListener != null && mFocusChangeListener.get() != null) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
            AudioFocusRequest sss = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(audioAttributes)
                .setOnAudioFocusChangeListener(afChangeListener)
                .build();
            audioManager.requestAudioFocus(sss);
        } else {
            audioManager.requestAudioFocus(afChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        }
        //        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
        //
        //        //   }
    }
    
    AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        
        
        @Override
        public void onAudioFocusChange(int focusChange) {
            Log.e("tag", "focusChange====" + focusChange);
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                //pause play
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                //resume play
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                //stop
            }
        }
    };
    
    
}
