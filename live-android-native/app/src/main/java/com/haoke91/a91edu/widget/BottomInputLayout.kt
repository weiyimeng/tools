package com.haoke91.a91edu.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.blankj.utilcode.util.ToastUtils
import com.gaosiedu.live.sdk.android.api.user.coupon.add.LiveUserCouponAddRequest
import com.gaosiedu.live.sdk.android.api.user.coupon.add.LiveUserCouponAddResponse
import com.gaosiedu.live.sdk.android.api.user.coupon.list.LiveUserCouponListResponse
import com.haoke91.a91edu.R
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.utils.manager.UserManager
import kotlinx.android.synthetic.main.layout_bottom_input.view.*

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/9/25 上午10:33
 * 修改人：weiyimeng
 * 修改时间：2018/9/25 上午10:33
 * 修改备注：
 * @version
 */
class BottomInputLayout : RelativeLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
            defStyleAttr)
    
    init {
        View.inflate(context, R.layout.layout_bottom_input, this)
        tv_exchange.setOnClickListener {
            if (TextUtils.isEmpty(et_exchange_code.text.toString())) {
                ToastUtils.showShort("请输入兑换码")
                return@setOnClickListener
            }
            var request = LiveUserCouponAddRequest()
            request.userId = UserManager.getInstance().userId
            request.couponNo = et_exchange_code.text.toString()
            Api.getInstance().post(request, LiveUserCouponAddResponse::class.java, object : ResponseCallback<LiveUserCouponAddResponse>() {
                override fun onResponse(date: LiveUserCouponAddResponse?, isFromCache: Boolean) {
                    listener?.exchangeSuccess(date!!.data)
                }
                
                override fun onError() {
                    ToastUtils.showShort("兑换失败")
                }
            }, ""
            )
        }
    }
    
    var listener: onExchangeSuccessListener? = null
    
    interface onExchangeSuccessListener {
        fun exchangeSuccess(date: LiveUserCouponListResponse.ListData)
    }
}
