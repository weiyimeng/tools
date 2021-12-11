package com.gstudentlib.player

import android.media.AudioManager
import android.media.MediaPlayer
import com.gsbaselib.utils.LOGGER
import java.io.IOException

/**
 * 在线音频播放器
 * 可进行播放，暂停，停止
 * 后期可加入离线播放
 * 作者：created by 逢二进一 on 2019/9/11 10:25
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
class Player constructor(tag: Int) : MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, IPlayerView {

    /**
     * 媒体播放器
     */
    private var mTag = 0
    private var mMediaPlayer: MediaPlayer? = null
    private var mOnPlayerStatusListener: OnPlayerStatusListener? = null

    init {
        this.mTag = tag
        this.createPlay()
    }

    /**
     * 构建播放器
     */
    private fun createPlay() {
        try {
            mMediaPlayer = MediaPlayer()
            mMediaPlayer?.run {
                setAudioStreamType(AudioManager.STREAM_MUSIC)// 设置媒体流类型
                setOnBufferingUpdateListener(this@Player)
                setOnPreparedListener(this@Player)
                setOnCompletionListener(this@Player)
                setOnErrorListener(this@Player)
            }
        } catch (e: Exception) {
            LOGGER.log("context", e)
        }
    }

    /**
     * 设置在线播放地址
     * @param url
     */
    override fun withUrl(url: String) {
        try {
            mMediaPlayer?.reset()
            mMediaPlayer?.setDataSource(url)
            mMediaPlayer?.prepareAsync()
        } catch (e: IllegalArgumentException) {
            LOGGER.log("context", e)
        } catch (e: SecurityException) {
            LOGGER.log("context", e)
        } catch (e: IllegalStateException) {
            LOGGER.log("context", e)
        } catch (e: IOException) {
            LOGGER.log("context", e)
        }
    }

    /**
     * 播放
     */
    override fun play() {
        try {
            mMediaPlayer?.start()
        } catch (e: Exception) {
        }
    }

    override fun isPlaying(): Boolean {
        return try {
            mMediaPlayer?.isPlaying ?: false
        } catch (e: Exception) {
            true
        }
    }

    /**
     * 暂停
     */
    override fun pause() {
        try {
            mMediaPlayer?.pause()
        } catch (e: Exception) {
        }
    }

    /**
     * 停止
     * 即再次播放时将指针滑向0
     */
    override fun stop() {
        this.pause()
        try {
            mMediaPlayer?.seekTo(0)
        } catch (e: Exception) {
        }
    }

    /**
     * 销毁
     */
    override fun destroy() {
        try {
            mMediaPlayer?.stop()
            mMediaPlayer?.release()
            mMediaPlayer = null
        } catch (e: Exception) {
        }
    }

    override fun reCreate(url: String) {
        if (mMediaPlayer == null) {
            this.createPlay()
        }
        this.withUrl(url)
    }

    /**
     * 播放准备
     * @param mp
     */
    override fun onPrepared(mp: MediaPlayer) {
        mOnPlayerStatusListener?.onPrepared(mTag)
    }

    /**
     * 播放完成
     * @param mp
     */
    override fun onCompletion(mp: MediaPlayer) {
        mOnPlayerStatusListener?.onCompletion(mTag)
    }

    /**
     * 缓冲更新
     */
    override fun onBufferingUpdate(mp: MediaPlayer, percent: Int) {}

    /**
     * 设置播放器状态监听
     * @param onPlayerStatusListener
     */
    override fun setOnPlayerStatusListener(onPlayerStatusListener: OnPlayerStatusListener) {
        this.mOnPlayerStatusListener = onPlayerStatusListener
    }

    /**
     * 播放发生错误
     * @param mp
     * @param what
     * @param extra
     * @return
     */
    override fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean {
        return true
    }

}