package com.haoke91.a91edu.widget

import android.view.View

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/9/29 上午9:26
 * 修改人：weiyimeng
 * 修改时间：2018/9/29 上午9:26
 * 修改备注：
 */
abstract class NoDoubleClickListener : View.OnClickListener {
    
    abstract fun onDoubleClick(v: View)
    
    override fun onClick(v: View) {
        val curClickTime = System.currentTimeMillis()
        if (curClickTime - lastClickTime >= MIN_CLICK_DELAY_TIME) {
            lastClickTime = curClickTime
            onDoubleClick(v)
        }
    }
    
    companion object {
        private val MIN_CLICK_DELAY_TIME = 500
        private var lastClickTime: Long = 0
    }
    
}
