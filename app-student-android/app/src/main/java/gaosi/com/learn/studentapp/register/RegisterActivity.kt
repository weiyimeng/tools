package gaosi.com.learn.studentapp.register

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.github.mzule.activityrouter.annotation.Router
import com.gsbaselib.base.bean.BaseData
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.GSHttpResponse
import com.gsbaselib.net.callback.GSJsonCallback
import com.gsbaselib.utils.FileUtil
import com.gsbaselib.utils.LangUtil
import com.gsbaselib.utils.ToastUtil
import com.gsbaselib.utils.dialog.AbsAdapter
import com.gsbaselib.utils.dialog.DialogUtil
import com.gstudentlib.base.BaseActivity
import com.gstudentlib.util.SchemeDispatcher
import com.gstudentlib.util.rxcheck.IRxCheckStatus
import com.gstudentlib.util.rxcheck.IRxCheckStatusListener
import com.gstudentlib.util.rxcheck.RxCheckNetStatus
import com.gstudentlib.util.rxcheck.RxCheckStatusMaster
import com.lzy.okgo.model.Response
import com.r0adkll.slidr.Slidr
import gaosi.com.learn.R
import gaosi.com.learn.application.AppApi
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Pattern

/**
 * 作者：created by 逢二进一 on 2020/2/5 17:20
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
@Router("registerUser")
class RegisterActivity : BaseActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        // 设置右滑动返回
        Slidr.attach(this)
        this.checkSubmitButtonValid()
    }

    override fun initView() {
        super.initView()
        mHandler.postDelayed({ showSoftInput(edt_phone) }, 300)
        title_bar.setLeftListener(this)
        v_security_code.setOnClickListener(this)
        v_security_code.isClickable = false
        btn_login.setOnClickListener(this)
        setEditTextInhibitInputSpaChat(edt_phone , 11)
        edt_phone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkSubmitButtonValid()
                adjustUserIntent(1)
                if (!LangUtil.isEmpty(s)) {
                    iv_clear_phone.visibility = View.VISIBLE
                } else {
                    iv_clear_phone.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        edt_pass.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkSubmitButtonValid()
                adjustUserIntent(2)
                if (!TextUtils.isEmpty(s)) {
                    iv_clear_password.visibility = View.VISIBLE
                } else {
                    iv_clear_password.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        edt_phone.onFocusChangeListener = View.OnFocusChangeListener { _, _ ->
            if (!LangUtil.isEmpty(edt_phone.text)) {
                iv_clear_phone.visibility = View.VISIBLE
            } else {
                iv_clear_phone.visibility = View.GONE
            }
        }
        edt_pass.onFocusChangeListener = View.OnFocusChangeListener { _, _ ->
            if (!TextUtils.isEmpty(edt_pass.text)) {
                iv_clear_password.visibility = View.VISIBLE
            } else {
                iv_clear_password.visibility = View.GONE
            }
        }
        iv_clear_phone.setOnClickListener {
            edt_phone.setText("")
            edt_pass.setText("")
            v_security_code.stop()
            showSoftInput(edt_phone)
        }
        iv_clear_password.setOnClickListener {
            edt_pass.setText("")
            showSoftInput(edt_pass)
        }

        val agreePolicyStr = "请阅读并同意《用户服务协议》《隐私政策》"
        val builder = SpannableStringBuilder(agreePolicyStr)
        val firstString = "《用户服务协议》"
        val secondString = "《隐私政策》"
        val firstIndex = agreePolicyStr.indexOf(firstString)
        val firstLength = firstString.length
        val secondIndex = agreePolicyStr.indexOf(secondString)
        val secondLength = secondString.length
        builder.setSpan(ForegroundColorSpan(Color.parseColor("#1F243D")), firstIndex, firstIndex + firstLength, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        builder.setSpan(ForegroundColorSpan(Color.parseColor("#1F243D")), secondIndex, secondIndex + secondLength, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        //设置文字点击事件
        val clickableSpan1 = object : ClickableSpan() {
            override fun onClick(view: View) {
                var url = "axx://commonWeb?url=%s"
                val filePath = FileUtil.getJsBundleSaveFilePath("agreement_userService.html")
                url = String.format(url, "file://$filePath")
                SchemeDispatcher.jumpPage(this@RegisterActivity, url)
                collectClickEvent("XSD_355")
            }

            override fun updateDrawState(ds: TextPaint) {
                //去除连接下划线
                ds.isUnderlineText = false
            }
        }
        val clickableSpan2 = object : ClickableSpan() {
            override fun onClick(view: View) {
                var url = "axx://commonWeb?url=%s"
                val filePath = FileUtil.getJsBundleSaveFilePath("agreement_privacy.html")
                url = String.format(url, "file://$filePath")
                SchemeDispatcher.jumpPage(this@RegisterActivity, url)
                collectClickEvent("XSD_356")
            }

            override fun updateDrawState(ds: TextPaint) {
                //去除连接下划线
                ds.isUnderlineText = false
            }
        }
        builder.setSpan(clickableSpan1, firstIndex, firstIndex + firstLength, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        builder.setSpan(clickableSpan2, secondIndex, secondIndex + secondLength, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        tvAgreePolicy.movementMethod = LinkMovementMethod.getInstance()
        tvAgreePolicy.highlightColor = ContextCompat.getColor(this, android.R.color.transparent)
        tvAgreePolicy.text = builder
        checkBox.setOnCheckedChangeListener { _, _ ->
            checkSubmitButtonValid()
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.llTitleLeft -> finish()
            R.id.v_security_code -> {
                getSecurityCode()
            }
            R.id.btn_login -> {
                SchemeDispatcher.jumpPage(this@RegisterActivity, "axx://perfectUserInfo?phone=${edt_phone.text}&validateCode=${edt_pass.text}")
            }
        }
    }

    /**
     * 获取验证码
     * 0 发送失败  1 发送成功
     */
    private fun getSecurityCode() {
        if (TextUtils.isEmpty(edt_phone.text) || edt_phone.text.length < 11) {
            ToastUtil.showToast("请输入正确手机号")
            return
        }
        RxCheckStatusMaster
                .addCheckStatus(RxCheckNetStatus())
                .check(object : IRxCheckStatusListener {
                    override fun onCheckPass() {
                        showLoadingProgressDialog()
                        val params = HashMap<String, String>()
                        params["telPhone"] = edt_phone.text.toString()
                        GSRequest.startRequest(AppApi.registerGetValidateCode, params, object : GSJsonCallback<BaseData>() {

                            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<BaseData>) {
                                if (isFinishing || isDestroyed) {
                                    return
                                }
                                dismissLoadingProgressDialog()
                                if(result == null) {
                                    return
                                }
                                if(result.status == 0) {
                                    if(result.code == "AU_20005") {
                                        showTipsDialog()
                                        return
                                    }else {
                                        showResponseErrorMessage(result)
                                    }
                                }else {
                                    v_security_code.setState(true)
                                }
                            }

                            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                                if (isFinishing || isDestroyed) {
                                    return
                                }
                                dismissLoadingProgressDialog()
                                message ?: return
                                ToastUtil.showToast(message + "")
                            }
                        })
                    }

                    override fun onCheckUnPass(iRxCheckStatus: IRxCheckStatus?) {
                        iRxCheckStatus?.let {
                            if(it is RxCheckNetStatus) {
                                ToastUtil.showToast("当前无网络，请检查网络后重试！")
                            }
                        }
                    }
                })
    }

    /**
     * 展示提醒
     */
    private fun showTipsDialog() {
        DialogUtil.getInstance().create(this, R.layout.activity_attend_class_registed , 0.7f)
                .show(object : AbsAdapter() {
                    override fun bindListener(onClickListener: View.OnClickListener?) {
                        this.bindListener(onClickListener, R.id.tvCancel)
                        this.bindListener(onClickListener, R.id.tvConfirm)
                    }

                    override fun onClick(v: View?, dialog: DialogUtil?) {
                        super.onClick(v, dialog)
                        when (v?.id) {
                            R.id.tvConfirm -> {
                                //是
                                dialog?.dismiss()
                                finish()
                            }
                            R.id.tvCancel -> {
                                //否
                                dialog?.dismiss()
                            }
                        }
                    }
                })
    }

    /**
     * 禁止EditText输入空格、特殊符号
     * 设置自定义InputFilter后，xml文件中的maxlength属性失效，需手动添加LengthFilter
     *
     * @param editText 输入框
     * @param length   输入框限制的字符长度
     */
    private fun setEditTextInhibitInputSpaChat(editText: EditText, length: Int) {
        val filter_space = InputFilter { source, start, end, dest, dstart, dend ->
            if (source == " ") {
                ""
            } else {
                null
            }
        }
        val filter_speChat = InputFilter { charSequence, i, i1, spanned, i2, i3 ->
            val speChat = "[`~!@#_$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）— +|{}【】‘；：”“’。，、？]"
            val pattern = Pattern.compile(speChat)
            val matcher = pattern.matcher(charSequence.toString())
            if (matcher.find()) {
                ""
            } else {
                null
            }
        }
        editText.filters = arrayOf(filter_space, filter_speChat, InputFilter.LengthFilter(length))
    }

    /**
     * 自动调整用户的意图
     * 1. 验证码模式下当用户输入11个字符时自动跳到验证码输入框，验证码可点击模式
     * 2. 验证码模式下，用户输入完验证码后自动取消键盘
     */
    fun adjustUserIntent(inputActive: Int) {
        if (1 == inputActive) {
            if (edt_phone.text != null
                    && edt_phone.text.length > 10) {
                v_security_code.setCanUseStatus(true)
            } else {
                v_security_code.setCanUseStatus(false)
            }
            if (edt_phone.text != null
                    && edt_phone.text.length > 10) {
                this.showSoftInput(edt_pass)
            }
        } else if (2 == inputActive) {
            if (edt_pass.text != null
                    && edt_pass.text.length == 6) {
                this.toggleSoftInput()
            }
        }
    }

    /**
     * 键盘显示与隐藏
     */
    private fun toggleSoftInput() {
        try {
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    ?: return
            inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (e: Exception) { }
    }

    /**
     * 显示软键盘
     * @param view
     */
    private fun showSoftInput(view: View?) {
        if (view == null) {
            return
        }
        try {
            view.isFocusable = true
            view.requestFocus()
            view.requestFocusFromTouch()
            val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    ?: return
            inputManager.showSoftInput(view, 0)
        } catch (e: Exception) { }
    }

    /**
     * 检查提交按钮的合法性
     */
    private fun checkSubmitButtonValid() {
        //判断用户名和密码是否合法，只有在合法的情况下，才能点击
        val flag = (edt_phone.text != null && edt_phone.text.length > 10)
                && (edt_pass.text != null && edt_pass.text.length > 5)
        btn_login.isClickable = flag

        var drawable = ContextCompat.getDrawable(this, R.drawable.app_login_shape_unenable)
        if (flag) {
            drawable = ContextCompat.getDrawable(this, R.drawable.app_login_shape_enable)
        }
        btn_login.background = drawable
    }

}