package com.haoke91.a91edu.presenter.player

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Handler
import android.support.constraint.R.attr.content
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.blankj.utilcode.util.ToastUtils
import com.eduhdsdk.ui.MyIm
import com.gaosiedu.Constant
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.haoke91.a91edu.CacheData
import com.haoke91.a91edu.R
import com.haoke91.a91edu.adapter.LiveRoomAdapter
import com.haoke91.a91edu.entities.GetImHistoryResponse
import com.haoke91.a91edu.entities.player.Answer
import com.haoke91.a91edu.entities.player.ListStudent
import com.haoke91.a91edu.entities.player.Student
import com.haoke91.a91edu.model.BaseModel
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.presenter.BasePresenter
import com.haoke91.a91edu.ui.liveroom.BasePlayerActivity
import com.haoke91.a91edu.ui.liveroom.chat.MessageBuilder
import com.haoke91.a91edu.view.PlayerView
import com.haoke91.a91edu.widget.dialog.DialogUtil
import com.haoke91.baselibrary.utils.ICallBack
import com.haoke91.baselibrary.views.MarqueeTextView
import com.haoke91.im.mqtt.IMCallBack
import com.haoke91.im.mqtt.IMManager
import com.haoke91.im.mqtt.LogU
import com.haoke91.im.mqtt.entities.Constant.MessageFlag
import com.haoke91.im.mqtt.entities.Message
import com.haoke91.im.mqtt.entities.Prop
import com.haoke91.im.mqtt.entities.User
import com.haoke91.videolibrary.videoplayer.BaseVideoPlayer
import com.orhanobut.logger.Logger
import com.tmall.ultraviewpager.UltraViewPager
import com.tmall.ultraviewpager.transformer.UltraScaleTransformer
import org.json.JSONObject
import java.util.*

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/11/26 19:41
 */
class PlayerPresenter(view: PlayerView) : BasePresenter<BaseModel, PlayerView>(null, view), IMCallBack {
    private var mMediaPlayer: MediaPlayer? = null //音乐播放器
    private var mImManager: IMManager? = null
    private var mContext: Context = view as Context
    private var mv_topicBoard: ViewGroup? = null
    private var isMusicPlaying = false
    val groupUserCount: IntArray
        get() = IntArray(4)
    
    fun connect() {
        mImManager = IMManager.instance
        //                val config = IMManager.Config()
        //                config.broker = "tcp://post-cn-4590twjcw07.mqtt.aliyuncs.com:1883"
        //                config.accessKey = "LTAIqzljvPL7tP39"
        //                config.secretKey = "iNo9ACtu7Sul33GU6ijJslxqq9AST2"
        //                config.topic = "gsl_mq_test_2018-10-17"
        //                config.groupId = "GID_gsl_mq_tester_001"
        //                config.roomId = "1001"
        //                config.knowledgeId = "9137"
        //                config.subGroup = "1001"
        //                config.userId = "9999"
        //        val config = IMManager.Config()
        //        config.broker = "tcp://post-cn-4590xb58607.mqtt.aliyuncs.com:1883"
        //        config.accessKey = "LTAIUFgt6ij60EoO"
        //        config.secretKey = "5kujyBK8iFwByhMaY8yAf17G0mJ2hj"
        //        config.topic = "91haoke_scc_online_30_LOT"
        //        config.groupId = "GID_91haoke_scc_online_30"
        //        config.roomId = "1001"
        //        config.knowledgeId = "9137"
        //        config.subGroup = "18-q-1886-001"
        //        config.userId = "39944"
        //        config.clientId = "${config.groupId}@@@${config.userId}@${config.roomId}@${config.subGroup}"
        mImManager!!.setCurrentTime(CacheData.getInstance().currentTime)
        if (CacheData.getInstance().config == null) {
            ToastUtils.showShort("im 无法链接")
            return
        }
        mImManager!!.initialize(CacheData.getInstance().config, this)
        lastHistoryTime = 0
    }
    
    fun login() {
        //        val user = User()
        //        val prop = Prop()
        //        prop.headerUrl = "dfjajfj.jpg"
        //        prop.name = "kaiyang"
        //        user.id = "123321343144"
        //        user.role = Role.STUDENT.value
        //        user.prop = prop
        //        user.userId = "2010"
        //        user.loginStatus = LoginStatus.LOGIN.value
        if (CacheData.getInstance().user == null) {
            ToastUtils.showShort("用户信息为空")
            return
        }
        mImManager?.login(CacheData.getInstance().user, CacheData.getInstance().acl)
        //        onLineTime()
        //        initAcl()
        mView.onChangeUserProp(CacheData.getInstance().user.prop?.partGold ?: 0, CacheData.getInstance().user.prop?.partProgress ?: 0)
    }
    
    fun fillBoardView(view: ViewGroup) {
        this.mv_topicBoard = view
    }
    
    /**
     * 根据历史command 初始化权限列表
     */
    fun initAcl() {
        if (!CacheData.isLivingPlay) {
            return
        }
        var authMap = CacheData.getInstance().authMap
        if (authMap != null) {
            //是否开启弹幕
            if (authMap.containsKey("changeBarrageStatus")) {
                mView.onBarrageStatus(authMap["changeBarrageStatus"]!!.value)
            }
            //是否禁言
            if (authMap.containsKey("changeCommonStatus")) {
                if ("off" == authMap["changeCommonStatus"]?.value) { //禁止文本消息
                    val acl = mImManager?.getAcl()
                    mView.onForbidden(true)
                    IMManager.isForbidden = true
                } else { //可文本
                    val acl = mImManager?.getAcl()
                    mView.onForbidden(false)
                    IMManager.isForbidden = false
                }
            }
            if (authMap.containsKey("changeCommonStatus")) {
                if ("off" == authMap["changeCommonStatus"]?.value) { //禁止文本消息
                    val acl = mImManager?.getAcl()
                    mView.onForbidden(true)
                    IMManager.isForbidden = true
                } else { //可文本
                    val acl = mImManager?.getAcl()
                    mView.onForbidden(false)
                    IMManager.isForbidden = false
                }
            }
            //直播状态
            if (authMap.containsKey("changeLiveStatus")) {
                mView.onLiveStatus(authMap["changeLiveStatus"]!!.value)
            }
            //背景音乐是否播放
            if (authMap.containsKey("bgMusic")) {
                if ("play" == authMap["bgMusic"]?.value) {
                    if (mView?.onEnablePlayAudio() == true)
                        playAudio(authMap["bgMusic"]!!.cmdData, false)
                    log("bgmusic==" + mView.onEnablePlayAudio())
                }
            }
            //计时器
            //            if (authMap.containsKey("showTimer")) {
            //                var msg=authMap["showTimer"]
            //                var timeCount = msg?.value
            //                msg?.arriveTime
            //                (mContext as Activity).runOnUiThread {
            //                    DialogUtil.getInstance().showTimer(mv_topicBoard, timeCount?.toLong()?:0, {
            //                        playAudio("music_clock.mp3", true)
            //                    })
            //                }
            //            }
        }
    }
    
    /**
     * 退出
     */
    fun logout() {
        if (mImManager != null && mImManager!!.isConnecting()) {
            mImManager!!.logout()
        }
    }
    
    /**
     * 点赞
     */
    fun sendLike(msg: Message): Boolean {
        if (mImManager == null || !mImManager!!.isConnecting()) {
            ToastUtils.showShort("消息发送失败")
            return false
        }
        val obj = JsonObject()
        //        obj.addProperty("img_url", "")
        return mImManager!!.sendCustomMessageToGroup(msg)
    }
    
    
    /**
     * 送花
     */
    fun sendFlower(msg: Message): Boolean {
        if (mImManager == null || !mImManager!!.isConnecting()) {
            ToastUtils.showShort("消息发送失败")
            return false
        }
        return mImManager!!.sendCustomMessageToGroup(msg)
    }
    
    /**
     * 发送文本消息
     */
    fun sendMessage(msg: Message): Boolean {
        return mImManager?.sendTextMessageToGroup(msg) ?: false
    }
    
    /**
     * 多用于点赞 送花
     */
    fun sendCustomMessage(msg: Message): Boolean {
        if (mImManager == null || !mImManager!!.isConnecting()) {
            LogU.log("聊天连接错误")
            return false
        }
        return mImManager!!.sendCustomMessageToGroup(msg)
    }
    
    private var mTimer: Timer? = null
    /**
     * 定时消息（没5分钟发送一次）
     */
    fun onLineTime() {
        if (mTimer == null) {
            mTimer = Timer()
        }
        mTimer?.schedule(object : TimerTask() {
            override fun run() {
                if (mImManager == null || !mImManager!!.isConnecting()) {
                    LogU.log("聊天连接错误")
                    return
                }
                var msg = MessageBuilder.createCustomMessage(MessageFlag.ONLINETIME)
                val obj = JsonObject()
                obj.addProperty("onLineTime", 5)
                obj.addProperty("userId", mImManager?.getSessionUser()?.userId)
                msg.content = obj
                mImManager?.sendCustomMessageToGroup(msg)
            }
        }, 5 * 60 * 1000, 5 * 60 * 1000)
    }
    
    
    /**
     * 播放音频
     */
    fun playAudio(url: String, isNative: Boolean) {
        try {
            if (mMediaPlayer == null)
                mMediaPlayer = MediaPlayer()
            if (isMusicPlaying) {
                mMediaPlayer!!.reset()
            }
            var u = url
            if (isNative) {
                var fileDescriptor = mContext.assets.openFd(u)
                mMediaPlayer!!.setDataSource(fileDescriptor.fileDescriptor, fileDescriptor.startOffset, fileDescriptor.length)
            } else {
                if (!url.startsWith("http")) u = "http:" + url
                mMediaPlayer!!.setDataSource(u)
            }
            mMediaPlayer?.setOnErrorListener { mp, what, extra ->
                LogU.log("播放失败")
                true
            }
            mMediaPlayer?.setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
                override fun onCompletion(mp: MediaPlayer?) {
                    mMediaPlayer?.stop()
                    mMediaPlayer?.seekTo(0)
                    isMusicPlaying = false
                    LogU.log("播放完成")
                }
                
            })
            mMediaPlayer!!.prepareAsync()
            mMediaPlayer!!.setOnPreparedListener {
                mMediaPlayer!!.start()
                isMusicPlaying = true
            }
        } catch (_e: Exception) {
            _e.printStackTrace()
        }
        
    }
    
    /**
     * 发送题版答案
     */
    private fun sendAnswer(msgId: String?, keyId: Int, type: String, answer: String, consumeTime: String, receiveTime: String) {
        if (mImManager?.isConnecting() == true) {
            var msg = MessageBuilder.createCustomMessage(MessageFlag.ANSWER)
            var content = JsonObject()
            content.addProperty("keyId", keyId)
            content.addProperty("type", type)
            content.addProperty("answer", answer)
            content.addProperty("consumeTime", consumeTime) //回答用时 毫秒
            content.addProperty("receiveTime", receiveTime) //接收题版时间戳
            content.addProperty("msgId", msgId) //消息id
            msg.content = content
            mImManager!!.sendCustomMessageToGroup(msg)
        }
    }
    
    /**
     * 发送教师评价
     */
    public fun sendComment(comments: Array<String>) {
        if (comments != null && comments.isNotEmpty()) {
            if (mImManager?.isConnecting() == true) {
                var msg = MessageBuilder.createCustomMessage(MessageFlag.COMMENT)
                var content = JsonObject()
                content.addProperty("star", comments[0])
                content.addProperty("affect", comments[1])
                content.addProperty("tips", comments[2]) //
                content.addProperty("text", comments[3]) //
                content.addProperty("teacherId", CacheData.getInstance().teacherId)
                content.addProperty("time", mImManager?.getCurrentTime())
                msg.content = content
                mImManager!!.sendCustomMessageToGroup(msg)
            }
        }
    }
    
    fun stopAudio() {
        if (isMusicPlaying) {
            try {
                mMediaPlayer?.stop()
                mMediaPlayer?.seekTo(0)
                mMediaPlayer?.release()
            } catch (e: java.lang.Exception) {
            }
        }
    }
    
    /**
     * 暂停音乐
     */
    fun pauseAudio() {
        if (mMediaPlayer != null && isMusicPlaying) {
            mMediaPlayer?.pause()
        }
    }
    
    /**
     * 点赞
     */
    fun sendPraise() {
        if (mImManager == null || !mImManager!!.isConnecting()) {
            return
        }
        //        mImManager.sendCustomMessageToUser();
        //        Custom cus = new Custom();
        //        cus.like("http://scc.teacherv.top/resources/images/library/like.jpg");
        //        mImManager.sendCustomMessageToGroup(cus, null);
    }
    
    /**
     * 显示公告
     *
     * @param text
     * @param time
     */
    fun showNotice(_context: Context, text: String, time: Long) {
        //        final AlertDialog dialog = new AlertDialog.Builder(_context).create();
        //        val builder = AlertDialog.Builder(_context)
        //        val view = LayoutInflater.from(_context.applicationContext).inflate(com.haoke91.videolibrary.R.layout.dialog_hint, null)
        //        val tv = view.findViewById<TextView>(com.haoke91.videolibrary.R.id.tv_des)
        //        tv.text = text
        //        builder.setView(view)
        //        builder.setCancelable(false)
        //        val dialog = builder.show()
        //        val btnOk = view.findViewById<Button>(com.haoke91.videolibrary.R.id.btnOk)
        //        btnOk.setOnClickListener { dialog!!.dismiss() }
        //        Handler().postDelayed({
        //            if (dialog != null && dialog.isShowing) {
        //                LogU.log("关闭dialog")
        //                dialog.dismiss()
        //            }
        //        }, time*1000)
        var marqueeTextView = (_context as Activity).findViewById<MarqueeTextView>(R.id.marqueeTextView)
        if (marqueeTextView == null)
            return
        var builder = SpannableString("img" + text)
        //        var d = ContextCompat.getDrawable(_context, R.mipmap.icon_alarm)
        //        d!!.setBounds(0, 0, d!!.intrinsicWidth, d!!.intrinsicHeight)
        var bitmap = BitmapFactory.decodeResource(_context.resources, R.mipmap.icon_alarm)
        var imgSpan = MyIm(_context, bitmap)
        builder.setSpan(imgSpan, 0, 3, ImageSpan.ALIGN_BOTTOM)
        marqueeTextView.text = builder
        marqueeTextView.visibility = View.VISIBLE
        marqueeTextView.startScroll()
        Handler().postDelayed({
            marqueeTextView.visibility = View.GONE
        }, 60 * 1000)
    }
    
    /**
     * 弹出问卷调查
     *
     * @param type   题类型
     * @param msgId  题库id
     * @param answer
     * @param timer  minute
     */
    fun showWenjuan(parent: ViewGroup, type: String, msgId: String?, answer: String?, timer: String) {
        if (answer == null) {
            return
        }
        val ans = answer.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (ans == null || ans.size < 1) {
            return
        }
        var t: Long = 0
        try {
            t = java.lang.Long.parseLong(timer) * 60 * 1000
        } catch (e: Exception) {
        }
        
        //没题干 问卷 填空 选择 type wenjuan
        if (msgId != null && msgId.trim { it <= ' ' }.length > 0) { //在题库中提取msgid
        
        }
        //        if (Custom.isTianKong(type)) {
        //            DialogUtil.fillBlank(parent, new DialogUtil.OnItemSelectedListener() {
        //                @Override
        //                public void onSelected(View view) {
        //                    if (view instanceof EditText) {
        //                        Custom cus = new Custom();
        //                        cus.tianKong(((EditText) view).getText().toString().trim(), msgId);
        //                        sendCustomMessage(cus);
        //                    }
        //                }
        //            });
        //        } else if (Custom.isWenJuan(type)) {
        //            DialogUtil.choice(parent, ans, t, new DialogUtil.OnItemSelectedListener() {
        //                @Override
        //                public void onSelected(View view) {
        //                    Custom cus = new Custom();
        //                    if (view instanceof TextView) {
        //                        cus.xuanZe(((TextView) view).getText().toString(), msgId);
        //                    }
        //                    sendCustomMessage(cus);
        //                }
        //            });
        //        } else if (ans.length == 2 && !(answer.contains("A") || answer.contains("1"))) {//判断题
        //            DialogUtil.showJudge(parent, ans[0], ans[1], t / 1000, new DialogUtil.OnItemSelectedListener() {
        //                @Override
        //                public void onSelected(View view) {
        //                    Custom cus = new Custom();
        //                    if (view instanceof TextView) {
        //                        cus.xuanZe(((TextView) view).getText().toString(), msgId);
        //                    }
        //                    sendCustomMessage(cus);
        //                }
        //            });
        //        } else {//选择题
        //            DialogUtil.choice(parent, ans, t, null);
        //        }
    }
    
    /**
     * 更新本人属性信息
     */
    fun updateProp(_prop: Prop, userId: String) {
        //        Map<String, User> userContainer = mImManager.getUserContainer();
        //        if (userId != null) {
        //            for (User user : userContainer.values()) {
        //                if (user.userId.equals(userId)) {
        //                    user.prop = _prop;
        //                }
        //            }
        //        }
    }
    
    /**
     * 发送消息
     *
     * @param txt
     * @param userIds
     */
    fun sendMessage(txt: String, vararg userIds: String) {
        if (mImManager != null && mImManager!!.isConnecting()) {
            var msg = MessageBuilder.createTextMessage(txt)
            mImManager!!.sendTextMessageToGroup(msg)
        }
    }
    
    /**
     * 拉取历史消息
     */
    fun pullHistory(endTime: Long, pageSize: Int, callback: ResponseCallback<GetImHistoryResponse>) {
        val comStr = StringBuffer()
        comStr.append("appId=" + appId)
                .append("&random=" + random)
                .append("&role=" + role)
                .append("&roomId=" + roomId + "")
                .append("&stamp=" + System.currentTimeMillis())
                .append("&userId=" + userId)
                .append("&serverSignKey=" + serverSignKey)
        val appSign = com.haoke91.a91edu.utils.rsa.Md5Utils.MD5_LOWERCASE(comStr.toString())
        val url = Constant.BASEURL_IM + "/imApi/server/pull/msg/common?" + comStr + "&appSign=" + appSign
        
        val hashMap = HashMap<String, Any>()
        hashMap.put("subgroupId", subGroupId)
        hashMap.put("endTime", endTime.toString())
        hashMap.put("pageSize", pageSize.toString())
        Api.getInstance().netPost(url, hashMap, GetImHistoryResponse::class.java, callback, "load common history")
        
    }
    
    /**
     * 拉取角色属性
     *
     * @return
     */
    
    fun getProp(_userId: String): Prop? {
        //        if (mImManager != null && mImManager.isConnecting()) {
        //            return mImManager.getProp(_userId);
        //        }
        return null
    }
    
    fun onDestory() {
        logout()
        if (mImManager != null) {
            mImManager!!.exit()
        }
        stopAudio()
        mTimer?.cancel()
        mTimer = null
        lastHistoryTime = 0
    }
    
    override fun onConnected(code: Int) {
        if (code == 0) {
            //连接成功
            //延时2s 轮询历史命令
            mv_topicBoard?.postDelayed({ initAcl() }, 1500)
        }
        mView.onConnected(code)
    }
    
    override fun onTextMessage(msg: Message) {
        var obj = JSONObject(Gson().toJson(msg.content))
        Logger.e("===onTextMessage==" + obj.optString("text"))
        (mContext as Activity).runOnUiThread {
            mView.onTextMessage(msg)
        }
    }
    
    override fun onImageMessage(msg: Message) {
        Logger.e("OnImage")
    }
    
    override fun onAudioMessage(msg: Message) {
        Logger.e("onAudio")
    }
    
    override fun onVideoMessage(msg: Message) {
        Logger.e("onVideo")
    }
    
    override fun onLoginSuccess(time: Long) {
        LogU.log("登录成功，拉取历史数据")
        pullHistory(time, 20, object : ResponseCallback<GetImHistoryResponse>() {
            override fun onResponse(date: GetImHistoryResponse?, isFromCache: Boolean) {
                val data = date!!.data
                if (data != null && data.size > 0) {
                    lastHistoryTime = data[data.size - 1].time
                }
                mView.onGetHistory(data)
            }
            
            override fun onEmpty(date: GetImHistoryResponse?, isFromCache: Boolean) {
                super.onEmpty(date, isFromCache)
                mView.onGetHistory(null)
            }
            
            override fun onError() {
                super.onError()
                mView.onGetHistory(null)
            }
            
        })
    }
    
    override fun onLogout() {
        Logger.e("logout")
    }
    
    override fun onKickOut() {
        //        ToastUtils.showShort("您已被踢出")
        (mContext as Activity).runOnUiThread {
            mView?.onKickOut()
        }
    }
    
    override fun onLoginError(msg: Message) {
        Logger.e("loginError")
    }
    
    override fun onUserLogin(user: User) {
        Logger.e("onUserLogin")
        mView.onUserLogin(user)
    }
    
    override fun onUserLogout(user: User) {
        Logger.e("onUserLogout")
        mView.onUserLogout(user)
    }
    
    override fun onCustomMessage(msg: Message) {
        if (mImManager?.isInBackground != false) {
            return
        }
        //        var content = msg.content as JSONObject
        //        var content = JSONObject(Gson().toJson(msg.content))
        var content = JSONObject(Gson().toJson(msg.content))
        when (msg.flag) {
            MessageFlag.FLOWER.value -> {
                LogU.log("接收点赞/送花")
                ((mContext as Activity)).runOnUiThread {
                    mView.onTextMessage(msg)
                    //                    DialogUtil.getInstance().choice(mv_topicBoard, arrayListOf(Answer(), Answer()), 10 * 60, {
                    //                    })
                }
            }
            MessageFlag.LIKE.value -> {
                ((mContext as Activity)).runOnUiThread {
                    mView.onTextMessage(msg)
                }
                LogU.log("接收点赞/送花")
                //                (mContext as Activity).runOnUiThread {
                //                    DialogUtil.getInstance().commitEvaluate(mv_topicBoard, (mImManager?.getSessionUser()?.prop?.partGold ?: 0).toString(), (mImManager?.getSessionUser()?.prop?.partProgress ?: 0).toString(), { ss ->
                //                        sendComment(ss)
                //                    })
                //                }
                
            }
            "withdrewText" -> {
                //消息撤回
                var msgId = content.optString("msgId")
                (mContext as Activity).runOnUiThread {
                    mView.onWithdrewText(msgId)
                }
            }
            "showNotice" -> { //显示公告
                var text = content.optString("text")
                var validity_time = content.optLong("validity_time")
                (mContext as Activity).runOnUiThread {
                    showNotice(mContext, text, validity_time)
                }
            }
            "question" -> { //获取老师发送题板
                var time = content.optLong("validity_time")
                var body = content.optString("questionBody")
                var receiveTime = System.currentTimeMillis()
                (mContext as Activity).runOnUiThread {
                    var type = content.optString("type")
                    var keyId = content.optInt("keyId")
                    var msgId = msg.msgId
                    when (type) {
                        "TianKong" -> {
                            DialogUtil.KEYID = keyId
                            DialogUtil.getInstance().lookAndfillBlank(mv_topicBoard, body, time * 60, { s ->
                                var consumeTime = System.currentTimeMillis() - receiveTime
                                sendAnswer(msgId, keyId, "TianKong", s, consumeTime.toString(), receiveTime.toString())
                            })
                        }
                        "XuanZe" -> {
                            DialogUtil.KEYID = keyId
                            var answer = content.optString("answer")
                            var answers = Gson().fromJson<java.util.ArrayList<Answer>>(answer, object : TypeToken<java.util.ArrayList<Answer>>() {}.type)
                            DialogUtil.getInstance().lookAndChoice(mv_topicBoard, time * 60, body, answers, { s ->
                                var consumetime = System.currentTimeMillis() - receiveTime
                                sendAnswer(msgId, keyId, "XuanZe", s, consumetime.toString(), receiveTime.toString())
                            })
                        }
                        "WenJuan" -> {
                            DialogUtil.KEYID = keyId
                            var answer = content.optString("answer")
                            var answers = Gson().fromJson<java.util.ArrayList<Answer>>(answer, object : TypeToken<java.util.ArrayList<Answer>>() {}.type)
                            DialogUtil.getInstance().choice(mv_topicBoard, answers, time * 60, { s ->
                                var consumetime = System.currentTimeMillis() - receiveTime
                                sendAnswer(msgId, keyId, "WenJuan", s, consumetime.toString(), receiveTime.toString())
                            })
                        }
                    }
                }
            }
            "sendGold" -> {
                var userIds = content.optString("userIds")
                var gold = content.optString("gold")
                var total = content.optString("total")
                var isRandom = content.optBoolean("isRandom")
                //                if (userIds.contains(mImManager!!.getSessionUser().userId)) {
                //                ToastUtils.showShort("获得金币 total$total +gold=$gold userids==$userIds")
            }
            "sendProgress" -> { //不需要处理
                var userIds = content.optString("userIds")
                var progress = content.optString("progress")
                var total = content.optString("total")
                var isRandom = content.optBoolean("isRandom", true)
                //                ToastUtils.showShort("获得progress total==$total  progress==$progress  userids== $userIds")
            }
            "barrage" -> { //弹幕消息
                var text = content.optString("text")
                //                ToastUtils.showShort(text)
            }
            "rankList" -> { //榜单列表
                var rankListA = content.optString("rightRatioList") //正确率排行
                var rankListB = content.optString("averageConsumeList") //答题速度排行
                var rankListC = content.optString("advanceRatioList") //进步排行
                var listA: List<Student>
                var listB: List<Student>
                var listC: List<Student>
                var lists = arrayListOf<ListStudent>()
                
                if (!rankListA.isNullOrEmpty()) {
                    listA = Gson().fromJson(rankListA, object : TypeToken<List<Student>>() {}.type)
                    var listStudentA = ListStudent()
                    listStudentA.tag = "rightRatioList" //正确率
                    listStudentA.studentList = listA
                    lists.add(listStudentA)
                }
                if (!rankListB.isNullOrEmpty()) {
                    listB = Gson().fromJson(rankListB, object : TypeToken<List<Student>>() {}.type)
                    var listStudentB = ListStudent()
                    listStudentB.tag = "averageConsumeList" //答题速度
                    listStudentB.studentList = listB
                    lists.add(listStudentB)
                }
                if (!rankListC.isNullOrEmpty()) {
                    listC = Gson().fromJson(rankListC, object : TypeToken<List<Student>>() {}.type)
                    var listStudentC = ListStudent()
                    listStudentC.tag = "advanceRatioList"
                    listStudentC.studentList = listC
                    lists.add(listStudentC)
                }
                (mContext as Activity).runOnUiThread {
                    DialogUtil.getInstance().showRankList(mContext, lists)
                }
            }
            "withdrewText" -> { //撤回文本消息
                var msgId = content.optString("msgId")
                var sendUserId = content.optString("sendUserId")
            }
            "answerCount" -> { //回答问题统计
                var userId = content.optString("userId")
                var keyId = content.optString("keyId")
                var answerFlag = content.optString("answerFlag")
            }
        }
        mView.onCustomMessage(msg)
    }
    
    override fun onCommandMessage(msg: Message) { //命令消息
        if (mImManager?.isInBackground == true) {
            return
        }
        var content = JSONObject(Gson().toJson(msg.content))
        when (msg.flag) {
            "changeBarrageStatus" -> {
                var value = content.optString("value")
                mView.onBarrageStatus(value)
            }
            "changeUserProp" -> {
                var receiveUserId = content.optString("receiveUserId")
                var type = content.optString("flag")
                var isShow = content.optBoolean("is_show", true)
                if (receiveUserId == mImManager?.getSessionUser()?.userId) {
                    var gold = content.optInt("partGold")
                    var progress = content.optInt("partProgress")
                    var prop = mImManager?.getSessionUser()?.prop
                    prop?.partGold = gold
                    prop?.partProgress = progress
                    (mContext as Activity).runOnUiThread {
                        mView.onChangeUserProp(gold, progress)
                    }
                    var getGold = content.optInt("getGold")
                    var getProgress = content.optInt("getProgress")
                    if (!isShow)
                        return
                    (mContext as Activity).runOnUiThread({
                        if ("answer".equals(type, true)) {
                            var isTrue = content.optString("answerFlag")
                            DialogUtil.getInstance().showAnswerResult(mContext, getGold, getProgress, isTrue == "right")
                        } else if ("sendGold".equals(type, true) || "comment".equals(type, true) || "login".equals(type, true)) {
                            DialogUtil.getInstance().showGoldAnim(mContext, getGold)
                        } else {
                            DialogUtil.getInstance().showGoldAnim(mContext, getGold)
                        }
                    })
                }
            }
            "changeGroupProp" -> "改变组属性"
            "changeUserCommonMsgStatus" -> {
                var receiveUserId = content.optString("receiveUserId")
                var value = content.optString("value")
                if (receiveUserId == mImManager?.getSessionUser()?.userId) {
                    mImManager?.getSessionUser()?.prop?.speaking = value
                    (mContext as Activity).runOnUiThread {
                        if ("on" == value) {
                            LogU.log("我被解除禁言了 on")
                            mView.onForbidden(false)
                            IMManager.isForbidden = false
                        } else { //
                            LogU.log("我被禁言了 off")
                            mView.onForbidden(true)
                            IMManager.isForbidden = true
                        }
                    }
                }
            }
            "changeGroupCommonMsgStatus" -> { //组禁言
                var receiveSubGroupId = content.optString("receiveSubgroupId")
                var value = content.optString("value")
                (mContext as Activity).runOnUiThread {
                    if ("off" == value) { //off
                        LogU.log("都被禁言了 off")
                        mView.onForbidden(true)
                        IMManager.isForbidden = true
                    } else {
                        LogU.log("都解除禁言了 on")
                        mView.onForbidden(false)
                        IMManager.isForbidden = false
                    }
                }
            }
            "changeUserCustomMsgStatus" -> {
                var receiveUserId = content.optString("receiveUserId")
                var value = content.optString("value")
                if (receiveUserId == mImManager!!.getSessionUser().userId) {
                    if ("on" == value) {
                        LogU.log("我不能发自定义消息了 on")
                    } else {
                        LogU.log("我能发自定义消息了 off")
                    }
                }
            }
            "changeGroupCustomMsgStatus" -> {
                var receiveSubgroupId = content.optString("receiveSubgroupId")
                var value = content.optString("value")
                if ("on" == value) {
                    LogU.log("都不能发自定义消息了")
                }
            }
            "changeLiveStatus" -> { //改变直播间状态
                var status = content.optString("value")
                (mContext as Activity).runOnUiThread {
                    mView.onLiveStatus(status)
                }
                when (status) {
                    "start" -> stopAudio()
                    "suspend" -> "暂停"
                    "end" -> (mContext as Activity).runOnUiThread {
                        DialogUtil.getInstance().commitEvaluate(mv_topicBoard, (mImManager?.getSessionUser()?.prop?.partGold ?: 0).toString(), (mImManager?.getSessionUser()?.prop?.partProgress ?: 0).toString(), { ss ->
                            sendComment(ss)
                        })
                    }
                }
            }
            "closeQuestion" -> { //关闭题板
                var keyId = content.optInt("keyId")
                //关闭指定题版
                if (DialogUtil.KEYID == keyId) {
                    (mContext as Activity).runOnUiThread {
                        DialogUtil.getInstance().clearParent(mv_topicBoard)
                    }
                }
            }
            "showTimer" -> { //显示计时器
                var receiveSubgrouId = content.optString("receiveSubgroupId")
                var timeCount = content.optLong("timeCount")
                (mContext as Activity).runOnUiThread {
                    DialogUtil.getInstance().showTimer(mv_topicBoard, timeCount, {
                        playAudio("music_clock.mp3", true)
                    })
                }
            }
            "bgMusic" -> { //播放音乐
                var value = content.optString("value")
                when (value) {
                    "play" -> {
                        var url = content.optString("audioUrl")
                        if (mView?.onEnablePlayAudio() == true)
                            if (url.startsWith("http")) {
                                playAudio(url, false)
                            } else {
                                playAudio("http:" + url, false)
                            }
                    }
                    "stop" -> stopAudio()
                }
            }
            "comment" -> { //教师评价返回结果
                var receiveUserId = content.optString("receiveUserId")
                var partProgress = content.optInt("partProgress")
                var partGold = content.optInt("partGold")
                if (receiveUserId == mImManager?.getSessionUser()?.userId) {
                    mImManager?.getSessionUser()?.prop?.goldPoint = partGold
                    mImManager?.getSessionUser()?.prop?.progressPoint = partProgress
                    
                }
            }
        }
    }
    
    override fun onSysMessage(msg: Message) {
        Logger.e("onSysmessage")
    }
    
    override fun onPropChange(msg: Message) {
        Logger.e("===onProchange")
    }
    
    override fun onStatusChange(status: String) {
        Logger.e("onStatuschange")
    }
    
    override fun onUserPropChange(msg: Message) {
        Logger.e("===onUserpropChange")
        //        Prop p = mJsonUtils.toBean(mJsonUtils.toJson(msg.getContent()), Prop.class);
        //        List<String> targets = msg.getTargets();
        //        if (targets != null && targets.size() > 0) {
        //            updateProp(p, targets.get(0));
        //        }
    }
    
    override fun onUserStatusChange(msg: Message) {
        Logger.e("onUserStatusChange")
    }
    
    override fun onGroupPropChange(msg: Message) {
        Logger.e("onGroupPropChange")
    }
    
    override fun onGroupStatusChange(msg: Message) {
        Logger.e("onGroupStatusChange")
    }
    
    override fun onCommonMessagePush(msg: Message) {
        LogU.log("onCommonMessagePush")
        //        try {
        //            String json = mJsonUtils.toJson(msg.getContent());
        //            JSONObject obj = new JSONObject(json);
        //            String messages = obj.getString("messages");
        //            LogU.log(messages);
        //            if (messages != null && messages.length() > 0) {
        //                List<Message> ms = mJsonUtils.toList(messages, Message.class);
        //                if (ms != null && ms.size() > 0) {
        //                    mView.onCommonHistory(ms);
        //                }
        //            }
        //        } catch (JSONException _e) {
        //            _e.printStackTrace();
        //        }
        
        
    }
    
    override fun onCustomMessagePush(msg: Message) {
        Logger.e("onCustomMessagePush")
    }
    
    override fun onUserInfoPush(msg: Message) {
        LogU.log("onUserInfoPush")
    }
    
    override fun onError(err: String) {
        ToastUtils.showShort(err)
    }
    
    
    override fun onUserCountChange(timerUserCountData: Int) {
    
    }
    
    override fun onTimerUpdate(time: Long) {
    
    }
    
    override fun onWarning(code: Int) {
        when (code) {
            IMCallBack.WARNING_OFEN -> ToastUtils.showShort("发送频繁")
            IMCallBack.WARNING_UNCONNECTED -> ToastUtils.showShort("断开连接")
            IMCallBack.WARNING_FORBIDDEN -> ToastUtils.showShort("您已被禁言")
        }
    }
    
    private fun showList(lists: List<ListStudent>) {
        val container = (mContext as Activity).findViewById<FrameLayout>(android.R.id.content)
        val ultraViewPager = UltraViewPager(mContext)
        
        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL)
        val liveRoomAdapter = LiveRoomAdapter(mContext, lists, object : ICallBack<String, String> {
            override fun onPrev(t: String?): String {
                return ""
            }
            
            override fun call(t: String?, p: Int) {
                container.removeView(ultraViewPager)
            }
            
        })
        ultraViewPager.adapter = liveRoomAdapter
        ultraViewPager.setMultiScreen(0.6f)
        ultraViewPager.setItemRatio(1.0)
        ultraViewPager.setRatio(2.0f)
        ultraViewPager.setMaxHeight(800)
        ultraViewPager.setAutoMeasureHeight(true)
        //        if (style == 5) {
        ultraViewPager.setPageTransformer(false, UltraScaleTransformer())
        //        }
        //        if (style == 6) {
        //            ultraViewPager.setPageTransformer(false, new UltraDepthScaleTransformer());
        //        }
        
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.CENTER
        ultraViewPager.layoutParams = layoutParams
        container.addView(ultraViewPager)
        //        liveRoomAdapter.setOnPagerItemClickListener { container.removeView(ultraViewPager) }
    }
    
    fun onResume() {
        mImManager?.isInBackground(false)
    }
    
    fun onStop() {
        mImManager?.isInBackground(true)
        stopAudio()
    }
    
    companion object {
        var topicName = ""
        var appId = ""
        var roomId = 0
        var role = ""
        var userId = ""
        var random = ""
        var stamp = 0L
        var serverSignKey = ""
        var subGroupId = ""
        
        fun InitInfo(appId: String, roomId: Int, role: String, userId: String, random: String, stamp: Long, serverSignKey: String, subGroupId: String) {
            this.appId = appId
            this.roomId = roomId
            this.role = role
            this.userId = userId
            this.random = random
            this.serverSignKey = serverSignKey
            this.subGroupId = subGroupId
        }
        
        var lastHistoryTime = 0L
    }
}
