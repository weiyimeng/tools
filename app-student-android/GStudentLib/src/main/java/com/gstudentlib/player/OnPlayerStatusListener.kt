package com.gstudentlib.player

/**
 * 作者：created by 逢二进一 on 2019/9/11 10:20
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
interface OnPlayerStatusListener {

    //可以播放
    fun onPrepared(tag: Int)

    //播放完成
    fun onCompletion(tag: Int)

}