package gaosi.com.learn.studentapp.aiscene.core

import android.content.Context
import com.alibaba.fastjson.JSON
import com.chivox.cube.util.FileHelper
import com.gaosi.recorder.RecordConfig
import com.gaosi.recorder.RecordManager
import com.gaosi.recorder.listener.RecordResultListener
import com.gaosi.recorder.listener.RecordSoundSizeListener
import com.gsbaselib.base.log.LogUtil
import com.gstudentlib.base.STBaseConstants
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import gaosi.com.learn.studentapp.aiscene.AiScene
import gaosi.com.learn.studentapp.aiscene.core.AiSceneCoreService.Holder.AI_SCENE_URL
import java.io.File
import java.util.*
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.collections.HashMap


/**
 * 作者：created by 逢二进一 on 2019/8/5 14:09
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
class AiSceneCoreService: RecordResultListener, RecordSoundSizeListener {

    private var mContext: Context? = null
    private var mAiSceneCoreLaunchParam: AiSceneCoreLaunchParam? = null
    private var mRecordStatus = AiScene.RECORD_STATUS.NONE
    private var mOnLaunchListener: OnLaunchListener? = null
    private var mRecordStartTime: Long = 0

    private object Holder {
        val INSTANCE = AiSceneCoreService()
        const val AI_SCENE_URL = "https://ai.aixuexi.com/lat/"
    }
    companion object{
        val instance: AiSceneCoreService by lazy { Holder.INSTANCE }
    }

    fun init(baseContext: Context) {
        this.mContext = baseContext
        RecordManager.instance.changeFormat(RecordConfig.RecordFormat.WAV)
        RecordManager.instance.changeRecordDir(FileHelper.getFilesDir(this.mContext).absolutePath + "/Records/")
    }

    /**
     * 启动录音评测
     */
    fun recordStart(aiSceneCoreLaunchParam: AiSceneCoreLaunchParam , onLaunchListener: OnLaunchListener) {
        this.mOnLaunchListener = onLaunchListener
        this.mAiSceneCoreLaunchParam = aiSceneCoreLaunchParam
        if(mRecordStatus == AiScene.RECORD_STATUS.NONE) {
            this.mOnLaunchListener?.onError(-1 , "mRecordStatus == AiScene.RECORD_STATUS.NONE")
            return
        }
        if(mRecordStatus == AiScene.RECORD_STATUS.RECORDING) {
            this.mOnLaunchListener?.onError(-1 , "mRecordStatus == AiScene.RECORD_STATUS.RECORDING")
            return
        }
        if(mRecordStatus == AiScene.RECORD_STATUS.TESTING) {
            this.mOnLaunchListener?.onError(-1 , "mRecordStatus == AiScene.RECORD_STATUS.TESTING")
            return
        }
        mRecordStatus = AiScene.RECORD_STATUS.RECORDING
        RecordManager.instance.setRecordResultListener(this)
        if(this.mAiSceneCoreLaunchParam?.isSoundIntensityEnable() == true) {
            RecordManager.instance.setRecordSoundSizeListener(this)
        }else {
            RecordManager.instance.setRecordSoundSizeListener(null)
        }
        mRecordStartTime = System.currentTimeMillis()
        RecordManager.instance.start()
    }

    /**
     * 结束评测
     */
    fun recordStop(): Int {
        return if(mRecordStatus == AiScene.RECORD_STATUS.RECORDING) {
            RecordManager.instance.stop()
            mRecordStatus = AiScene.RECORD_STATUS.TESTING
            1
        }else {
            -1
        }
    }

    override fun onResult(result: File?) {
        LogUtil.d("文件大小：" + result?.length())
        if(result == null
                || !result?.exists()
                || !result?.isFile) {
            this.mRecordStatus = AiScene.RECORD_STATUS.STOPED
            this.mOnLaunchListener?.onError(-1 , "文件等于null")
            return
        }
        if(this.mAiSceneCoreLaunchParam != null && this.mAiSceneCoreLaunchParam?.getBranchs() != null) {
            this.mRecordStatus = AiScene.RECORD_STATUS.TESTING
            this.mOnLaunchListener?.onBeforeLaunch(System.currentTimeMillis() - mRecordStartTime)
            val params = HashMap<String, String>()
            params["scenesId"] = this.mAiSceneCoreLaunchParam?.getScenesId()?:""
            params["branches"] = JSON.toJSONString(this.mAiSceneCoreLaunchParam?.getBranchs())
            params["token"] = STBaseConstants.Token
            val timestamp = System.currentTimeMillis()
            params["timestamp"] = timestamp.toString()
            params["sign"] = createSign(STBaseConstants.Token , timestamp.toString() , "3520382f0b4c0923rec52tdc267f6a1k")?:""
            OkGo.post<String>(AI_SCENE_URL)
                    .tag(this)
                    .params(params)
                    .isMultipart(true)
                    .params("audio" , result).execute(object : StringCallback() {

                        override fun onSuccess(response: com.lzy.okgo.model.Response<String>?) {
                            if(response?.body() != null) {
                                mOnLaunchListener?.onAfterLaunch(1 , response?.body().toString() , result)
                            }else {
                                mOnLaunchListener?.onAfterLaunch(-1 , "" , result)
                            }
                            mRecordStatus = AiScene.RECORD_STATUS.STOPED
                        }

                        override fun onError(response: com.lzy.okgo.model.Response<String>?) {
                            super.onError(response)
                            mOnLaunchListener?.onAfterLaunch(-1 , response?.message()?:"net error" , result)
                            mRecordStatus = AiScene.RECORD_STATUS.STOPED
                        }
                    })
        }else {
            this.mRecordStatus = AiScene.RECORD_STATUS.STOPED
            this.mOnLaunchListener?.onError(-1 , "this.mAiSceneCoreLaunchParam != null")
        }
    }

    override fun onSoundSize(soundSize: Int) {
        this.mOnLaunchListener?.onRealTimeVolume(soundSize)
    }

    /**
     * 获取当前的评测状态
     */
    fun getRecordStatus(): Int {
        return mRecordStatus
    }

    /**
     * 更新当前的评测状态
     */
    fun updateStatus(recordStatus: Int) {
        this.mRecordStatus = recordStatus
    }

    /**
     * 释放评测资源
     */
    fun release(){
        this.mContext = null
        this.mAiSceneCoreLaunchParam = null
        this.mOnLaunchListener = null
        this.mRecordStatus = AiScene.RECORD_STATUS.NONE
        this.recordStop()
    }

    /**
     * 创建签名
     *
     * @param params   参数信息
     * @param signKey  签名Key
     * @return 签名字符串
     */
    fun createSign(params: HashMap<String , String> , signKey: String): String? {
        var sign: String? = null
        val toSign = StringBuilder()
        val sortedMap = TreeMap(params)
        for(key in sortedMap.keys) {
            if ("sign" != key) {
                val value = params[key]
                toSign.append(key).append("=").append(value).append("&")
            }
        }
        toSign.append("key=").append(signKey)
        sign = getMD5(toSign.toString()).toUpperCase()
        return sign
    }

    /**
     * 创建签名
     *
     * @param token  token信息
     * @param timestamp  时间戳
     * @param signKey 签名Key
     * @return 签名字符串
     */
    fun createSign(token: String , timestamp: String, signKey: String): String? {
        var sign: String? = null
        val toSign = StringBuilder()
        toSign.append("token").append("=").append(token).append("&")
                .append("timestamp").append("=").append(timestamp).append("&")
                .append("key=").append(signKey)
        LogUtil.d("createSign: $toSign")
        sign = getMD5(toSign.toString()).toUpperCase()
        LogUtil.d("createSign: $sign")
        return sign
    }

    /**
     *
     * @param str  要签名的字符串
     * @return 签名字符串
     */
    private fun getMD5(str: String): String {
        var secretBytes: ByteArray? = null
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    str.toByteArray())
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("没有这个md5算法！")
        }
        var md5code = BigInteger(1, secretBytes).toString(16)
        for (i in 0 until 32 - md5code.length) {
            md5code = "0$md5code"
        }
        return md5code
    }

}