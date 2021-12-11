package com.haoke91.a91edu.ui.login

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.Editable
import android.text.TextPaint
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.view.View
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.ToastUtils
import com.gaosiedu.live.sdk.android.api.auth.register.LiveAuthRegisterRequest
import com.gaosiedu.live.sdk.android.api.auth.register.LiveAuthRegisterResponse
import com.gaosiedu.live.sdk.android.api.sms.code.LiveMobileCodeRequest
import com.gaosiedu.live.sdk.android.api.sms.code.LiveMobileCodeResponse
import com.haoke91.a91edu.R
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.ui.BaseActivity
import com.haoke91.a91edu.ui.GeneralWebViewActivity
import com.haoke91.a91edu.utils.TextWatcher
import com.haoke91.a91edu.utils.Utils
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.a91edu.utils.rsa.RSAUtils
import com.haoke91.baselibrary.event.MessageItem
import com.haoke91.baselibrary.event.RxBus
import kotlinx.android.synthetic.main.activity_regist.*
import kotlinx.android.synthetic.main.common_toolbar.*
import java.util.*

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/11/20 10:10
 */
class RegisterActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.activity_regist
    }
    
    override fun initialize() {
        toolbar_right.text = resources.getString(R.string.login)
        toolbar_right.visibility = View.VISIBLE
        toolbar_right.setOnClickListener {
            LoginActivity.start(this)
            finish()
        }
        initListener()
        var state = SpanUtils().append("我已阅读并同意").append("《用户协议》").setClickSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                GeneralWebViewActivity.start(this@RegisterActivity, "https://file-cdn.91haoke.com/static/other/userAgreement.html")
            }
            
            override fun updateDrawState(ds: TextPaint) {
                ds.color = Color.parseColor("#75C82B")
                ds.bgColor = Color.WHITE
                //                ds.isUnderlineText = true
            }
        }).create()
        cb_protocol.movementMethod = LinkMovementMethod.getInstance()
        
        cb_protocol.text = state!!
    }
    
    /**
     * 监听
     */
    private fun initListener() {
        //手机号
        et_mobile.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            changeLineColor(line_mobile, hasFocus)
            iv_clean_phone.visibility = if (hasFocus && !ObjectUtils.isEmpty(et_mobile.text.toString())) View.VISIBLE else View.GONE
        }
        et_mobile.addTextChangedListener(object : TextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                iv_clean_phone.visibility = if (!s.toString().isEmpty()) View.VISIBLE else View.GONE
                goto_regist.isEnabled = checkEnableRegister()
                if (et_mobile.text?.toString()?.trim()?.length == 11) {
                    tv_sendSms.isEnabled = true
                    tv_sendSms.setTextColor(Color.parseColor("#363636"))
                } else {
                    tv_sendSms.isEnabled = false
                    tv_sendSms.setTextColor(Color.parseColor("#c3c3c3"))
                    
                }
            }
        })
        //短信
        et_sms.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            changeLineColor(line_sms, hasFocus)
            iv_clean_sms.visibility = if (hasFocus && !ObjectUtils.isEmpty(et_sms.text.toString())) View.VISIBLE else View.GONE
        }
        et_sms.addTextChangedListener(object : TextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                iv_clean_sms.visibility = if (!s.toString().isEmpty()) View.VISIBLE else View.GONE
                goto_regist.isEnabled = checkEnableRegister()
            }
        })
        //密码
        et_pwd.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            changeLineColor(line_pwd, hasFocus)
            iv_canSee.visibility = if (hasFocus && !ObjectUtils.isEmpty(et_pwd.text.toString())) View.VISIBLE else View.GONE
            iv_clean_pwd.visibility = if (hasFocus && !ObjectUtils.isEmpty(et_pwd.text.toString())) View.VISIBLE else View.GONE
        }
        et_pwd.addTextChangedListener(object : TextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                goto_regist.isEnabled = checkEnableRegister()
                iv_canSee2.visibility = if (!ObjectUtils.isEmpty(et_pwd2.text.toString())) View.VISIBLE else View.GONE
                iv_clean_pwd2.visibility = if (!ObjectUtils.isEmpty(et_pwd2.text.toString())) View.VISIBLE else View.GONE
            }
        })
        //2次密码
        et_pwd2.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            changeLineColor(line_pwd2, hasFocus)
            iv_canSee2.visibility = if (hasFocus && !ObjectUtils.isEmpty(et_pwd2.text.toString())) View.VISIBLE else View.GONE
            iv_clean_pwd2.visibility = if (hasFocus && !ObjectUtils.isEmpty(et_pwd2.text.toString())) View.VISIBLE else View.GONE
        }
        et_pwd2.addTextChangedListener(object : TextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                goto_regist.isEnabled = checkEnableRegister()
                iv_canSee2.visibility = if (!ObjectUtils.isEmpty(et_pwd2.text.toString())) View.VISIBLE else View.GONE
                iv_clean_pwd2.visibility = if (!ObjectUtils.isEmpty(et_pwd2.text.toString())) View.VISIBLE else View.GONE
            }
        })
        iv_clean_phone.setOnClickListener { et_mobile.setText("") }
        iv_clean_sms.setOnClickListener { et_sms.setText("") }
        iv_clean_pwd.setOnClickListener { et_pwd.setText("") }
        iv_clean_pwd2.setOnClickListener { et_pwd2.setText("") }
        tv_sendSms.setOnClickListener { networkForSms() }
        iv_canSee.setOnClickListener {
            iv_canSee.isSelected = !iv_canSee.isSelected
            if (iv_canSee.isSelected)
                et_pwd.transformationMethod = HideReturnsTransformationMethod.getInstance() //密码可见
            else
                et_pwd.transformationMethod = PasswordTransformationMethod.getInstance()
        }
        iv_canSee2.setOnClickListener {
            iv_canSee2.isSelected = !iv_canSee2.isSelected
            if (iv_canSee2.isSelected)
                et_pwd2.transformationMethod = HideReturnsTransformationMethod.getInstance() //密码可见
            else
                et_pwd2.transformationMethod = PasswordTransformationMethod.getInstance()
            
        }
        goto_regist.setOnClickListener {
            if (!cb_protocol.isChecked) {
                ToastUtils.showShort("请同意用户协议")
                return@setOnClickListener
            }
            if (isSameOfPwds()) {
                networkForRegister(goto_regist)
            }
        }
    }
    
    /**
     * change the line color when has focus
     */
    private fun changeLineColor(v: View, hasFocus: Boolean) {
        if (hasFocus) {
            v.setBackgroundColor(Color.parseColor("#75C82B"))
        } else {
            v.setBackgroundColor(Color.parseColor("#E9E9E9"))
        }
    }
    
    /**
     * 是否可注册
     */
    private fun checkEnableRegister(): Boolean {
        return (!et_mobile.text.toString().isNullOrBlank() && !et_sms.text.toString().isNullOrBlank() && !et_pwd.text.toString().isNullOrBlank() && !et_pwd2.text.toString().isNullOrBlank())
    }
    
    /**
     *
     */
    private fun isSameOfPwds(): Boolean {
        
        var pwd1 = et_pwd.text.toString()
        var pwd2 = et_pwd2.text.toString()
        return if ((pwd1.isNullOrBlank() || pwd2.isNullOrBlank()) || pwd1 == pwd2) {
            true
        } else {
            ToastUtils.showShort("两次输入密码请保持一致")
            false
        }
    }
    
    /**
     * 短信计时器
     */
    private fun timerSms(seconds: Int) {
        var timer = Timer()
        var current = seconds
        timer.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    if (current > 0) {
                        tv_sendSms.isEnabled = false
                        tv_sendSms.text = "重新获取（${current--}）"
                        tv_sendSms.setTextColor(Color.parseColor("#c3c3c3"))
                        
                    } else {
                        tv_sendSms.text = "获取验证码"
                        tv_sendSms.isEnabled = true
                        tv_sendSms.setTextColor(Color.parseColor("#363636"))
                        
                        timer.cancel()
                    }
                }
            }
            
        }, 200, 1000)
    }
    
    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, RegisterActivity::class.java))
        }
    }
    
    /**
     * 请求短信
     */
    private fun networkForSms() {
        
        var mobile = et_mobile.text.toString().trim()
        if (mobile.isEmpty()) {
            ToastUtils.showShort(getString(R.string.mobile_is_empty))
            return
        }
        if (mobile.length != 11) {
            ToastUtils.showShort("请检查手机号是否正确")
            return
        }
        tv_sendSms.isEnabled = false
        tv_sendSms.setTextColor(Color.parseColor("#c3c3c3"))
        
        var request = LiveMobileCodeRequest()
        request.mobile = et_mobile.text.toString()
        request.type = 1
        Api.getInstance().post(request, LiveMobileCodeResponse::class.java, object : ResponseCallback<LiveMobileCodeResponse>() {
            override fun onResponse(date: LiveMobileCodeResponse?, isFromCache: Boolean) {
                if (Utils.isSuccess(date?.code)) {
                    timerSms(60)
                } else {
                    ToastUtils.showShort(date?.msg)
                    tv_sendSms.isEnabled = true
                    tv_sendSms.setTextColor(Color.parseColor("#363636"))
                    
                }
            }
            
            override fun onFail(date: LiveMobileCodeResponse?, isFromCache: Boolean) {
                tv_sendSms.isEnabled = true
                tv_sendSms.setTextColor(Color.parseColor("#363636"))
                
            }
            
            override fun onEmpty(date: LiveMobileCodeResponse?, isFromCache: Boolean) {
                super.onEmpty(date, isFromCache)
                tv_sendSms.isEnabled = true
                tv_sendSms.setTextColor(Color.parseColor("#363636"))
                
            }
            
            override fun onError() {
                super.onError()
                ToastUtils.showShort("短信发送失败")
                tv_sendSms.isEnabled = true
                tv_sendSms.setTextColor(Color.parseColor("#363636"))
                
            }
            
        }, "for sms")
    }
    
    private fun networkForRegister(v_register: View) {
        v_register.isEnabled = false
        Utils.loading(this)
        var request = LiveAuthRegisterRequest()
        request.username = RSAUtils.encryptedDataOnJava(et_mobile.text.toString().trim())
        request.password = RSAUtils.encryptedDataOnJava(et_pwd.text.toString().trim())
        request.mobileCode = et_sms.text.toString().trim()
        Api.getInstance().post(request, LiveAuthRegisterResponse::class.java, object : ResponseCallback<LiveAuthRegisterResponse>() {
            override fun onResponse(date: LiveAuthRegisterResponse?, isFromCache: Boolean) {
                if (Utils.isSuccess(date?.code)) {
                    ToastUtils.showShort("注册成功")
                    UserManager.getInstance().saveToken(date?.data?.token)
                    val messageItem = MessageItem(MessageItem.action_login, null)
                    RxBus.getIntanceBus().post(messageItem)
                    v_register.postDelayed({
                        finish()
                    }, 2000)
                } else {
                    ToastUtils.showShort(date?.msg)
                }
                v_register.isEnabled = true
            }
            
            override fun onEmpty(date: LiveAuthRegisterResponse?, isFromCache: Boolean) {
                super.onEmpty(date, isFromCache)
                v_register.isEnabled = true
            }
            
            override fun onError() {
                super.onError()
                v_register.isEnabled = true
                ToastUtils.showShort("连接异常")
            }
            
        }, "")
    }
}
