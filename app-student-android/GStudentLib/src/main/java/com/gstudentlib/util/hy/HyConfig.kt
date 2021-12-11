package com.gstudentlib.util.hy

import android.text.TextUtils
import com.gsbaselib.base.GSBaseApplication
import com.gsbaselib.base.bean.BaseData
import com.gsbaselib.base.log.LogUtil
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.FileDownloadCallback
import com.gsbaselib.net.callback.GSHttpResponse
import com.gsbaselib.net.callback.GSJsonCallback
import com.gsbaselib.utils.FileUtil
import com.gsbaselib.utils.SharedPreferenceUtil
import com.gsbaselib.utils.ZipUtil
import com.gstudentlib.GSAPI
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.event.UpdateHyResourceEvent
import com.lzy.okgo.model.Response
import java.io.File
import java.util.HashMap
import java.util.concurrent.ArrayBlockingQueue

/**
 * 作者：created by 逢二进一 on 2019/9/11 18:27
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
object HyConfig {

    private const val HY_CONFIG = "HY_CONFIG"
    private const val HY_VERSION = "HY_VERSION"

    private var hyVersion: String? = null
    private var hyResourcePath: String? = null
    var takeSize = 0

    //下载失败
    var DOWN_ERROR = -1
    //下载成功
    var DOWN_SUCCESS = 1
    //解压失败
    var ZIP_ERROR = -2
    //解压成功
    var ZIP_SUCCESS = 2
    //暂无状态
    var NONE = 0
    //建立阻塞队列
    private val arrayBlockingQueue = ArrayBlockingQueue<UpdateHyResourceEvent>(3)

    /**
     * 获取本地图片所在的文件夹路径 /data/data/gaosi.com.learn/files/images
     * @return 本地图片所在的文件夹路径
     */
    val hyResourceImagePath: String
        get() {
            val path = hyResourcePath + File.separator + "images"
            val file = File(path)
            if (!file.exists()) {
                try {
                    file.mkdirs()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            return path
        }

    fun init() {
        hyResourcePath = FileUtil.getFilesDir(GSBaseApplication.getApplication())
        hyVersion = SharedPreferenceUtil.getStringValueFromSP(HY_CONFIG, HY_VERSION, "0")
        arrayBlockingQueue.clear()
        STBaseConstants.isResourceUpdate = false
        STBaseConstants.isResourceUpdateSuccess = true
        takeSize = 3
    }

    fun getHyResourcePath(files: String): String {
        val path = hyResourcePath + File.separator + files
        val file = File(path)
        if (!file.exists()) {
            try {
                file.mkdirs()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return path
    }

    fun updateHyResource() {
        val params = HashMap<String, String>()
        params["version"] = hyVersion ?: ""
        params["isForce"] = "0"
        GSRequest.startRequest(GSAPI.hyResourceUpdate, params, object : GSJsonCallback<HyResourceData>() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<HyResourceData>) {
                if (result.isSuccess && result.body != null && result.body.isUpdate == 1 && !TextUtils.isEmpty(result.body.currentVersion)) {
                    //需要更新
                    hyVersion = result.body.currentVersion
                    STBaseConstants.isResourceUpdate = true
                    STBaseConstants.isResourceUpdateSuccess = false
                    putEvent(true, false, result.body.zipUrl, NONE)
                } else {
                    STBaseConstants.isResourceUpdate = false
                    putEvent(false, false, null, NONE)
                }
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                STBaseConstants.isResourceUpdate = false
                putEvent(false, false, null, NONE)
            }
        })
    }

    /**
     * 堵塞队列放入
     * @param event
     */
    @Synchronized
    private fun putEvent(event: UpdateHyResourceEvent) {
        try {
            arrayBlockingQueue.put(event)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    @Synchronized
    fun putEvent(shouldUpdate: Boolean, finishUpdate: Boolean, zipUrl: String?, updateStatus: Int) {
        val event = UpdateHyResourceEvent()
        event.finishUpdate = finishUpdate
        event.shouldUpdate = shouldUpdate
        event.zipUrl = zipUrl
        event.updateStatus = updateStatus
        putEvent(event)
    }

    /**
     * 堵塞队列取出
     */
    fun takeEvent(): UpdateHyResourceEvent? {
        try {
            return arrayBlockingQueue.take()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return null
    }

    fun downloadHyResource(url: String?) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        LogUtil.w("开始下载$url")
        showProgress()//显示进度条
        GSRequest.download(url, "update.zip", object : FileDownloadCallback() {
            override fun onDownloadProcess(process: Float) {
                LogUtil.e("hy download process = $process")
            }

            override fun onResponseSuccess(response: Response<File>?, code: Int, file: File?) {
                LogUtil.w("下载完成")
                putEvent(true, false, null, DOWN_SUCCESS)
                file?.absolutePath?.let { unZip(it) }
            }

            override fun onResponseError(response: Response<File>?, code: Int, message: String?) {
                LogUtil.w("下载失败$message")
                putEvent(true, false, null, DOWN_ERROR)
                hideProgresss()
            }
        })
    }

    /**
     * 解压下载包
     * @param path
     */
    private fun unZip(path: String) {
        Thread(Runnable {
            if (ZipUtil.upZipFile(path, hyResourceImagePath)) {
                LogUtil.w("onUnZipFinished")
                SharedPreferenceUtil.setStringDataIntoSP(HY_CONFIG, HY_VERSION, hyVersion)
                val file = File(path)
                if (file.canRead()) {
                    file.delete()
                }
                STBaseConstants.isResourceUpdateSuccess = true
                putEvent(true, true, null, ZIP_SUCCESS)
                hideProgresss()
            } else {
                putEvent(true, true, null, ZIP_ERROR)
                hideProgresss()
            }
        }).start()
    }

    /**
     * 展示下载进度条
     */
    private fun showProgress() {

    }

    /**
     * 隐藏下载进度条
     */
    fun hideProgresss() {

    }

    private class HyResourceData : BaseData() {
        var tag: String? = ""
        var date: String? = ""
        var name: String? = ""
        var currentVersion: String? = ""
        var isUpdate: Int = 0
        var zipUrl: String? = ""
        var version: String? = ""
    }

}