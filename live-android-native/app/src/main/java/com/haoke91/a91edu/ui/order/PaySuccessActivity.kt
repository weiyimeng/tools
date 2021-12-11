package com.haoke91.a91edu.ui.order

import android.content.Context
import android.content.Intent
import android.view.GestureDetector
import android.view.View

import com.blankj.utilcode.util.ActivityUtils
import com.haoke91.a91edu.MainActivity
import com.haoke91.a91edu.R
import com.haoke91.a91edu.ui.BaseActivity
import com.haoke91.a91edu.widget.NoDoubleClickListener
import kotlinx.android.synthetic.main.activity_pay_success.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/22 下午7:53
 * 修改人：weiyimeng
 * 修改时间：2018/7/22 下午7:53
 * 修改备注：
 */
class PaySuccessActivity : BaseActivity() {
    
    private val onClickListener = object : NoDoubleClickListener() {
        override fun onDoubleClick(v: View) {
            when (v.id) {
                R.id.tv_continue -> ActivityUtils.finishToActivity(MainActivity::class.java, false, true)
                R.id.tv_order_see -> {
                    OrderCenterActivity.start(this@PaySuccessActivity)
                    finish()
                }
                R.id.toolbar_back -> onBackPressed()
                
            }
        }
    }
    
    override fun onBackPressed() {
        ActivityUtils.finishToActivity(MainActivity::class.java, false, true)
        
    }
    
    override fun getLayout(): Int {
        return R.layout.activity_pay_success
    }
    
    override fun initialize() {
        val money = intent.getStringExtra("money")
        tv_order_money.text = money
        val isBackClass = intent.getBooleanExtra("isBackClass", false)
        if (isBackClass) {
            toolbar_title.text = "申请成功"
        }
        tv_continue.setOnClickListener(onClickListener)
        tv_order_see.setOnClickListener(onClickListener)
        toolbar_back.setOnClickListener(onClickListener)
        
    }
    
    companion object {
        
        fun start(context: Context, money: String) {
            val intent = Intent(context, PaySuccessActivity::class.java)
            intent.putExtra("money", money)
            context.startActivity(intent)
        }
        
        fun start(context: Context, money: String, isBackClass: Boolean) {
            val intent = Intent(context, PaySuccessActivity::class.java)
            intent.putExtra("money", money)
            intent.putExtra("isBackClass", isBackClass)
            context.startActivity(intent)
        }
    }
    
}
