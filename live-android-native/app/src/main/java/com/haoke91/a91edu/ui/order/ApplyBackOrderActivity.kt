package com.haoke91.a91edu.ui.order

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.ArrayMap
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.gaosiedu.live.sdk.android.api.user.order.returned.create.LiveUserOrderReturnedCreateRequest
import com.gaosiedu.live.sdk.android.api.user.order.returned.create.LiveUserOrderReturnedCreateResponse
import com.gaosiedu.live.sdk.android.api.user.order.returned.pre.LiveUserOrderReturnedPreRequest
import com.gaosiedu.live.sdk.android.api.user.order.returned.pre.LiveUserOrderReturnedPreResponse
import com.gaosiedu.live.sdk.android.api.user.order.returned.preCalc.LiveUserOrderReturnedPreCalcRequest
import com.gaosiedu.live.sdk.android.base.ApiResponse
import com.gaosiedu.live.sdk.android.domain.OrderItemDomain
import com.google.gson.Gson
import com.haoke91.a91edu.R
import com.haoke91.a91edu.adapter.ApplyBackOrderAdapter
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.ui.BaseActivity
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.a91edu.widget.NoDoubleClickListener
import com.haoke91.a91edu.widget.dialog.ConfirmBackDialog
import com.haoke91.a91edu.widget.flowlayout.FlowLayout
import com.haoke91.a91edu.widget.flowlayout.TagAdapter
import com.haoke91.baselibrary.utils.ACallBack
import com.haoke91.baselibrary.views.richEditText.TopicModel
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_apply_order.*
import org.apache.commons.lang3.text.StrBuilder
import java.util.Map

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/11/13 11:31 AM
 * 修改人：weiyimeng
 * 修改时间：2018/11/13 11:31 AM
 * 修改备注：
 * @version
 */
class ApplyBackOrderActivity : BaseActivity() {
    private var orderId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        orderId = intent.getIntExtra(ORDER, -1)
        super.onCreate(savedInstanceState)
    }
    
    override fun initialize() {
        empty_view.showLoading()
        val request = LiveUserOrderReturnedPreRequest()
        request.userId = UserManager.getInstance().userId
        request.orderId = orderId
        Api.getInstance().post(request, LiveUserOrderReturnedPreResponse::class.java, object : ResponseCallback<LiveUserOrderReturnedPreResponse>() {
            override fun onResponse(date: LiveUserOrderReturnedPreResponse, isFromCache: Boolean) {
                empty_view.showContent()
                setDate(date)
            }
        }, "")
        var strings = resources.getStringArray(R.array.apply_back_reason)
        val tagAdapter = object : TagAdapter<String>(strings) {
            override fun getView(parent: FlowLayout, position: Int, s: String): View {
                val tv = LayoutInflater.from(Utils.getApp()).inflate(R.layout.item_tag_select, parent, false) as TextView
                tv.text = s
                return tv
            }
            
            override fun onSelected(position: Int, view: View) {
                //                view.isSelected = true
                //                (view as TextView).setTextColor(Color.parseColor("#FF4F00"))
                //                view.setBackgroundResource(R.drawable.bg_tag_gray_20)
                (view as TextView).setTextColor(Color.parseColor("#FF4F00"))
                
                //                addReason(strings[position])
            }
            
            override fun unSelected(position: Int, view: View) {
                //                addReason(strings[position])
                
                //                (view as TextView).setTextColor(Color.parseColor("#363636"))
                //                view.setBackgroundResource(R.drawable.bg_tag_gray_20)
                (view as TextView).setTextColor(Color.parseColor("#363636"))
                
                //
                //                view.setSelected(false)
                
            }
        }
        tf_content.adapter = tagAdapter
        rg_back_method.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.back_origin -> {
                    back_origin.setTextColor(Color.parseColor("#363636"))
                    back_hank.setTextColor(Color.parseColor("#979797"))
                    back_balance.setTextColor(Color.parseColor("#979797"))
                    backMethod = 1
                }
                R.id.back_hank -> {
                    back_origin.setTextColor(Color.parseColor("#979797"))
                    back_hank.setTextColor(Color.parseColor("#363636"))
                    back_balance.setTextColor(Color.parseColor("#979797"))
                    backMethod = 3
                    
                }
                R.id.back_balance -> {
                    back_origin.setTextColor(Color.parseColor("#979797"))
                    back_hank.setTextColor(Color.parseColor("#979797"))
                    back_balance.setTextColor(Color.parseColor("#363636"))
                    backMethod = 2
                    
                }
                else -> {
                    backMethod = 0
                    
                }
            }
        }
        
        tv_commit.setOnClickListener(onclickListener)
        tv_cancel.setOnClickListener(onclickListener)
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
        if (backMethod == 0) {
            ToastUtils.showShort("请选择退款方式")
            return
        }
        val arrayMap = adapter!!.choiceOrder
        if (ObjectUtils.isEmpty(arrayMap)) {
            ToastUtils.showShort("请选择退款课程")
            return
        }
        if (ObjectUtils.isEmpty(tf_content.selectedList) && ObjectUtils.isEmpty(et_evaluate.text.toString().trim())) {
            ToastUtils.showShort("请填写退款原因")
            return
        }
        //        * 用户选择退款方式，1：原路返回，2余额 3退至银行卡
        
        var returnWay = when (backMethod) {
            1 -> "原路返回"
            2 -> "退到余额"
            else -> "退至银行卡"
        }
        
        
        ConfirmBackDialog.showDialog(this, ACallBack { type ->
            when (type) {
                "2" -> commitApply(arrayMap as ArrayMap<Int, OrderItemDomain>)
            }
        }, returnWay, tv_except_back.text?.toString())
    }
    
    private fun commitApply(arrayMap: ArrayMap<Int, OrderItemDomain>) {
        var request = LiveUserOrderReturnedCreateRequest()
        request.userId = UserManager.getInstance().userId
        request.orderId = orderId
        var strings = resources.getStringArray(R.array.apply_back_reason)
        var reason = et_evaluate.text.toString().trim()
        for (value in tf_content.selectedList) {
            reason += if (TextUtils.isEmpty(reason)) {
                "${strings[value]}"
                
            } else {
                ",${strings[value]}"
            }
        }
        request.reason = reason
        request.returnWays = backMethod
        var courseId = StrBuilder()
        for (set in arrayMap.entries) {
            val entry = set as Map.Entry<Int, OrderItemDomain>
            val value = entry.value
            courseId.append(value.id).append(",")
        }
        var s = courseId.toString()
        if (!ObjectUtils.isEmpty(s)) {
            s = s.substring(0, courseId.toString().length - 1)
            request.orderItemIdsStr = s
        }
        
        if (backMethod == 3) {
            SetBankInfoActivity.start(this, request, orderId, tv_except_back.text?.toString())
            return
        }
        applyBackOrder(request)
    }
    
    
    private fun applyBackOrder(request: LiveUserOrderReturnedCreateRequest) {
        request.path = "user/order/return/create"
        showLoadingDialog()
        Api.getInstance().post(request, ApiResponse::class.java, object : ResponseCallback<ApiResponse>() {
            override fun onResponse(date: ApiResponse?, isFromCache: Boolean) {
                if (isDestroyed) {
                    return
                }
                dismissLoadingDialog()
                PaySuccessActivity.start(this@ApplyBackOrderActivity, "", true)
            }
            
            override fun onError() {
                if (isDestroyed) {
                    return
                }
                dismissLoadingDialog()
            }
        }, "")
    }
    
    private var backMethod: Int = 0
    
    //添加退款理由
    private fun addReason(s: String) {
        var topic = TopicModel()
        topic.topicName = s
        et_evaluate.resolveTopicResult(topic)
    }
    
    private var onOrderChangeListener = ACallBack<OrderItemDomain> {
        val arrayMap = adapter!!.choiceOrder
        if (ObjectUtils.isEmpty(arrayMap)) {
            tv_cast_money.text = "¥0"   //消耗金额
            tv_except_back.text = "¥0"  //预退金额
            
            return@ACallBack
        }
        
        var courseId = StrBuilder()
        for (set in arrayMap.entries) {
            val entry = set as Map.Entry<Int, OrderItemDomain>
            val value = entry.value
            courseId.append(value.id).append(",")
        }
        var s = courseId.toString()
        var request = LiveUserOrderReturnedPreCalcRequest()
        request.userId = UserManager.getInstance().userId
        request.orderId = orderId
        if (!ObjectUtils.isEmpty(s)) {
            s = s.substring(0, courseId.toString().length - 1)
            request.orderItemIdsStr = s
        }
        Api.getInstance().post(request, LiveUserOrderReturnedPreResponse::class.java, object : ResponseCallback<LiveUserOrderReturnedPreResponse>() {
            override fun onResponse(date: LiveUserOrderReturnedPreResponse, isFromCache: Boolean) {
                //                    val toJson = Gson().toJson(date)
                setMoneyDate(date)
            }
        }, "")
        
    }
    
    lateinit var adapter: ApplyBackOrderAdapter
    private fun setDate(date: LiveUserOrderReturnedPreResponse) {
        var layoutManager = LinearLayoutManager(this)
        rv_order_list.layoutManager = layoutManager
        rv_order_list.setHasFixedSize(true)
        rv_order_list.isNestedScrollingEnabled = false
        adapter = ApplyBackOrderAdapter(this, date.data.orderItemList)
        adapter.setOnOrderChangeListener(onOrderChangeListener)
        rv_order_list.adapter = adapter
        setMoneyDate(date)
        
        when (date.data.payWayId) {
            1, 2 -> {
                if (date.data.amount > date.data.retiredAmount) {
                    if (date.data.expireStatus == 1) { //未过期
                        back_hank.visibility = View.GONE
                        back_balance.visibility = View.GONE
                    } else {
                        back_origin.visibility = View.GONE
                    }
                } else {
                    back_hank.visibility = View.GONE
                    back_origin.visibility = View.GONE
                }
            }
        //余额
            else -> {
                back_hank.visibility = View.GONE
                back_origin.visibility = View.GONE
            }
            
        }
        
    }
    
    private fun setMoneyDate(date: LiveUserOrderReturnedPreResponse) {
        tv_payed_money.text = "¥${date.data.amount + date.data.balance}" //实付
        tv_back_order_money.text = "¥${date.data.retiredAmount}" //已退金额
        tv_cast_money.text = "¥${date.data.consumeAmount}"  //消耗金额
        tv_service_money.text = "¥${date.data.totalMaterialAmount}" //服务费
        //        tv_active_save.text = "￥" + date.data.activityAmount //活动优惠
        //        tv_balance_save.text = "￥" + date.data.balance //余额抵扣
        tv_except_back.text = "¥${date.data.returnPrice}" //预退金额
    }
    
    override fun getLayout(): Int {
        return R.layout.activity_apply_order
        
    }
    
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            var request = data?.getSerializableExtra(SetBankInfoActivity.Companion.REQUEST) as LiveUserOrderReturnedCreateRequest
            applyBackOrder(request)
        }
    }
    
    companion object {
        private val ORDER = "orderNo"
        
        fun start(context: Context, orderNo: Int) {
            val intent = Intent(context, ApplyBackOrderActivity::class.java)
            intent.putExtra(ORDER, orderNo)
            context.startActivity(intent)
        }
    }
}
