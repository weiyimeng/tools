package com.haoke91.a91edu.ui.login

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.Editable
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.ToastUtils
import com.gaosiedu.live.sdk.android.api.auth.forget.LiveAuthForgetRequest
import com.gaosiedu.live.sdk.android.api.auth.forget.LiveAuthForgetResponse
import com.gaosiedu.live.sdk.android.api.auth.register.LiveAuthRegisterRequest
import com.gaosiedu.live.sdk.android.api.auth.register.LiveAuthRegisterResponse
import com.gaosiedu.live.sdk.android.api.sms.code.LiveMobileCodeRequest
import com.gaosiedu.live.sdk.android.api.sms.code.LiveMobileCodeResponse
import com.haoke91.a91edu.R
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.ui.BaseActivity
import com.haoke91.a91edu.utils.TextWatcher
import com.haoke91.a91edu.utils.Utils
import com.haoke91.a91edu.utils.rsa.RSAUtils
import kotlinx.android.synthetic.main.activity_reset.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.layout_reset_first.*
import kotlinx.android.synthetic.main.layout_reset_second.*
import java.util.*

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/11/23 15:12
 */
class ResetActivity : BaseActivity() {
    
    private var isFirstPage = true
    override fun getLayout(): Int {
        return R.layout.activity_reset
    }
    
    override fun initialize() {
        isFirstPage = true
        toolbar_back.setOnClickListener {
            onBackPressed()
        }
        //手机号
        et_mobile.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            changeLineColor(line_mobile, hasFocus)
            iv_clean_phone.visibility = if (hasFocus && !ObjectUtils.isEmpty(et_mobile.text.toString())) View.VISIBLE else View.GONE
        }
        et_mobile.addTextChangedListener(object : TextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                iv_clean_phone.visibility = if (!s.toString().isEmpty()) View.VISIBLE else View.GONE
                goto_next.isEnabled = checkEnableNextStep()
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
                goto_next.isEnabled = checkEnableNextStep()
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
                goto_reset.isEnabled = checkEnableReset()
                iv_canSee.visibility = if (!ObjectUtils.isEmpty(et_pwd.text.toString())) View.VISIBLE else View.GONE
                iv_clean_pwd.visibility = if (!ObjectUtils.isEmpty(et_pwd.text.toString())) View.VISIBLE else View.GONE
            }
        })
        iv_clean_pwd.setOnClickListener {
            et_pwd.setText("")
        }
        iv_canSee.setOnClickListener {
            iv_canSee.isSelected = !iv_canSee.isSelected
            if (iv_canSee.isSelected) {
                et_pwd.transformationMethod = HideReturnsTransformationMethod.getInstance() //密码可见
            } else {
                et_pwd.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
        iv_canSee2.setOnClickListener {
            iv_canSee2.isSelected = !iv_canSee2.isSelected
            if (iv_canSee2.isSelected) {
                et_pwd2.transformationMethod = HideReturnsTransformationMethod.getInstance() //密码可见
            } else {
                et_pwd2.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
        //2次密码
        et_pwd2.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            changeLineColor(line_pwd2, hasFocus)
            iv_canSee2.visibility = if (hasFocus && !ObjectUtils.isEmpty(et_pwd2.text.toString())) View.VISIBLE else View.GONE
            iv_clean_pwd2.visibility = if (hasFocus && !ObjectUtils.isEmpty(et_pwd2.text.toString())) View.VISIBLE else View.GONE
        }
        et_pwd2.addTextChangedListener(object : TextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                goto_reset.isEnabled = checkEnableReset()
                iv_canSee2.visibility = if (!ObjectUtils.isEmpty(et_pwd2.text.toString())) View.VISIBLE else View.GONE
                iv_clean_pwd2.visibility = if (!ObjectUtils.isEmpty(et_pwd2.text.toString())) View.VISIBLE else View.GONE
            }
        })
        iv_clean_pwd2.setOnClickListener {
            et_pwd2.setText("")
        }
        iv_clean_phone.setOnClickListener { et_mobile.setText("") }
        iv_clean_sms.setOnClickListener { et_sms.setText("") }
        tv_sendSms.setOnClickListener { networkForSms(tv_sendSms) }
        goto_next.setOnClickListener {
            if (checkEnableNextStep())
                gotoSecondStep()
        }
        
        goto_reset.setOnClickListener {
            if (isSameOfPwds()) {
                networkForReset(goto_reset)
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
     * 是否空
     */
    private fun checkEnableNextStep(): Boolean {
        return (!et_mobile.text.toString().isNullOrBlank() && !et_sms.text.toString().isNullOrBlank())
    }
    
    /**
     * 是否空
     */
    private fun checkEnableReset(): Boolean {
        return (!et_pwd.text.toString().isNullOrBlank() && !et_pwd2.text.toString().isNullOrBlank())
    }
    
    /**
     * 密码是否一致
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
                    } else {
                        tv_sendSms.text = "获取验证码"
                        tv_sendSms.isEnabled = true
                        timer.cancel()
                    }
                }
            }
            
        }, 200, 1000)
    }
    
    /**
     * 去确认密码页
     */
    private fun gotoSecondStep() {
        isFirstPage = false
        var obj = ObjectAnimator.ofFloat(reset_1, "translationX", -reset_1.measuredWidth.toFloat()).setDuration(300)
        obj.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }
            
            override fun onAnimationCancel(animation: Animator?) {
            }
            
            override fun onAnimationStart(animation: Animator?) {
                reset_2.visibility = View.VISIBLE
                ObjectAnimator.ofFloat(reset_2, "translationX", reset_1.measuredWidth.toFloat(), 0f).setDuration(300).start()
            }
            
            override fun onAnimationEnd(animation: Animator?) {
                reset_1.visibility = View.GONE
            }
        })
        obj.start()
    }
    
    /**
     * 返回验证码页
     */
    private fun gotoFirstStep() {
        isFirstPage = true
        var obj = ObjectAnimator.ofFloat(reset_2, "translationX", 0f, reset_1.measuredWidth.toFloat()).setDuration(300)
        obj.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }
            
            override fun onAnimationEnd(animation: Animator?) {
                reset_2.visibility = View.GONE
            }
            
            override fun onAnimationCancel(animation: Animator?) {
            }
            
            override fun onAnimationStart(animation: Animator?) {
                reset_1.visibility = View.VISIBLE
                ObjectAnimator.ofFloat(reset_1, "translationX", -reset_1.measuredWidth.toFloat(), 0f).setDuration(300).start()
            }
            
        })
        obj.start()
    }
    
    /**
     * 请求短信
     */
    private fun networkForSms(v_sms: View) {
        
        var mobile = et_mobile.text.toString().trim()
        if (mobile.isEmpty()) {
            ToastUtils.showShort(getString(R.string.mobile_is_empty))
            return
        }
        if (mobile.length != 11) {
            ToastUtils.showShort("请检查手机号是否正确")
            return
        }
        v_sms.isEnabled = false
        var request = LiveMobileCodeRequest()
        request.mobile = et_mobile.text.toString()
        request.type = 2
        Api.getInstance().post(request, LiveMobileCodeResponse::class.java, object : ResponseCallback<LiveMobileCodeResponse>() {
            override fun onResponse(date: LiveMobileCodeResponse?, isFromCache: Boolean) {
                if (Utils.isSuccess(date?.code)) {
                    timerSms(60)
                } else {
                    ToastUtils.showShort(date?.msg)
                }
            }
            
            override fun onError() {
                super.onError()
                ToastUtils.showShort("短信发送失败")
                v_sms.isEnabled = true
            }
    
            override fun onFail(date: LiveMobileCodeResponse?, isFromCache: Boolean) {
                super.onFail(date, isFromCache)
                v_sms.isEnabled=true
            }
            
        }, "for sms")
    }
    
    private fun networkForReset(v_regist: View) {
        v_regist.isEnabled = false
        var request = LiveAuthForgetRequest()
        request.username = RSAUtils.encryptedDataOnJava(et_mobile.text.toString().trim())
        request.mobileCode = et_sms.text.toString().trim()
        request.password = RSAUtils.encryptedDataOnJava(et_pwd.text.toString().trim())
        Api.getInstance().post(request, LiveAuthForgetResponse::class.java, object : ResponseCallback<LiveAuthForgetResponse>() {
            override fun onResponse(date: LiveAuthForgetResponse?, isFromCache: Boolean) {
                if (Utils.isSuccess(date?.code)) {
                    ToastUtils.showShort("重置密码成功")
                    finish()
                } else {
                    ToastUtils.showShort(date?.msg)
                }
                v_regist.isEnabled = true
            }
            
            override fun onFail(date: LiveAuthForgetResponse?, isFromCache: Boolean) {
                super.onFail(date, isFromCache)
                v_regist.isEnabled = true
            }
            
            override fun onEmpty(date: LiveAuthForgetResponse?, isFromCache: Boolean) {
                super.onEmpty(date, isFromCache)
                v_regist.isEnabled = true
            }
            
            override fun onError() {
                super.onError()
                v_regist.isEnabled = true
            }
            
        }, "for regist")
    }
    
    override fun onBackPressed() {
        if (isFirstPage) {
            finish()
        } else {
            gotoFirstStep()
        }
    }
    
    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, ResetActivity::class.java))
        }
    }
}
