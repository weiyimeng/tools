package com.haoke91.a91edu.fragment.main

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.ToastUtils
import com.eduhdsdk.interfaces.JoinmeetingCallBack
import com.eduhdsdk.interfaces.MeetingNotify
import com.eduhdsdk.message.RoomClient
import com.haoke91.a91edu.R
import com.haoke91.a91edu.entities.UserInfo
import com.haoke91.a91edu.fragment.BaseFragment
import com.haoke91.a91edu.ui.address.AddressManagerActivity
import com.haoke91.a91edu.ui.found.GoldGoodsActivity
import com.haoke91.a91edu.ui.liveroom.AgoraActivity
import com.haoke91.a91edu.ui.liveroom.LivePlayerActivity
import com.haoke91.a91edu.ui.liveroom.TkActivity
import com.haoke91.a91edu.ui.login.LoginActivity
import com.haoke91.a91edu.ui.mycollect.MycollectActivity
import com.haoke91.a91edu.ui.order.OrderCenterActivity
import com.haoke91.a91edu.ui.order.ShoppingCartActivity
import com.haoke91.a91edu.ui.user.MyBalanceActivity
import com.haoke91.a91edu.ui.user.MyCouponsActivity
import com.haoke91.a91edu.ui.user.SettingActivity
import com.haoke91.a91edu.ui.user.UserInfoActivity
import com.haoke91.a91edu.utils.imageloader.GlideUtils
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.a91edu.widget.NoDoubleClickListener
import com.haoke91.a91edu.widget.dialog.DialogUtil
import com.haoke91.baselibrary.event.MessageItem
import com.haoke91.baselibrary.event.RxBus
import com.talkcloud.room.TKRoomManager
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.dialog_evaluatecommit.*
import kotlinx.android.synthetic.main.fragment_mine.*
import java.util.*


/**
 * 项目名称：MyHaoke1
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/6/4 15:26
 */
class MineFragment : BaseFragment() {
    
    private val onClickListener = object : NoDoubleClickListener() {
        override fun onDoubleClick(v: View) {
            
            if (v.id == R.id.tv_userInfo_setting) {
                SettingActivity.start(mContext)
                return
            }
            if (!UserManager.getInstance().isLogin) {
                LoginActivity.start(mContext)
                return
            }
            when (v.id) {
                R.id.iv_user_head -> UserInfoActivity.start(mContext)
                R.id.tv_shopping_cart -> ShoppingCartActivity.start(mContext)
                R.id.tv_collect -> MycollectActivity.start(mContext)
                R.id.tv_order -> OrderCenterActivity.start(mContext)
                R.id.tv_userInfo_address -> AddressManagerActivity.start(mContext, true)
                R.id.tv_coupons -> MyCouponsActivity.start(mContext)
                R.id.tv_edit_userInfo -> UserInfoActivity.start(mContext)
                R.id.tv_balance -> MyBalanceActivity.start(mContext)
                R.id.cl_userInfo -> if (!UserManager.getInstance().isLogin) {
                    LoginActivity.start(mContext)
                }
            //                R.id.play_view_talk -> joinTalkPlay()
                R.id.tv_gotoGold -> GoldGoodsActivity.start(mContext)
            }
        }
    }
    private var currentUser: UserInfo? = null
    
    override fun getLayout(): Int {
        return R.layout.fragment_mine
    }
    
    /**
     * playback
     */
    /**
     * live
     */
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mRxBus = RxBus.getIntanceBus()
        mRxBus.doSubscribe(mContext.javaClass, MessageItem::class.java, Consumer { messageItem ->
            if (ObjectUtils.isEmpty(messageItem)) {
                return@Consumer
            }
            if (messageItem.type == MessageItem.action_login) {
                fetchData()
            }
            //                else if (messageItem.getType() == messageItem.change_head) {
            //                    GlideUtils.load(mContext, messageItem.getMessage(), iv_user_head);
            //                }
        })
        tv_order.setOnClickListener(onClickListener)
        tv_collect.setOnClickListener(onClickListener)
        tv_shopping_cart.setOnClickListener(onClickListener)
        tv_userInfo_setting.setOnClickListener(onClickListener)
        tv_userInfo_message.setOnClickListener(onClickListener)
        tv_userInfo_address.setOnClickListener(onClickListener)
        cl_userInfo.setOnClickListener(onClickListener)
        tv_balance.setOnClickListener(onClickListener)
        tv_coupons.setOnClickListener(onClickListener)
        play_view.setOnClickListener(onClickListener)
        tv_edit_userInfo.setOnClickListener(onClickListener)
        play_view_agora.setOnClickListener(onClickListener)
        play_view_talk.setOnClickListener(onClickListener)
        tv_gotoGold.setOnClickListener(onClickListener)
    }
    
    override fun fetchData() {
        setLoginStatus(UserManager.getInstance().isLogin)
    }
    
    private fun setLoginStatus(isLogin: Boolean) {
        if (isLogin) {
            currentUser = UserManager.getInstance().loginUser
            tv_user_level.text = currentUser?.levelName
            tv_goldCount.text = currentUser?.gold.toString()
            tv_growValue.text = currentUser?.totalGrowth.toString()
        }
        GlideUtils.loadHead(mContext, if (isLogin) currentUser?.smallHeadimg else "", iv_user_head)
        
        tv_user_name.text = if (isLogin) currentUser!!.loginName else "登录/注册"
        tv_user_level.visibility = if (isLogin) View.VISIBLE else View.GONE
        tv_goldCount.visibility = if (isLogin) View.VISIBLE else View.GONE
        tv_grow.visibility = if (isLogin) View.VISIBLE else View.GONE
        tv_growValue.visibility = if (isLogin) View.VISIBLE else View.GONE
        tv_edit_userInfo.visibility = if (isLogin) View.VISIBLE else View.GONE
        tv_login_more.visibility = if (isLogin) View.GONE else View.VISIBLE
    }
    
    
}
