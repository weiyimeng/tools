package com.haoke91.a91edu.ui.liveroom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.eduhdsdk.message.RoomSession
import com.eduhdsdk.tools.TKCallBack
import com.eduhdsdk.ui.ChatView
import com.eduhdsdk.ui.TKPlayer
import com.haoke91.a91edu.R

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/11/14 17:24
 */
class TalkPlayer : RelativeLayout {
    
    //    private lateinit var mTopToolBar: LivePlayerToolbar
    private var tvTitle: TextView? = null
    private var tvGold: TextView? = null
    private var tvProgress: TextView? = null
    private var mChatView: ChatView? = null
    
    constructor(context: Context) : super(context) {
        initPlayerConfig()
    }
    
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initPlayerConfig()
    }
    
    private lateinit var mTKPlayer: TKPlayer
    private fun initPlayerConfig() {
        mTKPlayer = TKPlayer(context)
        //        mTopToolBar = LivePlayerToolbar(context)
        this.addView(mTKPlayer, 0)
    }
    
    fun create() {
        mTKPlayer.onCreate()
        mChatView = mTKPlayer.findViewById(R.id.chatView)
        var topBar = LayoutInflater.from(context).inflate(R.layout.biz_video_media_toolbar, null)
        tvTitle = topBar.findViewById(R.id.title)
        tvGold = topBar.findViewById(R.id.tv_goldNum)
        tvProgress = topBar.findViewById(R.id.tv_growNum)
        topBar.findViewById<TextView>(R.id.tv_resolution).visibility = View.GONE
        topBar.findViewById<TextView>(R.id.tv_speed).visibility = View.GONE
        topBar.findViewById<ImageView>(R.id.back_pl).setOnClickListener { mTKPlayer.showExitDialog() }
        mTKPlayer.replaceToolbar(topBar)
        tvTitle?.text = RoomSession.roomName
    }
    
    fun updateToolBar(topicName: String, gold: Int, progress: Int) {
        if (!topicName.isNullOrEmpty()) {
            tvTitle?.text = topicName
        }
        if (gold >= 0) {
            tvGold?.text = gold.toString()
        }
        if (progress >= 0) {
            tvProgress?.text = progress.toString()
        }
    }
    
    fun setTkCallBack(callBack: TKCallBack) {
        mTKPlayer.setTKCallBack(callBack)
    }
    
    fun resumePlay() {
        mTKPlayer.onResume()
    }
    
    fun startPlay() {
        mTKPlayer.onStart()
    }
    
    fun finishPlay() {
        mTKPlayer.onBackPressed()
    }
    
    fun onPause() {
        mTKPlayer.onPause()
    }
    
    fun stopPlay() {
        mTKPlayer.onStop()
    }
    
    private var mCallBack: PlayCallBack? = null
    fun setCallBack(callback: PlayCallBack) {
        this.mCallBack = callback
    }
    
    interface PlayCallBack {
        fun call(code: Int) {}
    }
    
    /**
     * 设置右侧聊天区的点击事件
     */
    fun setChatViewCallBack(chatViewClickListener: ChatView.ChatViewClickListener) {
        mChatView?.setCallBack(chatViewClickListener)
    }
    
    /**
     * 禁言
     */
    fun isForbidden(isForbidden: Boolean) {
        mChatView?.enableSay(isForbidden)
    }
}
