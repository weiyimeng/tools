package gaosi.com.learn.studentapp.aiscene.core

import java.io.File

/**
 * 作者：created by 逢二进一 on 2019/8/5 14:26
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
interface OnLaunchListener: OnErrorListener {

    fun onBeforeLaunch(var1: Long)

    fun onRealTimeVolume(var1: Int)

    fun onAfterLaunch(var1: Int, var2: String?, var3: File?)

}