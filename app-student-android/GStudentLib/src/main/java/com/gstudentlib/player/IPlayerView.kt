package com.gstudentlib.player

/**
 * 作者：created by 逢二进一 on 2019/9/11 10:20
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
interface IPlayerView {

    companion object {
        //操作形式
        val TAG_ANSWER_PLAYER= 0X01 //原音播放
        val TAG_STUDENT_ANSWER_PLAYER = 0X02//回放播放
    }

    /**
     * 播放地址
     * @param url
     */
    fun withUrl(url: String)

    /**
     * 播放
     */
    fun play()

    /**
     * 是否在播放中
     * @return
     */
    fun isPlaying(): Boolean

    /**
     * 暂停
     */
    fun pause()

    /**
     * 停止
     */
    fun stop()

    /**
     * 销毁
     */
    fun destroy()

    /**
     * 重新创建
     */
    fun reCreate(url: String)

    /**
     * 设置播放器状态监听
     * @param onPlayerStatusListener
     */
    fun setOnPlayerStatusListener(onPlayerStatusListener: OnPlayerStatusListener)

}