package gaosi.com.learn.studentapp.login

import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.*
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.github.mzule.activityrouter.annotation.Router
import com.gsbaselib.base.bean.BaseData
import com.gsbaselib.base.inject.GSAnnotation
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.GSHttpResponse
import com.gsbaselib.net.callback.GSJsonCallback
import com.gsbaselib.utils.LangUtil
import com.gsbaselib.utils.SharedPreferenceUtil
import com.gsbaselib.utils.StatusBarUtil
import com.gsbaselib.utils.ToastUtil
import com.gsbaselib.utils.dialog.AbsAdapter
import com.gsbaselib.utils.dialog.DialogUtil
import com.gsbiloglib.log.GSLogUtil
import com.gstudentlib.GSAPI
import com.gstudentlib.StatisticsDictionary
import com.gstudentlib.base.BaseActivity
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.util.SchemeDispatcher
import com.lzy.okgo.model.Response
import com.qiyukf.unicorn.api.Unicorn
import java.util.HashMap
import gaosi.com.learn.R
import gaosi.com.learn.bean.login.UpdatePasswordSecurityCodeResponse
import kotlinx.android.synthetic.main.activity_update_password.*
import kotlinx.android.synthetic.main.ui_update_password.*
import java.util.regex.Pattern

/**
 * 验证手机号页面
 * @author pingfu
 */
@Router("updatePassword")
@GSAnnotation(pageId = StatisticsDictionary.updatePsw)
class UpdatePasswordActivity : BaseActivity(), View.OnClickListener {

    /**
     * 用户手机号
     * @return
     */
    val phone: String
        get() = SharedPreferenceUtil.getStringValueFromSP("userInfo", "userPhone", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)
        val phone = phone
        title_bar.setTitle("修改密码")

        tv_verify_phone.text = "验证手机号"
        edt_security_code.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        setEditTextInhibitInputSpaChat(edt_login_phone, 11)
        setEditTextInhibitInputSpaChat(edt_security_code, 6)
        btn_next.text = "下一步"

        edt_login_phone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkSubmitButtonValid()
                if (edt_login_phone.text != null && edt_login_phone.text.length > 10) {
                    showSoftInput(edt_security_code)
                    v_security_code.setCanUseStatus(true)
                } else {
                    v_security_code.setCanUseStatus(false)
                }
                if (!LangUtil.isEmpty(s)) {
                    iv_clear_login_phone.visibility = View.VISIBLE
                } else {
                    iv_clear_login_phone.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        edt_security_code.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkSubmitButtonValid()
                if (edt_security_code.text != null && edt_security_code.text.length == 6) {
                    toggleSoftInput()
                }
                if (!TextUtils.isEmpty(s)) {
                    iv_clear_security_code.visibility = View.VISIBLE
                } else {
                    iv_clear_security_code.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        edt_login_phone.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!LangUtil.isEmpty(edt_login_phone.text)) {
                iv_clear_login_phone.visibility = View.VISIBLE
            } else {
                iv_clear_login_phone.visibility = View.GONE
            }
        }
        edt_security_code.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!TextUtils.isEmpty(edt_security_code.text)) {
                iv_clear_security_code.visibility = View.VISIBLE
            } else {
                iv_clear_security_code.visibility = View.GONE
            }
        }

        title_bar.setLeftListener(this)
        iv_clear_login_phone.setOnClickListener(this)
        iv_clear_security_code.setOnClickListener(this)
        v_security_code.setOnClickListener(this)
        v_security_code.isClickable = false

        btn_login.setOnClickListener(this)
        btn_next.setOnClickListener(this)
        tvQuestion.setOnClickListener(this)
        val intent = intent
        if (intent != null) {
            val title = intent.getStringExtra("title")
            if (!TextUtils.isEmpty(title)) {
                title_bar.setTitle(title)
            }
        }
        v_security_code_card.visibility = View.VISIBLE
        if (STBaseConstants.hasLogin()) {
            setDefaultPhoneNum(STBaseConstants.userInfo.phone)
        } else {
            if (!TextUtils.isEmpty(phone)) {
                changePhoneNum(phone)
            }
        }
        mHandler.postDelayed({
            if (edt_login_phone.text == null || edt_login_phone.text.length < 11) {
                this.showSoftInput(edt_login_phone)
            } else {
                this.showSoftInput(edt_security_code)
            }
        }, 300)
        v_update_password.visibility = View.INVISIBLE
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when {
            v == iv_clear_login_phone -> edt_login_phone.setText("")
            v == iv_clear_security_code -> edt_security_code.setText("")
            v === v_security_code -> getSecurityCode()
            v === btn_next -> checkSecurityCode()
            v === btn_login -> showTips()
            v === tvQuestion -> {
                try {
                    Unicorn.setUserInfo(null)
                } catch (e: Exception) {
                }
                SchemeDispatcher.gotoQiniu(this, null)
            }
            v.id == R.id.llTitleLeft -> finish()
        }
    }

    private fun getSecurityCode() {
        if (TextUtils.isEmpty(inputPhoneNum)) {
            ToastUtil.showToast("请输入手机号")
            return
        }

        val params = HashMap<String, String>()
        params["username"] = inputPhoneNum
        GSRequest.startRequest(GSAPI.getSecuretyCodeUpdate, params, object : GSJsonCallback<UpdatePasswordSecurityCodeResponse>() {

            override fun onResponseSuccess(response: Response<*>, i: Int, result: GSHttpResponse<UpdatePasswordSecurityCodeResponse>) {
                if (isFinishing || isDestroyed) {
                    return
                }
                showResponseErrorMessage(result)
                if (result.body == null) {
                    GSLogUtil.collectPerformanceLog(GSLogUtil.PerformanceType.HTTP_REQUEST_ERROR, mUrl, mResponse)
                    return
                }
                val securityCodeResponse = result.body
                if (securityCodeResponse.isBeiXiao == 1) {
                    //是北校用户，弹框提示到爱学习官网更改密码
                    DialogUtil.getInstance().create(this@UpdatePasswordActivity, R.layout.ui_bx_update_password_tips_dialog)
                            .show(object : AbsAdapter() {
                                override fun bindListener(onClickListener: View.OnClickListener) {
                                    bindListener(onClickListener, R.id.tvConfirm)
                                }

                                override fun onClick(v: View, dialog: DialogUtil) {
                                    super.onClick(v, dialog)
                                    when (v.id) {
                                        R.id.tvConfirm -> {
                                            dialog.dismiss()
                                        }
                                    }
                                }
                            })
                    setCanInputPassword(false)
                } else {
                    //倒计时
                    v_security_code.setState(true)
                    setCanInputPassword(true)
                }
            }

            override fun onResponseError(response: Response<*>, i: Int, message: String) {
                if (isFinishing || isDestroyed) {
                    return
                }
                ToastUtil.showToast(message + "")
            }
        })
    }

    private fun checkSecurityCode() {
        val params = HashMap<String, String>()
        params["username"] = inputPhoneNum
        params["validateCode"] = inputPassword
        GSRequest.startRequest(GSAPI.verityPhone, params, object : GSJsonCallback<BaseData>() {
            override fun onResponseSuccess(response: Response<*>, i: Int, result: GSHttpResponse<BaseData>) {
                if (isFinishing || isDestroyed) {
                    return
                }
                if (showResponseErrorMessage(result) == 1) {
                    showUpdatePasswordView()
                }
            }

            override fun onResponseError(response: Response<*>, i: Int, message: String) {
                if (isFinishing || isDestroyed) {
                    return
                }
                ToastUtil.showToast(message + "")
            }
        })
    }

    /**
     * 检查 下一步 按钮的合法性
     */
    private fun checkSubmitButtonValid() {
        //判断用户名和密码是否合法，只有在合法的情况下，才能点击
        val flag = (edt_login_phone.text != null && edt_login_phone.text.length > 10
                && edt_security_code.text != null && edt_security_code.text.length > 5)
        btn_next.isClickable = flag

        var drawable = ContextCompat.getDrawable(this, R.drawable.app_login_shape_unenable)
        if (flag) {
            drawable = ContextCompat.getDrawable(this, R.drawable.app_login_shape_enable)
        }
        btn_next.background = drawable
    }

    private fun showUpdatePasswordView() {
        val positions = IntArray(2)
        v_security_code_card.getLocationOnScreen(positions)

        v_security_code_card.visibility = View.INVISIBLE
        v_update_password.visibility = View.VISIBLE
    }

    /**
     * 修改密码前
     */
    private fun showTips() {
        val isUpdatePsw = SharedPreferenceUtil.getBooleanValueFromSP("userInfo", inputPhoneNum + "_isUpdatePsw")
        if (isUpdatePsw) {
            updatePassword()
        } else {
            SharedPreferenceUtil.setBooleanDataIntoSP("userInfo", inputPhoneNum + "_isUpdatePsw", true)
            DialogUtil.getInstance().create(this, R.layout.activity_update_password_tips)
                    .show(object : AbsAdapter() {
                        override fun bindListener(onClickListener: View.OnClickListener) {
                            this.bindListener(onClickListener, R.id.tvCancel)
                            this.bindListener(onClickListener, R.id.tvConfirm)
                        }

                        override fun onClick(v: View?, dialog: DialogUtil?) {
                            super.onClick(v, dialog)
                            when (v?.id) {
                                R.id.tvConfirm -> {
                                    dialog?.dismiss()
                                    updatePassword()
                                }
                                R.id.tvCancel -> dialog?.dismiss()
                                else -> {
                                }
                            }
                        }
                    })
        }
    }

    private fun updatePassword() {
        val password = v_update_password.password1
        val password2 = v_update_password.password2
        if (password.length < 6 || password2.length < 6) {
            ToastUtil.showToast("请求输入不少于6位的密码")
            return
        }

        val params = HashMap<String, String>()
        if (STBaseConstants.hasLogin()) {
            params["studentId"] = STBaseConstants.userId
        }

        var username = inputPhoneNum
        if (LangUtil.isEmpty(username) && STBaseConstants.userInfo != null) {
            username = STBaseConstants.userInfo.phone
        }

        if (LangUtil.isEmpty(username)) {
            ToastUtil.showToast("无法得到手机号")
            return
        }

        params["username"] = username
        params["password"] = password
        params["confirmPassword"] = password2
        params["validateCode"] = inputPassword
        GSRequest.startRequest(GSAPI.updatePassword, params, object : GSJsonCallback<BaseData>() {
            override fun onResponseSuccess(response: Response<*>, i: Int, result: GSHttpResponse<BaseData>) {
                if (isFinishing || isDestroyed) {
                    return
                }
                if (showResponseErrorMessage(result) == 1) {
                    ToastUtil.showToast("密码修改成功！")
                    finish()
                }
            }

            override fun onResponseError(response: Response<*>, i: Int, message: String) {
                if (isFinishing || isDestroyed) {
                    return
                }
                ToastUtil.showToast(message + "")
            }
        })
    }

    override fun setStatusBar() {
        this.setStatusBar(resources.getColor(R.color.white), 0)
        StatusBarUtil.setLightMode(this)
    }

    /**
     * 获取账号
     * @return
     */
    val inputPhoneNum: String
        get() = if (TextUtils.isEmpty(edt_login_phone.text)) {
            ""
        } else edt_login_phone.text.toString()

    /**
     * 获取密码或者验证码
     * @return
     */
    private val inputPassword: String
        get() = if (TextUtils.isEmpty(edt_security_code.text)) {
            ""
        } else edt_security_code.text.toString()

    /**
     * 设置默认首页号码
     * @param phoneNum
     */
    private fun setDefaultPhoneNum(phoneNum: String) {
        edt_login_phone.isFocusable = false
        edt_login_phone.isFocusableInTouchMode = false
        edt_login_phone.setText(phoneNum)
        iv_clear_login_phone.visibility = View.GONE
    }

    /**
     * 切换手机号
     * @param phoneNum
     */
    private fun changePhoneNum(phoneNum: CharSequence?) {
        if (phoneNum != null && phoneNum.isNotEmpty() && phoneNum.length <= 11) {
            edt_login_phone.setText(phoneNum)
            edt_login_phone.setSelection(phoneNum.length)
        }
        if (phoneNum != null && phoneNum.length > 10) {
            showSoftInput(edt_security_code)
        } else {
            showSoftInput(edt_login_phone)
        }
    }

    /**
     * 开发password的输入
     * @param canInputPassword
     */
    fun setCanInputPassword(canInputPassword: Boolean) {
        edt_security_code.isFocusable = canInputPassword
        edt_security_code.isFocusableInTouchMode = canInputPassword
        if (canInputPassword) {
            edt_security_code.requestFocus()
        }
    }

    /**
     * 显示软键盘
     * @param view
     */
    private fun showSoftInput(view: View?) {
        view ?: return
        try {
            view.isFocusable = true
            view.requestFocus()
            view.requestFocusFromTouch()
            val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(view, 0)
        } catch (e: Exception) {
        }

    }

    /**
     * 键盘显示与隐藏
     */
    private fun toggleSoftInput() {
        try {
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (e: Exception) {
        }

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
}
