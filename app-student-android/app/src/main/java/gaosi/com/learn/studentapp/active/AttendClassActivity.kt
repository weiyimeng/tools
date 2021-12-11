package gaosi.com.learn.studentapp.active

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.*
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.gaosi.passport.Passport
import com.gaosi.passport.PassportAPI
import com.gaosi.passport.bean.BaseResponseBean
import com.gaosi.passport.bean.UserSwitchResponseBean
import com.github.mzule.activityrouter.annotation.Router
import com.gsbaselib.base.inject.GSAnnotation
import com.gsbaselib.base.log.LogUtil
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.GSHttpResponse
import com.gsbaselib.net.callback.GSJsonCallback
import com.gsbaselib.utils.ActivityCollector
import com.gsbaselib.utils.ToastUtil
import com.gsbaselib.utils.dialog.AbsAdapter
import com.gsbaselib.utils.dialog.DialogUtil
import com.gsbaselib.utils.glide.ImageLoader
import com.gsbiloglib.log.GSLogUtil
import com.gstudentlib.GSAPI
import com.gstudentlib.SDKConfig
import com.gstudentlib.StatisticsDictionary
import com.gstudentlib.base.BaseActivity
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.bean.StudentInfo
import com.gstudentlib.util.hy.HyConfig
import com.gstudentlib.util.rxcheck.IRxCheckStatus
import com.gstudentlib.util.rxcheck.IRxCheckStatusListener
import com.gstudentlib.util.rxcheck.RxCheckNetStatus
import com.gstudentlib.util.rxcheck.RxCheckStatusMaster
import com.lzy.okgo.model.Response
import com.r0adkll.slidr.Slidr
import gaosi.com.learn.R
import gaosi.com.learn.bean.active.PerfectInfoBean
import gaosi.com.learn.studentapp.login.LoginActivity
import gaosi.com.learn.studentapp.main.MainActivity
import kotlinx.android.synthetic.main.activity_attend_class.*
import java.util.HashMap
import java.util.regex.Pattern

/**
 * 新增学员页面
 * 注册过来的
 * type == 0 属于没有登录过，此时提交参数为phone与code验证码
 * 登录过来的
 * type == 1 属于登录过，此时填写当前手机号码，但是没有code验证码
 */
@Router("attendClassRome")
@GSAnnotation(pageId = StatisticsDictionary.attendClassRome)
class AttendClassActivity : BaseActivity() {

    private var gender: Int? = -1 //0是没有studentId， 1是有studentId
    private var mClassCode: String? = ""
    private var mValidateCode: String? = ""
    private var mPhone: String? = ""
    private var mInstitutionId: String? = ""
    private var mType: String? = "" //0未登录新增， 1未登录选择 2 已登录当前正在登录的绑定 3 已登录新增 4 已登录选择

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attend_class)
        // 设置右滑动返回
        Slidr.attach(this)
        this.getIntentData()
        this.checkSubmitButtonValid()
    }

    private fun getIntentData() {
        intent?.apply {
            mClassCode = getStringExtra("classCode") ?: ""
            mValidateCode = getStringExtra("validateCode") ?: ""
            mPhone = getStringExtra("phone") ?: ""
            mInstitutionId = getStringExtra("institutionId") ?: ""
        }
    }

    override fun initView() {
        super.initView()
        mHandler.postDelayed({ showSoftInput(etName) }, 300)
        title_bar.setLeftListener(this)
        llMan.setOnClickListener(this)
        llWoman.setOnClickListener(this)
        btn_login.setOnClickListener(this)
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkSubmitButtonValid()
                if (!TextUtils.isEmpty(s)) {
                    ivClearName.visibility = View.VISIBLE
                    if (s.length > 10){ //最多10个字
                        etName.setText(s.toString().substring(0,10))
                        etName.setSelection(10)
                        ToastUtil.showToast("输入的名字过长,请重新输入! ")
                    }
                } else {
                    ivClearName.visibility = View.GONE
                }
            }
            override fun afterTextChanged(s: Editable) {
            }
        })
        etName.onFocusChangeListener = View.OnFocusChangeListener { _, _ ->
            if (!TextUtils.isEmpty(etName.text)) {
                ivClearName.visibility = View.VISIBLE
            } else {
                ivClearName.visibility = View.GONE
            }
        }
        ivClearName.setOnClickListener {
            etName.setText("")
            showSoftInput(etName)
        }
        val filters = arrayOf<InputFilter>(EditNameFilter())
        etName.filters = filters
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.llTitleLeft -> finish()
            R.id.llMan -> {
                gender = 1
                checkSubmitButtonValid()
                ivClassMan.setImageResource(R.drawable.active_class_man_selected)
                ivClassWoman.setImageResource(R.drawable.active_class_women)
                tvClassMan.setTextColor(Color.parseColor("#00C2FF"))
                tvClassWoman.setTextColor(Color.parseColor("#4cFFAB91"))
            }
            R.id.llWoman -> {
                gender = 0
                checkSubmitButtonValid()
                ivClassMan.setImageResource(R.drawable.active_class_man)
                ivClassWoman.setImageResource(R.drawable.active_class_women_selected)
                tvClassMan.setTextColor(Color.parseColor("#4c00C2FF"))
                tvClassWoman.setTextColor(Color.parseColor("#FFAB91"))
            }
            R.id.btn_login -> {
                bindClass()
                collectClickEvent("as1015_clk_createuserinfo_enter")
            }
        }
    }

    /**
     * 绑定班级
     */
    private fun bindClass() {
        RxCheckStatusMaster
                .addCheckStatus(RxCheckNetStatus())
                .check(object : IRxCheckStatusListener {
                    override fun onCheckPass() {
                        showLoadingProgressDialog()
                        val paramMap = HashMap<String, String>()
                        paramMap["classCode"] = mClassCode?:""
                        paramMap["institutionId"] = mInstitutionId?:""
                        if(STBaseConstants.hasLogin()) {
                            mType = "3"
                            paramMap["phone"] = STBaseConstants.userInfo.parentTel1
                            paramMap["name"] = etName.text.trim().toString()
                            paramMap["gender"] = gender.toString()
                            paramMap["type"] = mType?:"" //
                        }else {
                            mType = "0"
                            paramMap["phone"] = mPhone ?: ""
                            paramMap["validateCode"] = mValidateCode ?: ""
                            paramMap["name"] = etName.text.trim().toString()
                            paramMap["gender"] = gender.toString()
                            paramMap["type"] = mType?:""
                        }
                        GSRequest.startRequest(GSAPI.perfectStudentInfo, paramMap, object : GSJsonCallback<PerfectInfoBean>() {

                            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<PerfectInfoBean>) {
                                if (isFinishing || isDestroyed) {
                                    return
                                }
                                dismissLoadingProgressDialog()
                                showResponseErrorMessage(result)
                                if (result.body == null) {
                                    GSLogUtil.collectPerformanceLog(GSLogUtil.PerformanceType.HTTP_REQUEST_ERROR, mUrl, mResponse)
                                    return
                                }
                                // 0学生不对 1 入班成功   2入班失败
                                val body = result.body
                                body?.let {
                                    when(it.intoStatus) {
                                        0 -> {
                                            ToastUtil.showToast("学生不对")
                                        }
                                        1 -> {
                                            if(TextUtils.isEmpty(body.axxProof) && !STBaseConstants.hasLogin()) {
                                                DialogUtil.getInstance().create(this@AttendClassActivity ,R.layout.activity_into_class_tips)
                                                        .show(object : AbsAdapter(){
                                                            override fun bindListener(onClickListener: View.OnClickListener?) {
                                                                this.bindListener(onClickListener, R.id.tvConfirm)
                                                            }
                                                            override fun onClick(v: View?, dialog: DialogUtil?) {
                                                                super.onClick(v, dialog)
                                                                when (v?.id) {
                                                                    R.id.tvConfirm -> {
                                                                        dialog?.dismiss()
                                                                        val intent = Intent(this@AttendClassActivity, LoginActivity::class.java)
                                                                        startActivity(intent)
                                                                        ActivityCollector.getInstance().finishActivity(AttendClassActivity::class.java)
                                                                        ActivityCollector.getInstance().finishActivity(SelectStudentActivity::class.java)
                                                                        ActivityCollector.getInstance().finishActivity(RegisterAccountActivity::class.java)
                                                                        ActivityCollector.getInstance().finishActivity(ActiveClassActivity::class.java)
                                                                    }
                                                                }
                                                            }
                                                        })
                                            }else if(STBaseConstants.hasLogin()){
                                                showLoadingProgressDialog()
                                                PassportAPI.instance.refresh(object : Passport.CheckTokenValidity{
                                                    override fun onPass() {
                                                        if (isFinishing || isDestroyed) {
                                                            return
                                                        }
                                                        dismissLoadingProgressDialog()
                                                        //添加完成后直接重新打开首页面
                                                        if(PassportAPI.instance.getIsUsePassport() == true) {
                                                            STBaseConstants.Token = PassportAPI.instance.getToken_S()
                                                        }
                                                        STBaseConstants.isBeixiao = it.isBeiXiao?:0
                                                        STBaseConstants.changedPasswordCode = it.changedPasswordCode?:0
                                                        loginForUser(it.student!! , false)
                                                    }

                                                    override fun onFail() {
                                                        if (isFinishing || isDestroyed) {
                                                            return
                                                        }
                                                        dismissLoadingProgressDialog()
                                                        ToastUtil.showToast("刷新Token失败，请重新登录后完成入班！")
                                                        SDKConfig.exitLogin()
                                                        val intent = Intent(this@AttendClassActivity, LoginActivity::class.java)
                                                        startActivity(intent)
                                                        ActivityCollector.getInstance().finishActivity(AttendClassActivity::class.java)
                                                        ActivityCollector.getInstance().finishActivity(SelectStudentActivity::class.java)
                                                        ActivityCollector.getInstance().finishActivity(RegisterAccountActivity::class.java)
                                                        ActivityCollector.getInstance().finishActivity(ActiveClassActivity::class.java)
                                                        ActivityCollector.getInstance().finishActivity(MainActivity::class.java)
                                                    }
                                                })
                                            }else {
                                                //添加完成后直接重新打开首页面
                                                STBaseConstants.isBeixiao = it.isBeiXiao?:0
                                                STBaseConstants.changedPasswordCode = it.changedPasswordCode?:0
                                                PassportAPI.instance.updateToken(body.axxProof?:"" , body.refreshToken?:"")
                                                loginForUser(it.student!! , false)
                                            }
                                        }
                                        2 -> {
                                            ToastUtil.showToast("入班失败")
                                        }
                                    }
                                }
                            }

                            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                                if (isFinishing || isDestroyed) {
                                    return
                                }
                                dismissLoadingProgressDialog()
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
     * 通过userId进行登录
     */
    private fun loginForUser(studentInfo: StudentInfo, isForce: Boolean) {
        showLoadingProgressDialog()
        PassportAPI.instance.userSwitch(studentInfo.userId, isForce, object : Passport.Callback<UserSwitchResponseBean> {
            override fun onSuccess(response: BaseResponseBean<UserSwitchResponseBean>) {
                if (isFinishing) {
                    return
                }
                dismissLoadingProgressDialog()
                if (response.status == 0) {
                    if (response.errorCode == PassportAPI.CODE_1003) {
                        showLoginInAnotherDeviceDialog(studentInfo)
                    } else {
                        STBaseConstants.Token = PassportAPI.instance.getToken_S()
                        checkClientId(studentInfo , STBaseConstants.Token)
                    }
                } else {
                    STBaseConstants.Token = PassportAPI.instance.getToken_S()
                    checkClientId(studentInfo , STBaseConstants.Token)
                }
            }

            override fun onError(message: String?, code: Int?) {
                if (isFinishing) {
                    return
                }
                dismissLoadingProgressDialog()
                ToastUtil.showToast(message!! + "")
            }
        })
    }

    /**
     * 展示当前用户在其它设备登录
     */
    private fun showLoginInAnotherDeviceDialog(studentInfo: StudentInfo) {
        DialogUtil.getInstance().create(this, R.layout.ui_user_login_another_device)
                .show(object : AbsAdapter() {
                    override fun bindListener(onClickListener: View.OnClickListener) {
                        this.bindText(R.id.tvName, studentInfo.truthName)
                        this.bindText(R.id.tvInstitutionName, studentInfo.institutionName)
                        val ivHeader = this.findViewById<ImageView>(R.id.ivHeader)
                        if (studentInfo.path != null) {
                            ImageLoader.setCircleImageViewResource(ivHeader, studentInfo.path, R.drawable.icon_default_header)
                        } else {
                            ivHeader.setImageResource(R.drawable.icon_default_header)
                        }
                        this.bindListener(onClickListener, R.id.tvCancel, R.id.tvConfirm)
                    }

                    override fun onClick(v: View?, dialog: DialogUtil?) {
                        super.onClick(v, dialog)
                        when (v!!.id) {
                            R.id.tvCancel -> dialog?.dismiss()
                            R.id.tvConfirm -> {
                                dialog?.dismiss()
                                loginForUser(studentInfo, true)
                            }
                        }
                    }
                })
    }

    /**
     * 绑定JPUSH账号，同时跳转首页面
     */
    private fun checkClientId(studentInfo: StudentInfo? , token: String?) {
        if (studentInfo == null) {
            return
        }
        ToastUtil.showToast("提交成功，欢迎加入新班级")
        studentInfo.isBeiXiao = STBaseConstants.isBeixiao
        SDKConfig.updateInfo(studentInfo , token)
        MainActivity.tabTag = 0
        HyConfig.updateHyResource()//重新执行一次更新操作
        val intent = Intent(mContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        ActivityCollector.getInstance().finishAllActivity()
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
            val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager ?: return
            inputManager.showSoftInput(view, 0)
        } catch (e: Exception) { }
    }

    /**
     * 检查提交按钮的合法性
     */
    private fun checkSubmitButtonValid() {
        //判断用户名和密码是否合法，只有在合法的情况下，才能点击
        val flag = (etName.text != null && etName.text.trim().isNotEmpty()) && gender != -1
        btn_login.isClickable = flag

        var drawable = ContextCompat.getDrawable(this, R.drawable.app_login_shape_unenable)
        if (flag) {
            drawable = ContextCompat.getDrawable(this, R.drawable.app_login_shape_enable)
        }
        btn_login.background = drawable
    }

    /**
     * 用户名过滤
     */
    private class EditNameFilter: InputFilter {

        fun stringFilter(str : String?): String {
            val regEx  =  "[^a-zA-Z0-9\u4E00-\u9FA5]"
            val p = Pattern.compile(regEx)
            val m = p.matcher(str)
            return m.replaceAll("").trim()
        }

        override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? {
            LogUtil.d("EditNameFilter" + source?.toString())
            if(TextUtils.isEmpty(source)) {
                return ""
            }
            return stringFilter(source.toString())
        }
    }

}
