package com.haoke91.a91edu.ui.order

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.alipay.sdk.app.H5PayCallback
import com.alipay.sdk.app.PayTask
import com.alipay.sdk.util.H5PayResultModel
import com.blankj.utilcode.constant.TimeConstants
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.gaosiedu.live.sdk.android.api.order.create.LiveOrderCreateResponse
import com.gaosiedu.live.sdk.android.api.pay.ali.LivePayAliRequest
import com.gaosiedu.live.sdk.android.api.pay.ali.LivePayAliResponse
import com.gaosiedu.live.sdk.android.api.pay.wx.LivePayWxRequest
import com.gaosiedu.live.sdk.android.api.pay.wx.LivePayWxResponse
import com.gaosiedu.live.sdk.android.api.pay.wx.WxInfo
import com.google.gson.Gson
import com.haoke91.a91edu.R
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.ui.BaseActivity
import com.haoke91.a91edu.ui.GeneralWebViewActivity
import com.haoke91.a91edu.utils.AliPayUtils
import com.haoke91.a91edu.utils.WXPayUtils
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.a91edu.widget.NoDoubleClickListener
import com.hp.hpl.sparta.xpath.ThisNodeTest
import com.lidroid.xutils.view.annotation.event.OnNothingSelected
import com.orhanobut.logger.Logger
import com.umeng.analytics.game.UMGameAgent.pay
import com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext
import com.umeng.socialize.utils.DeviceConfig.context
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_pay.*
import okhttp3.Response
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.text.SimpleDateFormat
import java.util.HashMap
import java.util.concurrent.TimeUnit

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/9/26 上午11:05
 * 修改人：weiyimeng
 * 修改时间：2018/9/26 上午11:05
 * 修改备注：
 * @version
 */
class PayActivity : BaseActivity() {
    lateinit var date: LiveOrderCreateResponse.ResultData
    override fun onCreate(savedInstanceState: Bundle?) {
        date = intent.getSerializableExtra("date") as LiveOrderCreateResponse.ResultData
        super.onCreate(savedInstanceState)
        
        
    }
    
    override fun getLayout(): Int {
        return R.layout.activity_pay
    }
    
    
    private val onClicker = object : NoDoubleClickListener() {
        override fun onDoubleClick(v: View) {
            when (v) {
                rl_wx_pay -> cb_wx_pay.performClick()
                rl_ali_pay -> cb_ali_pay.performClick()
                tv_order_pay -> pay()
            }
        }
    }
    
    override fun initialize() {
        tv_order_no.text = "订单号:${date.orderNo}"
        tv_pay_count.text = "¥${date.dueAmount}"
        //        tv_time_count.text = date.payTime
        
        rl_wx_pay.setOnClickListener(onClicker)
        rl_ali_pay.setOnClickListener(onClicker)
        tv_order_pay.setOnClickListener(onClicker)
        cb_ali_pay.setOnCheckedChangeListener { _, isCheck ->
            cb_wx_pay.isChecked = false
            cb_ali_pay.isChecked = isCheck
        }
        cb_wx_pay.setOnCheckedChangeListener { _, isCheck ->
            cb_ali_pay.isChecked = false
            cb_wx_pay.isChecked = isCheck
        }
        
        var endLine = TimeUtils.getTimeSpan(date.createTime, date.nowTime, TimeConstants.MSEC)
        
        //        val time = TimeUtils.string2Date(date.createTime).time
        
        endLine += 30 * TimeConstants.MIN
        if (endLine < 0) {
            tv_time_count.text = "00:00"
            
        } else {
            tv_time_count.text = TimeUtils.millis2String(endLine, SimpleDateFormat("mm:ss"))
            timer(endLine)
        }
        
        
    }
    
    var disable: Subscription? = null
    private fun timer(longTime: Long) {
        Flowable.interval(0, 1, TimeUnit.SECONDS) //设置0延迟，每隔一秒发送一条数据
                .onBackpressureBuffer() //加上背压策略
                .take(longTime / 1000) //设置循环次数
                .map { aLong ->
                    longTime - aLong * 1000
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Long> {
                    override fun onSubscribe(s: Subscription?) {
                        disable = s
                        s?.request(Long.MAX_VALUE)
                    }
                    
                    override fun onNext(aLong: Long?) {
                        tv_time_count.text = TimeUtils.millis2String(aLong!!, SimpleDateFormat("mm:ss"))
                        if (aLong < 0) {
                            disable?.cancel()
                        }
                    }
                    
                    override fun onComplete() {
                        
                        disable?.cancel()
                    }
                    
                    override fun onError(t: Throwable?) {
                    
                    }
                })
        
    }
    
    
    private fun pay() {
        when {
            cb_wx_pay.isChecked -> {
                //                val builder = WXPayUtils.WXPayBuilder()
                //                builder.setAppId("123")
                //                        .setPartnerId("56465")
                //                        .setPrepayId("41515")
                //                        .setPackageValue("5153")
                //                        .setNonceStr("5645")
                //                        .setTimeStamp("56512")
                //                        .setSign("54615")
                //                        .build().toWXPayNotSign(this@PayActivity, "123")
                
                wxPay()
            }
            cb_ali_pay.isChecked -> {
                showLoadingDialog()
                var request = LivePayAliRequest()
                request.orderNo = date.orderNo
                request.userId = UserManager.getInstance().userId
                Api.getInstance().post(request, LivePayAliResponse::class.java, object : ResponseCallback<LivePayAliResponse>() {
                    override fun onResponse(date: LivePayAliResponse, isFromCache: Boolean) {
                        if (isDestroyed) {
                            return
                        }
                        dismissLoadingDialog()
                        if ("init" == date?.data?.status) {
                            val payUrl = date.data.data
                            val aliPayInfo = Gson().fromJson(payUrl, LivePayAliResponse.AlipayInfo::class.java)
                            var builder = AliPayUtils.ALiPayBuilder()
                            builder.money = this@PayActivity.date.dueAmount.toString()
                            builder.build().toALiPay(this@PayActivity, aliPayInfo.platformParam)
                        }
                        
                    }
                }, "")
            }
            else -> ToastUtils.showShort("请选择支付方式")
        }
    }
    
    private fun wxPay() {
        showLoadingDialog()
        var request = LivePayWxRequest()
        request.orderNo = date.orderNo
        request.userId = UserManager.getInstance().userId
        Api.getInstance().post(request, LivePayWxResponse::class.java, object : ResponseCallback<LivePayWxResponse>() {
            override fun onResponse(date: LivePayWxResponse?, isFromCache: Boolean) {
                if (isDestroyed) {
                    return
                }
                dismissLoadingDialog()
                if (date?.data?.status == "init") {
                    val data = date?.data?.data
                    val payInfo = Gson().fromJson(data, WxInfo::class.java)
                    //微信地址+&redirect_url=微信回调地址
                    GeneralWebViewActivity.start(this@PayActivity, payInfo?.data, this@PayActivity.date.orderNo, this@PayActivity.date.dueAmount)
                    
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
    
    //    11-28 09:56:25.875 23976-24186/com.haoke91.a91edu I/OkGo: 	body:{"code":"SUCCESS","data":{"data":"{\"data\":{\"nonce_str\":\"GPYfTIgElQxzkkES\",\"mweb_url\":\"https://wx.tenpay.com/cgi-bin/mmpayweb-bin/checkmweb?prepay_id=wx280956258468588ca5b65c2f2575972851&package=4005609763\",\"appid\":\"wx5f8565545eb43142\",\"sign\":\"2A62A01BB6D4422B056497EAAD2C7E2E\",\"trade_type\":\"MWEB\",\"return_msg\":\"OK\",\"result_code\":\"SUCCESS\",\"mch_id\":\"1307262801\",\"return_code\":\"SUCCESS\",\"key\":\"s9lVxL6O0fqNrtsmsfGUjPSoOgdcpf6J\",\"prepay_id\":\"wx280956258468588ca5b65c2f2575972851\"},\"orderNo\":\"701648002941\",\"status\":\"init\"}"},"msg":"成功"}
    
    override fun onDestroy() {
        super.onDestroy()
        disable?.cancel()
    }
    
    companion object {
        fun start(context: Context, date: LiveOrderCreateResponse.ResultData) {
            val intent = Intent(context, PayActivity::class.java)
            intent.putExtra("date", date)
            context.startActivity(intent)
        }
    }
}
