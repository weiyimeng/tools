package com.haoke91.a91edu.ui.order

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.widget.RelativeLayout

import com.blankj.utilcode.util.ObjectUtils
import com.gaosiedu.live.sdk.android.api.order.create.LiveOrderCreateResponse
import com.gaosiedu.live.sdk.android.api.user.order.cancel.LiveUserOrderCancelRequest
import com.gaosiedu.live.sdk.android.api.user.order.cancel.LiveUserOrderCancelResponse
import com.gaosiedu.live.sdk.android.api.user.order.detail.orderNo.LiveUserOrderDetailOrderNORequest
import com.google.gson.Gson
import com.haoke91.a91edu.R
import com.haoke91.a91edu.adapter.OrderDetailAdapter
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.ui.BaseActivity
import com.haoke91.a91edu.utils.Utils
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.a91edu.widget.dialog.SeeFlowDialog
import com.haoke91.a91edu.widget.NoDoubleClickListener
import com.haoke91.baselibrary.event.MessageItem
import com.haoke91.baselibrary.event.RxBus
import com.haoke91.baselibrary.utils.DensityUtil
import com.haoke91.baselibrary.views.TipDialog
import kotlinx.android.synthetic.main.activity_orderdetail.*
import java.math.BigDecimal

/**
 * 项目名称：91haoke_Android
 * 类描述：  订单详情  待支付  已支付
 * 创建时间：2018/8/8 上午11:36
 * 修改人：weiyimeng
 * 修改时间：2018/8/8 上午11:36
 * 修改备注：
 */
class OrderDetailPayActivity : BaseActivity() {
    
    /**
     * 已支付
     * 待支付
     */
    private var orderType: String? = null
    //订单号
    private lateinit var orderNo: String
    
    private val onClickListener = object : NoDoubleClickListener() {
        override fun onDoubleClick(v: View) {
            when (v.id) {
                R.id.tv_see_flow -> SeeFlowDialog(this@OrderDetailPayActivity, date).show()
                R.id.tv_cancel_order -> cancelOrder() //取消订单
                R.id.tv_apply_back -> ApplyBackOrderActivity.Companion.start(this@OrderDetailPayActivity, id) //取消订单
            }
        }
    }
    
    private var id: Int = 0 //订单取消id;
    private var date: LiveOrderCreateResponse.ResultData? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        orderType = intent.getStringExtra(AllOrderFragment.ORDER_TYPE)
        orderNo = intent.getStringExtra(ORDERED)
        super.onCreate(savedInstanceState)
        //        setDate();
    }
    
    
    private fun confirmOrder() {
        PayActivity.start(this, date!!)
    }
    
    private fun cancelOrder() {
        val dialog = TipDialog(this)
        dialog.setTextDes("确认取消该订单？")
        dialog.setButton1(getString(R.string.action_ok)) { _, dialog ->
            showLoadingDialog()
            val request = LiveUserOrderCancelRequest()
            request.userId = UserManager.getInstance().userId
            request.id = id
            Api.getInstance().post(request, LiveUserOrderCancelResponse::class.java, object : ResponseCallback<LiveUserOrderCancelResponse>() {
                override fun onResponse(date: LiveUserOrderCancelResponse, isFromCache: Boolean) {
                    if (isDestroyed) {
                        return
                    }
                    dismissLoadingDialog()
                    if (date.data.result == 1) {
                        finish()
                        notifyOrderList()
                    }
                    
                }
                
                override fun onError() {
                    if (isDestroyed) {
                        return
                    }
                    dismissLoadingDialog()
                }
            }, "")
            dialog.dismiss()
        }
        dialog.setButton2(getString(R.string.cancel)) { _, dialog -> dialog.dismiss() }
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
    }
    
    private fun notifyOrderList() {
        val messageItem = MessageItem(MessageItem.order_change, orderType)
        RxBus.getIntanceBus().post(messageItem)
        
    }
    
    override fun getLayout(): Int {
        return R.layout.activity_orderdetail
    }
    
    override fun initialize() {
        tv_cancel_order.setOnClickListener(onClickListener)
        tv_see_flow.setOnClickListener(onClickListener)
        tv_apply_back.setOnClickListener(onClickListener)
        val layoutManager = LinearLayoutManager(this)
        rv_order_list.setHasFixedSize(true)
        rv_order_list.isNestedScrollingEnabled = false
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_order_list.layoutManager = layoutManager
        empty_view?.showLoading()
        val request = LiveUserOrderDetailOrderNORequest()
        request.userId = UserManager.getInstance().userId
        request.orderNo = orderNo
        Api.getInstance().post(request, LiveOrderCreateResponse::class.java, object : ResponseCallback<LiveOrderCreateResponse>() {
            override fun onResponse(date: LiveOrderCreateResponse, isFromCache: Boolean) {
                //                var s = Gson().toJson(date)
                setDate(date.data)
                empty_view?.showContent()
            }
            
            override fun onError() {
                empty_view?.showError()
            }
        }, "")
        //        rl_order_pay_method.visibility = if (AllOrderFragment.have_pay == orderType) View.VISIBLE else View.GONE
        
        when (orderType) {
            AllOrderFragment.have_pay -> {
                rl_order_bottom.visibility = View.GONE
                rl_have_pay.visibility = View.VISIBLE
                tv_cancel_time.visibility = View.GONE
                tv_order_status.text = getString(R.string.have_pay)
                rl_order_time.visibility = View.VISIBLE
                rl_order_pay_method.visibility = View.VISIBLE
                //                tv_pay_money.text=
                tv_pay_type.text = getString(R.string.payed_money)
                val layoutParams = empty_view?.layoutParams as RelativeLayout.LayoutParams
                layoutParams.bottomMargin = DensityUtil.dip2px(this, 50f)
            }
            AllOrderFragment.wait_pay -> {
                rl_order_bottom.visibility = View.VISIBLE
                rl_have_pay.visibility = View.GONE
                tv_cancel_time.visibility = View.VISIBLE
                tv_order_status.text = getString(R.string.wait_pay)
                rl_order_time.visibility = View.GONE
                rl_order_pay_method.visibility = View.GONE
                tv_pay_type.text = getString(R.string.pay_money)
                val layoutParams = empty_view?.layoutParams as RelativeLayout.LayoutParams
                layoutParams.bottomMargin = DensityUtil.dip2px(this, 50f)
            }
            AllOrderFragment.back_order -> {
                rl_order_bottom.visibility = View.GONE
                rl_have_pay.visibility = View.GONE
                tv_cancel_time.visibility = View.GONE
                tv_order_status.text = getString(R.string.back_order)
                rl_order_time.visibility = View.VISIBLE
                rl_order_pay_method.visibility = View.VISIBLE
                tv_pay_type.text = getString(R.string.payed_money)
                
            }
            AllOrderFragment.back_order_ing -> {
                rl_order_bottom.visibility = View.GONE
                rl_have_pay.visibility = View.GONE
                tv_cancel_time.visibility = View.GONE
                tv_order_status.text = getString(R.string.back_order_ing)
                rl_order_time.visibility = View.VISIBLE
                rl_order_pay_method.visibility = View.VISIBLE
                tv_pay_type.text = getString(R.string.payed_money)
                
            }
            AllOrderFragment.back_order_some -> {
                rl_order_bottom.visibility = View.GONE
                rl_have_pay.visibility = View.VISIBLE
                tv_cancel_time.visibility = View.GONE
                tv_order_status.text = getString(R.string.back_order_some)
                rl_order_time.visibility = View.VISIBLE
                rl_order_pay_method.visibility = View.VISIBLE
                tv_pay_type.text = getString(R.string.payed_money)
                val layoutParams = empty_view?.layoutParams as RelativeLayout.LayoutParams
                layoutParams.bottomMargin = DensityUtil.dip2px(this, 50f)
            }
        }
        
    }
    
    private fun setDate(date: LiveOrderCreateResponse.ResultData) {
        id = date.id
        this.date = date
        if (!ObjectUtils.isEmpty(date.orderAddressDomain) || date.refundable) {
            if (!ObjectUtils.isEmpty(date.orderAddressDomain)) {
                vg_order_address.visibility = View.VISIBLE
                tv_order_recipient.text = StringBuffer().append(date.orderAddressDomain.contacts).append(" ").append(date.orderAddressDomain.contactNumber)
                tv_order_city.text = date.orderAddressDomain.general
            } else {
                bg_pay.layoutParams.height = DensityUtil.dip2px(this, 60f)
                vg_order_address.visibility = View.GONE
            }
            
            if (date.refundable) {
                tv_apply_back.visibility = View.VISIBLE
            } else {
                tv_apply_back.visibility = View.GONE
            }
            
            val layoutParams = empty_view.layoutParams as RelativeLayout.LayoutParams
            layoutParams.bottomMargin = DensityUtil.dip2px(this, 50f)
        } else {
            if (ObjectUtils.isEmpty(date.orderAddressDomain)) {
                vg_order_address.visibility = View.GONE
                bg_pay.layoutParams.height = DensityUtil.dip2px(this, 60f)
            }
        }
        
        tv_order_num.text = date.orderItemList.size.toString()
        tv_order_total.text = "¥${date.orderAmount}"
        tv_coupon_save.text = "¥${date.couponAmount}"
        tv_active_save.text = "¥${date.activityAmount}"
        tv_balance_save.text = "¥${date.balance}"
        
        if (AllOrderFragment.wait_pay == orderType) {
            tv_pay_money.text = "¥${date.dueAmount}"
        } else {
            tv_pay_money.text = "¥${date.amount}"
        }
        
        tv_order_no.text = date.orderNo
        tv_order_time.text = date.createTime
        tv_pay_method.text = if (date.payWayId == 1) "支付宝" else if (date.payWayId == 2) "微信" else if (date.balance > BigDecimal.ZERO) "余额抵扣" else if (date.couponAmount?.abs()!! > BigDecimal.ZERO) "优惠券支付" else ""
        if (TextUtils.isEmpty(tv_pay_method.text)) {
            rl_order_pay_method.visibility = View.GONE
        }
        val orderDetailAdapter = OrderDetailAdapter(this, date.orderItemList)
        orderDetailAdapter.setOrderType(orderType)
        orderDetailAdapter.setOrderNo(date.orderNo)
        rv_order_list!!.adapter = orderDetailAdapter
        tv_order_pay.setOnClickListener { confirmOrder() }
    }
    
    companion object {
        private val ORDERED = "orderNo"
        
        fun start(context: Context, orderType: String, orderNo: String) {
            val intent = Intent(context, OrderDetailPayActivity::class.java)
            intent.putExtra(AllOrderFragment.ORDER_TYPE, orderType)
            intent.putExtra(ORDERED, orderNo)
            context.startActivity(intent)
        }
    }
}
