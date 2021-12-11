package com.haoke91.a91edu.ui.order

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View

import com.blankj.utilcode.util.ObjectUtils
import com.gaosiedu.live.sdk.android.api.cart.add.LiveCartCreateResponse
import com.gaosiedu.live.sdk.android.api.cart.add.again.LiveCartAddAgainRequest
import com.gaosiedu.live.sdk.android.api.order.create.LiveOrderCreateResponse
import com.gaosiedu.live.sdk.android.api.user.order.detail.orderNo.LiveUserOrderDetailOrderNORequest
import com.gaosiedu.live.sdk.android.domain.OrderItemDomain
import com.haoke91.a91edu.R
import com.haoke91.a91edu.adapter.OrderDetailAdapter
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.ui.BaseActivity
import com.haoke91.a91edu.utils.Utils
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.baselibrary.event.MessageItem
import com.haoke91.baselibrary.event.RxBus
import kotlinx.android.synthetic.main.activity_orderdetail_cancel.*

import org.apache.commons.lang3.text.StrBuilder

/**
 * 项目名称：91haoke_Android
 * 类描述：  订单详情  已取消
 * 创建时间：2018/8/8 上午11:36
 * 修改人：weiyimeng
 * 修改时间：2018/8/8 上午11:36
 * 修改备注：
 */
class OrderDetailCancelActivity : BaseActivity() {
    
    /**
     *
     */
    private var orderType: String? = null
    private var orderNo: String? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        orderType = intent.getStringExtra(AllOrderFragment.ORDER_TYPE)
        orderNo = intent.getStringExtra("orderNo")
        super.onCreate(savedInstanceState)
        //        setDate();
    }
    
    private fun setDate() {
        //        ((TextView) findViewById(R.id.tv_order_status)).setText(AllOrderFragment.back_order.equals(orderType) ? getString(R.string.back_order) : getString(R.string.back_order_check));
        //        findViewById(R.id.rl_order_bottom).setVisibility(AllOrderFragment.back_order.equals(orderType) ? View.GONE : View.VISIBLE);
        //        findViewById(R.id.rl_back_num).setVisibility(AllOrderFragment.back_order.equals(orderType) ? View.VISIBLE : View.GONE);
        //        findViewById(R.id.rl_really_back_num).setVisibility(AllOrderFragment.back_order.equals(orderType) ? View.VISIBLE : View.GONE);
        //        findViewById(R.id.rl_order_pay_method).setVisibility(AllOrderFragment.have_pay.equals(orderType) ? View.VISIBLE : View.GONE);
    }
    
    override fun getLayout(): Int {
        return R.layout.activity_orderdetail_cancel
    }
    
    override fun initialize() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_order_list.layoutManager = layoutManager
        rv_order_list.setHasFixedSize(true)
        rv_order_list.isNestedScrollingEnabled = false
        //        OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(this, Arrays.asList("", "", ""));
        //        orderDetailAdapter.setOrderType(orderType);
        //        rv_order_list.setAdapter(orderDetailAdapter);
        val request = LiveUserOrderDetailOrderNORequest()
        request.userId = UserManager.getInstance().userId
        request.orderNo = orderNo
        empty_view?.showLoading()
        Api.getInstance().post(request, LiveOrderCreateResponse::class.java, object : ResponseCallback<LiveOrderCreateResponse>() {
            override fun onResponse(date: LiveOrderCreateResponse, isFromCache: Boolean) {
                //                String s = new Gson().toJson(date);
                setDate(date.data)
                empty_view?.showContent()
                
            }
            
            override fun onError() {
                empty_view?.showError()
            }
        }, "")
    }
    
    private fun setDate(date: LiveOrderCreateResponse.ResultData) {
        //        ((TextView) findViewById(R.id.tv_order_num)).setText(String.valueOf(date.getOrderItemList().size()));
        rl_order_bottom.visibility = View.VISIBLE
        tv_order_total.text = "¥${date.orderAmount}"
        tv_coupon_save.text = "-¥${date.couponAmount?.abs()}"
        tv_active_save.text = "-¥${date.activityAmount?.abs()}"
        tv_balance_save.text = "¥${date.balance}"
        tv_pay_money.text = "¥${date.dueAmount}"
        tv_order_no.text = date.orderNo
        tv_order_time.text = date.createTime
        tv_pay_method.text = if (date.payWayId == 1) "支付宝" else if (date.payWayId == 1) "余额抵扣" else "微信"
        tv_order_pay.setOnClickListener { confirmOrder(date.orderItemList) }
        
        val orderDetailAdapter = OrderDetailAdapter(this, date.orderItemList)
        orderDetailAdapter.setOrderType(orderType!!)
        rv_order_list.adapter = orderDetailAdapter
    }
    
    private fun confirmOrder(orderItemList: List<OrderItemDomain>) {
        showLoadingDialog()
        val orderId = StrBuilder()
        for (item in orderItemList) {
            orderId.append(item.course.id).append(",")
        }
        
        var s = orderId.toString()
        s = s.substring(0, orderId.toString().length - 1)
        val response = LiveCartAddAgainRequest()
        response.userId = UserManager.getInstance().userId
        response.courseIds = s
        val finalS = s
        Api.getInstance().post(response, LiveCartCreateResponse::class.java, object : ResponseCallback<LiveCartCreateResponse>() {
            override fun onResponse(date: LiveCartCreateResponse, isFromCache: Boolean) {
                if (isDestroyed) {
                    return
                }
                dismissLoadingDialog()
                if (!ObjectUtils.isEmpty(date.data)) {
                    ShoppingCartActivity.start(this@OrderDetailCancelActivity, finalS)
                    notifyOrderList()
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
    
    private fun notifyOrderList() {
        val messageItem = MessageItem(MessageItem.order_change, orderType)
        RxBus.getIntanceBus().post(messageItem)
        
    }
    
    companion object {
        
        fun start(context: Context, orderType: String, orderNo: String) {
            val intent = Intent(context, OrderDetailCancelActivity::class.java)
            intent.putExtra(AllOrderFragment.ORDER_TYPE, orderType)
            intent.putExtra("orderNo", orderNo)
            context.startActivity(intent)
        }
    }
}
