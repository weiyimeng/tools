package com.haoke91.a91edu.ui.login

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.ToastUtils
import com.gaosiedu.live.sdk.android.api.auth.login.LiveLoginRequest
import com.haoke91.a91edu.R
import com.haoke91.a91edu.entities.LiveLoginResponse
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.ui.BaseActivity
import com.haoke91.a91edu.utils.AnimationTool
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.a91edu.utils.rsa.RSAUtils
import com.haoke91.baselibrary.event.MessageItem
import com.haoke91.baselibrary.event.RxBus
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/27 上午11:15
 * 修改人：weiyimeng
 * 修改时间：2018/7/27 上午11:15
 * 修改备注：
 */
class LoginActivity : BaseActivity() {
    
    private val onClickListener = View.OnClickListener { v ->
        if (btn_login === v) {
            if (TextUtils.isEmpty(et_mobile.text.toString())) {
                ToastUtils.showShort("请输入手机号码")
                return@OnClickListener
            }
            if (TextUtils.isEmpty(et_password.text.toString())) {
                ToastUtils.showShort("请输入密码")
            }
            
            KeyboardUtils.hideSoftInput(btn_login)
            showLoadingDialog()
            val liveLoginRequest = LiveLoginRequest()
            liveLoginRequest.username = RSAUtils.encryptedDataOnJava(et_mobile.text.toString())
            liveLoginRequest.password = RSAUtils.encryptedDataOnJava(et_password.text.toString())
            Api.getInstance().post(liveLoginRequest, LiveLoginResponse::class.java, object : ResponseCallback<LiveLoginResponse>() {
                override fun onResponse(date: LiveLoginResponse, isFromCache: Boolean) {
                    if (isDestroyed) {
                        return
                    }
                    dismissLoadingDialog()
                    UserManager.getInstance().saveUserInfo(date.data.userProfileDomain)
                    UserManager.getInstance().saveToken(date.data.token)
                    val messageItem = MessageItem(MessageItem.action_login, null)
                    RxBus.getIntanceBus().post(messageItem)
                    finish()
                }
                
                override fun onError() {
                    if (isDestroyed) {
                        return
                    }
                    dismissLoadingDialog()
                }
                
                override fun onFail(date: LiveLoginResponse?, isFromCache: Boolean) {
                    if (isDestroyed) {
                        return
                    }
                    dismissLoadingDialog()
                }
            }, "")
            
        }
    }
    
    override fun getLayout(): Int {
        return R.layout.activity_login
    }
    
    override fun initialize() {
        toolbar_right.text = resources.getString(R.string.regist)
        toolbar_title.visibility = View.GONE
        toolbar_right.visibility = View.VISIBLE
        toolbar_back.setOnClickListener { onBackPressed() }
        //        new AndroidBug5497Workaround(this);
        //        scrollView.setOnTouchListener(new View.OnTouchListener() {
        //            @Override
        //            public boolean onTouch(View v, MotionEvent event) {
        //                return true;
        //            }
        //        });
        toolbar_right.setOnClickListener {
            RegisterActivity.start(this)
            finish()
        }
        tv_forgetPwd.setOnClickListener { ResetActivity.start(this) }
    }
    
    @SuppressLint("ObjectAnimatorBinding")
    override fun initEvent() {
        et_mobile.setOnFocusChangeListener({ _, hasFocus ->
            if (hasFocus) {
                line1.setBackgroundColor(Color.parseColor("#75C82B"))
            } else {
                line1.setBackgroundColor(Color.parseColor("#E9E9E9"))
            }
            
            iv_clean_phone.visibility = if(hasFocus && !ObjectUtils.isEmpty(et_mobile.text.toString())) View.VISIBLE else View.GONE
        })
        et_mobile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            
            }
            
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            
            }
            
            override fun afterTextChanged(s: Editable) {
                if (!TextUtils.isEmpty(s)) {
                    iv_clean_phone!!.visibility = View.VISIBLE
                } else if (TextUtils.isEmpty(s)) {
                    iv_clean_phone!!.visibility = View.GONE
                }
                btn_login.isEnabled = !s.isNullOrEmpty() && !et_password.text.isNullOrEmpty()
            }
        })
        et_password.setOnFocusChangeListener({ _, hasFocus ->
            if (hasFocus) {
                line2.setBackgroundColor(Color.parseColor("#75C82B"))
            } else {
                line2.setBackgroundColor(Color.parseColor("#E9E9E9"))
            }
            iv_show_pwd.visibility = if (hasFocus && !ObjectUtils.isEmpty(et_password.text.toString())) View.VISIBLE else View.GONE
            iv_clean_password.visibility = if (hasFocus && !ObjectUtils.isEmpty(et_password.text.toString())) View.VISIBLE else View.GONE
        })
        et_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            
            }
            
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            
            }
            
            override fun afterTextChanged(s: Editable) {
                btn_login.isEnabled = !TextUtils.isEmpty(s) && !et_mobile.text.toString().isNullOrEmpty()
                iv_show_pwd.visibility = if (!ObjectUtils.isEmpty(et_password.text.toString())) View.VISIBLE else View.GONE
                iv_clean_password.visibility = if (!ObjectUtils.isEmpty(et_password.text.toString())) View.VISIBLE else View.GONE
                
            }
        })
        iv_clean_phone.setOnClickListener { et_mobile.setText("") }
        iv_clean_password.setOnClickListener { et_password.setText("") }
        iv_show_pwd.setOnClickListener {
            iv_show_pwd.isSelected = !iv_show_pwd.isSelected
            if (iv_show_pwd.isSelected) {
                et_password.transformationMethod = HideReturnsTransformationMethod.getInstance() //密码可见
            } else {
                et_password.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
        scrollView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (oldBottom != 0 && bottom != 0 && oldBottom - bottom > 200) {
                val dist = ll_content.bottom - bottom.toFloat()
                if (dist > 0) {
                    val mAnimatorTranslateY = ObjectAnimator.ofFloat(ll_content, "translationY", 0f, -dist)
                    mAnimatorTranslateY.duration = 200
                    mAnimatorTranslateY.interpolator = LinearInterpolator()
                    mAnimatorTranslateY.start()
                    AnimationTool.zoomIn(iv_logo, 0.6f, dist)
                }
                
            } else if (oldBottom != 0 && bottom != 0 && bottom - oldBottom > 200) {
                if (ll_content.bottom - oldBottom > 0) {
                    val mAnimatorTranslateY = ObjectAnimator.ofFloat(ll_content, "translationY", ll_content.translationY, 0f)
                    mAnimatorTranslateY.duration = 200
                    mAnimatorTranslateY.interpolator = LinearInterpolator()
                    mAnimatorTranslateY.start()
                    AnimationTool.zoomOut(iv_logo, 0.6f)
                }
            }
        }
        btn_login!!.setOnClickListener(onClickListener)
    }
    
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            onUserInteraction()
        }
        return if (window.superDispatchTouchEvent(ev)) {
            true
        } else onTouchEvent(ev)
    }
    
    companion object {
        
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
    
}
