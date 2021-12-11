package gaosi.com.learn.view

import android.content.Context
import android.content.res.Configuration
import android.text.*
import android.util.AttributeSet
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import com.gaosi.passport.Passport
import com.gaosi.passport.PassportAPI
import com.gaosi.passport.bean.BaseResponseBean
import com.gsbaselib.utils.LangUtil
import com.gsbaselib.utils.SharedPreferenceUtil
import com.gsbaselib.utils.ToastUtil
import com.gsbiloglib.builder.GSConstants
import com.gsbiloglib.log.GSLogUtil
import com.gstudentlib.util.SchemeDispatcher
import gaosi.com.learn.R
import gaosi.com.learn.studentapp.login.LoginActivity
import kotlinx.android.synthetic.main.ui_login_card.view.*
import java.util.regex.Pattern

/**
 * Created by pingfu on 2018/4/24.
 */
class LoginCardView : LinearLayout {

    //默认显示那种类型的登录方式：0代表默认显示密码登录，1代表默认显示验证码登录
    private var mLoginType = CODE_TYPE
    private var mRegisterType = CLASS_CODE_TYPE
    //是否显示一键登录
    private var mIsShowOneKeyLogin = false

    /**
     * 获取账号
     * @return
     */
    val inputPhoneNum: String
        get() = if (TextUtils.isEmpty(edt_phone.text)) {
            ""
        } else edt_phone.text.toString()

    /**
     * 获取密码或者验证码
     * @return
     */
    val inputPassword: String
        get() = if (TextUtils.isEmpty(edt_pass.text)) {
            ""
        } else edt_pass.text.toString()

    /**
     * 获取密码登陆时的验证码
     */
    val inputPasswordVerification: String
        get() = if (TextUtils.isEmpty(edt_password_verification.text)) {
            ""
        } else edt_password_verification.text.toString()

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    /**
     * 初始化布局view
     */
    private fun initView() {
        View.inflate(context, R.layout.ui_login_card, this)
        //如果是pad隐藏其他登录入口
        if ((resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
            llOtherLogin.visibility = View.GONE
        }
        btn_login.isClickable = false
        this.initRegister()
        this.initOneKeyLogin()
        this.initListener()
        v_security_code.isClickable = false
        v_password_verification_code.isClickable = false
    }

    /**
     * 初始化注册类型
     */
    private fun initRegister() {
        mRegisterType = SharedPreferenceUtil.getIntValueFromSP("userInfo" , "_registerType" , CLASS_CODE_TYPE)
        if(mRegisterType == CLASS_CODE_TYPE) {
            ivAttendClassLabel.setImageResource(R.drawable.app_icon_class_code_label)
            tvAttendClassRoom.text = "没有账号？使用班级码注册一个吧"
        }else if(mRegisterType == C_TYPE) {
            ivAttendClassLabel.setImageResource(R.drawable.app_icon_register_label)
            tvAttendClassRoom.text = "没有账号？立即注册"
        }
    }

    /**
     * 初始化一键登录状态
     */
    private fun initOneKeyLogin() {
        mIsShowOneKeyLogin = SharedPreferenceUtil.getBooleanValueFromSP("userInfo" , "_showOneKeyLogin" , false)
        if (mIsShowOneKeyLogin) {
            llOtherLogin.visibility = View.VISIBLE
        } else {
            llOtherLogin.visibility = View.GONE
        }
    }

    /**
     * 初始化监听事件
     */
    private fun initListener() {
        edt_phone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkSubmitButtonValid()
                adjustUserIntent(1)
                if (!LangUtil.isEmpty(s)) {
                    iv_clear_phone.visibility = View.VISIBLE
                } else {
                    iv_clear_phone.visibility = View.GONE

                    v_password_verification_code.stop()
                    edt_password_verification.setText("")
                    ll_password_verification.visibility = View.GONE
                    tv_tips_verification_code.visibility = View.GONE
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
        edt_password_verification.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, end: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, end: Int) {
                checkSubmitButtonValid()
                adjustUserIntent(2)
                if (!TextUtils.isEmpty(s)) {
                    iv_clear_password_verification.visibility = View.VISIBLE
                } else {
                    iv_clear_password_verification.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        edt_phone.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!LangUtil.isEmpty(edt_phone.text)) {
                iv_clear_phone.visibility = View.VISIBLE
            } else {
                iv_clear_phone.visibility = View.GONE
            }
        }
        edt_pass.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!TextUtils.isEmpty(edt_pass.text)) {
                iv_clear_password.visibility = View.VISIBLE
            } else {
                iv_clear_password.visibility = View.GONE
            }
        }
        edt_password_verification.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!TextUtils.isEmpty(edt_password_verification.text)) {
                iv_clear_password_verification.visibility = View.VISIBLE
            } else {
                iv_clear_password_verification.visibility = View.GONE
            }
        }
        v_security_code.setOnClickListener {
            getSecurityCode()
        }
        v_password_verification_code.setOnClickListener {
            getSecurityCode()
        }
        iv_clear_phone.setOnClickListener {
            edt_phone.setText("")
            showSoftInput(edt_phone)
        }
        iv_clear_password.setOnClickListener {
            edt_pass.setText("")
            showSoftInput(edt_pass)
        }
        iv_clear_password_verification.setOnClickListener {
            edt_password_verification.setText("")
            showSoftInput(edt_password_verification)
        }
        tvForgetPsw.setOnClickListener { SchemeDispatcher.jumpPage(context as LoginActivity, "axx://updatePassword?title=找回密码") }
        llAttendClassRoom.setOnClickListener {
            if(mRegisterType == CLASS_CODE_TYPE) {
                SchemeDispatcher.jumpPage(context as LoginActivity, "axx://activeClassRome")
                GSLogUtil.collectClickLog(GSConstants.P?.getCurrRefer(), "as2_clk_login_joinclass", "")
            }else if(mRegisterType == C_TYPE) {
                SchemeDispatcher.jumpPage(context as LoginActivity, "axx://registerUser")
            }
        }
    }

    /**
     * 设置验证码登录时获取验证码的状态
     * @param hasPressed
     */
    fun setLoginStatus(codeView: SecurityCodeView, hasPressed: Boolean) {
        codeView.setState(hasPressed)
    }

    /**
     * 切换密码登录
     */
    fun showPasswordLoginView() {
        edt_pass.setHint(R.string.login_pass_hint)
        v_security_code.visibility = View.GONE
        setEditTextInhibitInputSpaChat(edt_phone, 11)
        setEditTextInhibitInputSpaChat(edt_pass, 16)
        setEditTextInhibitInputSpaChat(edt_password_verification, 6)
        this.mLoginType = PASS_TYPE
    }

    /**
     * 显示注册功能
     */
    fun showRegisterView(type : Int) {
        this.mRegisterType = type
        SharedPreferenceUtil.setIntDataIntoSP("userInfo" , "_registerType" , mRegisterType)
        this.initRegister()
    }

    /**
     * 显示一键登录
     */
    fun showOneKeyLoginView(show: Boolean) {
        this.mIsShowOneKeyLogin = show
        SharedPreferenceUtil.setBooleanDataIntoSP("userInfo" , "_showOneKeyLogin" , mIsShowOneKeyLogin)
        this.initOneKeyLogin()
    }

    /**
     * 切换验证码登录
     */
    fun showSecurityCodeView() {
        edt_pass.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        setEditTextInhibitInputSpaChat(edt_phone, 11)
        setEditTextInhibitInputSpaChat(edt_pass, 6)
        this.mLoginType = CODE_TYPE
    }

    /**
     * 切换手机号
     * @param phoneNum
     */
    fun changePhoneNum(phoneNum: CharSequence?) {
        if (phoneNum != null && phoneNum.isNotEmpty() && phoneNum.length <= 11) {
            edt_phone.setText(phoneNum)
            edt_phone.setSelection(phoneNum.length)
        }
        if (phoneNum != null && phoneNum.length > 10) {
            this.showSoftInput(edt_pass)
        } else {
            this.showSoftInput(edt_phone)
        }
    }

    /**
     * 检查提交按钮的合法性
     */
    fun checkSubmitButtonValid() {
        //判断用户名和密码是否合法，只有在合法的情况下，才能点击
        val flag = if (ll_password_verification.visibility == View.VISIBLE) {
            (edt_phone.text != null && edt_phone.text.length > 10
                    && edt_password_verification.text != null && edt_password_verification.text.length > 5 && (context as LoginActivity).policyCheckedStatus)
        } else {
            (edt_phone.text != null && edt_phone.text.length > 10
                    && edt_pass.text != null && edt_pass.text.length > 5 && (context as LoginActivity).policyCheckedStatus)
        }
        btn_login.isClickable = flag

        if (flag) {
            btn_login.setImageResource(R.drawable.app_icon_login_light)
        } else {
            btn_login.setImageResource(R.drawable.app_icon_login_default)
        }
    }

    /**
     * 开发password的输入
     * @param canInputPassword
     */
    fun setCanInputPassword(canInputPassword: Boolean) {
        edt_pass.isFocusable = canInputPassword
        edt_pass.isFocusableInTouchMode = canInputPassword
        if (canInputPassword) {
            edt_pass.requestFocus()
        }
    }

    /**
     * 设置默认首页号码
     * @param phoneNum
     */
    fun setDefaultPhoneNum(phoneNum: String) {
        edt_phone.isFocusable = false
        edt_phone.isFocusableInTouchMode = false
        edt_phone.setText(phoneNum)
        iv_clear_phone.visibility = View.GONE
    }

    /**
     * 自动调整用户的意图
     * 1. 验证码模式下当用户输入11个字符时自动跳到验证码输入框，验证码可点击模式
     * 2. 验证码模式下，用户输入完验证码后自动取消键盘
     * 3. 手机号未完整输入焦点定位手机号输入框，当输入完整时自动切换至下一个输入框
     * 4. 由验证码模式切换至密码模式取消倒计时
     */
    fun adjustUserIntent(inputActive: Int) {
        if (1 == inputActive) {
            if (CODE_TYPE === mLoginType) {
                if (edt_phone.text != null && edt_phone.text.length > 10) {
                    v_security_code.setCanUseStatus(true)
                } else {
                    v_security_code.setCanUseStatus(false)
                }
            } else {
                if (edt_phone.text != null && edt_phone.text.length > 10) {
                    v_password_verification_code.setCanUseStatus(true)
                } else {
                    v_password_verification_code.setCanUseStatus(false)
                }
            }
            if (edt_phone.text != null && edt_phone.text.length > 10) {
                if (ll_password_verification.visibility == View.VISIBLE) {
                    this.showSoftInput(edt_password_verification)
                } else {
                    this.showSoftInput(edt_pass)
                }
            }
        } else if (2 == inputActive) {
            if (mLoginType === CODE_TYPE) {
                if (edt_pass.text != null && edt_pass.text.length == 6) {
                    this.toggleSoftInput()
                }
            } else {
                if (ll_password_verification.visibility == View.VISIBLE && edt_password_verification.text != null && edt_password_verification.text.length == 6) {
                    this.toggleSoftInput()
                }
            }
        } else if (3 == inputActive) {
            if (mLoginType === CODE_TYPE) {
                tvForgetPsw.visibility = View.GONE
            }
            if (edt_phone.text == null || edt_phone.text.length < 11) {
                this.showSoftInput(edt_phone)
            } else {
                if (ll_password_verification.visibility == View.VISIBLE) {
                    this.showSoftInput(edt_password_verification)
                } else {
                    this.showSoftInput(edt_pass)
                }
            }
        } else if (4 == inputActive) {
            if (CODE_TYPE === mLoginType) {
                v_security_code.stop()
            } else {
                v_password_verification_code.stop()
            }
        }
    }

    /**
     * 获取验证码
     */
    fun getSecurityCode() {
        if (TextUtils.isEmpty(inputPhoneNum) || inputPhoneNum.length < 11) {
            ToastUtil.showToast("请输入正确手机号")
            return
        }
        if (CODE_TYPE === mLoginType) {
            v_security_code.isClickable = false
        } else {
            v_password_verification_code.isClickable = false
        }
        PassportAPI.instance.sendValidateCode(inputPhoneNum, object : Passport.Callback<String> {
            override fun onSuccess(response: BaseResponseBean<String>) {
                if ((context as LoginActivity).isFinishing) {
                    return
                }
                if (response != null) {
                    if (response.status == 0) {
                        if (CODE_TYPE === mLoginType) {
                            v_security_code.isClickable = true
                        } else {
                            v_password_verification_code.isClickable = true
                        }
                        if (response.errorCode == PassportAPI.CODE_1001) {
                            ToastUtil.showToast("该账号未在爱学习注册")
                        } else {
                            ToastUtil.showToast(response.errorMessage!! + "")
                        }
                    } else {
                        if (CODE_TYPE === mLoginType) {
                            setLoginStatus(v_security_code, true)
                        } else {
                            setLoginStatus(v_password_verification_code, true)
                        }
                    }
                }
            }

            override fun onError(message: String?, code: Int?) {
                if ((context as LoginActivity).isFinishing) {
                    return
                }
                if (CODE_TYPE === mLoginType) {
                    v_security_code.isClickable = true
                } else {
                    v_password_verification_code.isClickable = true
                }
                ToastUtil.showToast(message!! + "")
            }
        })
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
            val inputManager = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (e: Exception) { }
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

    companion object {
        //密码登录方式
        const val PASS_TYPE = "1"
        //验证码登录方式
        const val CODE_TYPE = "0"

        //注册类型 班级码类型
        const val CLASS_CODE_TYPE = 0
        //注册类型 C端注册类型
        const val C_TYPE = 1
    }
}
