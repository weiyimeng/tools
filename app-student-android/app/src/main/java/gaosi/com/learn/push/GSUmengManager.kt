package gaosi.com.learn.push

import android.app.Application
import android.content.Context
import android.os.Handler
import android.widget.Toast
import com.gsbaselib.base.log.LogUtil

import com.gsbaselib.utils.Rom
import com.gsbaselib.utils.SharedPreferenceUtil
import com.gstudentlib.base.STBaseConfigManager
import com.gstudentlib.base.STBaseConstants
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.IUmengRegisterCallback
import com.umeng.message.PushAgent
import com.umeng.message.UTrack
import com.umeng.message.UmengMessageHandler
import com.umeng.message.entity.UMessage
import gaosi.com.learn.application.WeexApplication
import org.android.agoo.huawei.HuaWeiRegister
import org.android.agoo.xiaomi.MiPushRegistar

object GSUmengManager {

    private const val UMENG_APP_ID = "5af5117ba40fa35ead0000fa"
    private const val UMENG_APP_SECRET = "f8c686990d5543ce075a01eb4525dcc3"
    private const val MI_APP_ID = "2882303761517573962"
    private const val MI_APP_KEY = "5971757373962"

    fun initUmeng(application: Application) {
        UMConfigure.init(application, UMENG_APP_ID,
                STBaseConstants.channel, UMConfigure.DEVICE_TYPE_PHONE, UMENG_APP_SECRET)
        //开发环境不收集错误信息
        MobclickAgent.setCatchUncaughtExceptions(STBaseConfigManager.INSTANCE.isRelease)
        registerUmengPush(application)
    }

    private fun registerUmengPush(application: Application) {
        //注册推送服务，每次调用register方法都会回调该接口
        val pushAgent = PushAgent.getInstance(application)
        pushAgent.register(object : IUmengRegisterCallback {
            override fun onSuccess(deviceToken: String) {
                //注册成功会返回device token
                LogUtil.e("deviceToken = $deviceToken")
                SharedPreferenceUtil.setStringDataIntoSP("userInfo", "deviceToken", deviceToken)
            }

            override fun onFailure(s: String, s1: String) {
                LogUtil.e("binder umeng error = $s, $s1")
            }
        })

        if (Rom.isMiui()) {
            LogUtil.d("小米手机")
            MiPushRegistar.register(application, MI_APP_ID, MI_APP_KEY)
        }
        if (Rom.isEmui()) {
            LogUtil.d("华为手机")
            HuaWeiRegister.register(application)
        }
        if(Rom.isFlyme()) {
            LogUtil.d("魅族手机")
            //fixme 为啥没注册魅族推送
            //MeizuRegister.register(WeexApplication.getApplication(), "1004939", "316307ba0193489e8d8685b1613e4946");
        }
        pushAgent.messageHandler = createUmengMessageHandler()
        pushAgent.notificationClickHandler = GSUmengNotificationClickHandler(application)
    }

    /**
     * 用户自定义推送消息点击事件
     *
     * @return
     */
    private fun createUmengMessageHandler(): UmengMessageHandler {
        return object : UmengMessageHandler() {
            /**
             * 参考集成文档的1.6.3
             * http://dev.umeng.com/push/android/integration#1_6_3
             */
            override fun dealWithCustomMessage(context: Context?, msg: UMessage?) {
                Handler().post {
                    // 对自定义消息的处理方式，点击或者忽略
                    UTrack.getInstance(WeexApplication.getApplication()).trackMsgClick(msg)
                    Toast.makeText(context, msg?.custom, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
