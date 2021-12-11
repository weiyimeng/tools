package gaosi.com.learn.push

import android.app.Application
import android.content.Context
import com.gsbaselib.base.log.LogUtil
import com.gsbaselib.net.GSRequest

import com.gsbaselib.utils.LOGGER
import com.umeng.message.UmengNotificationClickHandler
import com.umeng.message.entity.UMessage

class GSUmengNotificationClickHandler(private val application: Application) : UmengNotificationClickHandler() {

    override fun dealWithCustomAction(context: Context?, msg: UMessage?) {
        //自定义行为
        //{"appId":5,"content":"就是要测试数据","link":"https://www.baidu.com","title":"this is test message","type":2}
        dealWithUMessage(msg?.custom)
    }

    override fun openActivity(context: Context?, msg: UMessage?) {
        //打开指定页面（Activity）
        dealWithUMessage(msg?.custom)
    }

    //{"appId":5,"content":"就是要测试数据","link":"https://www.baidu.com","title":"this is test message","type":2}
    override fun launchApp(context: Context?, msg: UMessage?) {
        //打开应用
        val extra = msg?.extra
        if (extra != null) {
            val value = extra["messageInfo"]
            dealWithUMessage(value)
        }
    }

    override fun openUrl(context: Context?, msg: UMessage?) {
        //使用系统默认浏览器打开指定网页
        dealWithUMessage(msg?.custom)
    }

    private fun dealWithUMessage(messageStr: String?) {
        if (messageStr == null) {
            return
        }
        try {
            LogUtil.d("messageStr:$messageStr")
            val event = GSRequest.getConverterFactory().StringToObjectConverter(PushEvent::class.java).convert(messageStr) as PushEvent
            PushEventDispatcher.dispatcher(application, event)
        } catch (e: Exception) {
            LOGGER.log(e)
        }
    }
}
