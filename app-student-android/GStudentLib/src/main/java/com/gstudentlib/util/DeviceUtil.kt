package com.gstudentlib.util

import android.app.Activity
import android.os.Build

/**
 * Created by huangshan on 2019/8/13.
 */
object DeviceUtil {

    /**
     * 是否有刘海屏
     *
     * @return
     */
    fun hasNotchInScreen(activity: Activity): Boolean {

        //fixme 不知道为何获取rootWindowInsets为空

        // android  P 以上有标准 API 来判断是否有刘海屏
//        if (VERSION.SDK_INT >= VERSION_CODES.P) {
//            val decorView = activity.window.decorView
//            val rootWindowInsets = decorView.rootWindowInsets
//            return if (rootWindowInsets != null) {
//                val displayCutout = rootWindowInsets.displayCutout
//                displayCutout != null
//            } else {
//                false
//            }
//        } else {
            // 通过其他方式判断是否有刘海屏  目前官方提供有开发文档的就 小米，vivo，华为（荣耀），oppo
            val manufacturer = Build.MANUFACTURER
            return when {
                manufacturer.isNullOrEmpty() -> false
                manufacturer.equals("HUAWEI", ignoreCase = true) -> hasNotchHw(activity)
                manufacturer.equals("xiaomi", ignoreCase = true) -> hasNotchXiaoMi()
                manufacturer.equals("oppo", ignoreCase = true) -> hasNotchOPPO(activity)
                manufacturer.equals("vivo", ignoreCase = true) -> hasNotchVIVO()
                else -> false
            }
//        }
//        return false
    }

    /**
     * 判断vivo是否有刘海屏
     * https://swsdl.vivo.com.cn/appstore/developer/uploadfile/20180328/20180328152252602.pdf
     *
     * @return
     */
    private fun hasNotchVIVO(): Boolean {
        return try {
            val c = Class.forName("android.util.FtFeature")
            val get = c.getMethod("isFeatureSupport", Int::class.javaPrimitiveType)
            get.invoke(c, 0x20) as Boolean
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 判断oppo是否有刘海屏
     * https://open.oppomobile.com/wiki/doc#id=10159
     *
     * @param activity
     * @return
     */
    private fun hasNotchOPPO(activity: Activity): Boolean {
        return activity.packageManager.hasSystemFeature("com.oppo.feature.screen.heteromorphism")
    }

    /**
     * 判断xiaomi是否有刘海屏
     * https://dev.mi.com/console/doc/detail?pId=1293
     *
     * @return
     */
    private fun hasNotchXiaoMi(): Boolean {
        return try {
            val c = Class.forName("android.os.SystemProperties")
            val get = c.getMethod("getInt", String::class.java, Int::class.javaPrimitiveType)
            get.invoke(c, "ro.miui.notch", 1) as Int == 1
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 判断华为是否有刘海屏
     * https://devcenter-test.huawei.com/consumer/cn/devservice/doc/50114
     *
     * @param activity
     * @return
     */
    private fun hasNotchHw(activity: Activity): Boolean {
        return try {
            val cl = activity.classLoader
            val hwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val get = hwNotchSizeUtil.getMethod("hasNotchInScreen")
            get.invoke(hwNotchSizeUtil) as Boolean
        } catch (e: Exception) {
            false
        }
    }
}