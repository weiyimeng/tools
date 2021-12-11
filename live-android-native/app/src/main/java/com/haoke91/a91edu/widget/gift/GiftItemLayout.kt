package com.haoke91.a91edu.widget.gift

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Handler
import android.os.Message
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import com.haoke91.a91edu.R
import com.haoke91.a91edu.presenter.update.UpdateDialogFragment.isShow
import com.haoke91.a91edu.utils.AnimationTool
import com.haoke91.a91edu.utils.imageloader.GlideUtils
import com.haoke91.baselibrary.utils.ACallBack
import kotlinx.android.synthetic.main.item_gift_layout.view.*

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/11/22 11:36 AM
 * 修改人：weiyimeng
 * 修改时间：2018/11/22 11:36 AM
 * 修改备注：
 * @version
 */
class GiftItemLayout(context: Context?) : FrameLayout(context) {
    private var duration = 2000L
    
    init {
        View.inflate(context, R.layout.item_gift_layout, this)
    }
    
    private var gift: GiftModel? = null
    fun setGift(gift: GiftModel?): Boolean {
        this.gift = gift
        return false
    }
    
    fun getGift(): GiftModel? {
        return gift
    }
    
    //    val currentGift: GiftModel?
    //        get() = gift
    
    private var isShow = false
    fun isShowing(): Boolean {
        return isShow
    }
    
    fun enter() {
        hideView()
        isShow = true
        infoTv.text = "送了一个${gift?.giftName}"
        nickNameTv.text = "${gift?.sendUserId}"
        GlideUtils.loadHead(context, gift?.sendUserImg, headIv)
        view_animation.background = ContextCompat.getDrawable(context, gift?.giftId ?: R.drawable.ic_prise)
        post {
            val flyFromLtoR = AnimationTool.createFlyFromLtoR(this, -width.toFloat(), 0f, 1000, OvershootInterpolator())
            flyFromLtoR.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                }
                
                override fun onAnimationEnd(animation: Animator?) {
                    setGiftNum(true)
                }
            })
            flyFromLtoR.start()
        }
    }
    
    /**
     * @param isFirst  是否第一次
     */
    fun setGiftNum(isFirst: Boolean) {
        tv_gift_num.text = "x ${gift?.giftNum}"
        removeCallbacks(dismissTask)
        if (isFirst) {
            tv_gift_num.visibility = View.VISIBLE
            dismissTask = Runnable {
                val createDismissAnimation = AnimationTool.createDismissAnimation(this@GiftItemLayout)
                createDismissAnimation.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        //                    isShow = false
                        call?.call(true)
                        
                    }
                    
                    override fun onAnimationStart(animation: Animator?) {
                        isShow = false
                    }
                })
                createDismissAnimation.start()
            }
            postDismissMessage(0)
            
        } else {
            val scaleGiftNum = AnimationTool.scaleGiftNum(tv_gift_num)
            scaleGiftNum.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                }
                
                override fun onAnimationStart(animation: Animator?) {
                    postDismissMessage(400)
                }
            })
            scaleGiftNum.start()
            
        }
    }
    
    private var dismissTask: Runnable? = null
    
    fun postDismissMessage(delayTime: Long) {
        postDelayed(dismissTask, duration + delayTime)
        
    }
    
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks(dismissTask)
    }
    
    private fun hideView() {
    }
    
    var call: ACallBack<Boolean>? = null
    fun setCallBack(call: ACallBack<Boolean>) {
        this.call = call
    }
}
