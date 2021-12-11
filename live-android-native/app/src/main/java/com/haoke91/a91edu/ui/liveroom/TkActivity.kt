package com.haoke91.a91edu.ui.liveroom

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.blankj.utilcode.util.ToastUtils
import com.eduhdsdk.tools.TKCallBack
import com.eduhdsdk.ui.ChatView
import com.eduhdsdk.ui.TKPlayer
import com.google.gson.JsonObject
import com.haoke91.a91edu.CacheData
import com.haoke91.a91edu.R
import com.haoke91.a91edu.presenter.player.PlayerPresenter
import com.haoke91.a91edu.ui.BaseActivity
import com.haoke91.a91edu.ui.liveroom.chat.MessageBuilder
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.a91edu.utils.share.UMengAnalytics
import com.haoke91.a91edu.view.PlayerView
import com.haoke91.a91edu.widget.dialog.DialogUtil
import com.haoke91.a91edu.widget.gift.GiftModel
import com.haoke91.a91edu.widget.gift.LiveGiftLayout
import com.haoke91.baselibrary.utils.ACallBack
import com.haoke91.im.mqtt.IMManager
import com.haoke91.im.mqtt.LogU
import com.haoke91.im.mqtt.entities.Constant
import com.haoke91.im.mqtt.entities.Message
import com.haoke91.im.mqtt.entities.User
import kotlinx.android.synthetic.main.activity_tk.*
import java.util.HashMap

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/11/14 17:50
 */
class TkActivity : BaseActivity(), PlayerView {
    private lateinit var mPlayerPresenter: PlayerPresenter
    private var gift_layout: LiveGiftLayout? = null
    override fun getLayout(): Int {
        return R.layout.activity_tk
    }
    
    override fun initialize() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        mPlayerPresenter = PlayerPresenter(this)
        tkPlayer.create()
        if (CacheData.isInitIm)
            mPlayerPresenter.connect()
        mPlayerPresenter.fillBoardView(fl_dialogParent)
        gift_layout = findViewById(R.id.gift_layout)
        
        tkPlayer.setChatViewCallBack(object : ChatView.ChatViewClickListener {
            override fun onSendFlower(view: View?) {
                var msg = MessageBuilder.createFlowerMessage()
                if (mPlayerPresenter.sendFlower(msg)) {
                    val giftModel = GiftModel(R.drawable.ic_flower, "花", UserManager.getInstance().loginUser.name, 1, UserManager.getInstance().loginUser.smallHeadimg)
                    gift_layout?.addGift(giftModel)
                }
            }
            
            override fun onSendLike(view: View?) {
                var msg = MessageBuilder.createPriseMessage()
                if (mPlayerPresenter.sendLike(msg)) {
                    val giftModel = GiftModel(R.drawable.ic_prise, "赞", UserManager.getInstance().loginUser.name, 1, UserManager.getInstance().loginUser.smallHeadimg)
                    gift_layout?.addGift(giftModel)
                    //                    messageList.addMessage(MessageBuilder.createPriseMessage())
                }
            }
        })
        tkPlayer.updateToolBar(PlayerPresenter.topicName, IMManager.instance.getSessionUser()?.prop?.partGold ?: 0, IMManager.instance.getSessionUser()?.prop?.partProgress ?: 0)
        tkPlayer.setTkCallBack(object : TKCallBack {
            override fun call(flag: Int) {
                if (flag == TKCallBack.DESTORY_PLAYER) {
                    mPlayerPresenter.logout()
                    finish()
                } else if (flag == TKCallBack.ONROOMJOIN && CacheData.isFirstEnter) {
                    CacheData.isFirstEnter = false
                    val map = HashMap<String, String>()
                    map.put("duration", (System.currentTimeMillis() - CacheData.CLICK_TIME).toString() + "")
                    UMengAnalytics.onEvent(this@TkActivity, UMengAnalytics.DURATION_BEFORE_GETSTREAM_TALKLIVE, map)
                }
            }
        })
    }
    
    override fun onStart() {
        super.onStart()
        tkPlayer.startPlay()
        LogU.log("onStart")
    }
    
    override fun onResume() {
        super.onResume()
        tkPlayer.resumePlay()
        mPlayerPresenter.onResume()
        //        DialogUtil.getInstance().showAnswerResult(this,1,1,true)
        //        tkPlayer.postDelayed({
        //            var msg=Message(Constant.MessageType.COMMAND,Constant.MessageFlag.CHANGELIVESTATUS,Constant.Role.STUDENT,"55555","ALL",User())
        //            var c=JsonObject();
        //            c.addProperty("value","end")
        //            msg.content=c
        //            mPlayerPresenter.onCommandMessage(msg)
        //        },5000)
//        DialogUtil.getInstance().showTimer(fl_dialogParent, 60, {
//            ToastUtils.showShort("1223")
//        })
    }
    
    override fun onPause() {
        super.onPause()
        tkPlayer.onPause()
        LogU.log("onPause")
    }
    
    override fun onStop() {
        tkPlayer.stopPlay()
        mPlayerPresenter.onStop()
        LogU.log("onStop")
        super.onStop()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        LogU.log("onDestroy")
        mPlayerPresenter.onDestory()
    }
    
    override fun onBackPressed() {
        tkPlayer.finishPlay()
    }
    
    override fun onConnected(code: Int) {
        if (code == 0) {
            mPlayerPresenter.login()
            mPlayerPresenter.onLineTime() //没五分钟发一次
        }
    }
    
    override fun onTextMessage(msg: Message?) {
    }
    
    override fun onCustomMessage(msg: Message?) {
    }
    
    override fun onUserLogin(user: User?) {
    }
    
    override fun onUserLogout(user: User?) {
    }
    
    override fun onGetHistory(messages: MutableList<Message>?) {
    }
    
    override fun onChangeUserProp(gold: Int, progress: Int) {
        tkPlayer.updateToolBar(PlayerPresenter.topicName, gold, progress)
    }
    
    override fun onLiveStatus(status: String?) {
        when (status) {
            "start" -> tkPlayer.startPlay()
            "suspend" -> tkPlayer.onPause()
            "end" -> {
                tkPlayer.stopPlay()
                //                })
            }
        }
    }
    
    override fun onBarrageStatus(status: String?) {
        if ("on" == status?.toLowerCase()) {
            LogU.log("弹幕开启") //暂时不实现
        }
    }
    
    override fun onForbidden(isForbidden: Boolean) {
        tkPlayer.isForbidden(isForbidden)
    }
    
    override fun onWithdrewText(id: String?) {
    }
    
    override fun onKickOut() {
        mPlayerPresenter.logout()
        tkPlayer.finishPlay()
    }
    
    companion object {
        fun stat(context: Context) {
            var intent = Intent(context, TkActivity::class.java)
            context.startActivity(intent)
        }
    }
    
    override fun onEnablePlayAudio(): Boolean {
        return true
    }
}
