package com.haoke91.a91edu.ui.order

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.widget.CompoundButton
import android.widget.RelativeLayout

import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.ToastUtils
import com.gaosiedu.live.sdk.android.api.cart.delete.LiveCartDeleteRequest
import com.gaosiedu.live.sdk.android.api.cart.delete.LiveCartDeleteResponse
import com.gaosiedu.live.sdk.android.api.cart.empty.LiveCartEmptyRequest
import com.gaosiedu.live.sdk.android.api.cart.empty.LiveCartEmptyResponse
import com.gaosiedu.live.sdk.android.api.cart.list.LiveCartListRequest
import com.gaosiedu.live.sdk.android.api.cart.list.LiveCartListResponse
import com.gaosiedu.live.sdk.android.api.cart.pre.LiveCartPreRequest
import com.gaosiedu.live.sdk.android.api.cart.pre.LiveCartPreResponse
import com.gaosiedu.live.sdk.android.api.order.pre.LivePreCreateOrderRequest
import com.gaosiedu.live.sdk.android.api.order.pre.LivePreCreateOrderResponse
import com.gaosiedu.live.sdk.android.domain.ShoppingCarDomain
import com.haoke91.a91edu.R
import com.haoke91.a91edu.adapter.CartOrderAdapter
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.ui.BaseActivity
import com.haoke91.a91edu.ui.course.CourseDetailActivity
import com.haoke91.a91edu.ui.course.SpecialClassActivity
import com.haoke91.a91edu.utils.ArithUtils
import com.haoke91.a91edu.utils.Utils
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.a91edu.widget.NoDoubleClickListener
import com.haoke91.baselibrary.recycleview.HorizontalDividerItemDecoration
import com.lzy.okgo.OkGo
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_shoppcart.*
import kotlinx.android.synthetic.main.activity_shoppcart.view.*

import org.apache.commons.lang3.text.StrBuilder

import java.math.BigDecimal
import java.util.Map

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/11 上午11:41
 * 修改人：weiyimeng
 * 修改时间：2018/7/11 上午11:41
 * 修改备注：
 */
class ShoppingCartActivity : BaseActivity() {
    private lateinit var adapter: CartOrderAdapter
    private val onOrderChangeListener = object : CartOrderAdapter.OnOrderChangeListener {
        override fun onOrderChange() {
            calculateTotalMoney()
            //            cb_cart_choice_all.isChecked = adapter.isAllCheck()
        }
        
        
    }
    private val onCheckedChangeListener = CompoundButton.OnCheckedChangeListener { button, isChecked ->
        if (button.isPressed) {
            adapter?.choiceAll(isChecked)
        }
        //        calculateTotalMoney()
    }
    
    private val onClickListener = object : NoDoubleClickListener() {
        override fun onDoubleClick(v: View) {
            when (v.id) {
                R.id.tv_cart_edit -> if (getString(R.string.edit).equals(tv_cart_edit.text.toString().trim { it <= ' ' }, ignoreCase = true)) {
                    tv_cart_edit.text = getString(R.string.complete)
                    adapter.changeEditStatus(true)
                    
                    tv_cart_delete.visibility = View.VISIBLE
                    if (adapter.isHasInvalid) {
                        tv_cart_delete_invalid.visibility = View.VISIBLE
                    } else {
                        tv_cart_delete_invalid.visibility = View.GONE
                    }
                    if (!ObjectUtils.isEmpty(tv_cart_save.text)) {
                        tv_cart_save.visibility = View.GONE
                    }
                    tv_cart_pay.visibility = View.GONE
                    tv_cart_total.visibility = View.GONE
                    tv_total_money.visibility = View.GONE
                    
                } else {
                    adapter.changeEditStatus(false)
                    tv_cart_edit.text = getString(R.string.edit)
                    if (!ObjectUtils.isEmpty(tv_cart_save.text)) {
                        tv_cart_save.visibility = View.VISIBLE
                    }
                    tv_cart_delete.visibility = View.GONE
                    tv_cart_delete_invalid.visibility = View.GONE
                    tv_cart_pay.visibility = View.VISIBLE
                    tv_cart_total.visibility = View.VISIBLE
                    tv_total_money.visibility = View.VISIBLE
                }
                R.id.tv_cart_pay -> {
                    val choiceOrder = adapter.choiceOrder
                    if (ObjectUtils.isEmpty(choiceOrder)) {
                        ToastUtils.showShort("请选择商品")
                        return
                    }
                    if (canCreateOrder) {
                        ToastUtils.showShort("有未开始课程")
                        return
                    }
                    showLoadingDialog()
                    val orderId = StrBuilder()
                    for (set in choiceOrder.entries) {
                        val entry = set as Map.Entry<Int, ShoppingCarDomain>
                        val value = entry.value
                        orderId.append(value.associateId).append(",")
                    }
                    var s = orderId.toString()
                    s = s.substring(0, orderId.toString().length - 1)
                    val response = LivePreCreateOrderRequest()
                    response.userId = UserManager.getInstance().userId
                    response.courseIdsStr = s
                    Api.getInstance().post(response, LivePreCreateOrderResponse::class.java, object : ResponseCallback<LivePreCreateOrderResponse>() {
                        override fun onResponse(date: LivePreCreateOrderResponse, isFromCache: Boolean) {
                            if (isDestroyed) {
                                return
                            }
                            dismissLoadingDialog()
                            if (!ObjectUtils.isEmpty(date.data)) {
                                ConfirmOrderActivity.start(this@ShoppingCartActivity, date.data)
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
                R.id.tv_cart_delete_invalid -> deleteInvalidCourse()
                
                R.id.tv_cart_delete -> deleteChoiceCourse()
                com.haoke91.baselibrary.R.id.empty_button -> {
                    SpecialClassActivity.start(this@ShoppingCartActivity)
                    finish()
                }
            //                R.id.cb_cart_choice_all -> {
            //                    Logger.e("cb_cart_choice_all.isChecked===" + cb_cart_choice_all.isChecked)
            //                    //                    cb_cart_choice_all.isChecked = !cb_cart_choice_all.isChecked
            //                    Logger.e("cb_cart_choice_all.isChecked===" + cb_cart_choice_all.isChecked)
            //                    adapter?.choiceAll(cb_cart_choice_all.isChecked)
            //                }
            
            }
        }
    }
    
    override fun getLayout(): Int {
        return R.layout.activity_shoppcart
    }
    
    
    override fun initialize() {
        val date = intent.getStringExtra("date")
        iv_back.setOnClickListener { finish() }
        emptyView.setOnClickListener(onClickListener)
        tv_cart_delete.setOnClickListener(onClickListener)
        tv_cart_delete_invalid.setOnClickListener(onClickListener)
        tv_cart_pay.setOnClickListener(onClickListener)
        cb_cart_choice_all.setOnCheckedChangeListener(onCheckedChangeListener)
        //        cb_cart_choice_all.setOnClickListener(onClickListener)
        
        tv_cart_edit.setOnClickListener(onClickListener)
        adapter = CartOrderAdapter(this, null)
        adapter.setChoice(date)
        adapter.setOnItemClickListener { _, position ->
            if (position < adapter.all.size) {
                CourseDetailActivity.start(this@ShoppingCartActivity, adapter.all[position].courseDomain.id)
            }
        }
        emptyView.showLoading()
        Api.getInstance().post(LiveCartListRequest(UserManager.getInstance().userId), LiveCartListResponse::class.java, object : ResponseCallback<LiveCartListResponse>() {
            override fun onResponse(date: LiveCartListResponse, isFromCache: Boolean) {
                //                var s1 = Gson().toJson(date)
                setCartListDate(date.data.list)
                emptyView.showContent()
            }
            
            override fun onEmpty(date: LiveCartListResponse, isFromCache: Boolean) {
                emptyView.showEmpty()
            }
            
            override fun onError() {
                emptyView.showError()
                
            }
        }, "")
        
    }
    
    private fun setCartListDate(data: List<ShoppingCarDomain>) {
        //        adapter = new CartOrderAdapter(this, data);
        adapter.setData(data)
        adapter.setOnOrderChangeListener(onOrderChangeListener)
        val linearLayoutManager = LinearLayoutManager(this)
        wr_cart_order_list.layoutManager = linearLayoutManager
        wr_cart_order_list.addItemDecoration(HorizontalDividerItemDecoration.Builder(this).color(
                resources.getColor(R.color.FBFBFB))
                .size(resources.getDimension(R.dimen.dp_4).toInt())
                .build())
        wr_cart_order_list.adapter = adapter
    }
    
    //1 计算未完毕 2 不可以购买 3 可以购买
    private var canCreateOrder: Boolean = false
    private var lastClickTime: Long = 0
    private fun calculateTotalMoney() {
        
        val choicedOrder = adapter.choiceOrder
        if (ObjectUtils.isEmpty(choicedOrder)) {
            tv_total_money.text = "¥0"
            
            (tv_cart_total.layoutParams as? RelativeLayout.LayoutParams)?.removeRule(RelativeLayout.ALIGN_PARENT_TOP)
            (tv_cart_total.layoutParams as? RelativeLayout.LayoutParams)?.addRule(RelativeLayout.CENTER_VERTICAL)
            val layoutParams = tv_total_money.layoutParams as? RelativeLayout.LayoutParams
            layoutParams?.removeRule(RelativeLayout.ALIGN_PARENT_TOP)
            layoutParams?.addRule(RelativeLayout.CENTER_VERTICAL)
            layoutParams?.addRule(RelativeLayout.RIGHT_OF, R.id.tv_cart_total)
            
            tv_cart_save.visibility = View.GONE
            tv_cart_save.text = ""
            return
        }
        
        val curClickTime = System.currentTimeMillis()
        if (curClickTime - lastClickTime < 200) {
            return
        }
        showLoadingDialog(false)
        lastClickTime = curClickTime
        OkGo.getInstance().cancelTag("calculateTotalMoney")
        var request = LiveCartPreRequest()
        request.userId = UserManager.getInstance().userId
        val orderId = StrBuilder()
        for (set in choicedOrder.entries) {
            val entry = set as Map.Entry<Int, ShoppingCarDomain>
            val value = entry.value
            orderId.append(value.associateId).append(",")
        }
        var s = orderId.toString()
        request.courseIdsStr = s.substring(0, orderId.toString().length - 1)
        Api.getInstance().post(request, LiveCartPreResponse::class.java, object : ResponseCallback<LiveCartPreResponse>() {
            override fun onResponse(date: LiveCartPreResponse, isFromCache: Boolean) {
                if (isDestroyed) {
                    return
                }
                dismissLoadingDialog()
                tv_total_money.text = "¥${date.data.dueAmount}"
                canCreateOrder = date.data.notCanCreateOrder
                if (ObjectUtils.isEmpty(date.data.activityAmount) || date.data.activityAmount == BigDecimal.ZERO) {
                    (tv_cart_total.layoutParams as? RelativeLayout.LayoutParams)?.removeRule(RelativeLayout.ALIGN_PARENT_TOP)
                    (tv_cart_total.layoutParams as? RelativeLayout.LayoutParams)?.addRule(RelativeLayout.CENTER_VERTICAL)
                    val layoutParams = tv_total_money.layoutParams as? RelativeLayout.LayoutParams
                    layoutParams?.removeRule(RelativeLayout.ALIGN_PARENT_TOP)
                    layoutParams?.addRule(RelativeLayout.CENTER_VERTICAL)
                    layoutParams?.addRule(RelativeLayout.RIGHT_OF, R.id.tv_cart_total)
                    tv_cart_save.visibility = View.GONE
                    tv_cart_save.text = ""
                } else {
                    (tv_cart_total.layoutParams as? RelativeLayout.LayoutParams)?.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                    val layoutParams = tv_total_money.layoutParams as? RelativeLayout.LayoutParams
                    layoutParams?.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                    layoutParams?.addRule(RelativeLayout.RIGHT_OF, R.id.tv_cart_total)
                    tv_cart_save.visibility = View.VISIBLE
                    tv_cart_save.text = getString(R.string.cart_save, date.data.activityAmount.abs().toString())
                }
                
            }
            
            override fun onError() {
                if (isDestroyed) {
                    return
                }
                dismissLoadingDialog()
                var totalMoney = BigDecimal.ZERO
                for (set in choicedOrder.entries) {
                    val entry = set as kotlin.collections.Map.Entry<Int, ShoppingCarDomain>
                    val value = entry.value
                    totalMoney = ArithUtils.add(totalMoney, value.courseDomain.price)
                }
                tv_total_money.text = "¥$totalMoney"
                val layoutParams = tv_total_money.layoutParams as? RelativeLayout.LayoutParams
                layoutParams?.addRule(RelativeLayout.CENTER_VERTICAL)
                layoutParams?.addRule(RelativeLayout.RIGHT_OF, R.id.tv_cart_total)
                (tv_cart_total.layoutParams as? RelativeLayout.LayoutParams)?.addRule(RelativeLayout.CENTER_VERTICAL)
                tv_cart_save.visibility = View.GONE
            }
            
        }, "calculateTotalMoney")
        
    }
    
    //删除选中课程
    private fun deleteChoiceCourse() {
        val choicedOrder = adapter.choiceOrder
        if (ObjectUtils.isEmpty(choicedOrder)) {
            ToastUtils.showShort("请选择商品")
            return
        }
        showLoadingDialog()
        val orderId = StrBuilder()
        for (set in choicedOrder.entries) {
            val entry = set as Map.Entry<Int, ShoppingCarDomain>
            val value = entry.value
            orderId.append(value.id).append(",")
        }
        var s = orderId.toString()
        s = s.substring(0, orderId.toString().length - 1)
        val liveCartDeleteRequest = LiveCartDeleteRequest()
        liveCartDeleteRequest.ids = s
        liveCartDeleteRequest.userId = UserManager.getInstance().userId
        Api.getInstance().post(liveCartDeleteRequest, LiveCartDeleteResponse::class.java, object : ResponseCallback<LiveCartDeleteResponse>() {
            override fun onResponse(date: LiveCartDeleteResponse, isFromCache: Boolean) {
                if (isDestroyed) {
                    return
                }
                dismissLoadingDialog()
                val all = adapter.all
                if (ObjectUtils.isEmpty(all)) {
                    return
                }
                val iterator = all.iterator()
                while (iterator.hasNext()) {
                    val item = iterator.next()
                    if (choicedOrder.containsKey(item.id)) {
                        iterator.remove()
                        //                        all.remove(item);
                    }
                }
                choicedOrder.clear()
                adapter.setData(all)
                if (ObjectUtils.isEmpty(all)) {
                    emptyView.showEmpty()
                }
                calculateTotalMoney()
                cb_cart_choice_all.isChecked = false
                //                adapter.notifyDataSetChanged();
            }
            
            override fun onError() {
                if (isDestroyed) {
                    return
                }
                dismissLoadingDialog()
            }
        }, "")
    }
    
    //删除过期课程
    private fun deleteInvalidCourse() {
        val liveCartEmptyRequest = LiveCartEmptyRequest()
        liveCartEmptyRequest.userId = UserManager.getInstance().userId
        Api.getInstance().post(liveCartEmptyRequest, LiveCartEmptyResponse::class.java, object : ResponseCallback<LiveCartEmptyResponse>() {
            override fun onResponse(date: LiveCartEmptyResponse, isFromCache: Boolean) {
                val all = adapter.all
                if (ObjectUtils.isEmpty(all)) {
                    return
                }
                //                for (ShoppingCarDomain item : all) {
                //                    if (item.getExpiredStatus() == 0) {
                //                        all.remove(item);
                //                    }
                //                }
                
                val iterator = all.iterator()
                while (iterator.hasNext()) {
                    val item = iterator.next()
                    if (item.expiredStatus == 0) {
                        iterator.remove()
                    }
                }
                adapter.setData(all)
                //                adapter.notifyDataSetChanged();
                
            }
        }, "")
        
    }
    
    companion object {
        
        fun start(context: Context) {
            val intent = Intent(context, ShoppingCartActivity::class.java)
            context.startActivity(intent)
        }
        
        fun start(context: Context, date: String) {
            val intent = Intent(context, ShoppingCartActivity::class.java)
            intent.putExtra("date", date)
            context.startActivity(intent)
        }
    }
}
