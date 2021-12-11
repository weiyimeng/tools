package com.gstudentlib.player

import android.media.AudioManager
import android.media.MediaPlayer
import com.gsbaselib.base.GSBaseApplication
import com.gsbaselib.base.log.LogUtil
import java.io.IOException

/**
 * 在线音频播放器
 * 可进行播放，暂停，停止
 * 后期可加入离线播放
 * 作者：created by 逢二进一 on 2019/9/11 10:27
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
class NavPlayer constructor(tag: Int) : MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener,
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
     * 初始化播放器
     */
    fun NavPlayer() {
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
                setOnBufferingUpdateListener(this@NavPlayer)
                setOnPreparedListener(this@NavPlayer)
                setOnCompletionListener(this@NavPlayer)
                setOnErrorListener(this@NavPlayer)
            }
        } catch (e: Exception) {
        }
    }

    /**
     * 设置在线播放地址
     * @param name
     */
    override fun withUrl(url: String) {
        mMediaPlayer?.run {
            val fileName = "audio/$url.mp3"
            try {
                val fd = GSBaseApplication.getApplication().assets.openFd(fileName)
                stop()
                reset()
                setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
                prepareAsync()
                fd.close()
            } catch (e: IllegalArgumentException) {
            } catch (e: SecurityException) {
            } catch (e: IllegalStateException) {
            } catch (e: IOException) {
            }
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
        LogUtil.d("onPrepared")
        mOnPlayerStatusListener?.onPrepared(mTag)
    }

    /**
     * 播放完成
     * @param mp
     */
    override fun onCompletion(mp: MediaPlayer) {
        LogUtil.d("onCompletion")
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

    override fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean {
        LogUtil.d("onError：$what  extra：$extra")
        this.play()
        return false
    }

}