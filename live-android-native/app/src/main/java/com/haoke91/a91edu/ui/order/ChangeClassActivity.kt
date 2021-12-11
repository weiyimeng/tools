package com.haoke91.a91edu.ui.order

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.gaosiedu.live.sdk.android.api.user.order.exchange.pre.LiveUserOrderExchangePreRequest
import com.gaosiedu.live.sdk.android.api.user.order.exchange.pre.LiveUserOrderExchangePreResponse
import com.gaosiedu.live.sdk.android.domain.OrderAddressDomain
import com.gaosiedu.live.sdk.android.domain.OrderItemDomain
import com.haoke91.a91edu.R
import com.haoke91.a91edu.adapter.ChangeClassAdapter
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.ui.BaseActivity
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.a91edu.widget.NoDoubleClickListener
import com.haoke91.a91edu.widget.flowlayout.FlowLayout
import com.haoke91.a91edu.widget.flowlayout.TagAdapter
import com.haoke91.baselibrary.event.RxBus
import com.haoke91.baselibrary.views.richEditText.TopicModel
import com.orhanobut.logger.Logger
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_change_class.*
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/11/13 11:31 AM
 * 修改人：weiyimeng
 * 修改时间：2018/11/13 11:31 AM
 * 修改备注： 转班
 * @version
 */
class ChangeClassActivity : BaseActivity() {
    private lateinit var courseItem: OrderItemDomain //原课程
    private lateinit var changeItem: OrderItemDomain //新课程
    
    override fun onCreate(savedInstanceState: Bundle?) {
        courseItem = intent.getSerializableExtra(ORDER) as OrderItemDomain
        changeItem = intent.getSerializableExtra("changeItem") as OrderItemDomain
        super.onCreate(savedInstanceState)
    }
    
    lateinit var adapter: ChangeClassAdapter
    lateinit var dates: ArrayList<Any>
    override fun initialize() {
        dates = ArrayList()
        dates.add(courseItem)
        dates.add(courseItem.courseId)
        dates.add(changeItem)
        var layoutManager = LinearLayoutManager(this)
        rv_order_list.layoutManager = layoutManager
        rv_order_list.setHasFixedSize(true)
        rv_order_list.isNestedScrollingEnabled = false
        adapter = ChangeClassAdapter(this, dates)
        rv_order_list.adapter = adapter
        
        var strings = resources.getStringArray(R.array.change_class_reason)
        val tagAdapter = object : TagAdapter<String>(strings) {
            override fun getView(parent: FlowLayout, position: Int, s: String?): View {
                val tv = LayoutInflater.from(Utils.getApp()).inflate(R.layout.item_tag_select, parent, false) as TextView
                tv.text = s
                return tv
            }
            
            override fun onSelected(position: Int, view: View) {
                (view as TextView).setTextColor(Color.parseColor("#FF4F00"))
                
                //                view.setBackgroundResource(R.drawable.bg_tag_gray_20)
                //
                //                addReason(strings[position])
            }
            
            override fun unSelected(position: Int, view: View) {
                //                addReason(strings[position])
                //
                (view as TextView).setTextColor(Color.parseColor("#363636"))
                
                //                view.setBackgroundResource(R.drawable.bg_tag_gray_20)
                
            }
        }
        tf_content.adapter = tagAdapter
        tv_payed_money.text = "¥${courseItem.price}" //实付
        tv_service_money.text = "¥${courseItem.materialCost?.setScale(2, BigDecimal.ROUND_DOWN)}" //课程消耗
        tv_commit.setOnClickListener(onclickListener)
        tv_cancel.setOnClickListener(onclickListener)
        getChangeClassMoney()
    }
    
    private var onclickListener = object : NoDoubleClickListener() {
        override fun onDoubleClick(v: View) {
            when (v.id) {
                R.id.tv_commit -> commit()
                R.id.tv_cancel -> finish()
            }
        }
        
    }
    
    private fun commit() {
        if (adapter.all.size != 3) {
            ToastUtils.showShort("请选择退款课程")
            return
        }
        
        var reason = et_evaluate.text?.toString()?.trim()
        val preCheckedList = tf_content.selectedList
        
        if (ObjectUtils.isEmpty(preCheckedList) && ObjectUtils.isEmpty(reason)) {
            ToastUtils.showShort("请填写转班原因")
            return
        }
        var strings = resources.getStringArray(R.array.change_class_reason)
        for (value in preCheckedList) {
            reason += if (TextUtils.isEmpty(reason)) {
                "${strings[value]}"
                
            } else {
                ",${strings[value]}"
            }
        }
        ConfirmChangeClassActivity.start(this, isNeedAddress, orderAddressDomain, returnBalance, adapter.all[2] as OrderItemDomain, reason)
    }
    
    
    //添加退款理由
    private fun addReason(s: String) {
        var topic = TopicModel()
        topic.topicName = s
        et_evaluate.resolveTopicResult(topic)
    }
    
    
    private fun setMoneyDate(date: LiveUserOrderExchangePreResponse) {
        //        tv_payed_money.text = "￥${date.data.orderAmount}" //实付
        //        tv_cast_money.text = "￥${date.data.consumeAmount}"  //消耗金额
        //        tv_service_money.text = "￥${date.data.totalMaterialAmount}" //服务费
        tv_new_course_money.text = "¥${(adapter.all[2] as? OrderItemDomain)?.course?.price}"
        //                tv_active_save.text = "￥${date.data.activityAmount}"   //活动优惠
        //        tv_balance_save.text = "￥${date.data.balance}" //余额抵扣
        tv_except_back.text = "¥${date.data.returnBalance.abs()}" //预退金额
        if (date.data.returnBalance > BigDecimal.ZERO) {
            tv_pay_type.text = getString(R.string.back_balance)
        } else {
            tv_pay_type.text = getString(R.string.extra_money)
            
        }
        isNeedAddress = date.data.needAddress
        returnBalance = date.data.returnBalance
        orderAddressDomain = date.data.orderAddressDomain
        
    }
    
    override fun getLayout(): Int {
        return R.layout.activity_change_class
        
    }
    
    
    override fun registeredEvent() {
        mRxBus = RxBus.getIntanceBus()
        mRxBus.doSubscribe(ConfirmOrderActivity::class.java, OrderItemDomain::class.java, Consumer { messageItem ->
            if (ObjectUtils.isEmpty(messageItem)) {
                return@Consumer
            }
            messageItem.orderNo = courseItem.orderNo
            messageItem.id = courseItem.id
            
            if (!ObjectUtils.isEmpty(dates) && dates.size == 3) {
                dates[2] = messageItem
            } else if (dates != null) {
                dates.add(messageItem)
            }
            adapter.notifyDataSetChanged()
            getChangeClassMoney()
        })
    }
    
    private var isNeedAddress: Boolean = false
    
    private lateinit var returnBalance: BigDecimal
    private var orderAddressDomain: OrderAddressDomain? = null
    
    private fun getChangeClassMoney() {
        if (adapter.all.size != 3) {
            return
        }
        tv_commit.isEnabled = false
        val request = LiveUserOrderExchangePreRequest()
        request.userId = UserManager.getInstance().userId
        request.useBalance = 1
        request.exchangeFree = (adapter.all[2] as OrderItemDomain).course.exchangeFree
        request.courseId = (adapter.all[2] as OrderItemDomain).courseId
        request.orderItemId = courseItem.id
        request.orderNO = courseItem.orderNo
        Api.getInstance().post(request, LiveUserOrderExchangePreResponse::class.java, object : ResponseCallback<LiveUserOrderExchangePreResponse>() {
            override fun onResponse(date: LiveUserOrderExchangePreResponse, isFromCache: Boolean) {
                //                val toJson = Gson().toJson(date)
                tv_commit.isEnabled = true
                setMoneyDate(date)
                
                
            }
        }, "")
        
    }
    
    override fun onDestroy() {
        super.onDestroy()
        if (mRxBus != null) {
            mRxBus.unSubscribe(ConfirmOrderActivity::class.java)
        }
    }
    
    companion object {
        private val ORDER = "orderNo"
        
        fun start(context: Context, item: OrderItemDomain?, changeItem: OrderItemDomain) {
            val intent = Intent(context, ChangeClassActivity::class.java)
            intent.putExtra(ORDER, item)
            intent.putExtra("changeItem", changeItem)
            context.startActivity(intent)
        }
    }
}
