package gaosi.com.learn.push

import android.content.Intent
import android.os.Bundle
import com.alibaba.fastjson.JSON
import com.gsbaselib.utils.LOGGER
import com.umeng.message.UmengNotifyClickActivity
import com.umeng.message.entity.UMessage
import gaosi.com.learn.R
import gaosi.com.learn.studentapp.main.MainActivity
import org.json.JSONObject

/**
 * 友盟推送的中间页面，用于接收离线消息并跳转到指定界面
 *
 * @author pingfu
 */
class GSUmengPushActivity : UmengNotifyClickActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_umeng_push)
    }

    override fun onMessage(intent: Intent?) {
        super.onMessage(intent)
        var message = intent?.getStringExtra("body")

        try {
            val uMessage = UMessage(JSONObject(message))
            uMessage.message_id = intent?.getStringExtra("id")
            uMessage.task_id = intent?.getStringExtra("task_id")
            message = ""
            if (uMessage.custom != null) {
                message = uMessage.custom
            } else if (uMessage.extra != null) {
                message = uMessage.extra["messageInfo"]
            }
            val event = JSON.parseObject(message, PushEvent::class.java)
            val mainIntent = Intent(this, MainActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable(PushEventDispatcher.PUSH_EVENT, event)
            mainIntent.putExtras(bundle)
            startActivity(mainIntent)
            finish()
        } catch (e: Exception) {
            LOGGER.log(e)
        }
    }
}
