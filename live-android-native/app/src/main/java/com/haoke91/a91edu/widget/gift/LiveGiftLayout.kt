package com.haoke91.a91edu.widget.gift

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.blankj.utilcode.util.ObjectUtils
import com.haoke91.a91edu.R
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.baselibrary.utils.ACallBack

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/11/22 10:56 AM
 * 修改人：weiyimeng
 * 修改时间：2018/11/22 10:56 AM
 * 修改备注：
 * @version
 */
class LiveGiftLayout(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    private var giftNum: Int = 0
    private var giftQueue = ArrayList<GiftModel>()
    
    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.LiveGiftLayout, 0, 0)
        a?.let {
            giftNum = it.getInt(R.styleable.LiveGiftLayout_git_num, 3)
            it.recycle()
        }
        //设置布局动画
        val transition = LayoutTransition()
        transition.setAnimator(LayoutTransition.CHANGE_APPEARING,
                transition.getAnimator(LayoutTransition.CHANGE_APPEARING))
        transition.setAnimator(LayoutTransition.APPEARING,
                transition.getAnimator(LayoutTransition.APPEARING))
        transition.setAnimator(LayoutTransition.DISAPPEARING,
                transition.getAnimator(LayoutTransition.CHANGE_APPEARING))
        transition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING,
                transition.getAnimator(LayoutTransition.DISAPPEARING))
        layoutTransition = transition
    }
    
    
    //添加礼物
    fun addGift(gift: GiftModel) {
        //正在显示  连续点击 +1
        var giftItem: GiftItemLayout
        for (index in 0 until childCount) {
            giftItem = getChildAt(index) as GiftItemLayout
            if (giftItem.getGift()?.giftId == gift.giftId && giftItem.getGift()?.sendUserId == gift.sendUserId) {
                //                    if (getChildAt(index) != null) {
                if (giftItem.isShowing()) {
                    giftItem.getGift()!!.giftNum = giftItem.getGift()!!.giftNum + gift.giftNum
                    if (giftItem.getGift()!!.giftNum > 10) {
                        return
                    }
                    giftItem.setGiftNum(false)
                    return
                    //                        }
                }
            }
            
        }
        //
        addGiftToQueue(gift)
    }
    
    private fun addGiftToQueue(gift: GiftModel) {
        
        if (giftQueue != null) {
            //            if (giftQueue.size == 0) {
            giftQueue.add(gift)
            createGift(gift)
            //            }
        }
        
        
    }
    
    private fun createGift(gift: GiftModel) {
        if (giftNum > childCount || gift.sendUserId?.toInt() == UserManager.getInstance().userId) {
            var giftItem = GiftItemLayout(context)
            giftItem.setCallBack(ACallBack { isEnd ->
                if (isEnd) {
                    if (!ObjectUtils.isEmpty(giftQueue)) {
                        removeView(giftItem)
                        giftQueue.remove(giftItem.getGift())
                    }
                }
            })
            giftItem.setGift(gift)
            val lp = layoutParams as RelativeLayout.LayoutParams
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            addView(giftItem)
            
            giftItem.enter()
        } else {
            //wait
        }
    }
    
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeAllViews()
        if (!ObjectUtils.isEmpty(giftQueue)) {
            giftQueue.clear()
        }
        
    }
    
}
