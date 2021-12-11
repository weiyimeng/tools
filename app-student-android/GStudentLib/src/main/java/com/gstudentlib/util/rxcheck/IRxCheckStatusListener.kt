package com.gstudentlib.util.rxcheck

/**
 * 作者：created by 逢二进一 on 2019/5/13 14:47
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
interface IRxCheckStatusListener {

    /**
     * 检查通过
     */
    fun onCheckPass()

    /**
     * 检查不通过
     */
    fun onCheckUnPass(iRxCheckStatus: IRxCheckStatus?)

}