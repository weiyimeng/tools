package gaosi.com.learn.application

import android.app.Application
import android.os.Process
import android.text.TextUtils
import android.util.Log
import com.chuanglan.shanyan_sdk.OneKeyLoginManager
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.gaosi.recorder.RecordManager
import com.gaosiedu.gsl.gslsaascore.GsLiveManager
import com.gsbaselib.InitBaseLib
import com.gsbaselib.base.GSBaseApplication
import com.gsbaselib.base.GSBaseConstants
import com.gsbaselib.base.log.LogUtil
import com.gstudentlib.SDK
import com.gstudentlib.SDKConfig
import com.gstudentlib.StatisticsDictionary
import com.gstudentlib.base.STBaseConstants
import com.lzy.okgo.request.base.Request
import io.fabric.sdk.android.Fabric

/**
 * 作者：created by 逢二进一 on 2019/10/12 16:43
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
object AppSDKConfig: SDK {

    //闪验app ID/SECRET
    public const val SHANYAN_APP_ID = "ZrmASyEH"
    public const val SHANYAN_APP_SECRET = "ufXzJf7j"

    override fun getBaseUrl(): String {
        return InitBaseLib.getInstance().configManager.baseUrl
    }

    override fun getVersion(): String {
        return "1.0.0"
    }

    override fun init(application: Application) {
        GsLiveManager.getInstance().init(application, "1000008")
        GsLiveManager.getInstance().initConfig("https://live-web.aixuexi.com/uapi")
        GsLiveManager.getInstance().setLogEnable(STBaseConstants.isDebug)
        object : Thread() {
            override fun run() {
                super.run()
                //设置线程优先级,不与主线程抢资源
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND)
                //闪验SDK初始化
                OneKeyLoginManager.getInstance().init(GSBaseApplication.getApplication(), SHANYAN_APP_ID) { code, result ->
                    LogUtil.i("闪验SDK 初始化: code==$code   result==$result")
                }
                RecordManager.instance.init(GSBaseApplication.getApplication(), STBaseConstants.isDebug)
                if (!STBaseConstants.isDebug) {
                    //只在release版本才统计crash,防止monkey出现错误统计
                    if(Android_ID_Utils.isFeatures() || Android_ID_Utils.checkIsNotRealPhone() || Android_ID_Utils.checkPipes()){
                        Fabric.with(GSBaseApplication.getApplication(), Answers())
                        Log.i("callback" , "虚拟机")
                    }else {
                        Fabric.with(GSBaseApplication.getApplication(), Crashlytics(), Answers())
                        Log.i("callback" , "非虚拟机")
                    }
                } else {
                    Fabric.with(GSBaseApplication.getApplication(), Answers())
                }
                //预加载 StatisticsDictionary
                StatisticsDictionary.init()
            }
        }.start()
    }

    override fun configSdkName(): String {
        return "app"
    }

    override fun configRequestHeader(request: Request<*, out Request<*, *>>) {
        //添加请求头部
        for(header in getHeaders().entries) {
            request.headers(header.key , header.value)
        }
    }

    override fun getApis(): ArrayList<String> {
        return arrayListOf()
    }

    override fun getHeaders(): Map<String, String> {
        val map = HashMap<String , String>()
        map["User-Agent"] = "WeAppPlusPlayground/1.0"
        map["appId"] = "" + 1 ////学生端中，所有的业务请求appId=1，更新请求比较特殊，appId=2 这是什么鬼设计
        map["X-Requested-With"] = "X-Requested-With"
        map["Content-Type"] = "application/json;charset=UTF-8"
        map["apiVersion"] = STBaseConstants.apiVersion
        map["appVersionName"] = STBaseConstants.deviceInfoBean.appVersion
        map["x-appVersion"] = STBaseConstants.deviceInfoBean.appVersion
        map["systemType"] = STBaseConstants.deviceInfoBean.systemType
        map["systemVersion"] = "" + STBaseConstants.deviceInfoBean.systemVersion
        map["deviceId"] = STBaseConstants.deviceInfoBean.deviceId
        map["deviceType"] = STBaseConstants.deviceInfoBean.systemType
        map["Api-Request-Time"] = "" + System.currentTimeMillis()
        if (!TextUtils.isEmpty(GSBaseConstants.channel)) {
            map["channel"] = STBaseConstants.channel
        }
        if (!TextUtils.isEmpty(STBaseConstants.Token)) {
            map["token"] = STBaseConstants.Token
            map[SDKConfig.getPassportFlag()?:""] = STBaseConstants.Token
        }
        if (!TextUtils.isEmpty(STBaseConstants.businessUserId)) {
            map["userId"] = STBaseConstants.businessUserId
        }
        return map
    }

}