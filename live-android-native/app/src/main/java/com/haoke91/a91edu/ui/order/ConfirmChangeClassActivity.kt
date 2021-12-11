package com.haoke91.a91edu.ui.order

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.ToastUtils
import com.gaosiedu.live.sdk.android.api.order.create.LiveOrderCreateResponse
import com.gaosiedu.live.sdk.android.api.user.address.list.LiveUserAddressListResponse
import com.gaosiedu.live.sdk.android.api.user.balance.info.LiveUserBalanceInfoRequest
import com.gaosiedu.live.sdk.android.api.user.balance.info.LiveUserBalanceInfoResponse
import com.gaosiedu.live.sdk.android.api.user.exchange.create.LiveUserExchangeCreateRequest
import com.gaosiedu.live.sdk.android.domain.OrderAddressDomain
import com.gaosiedu.live.sdk.android.domain.OrderItemDomain
import com.haoke91.a91edu.R
import com.haoke91.a91edu.adapter.ConfirmOrderClassAdapter
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.ui.BaseActivity
import com.haoke91.a91edu.ui.address.AddressManagerActivity
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.a91edu.widget.NoDoubleClickListener
import com.haoke91.baselibrary.event.MessageItem
import com.haoke91.baselibrary.event.RxBus
import com.haoke91.baselibrary.views.TipDialog
import com.umeng.analytics.game.UMGameAgent.pay
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_confirmorder.*
import java.math.BigDecimal
import java.util.*

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/11/24 11:23 AM
 * 修改人：weiyimeng
 * 修改时间：2018/11/24 11:23 AM
 * 修改备注：
 * @version
 */
class ConfirmChangeClassActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.activity_confirmorder
    }
    
    override fun initialize() {
        orderItem = intent.getSerializableExtra("orderItem") as OrderItemDomain
        needAddress = intent.getBooleanExtra("needAddress", false)
        var address: OrderAddressDomain? = null
        if (intent.hasExtra("address")) {
            address = intent.getSerializableExtra("address") as OrderAddressDomain
        }
        originPayMoney = (intent.getSerializableExtra("returnBalance") as BigDecimal).negate()
        if (originPayMoney < BigDecimal.ZERO || orderItem.course.isFree) {
            tv_pay_money.text = getString(R.string.back_money)
            tv_order_leftPay.text = getString(R.string.back_balance)
            cb_balance_pay.isChecked = true
            cb_balance_pay.isEnabled = false
        } else {
            cb_balance_pay.isEnabled = true
            tv_pay_money.text = getString(R.string.pay_money)
        }
        tv_order_count.text = "1"
        tv_order_money.text = "¥${orderItem.course.price}"
        rl_order_choice_coupons.visibility = View.GONE
        rl_coupon_save.visibility = View.GONE
        rv_activity_save.visibility = View.GONE
        rv_order_list.setHasFixedSize(true)
        rv_order_list.isNestedScrollingEnabled = false
        val confirmOrderClassAdapter = ConfirmOrderClassAdapter(this, Arrays.asList(orderItem))
        rv_order_list.layoutManager = LinearLayoutManager(this)
        rv_order_list.adapter = confirmOrderClassAdapter
        cb_balance_pay.setOnCheckedChangeListener { _, isChecked ->
            val total = calculateTotalMoney()
            if (total >= BigDecimal.ZERO) {
                tv_pay_count.text = "¥$total"
            } else {
                tv_pay_count.text = "¥0.00"
            }
            
            if (isChecked) {
                tv_balance_save.text = "¥${getBalanceCast()}"
            } else {
                tv_balance_save.text = "¥0.00"
                
            }
        }
        
        setAddressInfo(needAddress, address)
        tv_order_pay.setOnClickListener(onClickListener)
        getBalance()
    }
    
    private fun setAddressInfo(needAddress: Boolean, address: OrderAddressDomain?) {
        if (needAddress) {
            cl_choice_address.visibility = View.VISIBLE
            cl_choice_address.setOnClickListener(onClickListener)
            tv_order_statement.visibility = View.VISIBLE
            if (ObjectUtils.isEmpty(address)) {
                tv_order_noAddress.visibility = View.VISIBLE
                tv_order_noAddress.setOnClickListener(onClickListener)
            } else {
                addressDate = LiveUserAddressListResponse.ListData()
                addressDate!!.id = address!!.id
                tv_order_city.text = StringBuffer().append(address.city).append(" ").append(address.area).toString()
                tv_order_address.text = address.address
                tv_order_recipient.text = StringBuffer().append(address.contacts).append(" ").append(address.contactNumber)
            }
        } else {
            tv_order_statement.visibility = View.GONE
            cl_choice_address.visibility = View.GONE
        }
    }
    
    private lateinit var orderItem: OrderItemDomain
    private var needAddress: Boolean = false
    private var addressDate: LiveUserAddressListResponse.ListData? = null //收货地址信息
    
    override fun registeredEvent() {
        mRxBus = RxBus.getIntanceBus()
        mRxBus.doSubscribe(ConfirmOrderActivity::class.java, LiveUserAddressListResponse.ListData::class.java, Consumer { messageItem ->
            addressDate = messageItem
            if (ObjectUtils.isEmpty(messageItem)) {
                return@Consumer
            }
            findViewById<View>(R.id.tv_order_noAddress).visibility = View.GONE
            tv_order_city.text = StringBuffer().append(messageItem.city).append(" ").append(messageItem.area).toString()
            tv_order_address.text = messageItem.address
            tv_order_recipient.text = StringBuffer().append(messageItem.contactPeople).append(" ").append(messageItem.contactMobile).toString()
        })
    }
    
    
    val onClickListener = object : NoDoubleClickListener() {
        override fun onDoubleClick(v: View) {
            when (v) {
                tv_order_pay -> commitOrder()
                cl_choice_address -> AddressManagerActivity.start(this@ConfirmChangeClassActivity, false)
                tv_order_noAddress -> AddressManagerActivity.start(this@ConfirmChangeClassActivity, false)
                
            }
        }
    }
    
    
    private fun commitOrder() {
        showLoadingDialog()
        if (needAddress && ObjectUtils.isEmpty(addressDate)) {
            ToastUtils.showShort("请选择收货地址")
            return
        }
        var request = LiveUserExchangeCreateRequest()
        request.userId = UserManager.getInstance().userId
        if (needAddress && !ObjectUtils.isEmpty(addressDate)) {
            request.addressId = addressDate?.id!!
        }
        request.exchangeFree = orderItem.course.exchangeFree
        request.useBalance = if (cb_balance_pay.isChecked) 1 else 0
        request.changeReason = intent.getStringExtra("reason")
        request.orderItemId = orderItem.id
        request.courseId = orderItem.courseId
        request.orderNO = orderItem.orderNo
        Api.getInstance().post(request, LiveOrderCreateResponse::class.java, object : ResponseCallback<LiveOrderCreateResponse>() {
            override fun onResponse(date: LiveOrderCreateResponse, isFromCache: Boolean) {
                if (isDestroyed) {
                    return
                }
                dismissLoadingDialog()
                if (getString(R.string.back_money) == tv_pay_money.text) {
                    PaySuccessActivity.start(this@ConfirmChangeClassActivity, tv_pay_count.text.toString(), true)
                } else {
                    pay(date)
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
    
    //支付
    private fun pay(date: LiveOrderCreateResponse) {
        if (date.data.hasNoPayOrder) {
            hasNoPayOrder()
        } else {
            if (date.data.dueAmount > BigDecimal.ZERO) {
                val messageItem = MessageItem(MessageItem.order_change, AllOrderFragment.wait_pay)
                RxBus.getIntanceBus().post(messageItem)
                PayActivity.start(this@ConfirmChangeClassActivity, date.data)
                finish()
            } else {
                PaySuccessActivity.start(this@ConfirmChangeClassActivity, tv_balance_save.text.toString())
                finish()
            }
        }
    }
    
    /**
     * 存在未支付订单 下单失败
     */
    private fun hasNoPayOrder() {
        val dialog = TipDialog(this)
        dialog.setTextDes("存在未支付订单")
        dialog.setButton1(getString(R.string.go_pay)) { _, dialog ->
            OrderCenterActivity.start(this@ConfirmChangeClassActivity, 1)
            dialog.dismiss()
            finish()
        }
        dialog.setButton2(getString(R.string.cancel)) { _, dialog -> dialog.dismiss() }
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
    }
    
    private var originPayMoney = BigDecimal.ZERO //差价  +退到余额  - 补交
    
    private var balanceSave = BigDecimal.ZERO //余额抵扣(实际)
    
    private var availBalance = BigDecimal.ZERO //可用余额
    private fun getBalance() {
        val liveUserBalanceInfoRequest = LiveUserBalanceInfoRequest()
        liveUserBalanceInfoRequest.userId = UserManager.getInstance().userId
        Api.getInstance().post(liveUserBalanceInfoRequest, LiveUserBalanceInfoResponse::class.java, object : ResponseCallback<LiveUserBalanceInfoResponse>() {
            override fun onResponse(date: LiveUserBalanceInfoResponse, isFromCache: Boolean) {
                availBalance = date.data.availableBalance
                if (ObjectUtils.isEmpty(availBalance)) {
                    availBalance = BigDecimal.ZERO
                }
                val payMoney = getBalanceLeft()
                if (payMoney >= BigDecimal.ZERO) {
                    balanceSave = this@ConfirmChangeClassActivity.originPayMoney
                    tv_pay_count.text = "¥0.00"
                    if (getString(R.string.back_money) == tv_pay_money.text) {
                        tv_order_left_count.text = getString(R.string.left_count_add, availBalance.toString(), payMoney.abs().toString())
                    } else {
                        tv_order_left_count.text = getString(R.string.left_count, availBalance.toString(), payMoney.abs().toString())
                    }
                } else {
                    balanceSave = date.data.availableBalance
                    tv_order_left_count.text = getString(R.string.left_count, availBalance.toString(), "0.00")
                    tv_pay_count.text = "¥$payMoney"
                }
                if (cb_balance_pay.isChecked) {
                    tv_balance_save.text = "¥${getBalanceCast()}"
                } else {
                    tv_balance_save.text = "¥0.00"
                }
                if (getString(R.string.back_money) == tv_pay_money.text) {
                    tv_pay_count.text = "¥${originPayMoney.abs()}"
                }
                cb_balance_pay.isChecked = true
            }
        }, "")
    }
    
    //剩余余额
    private fun getBalanceLeft(): BigDecimal {
        return availBalance.subtract(originPayMoney)
    }
    
    //余额抵扣
    private fun getBalanceCast(): BigDecimal {
        return if (getBalanceLeft() >= BigDecimal.ZERO) {
            val add = originPayMoney
            if (add < BigDecimal.ZERO) {
                BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_DOWN)
            } else add
        } else {
            availBalance.setScale(2, BigDecimal.ROUND_DOWN)
        }
        
    }
    
    //应付金额
    private fun calculateTotalMoney(): BigDecimal {
        return if (cb_balance_pay.isChecked) {
            originPayMoney.subtract(balanceSave)
        } else {
            originPayMoney
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        if (mRxBus != null) {
            mRxBus.unSubscribe(ConfirmChangeClassActivity::class.java)
        }
    }
    
    companion object {
        fun start(context: Context, needAddress: Boolean, address: OrderAddressDomain?, returnBalance: BigDecimal, orderItem: OrderItemDomain, reason: String?) {
            val intent = Intent(context, ConfirmChangeClassActivity::class.java)
            intent.putExtra("needAddress", needAddress)
            intent.putExtra("returnBalance", returnBalance)
            intent.putExtra("reason", reason)
            intent.putExtra("orderItem", orderItem)
            if (!ObjectUtils.isEmpty(address)) {
                intent.putExtra("address", address)
            }
            context.startActivity(intent)
        }
    }
}
