package com.gstudentlib.util.rxcheck

import com.gsbaselib.base.GSBaseApplication
import com.gsbaselib.utils.net.NetworkUtil

/**
 * 作者：created by 逢二进一 on 2019/5/13 14:50
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
class RxCheckNetStatus: IRxCheckStatus {

    /**
     * 检查网络
     */
    override fun check(): Boolean {
        return NetworkUtil.isConnected(GSBaseApplication.getApplication())
    }
}