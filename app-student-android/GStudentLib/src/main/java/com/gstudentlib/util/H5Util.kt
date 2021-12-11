package com.gstudentlib.util

import android.os.Build
import android.text.TextUtils
import com.gsbaselib.InitBaseLib
import com.gsbaselib.utils.SharedPreferenceUtil
import com.gsbaselib.utils.ViewUtil
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.util.hy.HyConfig
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * 作者：created by 逢二进一 on 2019/9/16 10:36
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
object H5Util {

    /**
     * 将Url拷贝到文件中
     * copy h5需要的信息到文件中
     */
    fun copyToFile(inputStream: InputStream, destFile: File, url: String, pageName: String, commonfile: String): Boolean {
        try {
            if (destFile.exists()) {
                destFile.delete()
            }
            val out = FileOutputStream(destFile)
            try {
                var buffer = ByteArray(inputStream.available())
                inputStream.read(buffer)
                var s = String(buffer)
                s = s.replace("gsUserInfo", STBaseConstants.UserInfo)
                if (STBaseConstants.Token == null) {
                    STBaseConstants.Token = ""
                }
                s = s.replace("gsToken", STBaseConstants.Token)
                if (s.contains("gsNotHeight")) {
                    var height = "20"
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        height = (ViewUtil.getStatusBarHeight() / 2).toString()
                    }
                    s = s.replace("gsNotHeight", "0")
                }
                if (s.contains("commonsrc")) {//// TODO: 2017/12/28 表示包含Vendor.web..js
                    s = s.replace("commonsrc", commonfile)
                }

                if (s.contains("gsReleaseType")) {
                    s = s.replace("gsReleaseType", InitBaseLib.getInstance().configManager.h5EnvironmentType)
                }

                if (s.contains("gsH5ResourceDir")) {
                    s = s.replace("gsH5ResourceDir", "file://" + HyConfig.hyResourceImagePath + File.separator)
                }

                if (s.contains("gsRootUrl") && InitBaseLib.getInstance().configManager.useOnlineH5) {
                    var debugH5Url = SharedPreferenceUtil.getStringValueFromSP("userInfo", "h5Ip", "")
                    debugH5Url = if (TextUtils.isEmpty(debugH5Url)) InitBaseLib.getInstance().configManager.h5ServerUrl else debugH5Url
                    s = s.replace("gsRootUrl", debugH5Url)
                }

                if (s.contains("gaosisrc")) {
                    s = s.replace("gaosisrc", url)
                    if (s.contains("gsPageName")) {
                        s = s.replace("gsPageName", pageName)
                    }
                    buffer = s.toByteArray()
                }
                out.write(buffer, 0, buffer.size)
            } finally {
                out.flush()
                try {
                    out.fd.sync()
                } catch (e: IOException) {
                }

                out.close()
            }
            return true
        } catch (e: IOException) {
            return false
        }

    }

}