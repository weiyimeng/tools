package com.haoke91.a91edu.ui.order

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

import com.blankj.utilcode.util.ObjectUtils
import com.gaosiedu.live.sdk.android.api.user.order.returned.cancel.LiveUserOrderReturnedCancelRequest
import com.gaosiedu.live.sdk.android.api.user.order.returned.detail.LiveUserOrderReturnedDetailRequest
import com.gaosiedu.live.sdk.android.api.user.order.returned.detail.LiveUserOrderReturnedDetailResponse
import com.gaosiedu.live.sdk.android.base.ApiResponse
import com.gaosiedu.live.sdk.android.domain.CouponDomain
import com.gaosiedu.live.sdk.android.domain.OrderItemDomain
import com.gaosiedu.live.sdk.android.domain.OrderReturnItemDomain
import com.google.gson.Gson
import com.haoke91.a91edu.R
import com.haoke91.a91edu.adapter.OrderDetailAdapter
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.ui.BaseActivity
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.baselibrary.event.MessageItem
import com.haoke91.baselibrary.event.RxBus
import com.haoke91.baselibrary.views.emptyview.EmptyView
import kotlinx.android.synthetic.main.activity_orderdetail_back.*

import java.util.ArrayList
import java.util.Arrays

/**
 * 项目名称：91haoke_Android
 * 类描述：  订单详情  已退款  退款审核中
 * 创建时间：2018/8/8 上午11:36
 * 修改人：weiyimeng
 * 修改时间：2018/8/8 上午11:36
 * 修改备注：
 */
class OrderDetailBackActivity : BaseActivity() {
    
    private var orderType: String? = null
    private var orderNo: String? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        orderType = intent.getStringExtra(AllOrderFragment.ORDER_TYPE)
        orderNo = intent.getStringExtra("orderNo")
        
        super.onCreate(savedInstanceState)
        //        setDate(date.getData());
    }
    
    private fun setDate(data: LiveUserOrderReturnedDetailResponse.ResultData) {
        tv_create_time.text = data.createTime
        tv_cancel_reason.text = data.reason
        if (!ObjectUtils.isEmpty(data.refundItems)) {
            tv_cancel_num.text = data.refundItems.size.toString()
        }
        tv_apply_time.text = data.createTime
        tv_cancel_no.text = data.orderReturnNo
        tv_order_money.text = "¥${data.price}"   //实付金额
        tv_back_money.text = "¥${data.returnPrice}" //已退金额
        tv_consume_money.text = "¥${data.consumeAmount} " //已退金额
        tv_note_money.text = "¥${data.notesPrice?.add(data.expressPrice)}" //资料服务费
        tv_consume_money.text = "¥${data.consumeAmount}" //已退金额
        tv_actual_return.text = "¥${data.returnPrice}"  //已退金额
        tv_balance_return.text = "¥${data.returnPriceBalance}"
        tv_cash_return.text = "¥${data.returnPriceCash}"
        tv_method_return.text = if (1 == data.returnWays) "原路返回" else "退至银行卡"
        
        val orderItemDomains = ArrayList<OrderItemDomain>()
        for (item in data.refundItems) {
            val orderItemDomain = OrderItemDomain()
            orderItemDomain.course = item.courseDomain
            orderItemDomain.price = item.price
            orderItemDomain.courseId = item.courseDomain?.id ?: 0
            orderItemDomains.add(orderItemDomain)
        }
        
        val orderDetailAdapter = OrderDetailAdapter(this, orderItemDomains)
        orderDetailAdapter.setOrderType(orderType!!)
        rv_order_list!!.adapter = orderDetailAdapter
        //        findViewById(R.id.rl_order_pay_method).setVisibility(AllOrderFragment.have_pay.equals(orderType) ? View.VISIBLE : View.GONE);
    }
    
    override fun getLayout(): Int {
        return R.layout.activity_orderdetail_back
    }
    
    override fun initialize() {
        when (orderType) {
            AllOrderFragment.back_order -> {
                tv_order_status.text = getString(R.string.back_order)
                rl_order_bottom.visibility = View.GONE
                rl_back_num.visibility = View.VISIBLE
                rl_really_back_num.visibility = View.VISIBLE
                rl_cash_return.visibility = View.VISIBLE
                rl_balance_return.visibility = View.VISIBLE
                rl_back_money.visibility = View.GONE
            }
            AllOrderFragment.back_order_ing -> {
                tv_order_status.text = getString(R.string.back_order_ing)
                rl_order_bottom.visibility = View.GONE
                rl_back_num.visibility = View.GONE
                rl_really_back_num.visibility = View.VISIBLE
                tv_really_back_tittle.text = getString(R.string.think_back_num)
                
                rl_cash_return.visibility = View.GONE
                rl_balance_return.visibility = View.GONE
            }
            AllOrderFragment.wait_back -> {
                tv_order_status.text = getString(R.string.wait_back)
                rl_order_bottom.visibility = View.GONE
                rl_back_num.visibility = View.GONE
                rl_really_back_num.visibility = View.VISIBLE
                tv_really_back_tittle.text = getString(R.string.think_back_num)
                
                rl_cash_return.visibility = View.GONE
                rl_balance_return.visibility = View.GONE
                //                rl_back_money.visibility = View.GONE
            }
            AllOrderFragment.reject_back -> {
                tv_order_status.text = getString(R.string.reject_back)
                rl_order_bottom.visibility = View.GONE
                rl_back_num.visibility = View.GONE
                rl_really_back_num.visibility = View.VISIBLE
                tv_really_back_tittle.text = getString(R.string.think_back_num)
                rl_cash_return.visibility = View.GONE
                rl_balance_return.visibility = View.GONE
                //                rl_back_money.visibility = View.GONE
            }
            
            AllOrderFragment.check_back -> {
                tv_order_status.text = getString(R.string.back_check)
                rl_order_bottom.visibility = View.VISIBLE
                rl_back_num.visibility = View.GONE
                rl_really_back_num.visibility = View.VISIBLE
                tv_really_back_tittle.text = getString(R.string.think_back_num)
                
                rl_cash_return.visibility = View.GONE
                rl_balance_return.visibility = View.GONE
                tv_cancel.setOnClickListener {
                    cancelApply()
                }
                
            }
            AllOrderFragment.cancel_back -> {
                tv_order_status.text = getString(R.string.cancel_back)
                rl_order_bottom.visibility = View.GONE
                rl_back_num.visibility = View.GONE
                rl_really_back_num.visibility = View.GONE
                rl_cash_return.visibility = View.GONE
                rl_balance_return.visibility = View.GONE
            }
        }
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_order_list!!.layoutManager = layoutManager
        rv_order_list!!.setHasFixedSize(true)
        rv_order_list!!.isNestedScrollingEnabled = false
        //
        val request = LiveUserOrderReturnedDetailRequest()
        request.userId = UserManager.getInstance().userId
        request.orderReturnId = orderNo
        empty_view.showLoading()
        Api.getInstance().post(request, LiveUserOrderReturnedDetailResponse::class.java, object : ResponseCallback<LiveUserOrderReturnedDetailResponse>() {
            override fun onResponse(date: LiveUserOrderReturnedDetailResponse, isFromCache: Boolean) {
                //                val s = Gson().toJson(date)
                setDate(date.data)
                empty_view.showContent()
                
            }
            
            override fun onError() {
                empty_view.showError()
            }
        }, "")
    }
    
    private fun cancelApply() {
        showLoadingDialog()
        var request = LiveUserOrderReturnedCancelRequest()
        request.id = orderNo
        request.userId = UserManager.getInstance().userId
        Api.getInstance().post(request, ApiResponse::class.java, object : ResponseCallback<ApiResponse>() {
            override fun onResponse(date: ApiResponse?, isFromCache: Boolean) {
                if (isDestroyed) {
                    return
                }
                dismissLoadingDialog()
                if (date?.data?.result == 1) {
                    val messageItem = MessageItem(MessageItem.order_change, AllOrderFragment.back_order)
                    RxBus.getIntanceBus().post(messageItem)
                    finish()
                }
            }
            
            override fun onError() {
                if (isDestroyed) {
                    return
                }
                dismissLoadingDialog()
            }
            
        }, "")
    }
    
    companion object {
        
        fun start(context: Context, orderType: String, orderNo: String) {
            val intent = Intent(context, OrderDetailBackActivity::class.java)
            intent.putExtra(AllOrderFragment.ORDER_TYPE, orderType)
            intent.putExtra("orderNo", orderNo)
            
            context.startActivity(intent)
        }
    }
}
