package gaosi.com.learn.studentapp.active

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.github.mzule.activityrouter.annotation.Router
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.GSHttpResponse
import com.gsbaselib.net.callback.GSJsonCallback
import com.gsbaselib.utils.LangUtil
import com.gsbaselib.utils.ToastUtil
import com.lzy.okgo.model.Response
import com.r0adkll.slidr.Slidr
import gaosi.com.learn.R
import gaosi.com.learn.bean.active.ClassBean
import gaosi.com.learn.bean.active.PerfectInfoBean
import java.util.HashMap
import android.content.Intent
import android.graphics.Color
import android.widget.ImageView
import com.alibaba.fastjson.JSON
import com.gaosi.passport.Passport
import com.gaosi.passport.PassportAPI
import com.gaosi.passport.bean.BaseResponseBean
import com.gaosi.passport.bean.UserSwitchResponseBean
import com.gsbaselib.base.event.EventBean
import com.gsbaselib.base.log.LogUtil
import com.gsbaselib.utils.ActivityCollector
import com.gsbaselib.utils.dialog.AbsAdapter
import com.gsbaselib.utils.dialog.DialogUtil
import com.gsbaselib.utils.glide.ImageLoader
import com.gsbiloglib.log.GSLogUtil
import com.gstudentlib.GSAPI
import com.gstudentlib.SDKConfig
import com.gstudentlib.base.BaseActivity
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.bean.StudentInfo
import com.gstudentlib.util.SchemeDispatcher
import com.gstudentlib.util.rxcheck.IRxCheckStatus
import com.gstudentlib.util.rxcheck.IRxCheckStatusListener
import com.gstudentlib.util.rxcheck.RxCheckNetStatus
import com.gstudentlib.util.rxcheck.RxCheckStatusMaster
import gaosi.com.learn.application.event.AppEventType
import gaosi.com.learn.studentapp.login.LoginActivity
import gaosi.com.learn.studentapp.main.MainActivity
import kotlinx.android.synthetic.main.activity_active_class.*
import org.greenrobot.eventbus.EventBus

@Router("activeClassRome")
class ActiveClassActivity : BaseActivity() {

    private var mClassBean: ClassBean? = null
    private var mClassCode: String? = ""

    companion object {
        const val INTO_CLASS_SUCCESS: Int = 0x100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active_class)
        // 设置右滑动返回
        Slidr.attach(this)
        btn_submit.setOnClickListener(this)
        btn_active.setOnClickListener(this)
    }

    override fun initView() {
        super.initView()
        title_bar.setLeftListener(this)
        mHandler.postDelayed({ showSoftInput(edtClassCode) }, 300)
        edtClassCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(edtClassCode.text != null && edtClassCode.text.length >= 12) {
                    toggleSoftInput()
                }
                if (edtClassCode.text != null && edtClassCode.text.length >= 6) {
                    btn_active.isEnabled =  true
                    btn_active.setTextColor(Color.parseColor("#A9DC35"))
                } else {
                    btn_active.isEnabled =  false
                    btn_active.setTextColor(Color.parseColor("#7dA9DC35"))
                }
                hideClassInfo()
            }
            override fun afterTextChanged(s: Editable) {}
        })
    }
    /**
     * 检查班级码
     * 如果没有在检查中此时说明预加载是成功的，则只需要判断当前的code是否一致
     */
    private fun checkClassCode() {
        RxCheckStatusMaster
                .addCheckStatus(RxCheckNetStatus())
                .check(object : IRxCheckStatusListener {
                    override fun onCheckPass() {
                        showLoadingProgressDialog()
                        mClassCode = edtClassCode.text.toString()
                        val paramMap = HashMap<String, String>()
                        paramMap["classCode"] = mClassCode?:""
                        GSRequest.startRequest(GSAPI.checkClassCode, paramMap, object : GSJsonCallback<ClassBean>() {

                            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<ClassBean>) {
                                if (isFinishing || isDestroyed) {
                                    return
                                }
                                dismissLoadingProgressDialog()
                                showResponseErrorMessage(result)
                                if (result.body == null) {
                                    GSLogUtil.collectPerformanceLog(GSLogUtil.PerformanceType.HTTP_REQUEST_ERROR, mUrl, mResponse)
                                    return
                                }
                                //为了防止在加载过程中用户操作了code值，与请求的code不相同了
                                if(mClassCode == edtClassCode.text.toString()) {
                                    showClassInfo(result.body)
                                }else {
                                    LogUtil.d("班级code发生改变")
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
     * 展示数据
     */
    private fun showClassInfo(cb : ClassBean?) {
        cb?.let {
            mClassBean = cb
            btn_submit.visibility = View.VISIBLE
            ll_class.visibility = View.VISIBLE
            tv_class_name.text = it.className
            if(LangUtil.isNotEmpty(it.teacherName)) {
                tv_teacher_name.text = it.teacherName?.get(0)
            }else {
                tv_teacher_name.text = "暂无"
            }
        }
    }

    /**
     * 删除展示数据
     */
    private fun hideClassInfo() {
        mClassBean = null
        btn_submit.visibility = View.GONE
        ll_class.visibility = View.GONE
        tv_class_name.text = ""
        tv_teacher_name.text = ""
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.llTitleLeft -> {
                finish()
            }
            R.id.btn_active -> checkClassCode()
            R.id.btn_submit -> {
                mClassBean?.let {
                    if(!STBaseConstants.hasLogin()) {
                        SchemeDispatcher.jumpPage(this, "axx://registerAccountRome?institutionId=${it.institutionId}&classCode=$mClassCode")
                    }else {
                        //已登录模式下直接进行绑定
                        bindClass()
                    }
                }
                collectClickEvent("as1013_clk_classcode_enter")
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
                        mClassCode = edtClassCode.text.toString()
                        paramMap["classCode"] = mClassCode?:""
                        paramMap["institutionId"] = mClassBean?.institutionId?:""
                        paramMap["studentId"] = STBaseConstants.userId
                        paramMap["phone"] = STBaseConstants.userInfo.parentTel1
                        paramMap["type"] = "2" //0未登录新增， 1未登录选择 2 已登录当前正在登录的绑定 3 已登录新增 4 已登录选择
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
                                    if(it.intoStatus == 3) {//需要弹框
                                        if(LangUtil.isNotEmpty(it.studentList)) {
                                            showChangeStudent(it)
                                        }else {
                                            showAddStudent()
                                        }
                                    }
                                    when(it.intoStatus) {
                                        0 -> {
                                            //ToastUtil.showToast("学生不对")
                                        }
                                        1 -> {
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
                                                    val intent = Intent(this@ActiveClassActivity, LoginActivity::class.java)
                                                    startActivity(intent)
                                                    ActivityCollector.getInstance().finishActivity(ActiveClassActivity::class.java)
                                                    ActivityCollector.getInstance().finishActivity(MainActivity::class.java)
                                                }
                                            })
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
        //发送入班成功通知
        EventBus.getDefault().post(EventBean(AppEventType.INTO_CLASS_COMPLETE))
        val intent = Intent()
        intent.putExtra("classId" , mClassBean?.classId)
        setResult(INTO_CLASS_SUCCESS , intent)
        finish()
    }

    /**
     * 展现切换学员dialog
     */
    private fun showChangeStudent(perfectInfoBean: PerfectInfoBean) {
        DialogUtil.getInstance().create(this, R.layout.ui_dialog_style_1 , true , true ,0.8f)
                .show(object : AbsAdapter() {
                    override fun bindListener(onClickListener: View.OnClickListener?) {
                        this.bindText(R.id.tvTop , "学员并未在该机构报名")
                        this.bindText(R.id.tvBottom , "请重新选择学员")
                        this.bindText(R.id.tvConfirm , "切换学员")
                        this.bindListener(onClickListener, R.id.tvConfirm)
                    }

                    override fun onClick(v: View?, dialog: DialogUtil?) {
                        super.onClick(v, dialog)
                        when (v?.id) {
                            R.id.tvConfirm -> {
                                //是
                                dialog?.dismiss()
                                gotoSelectStudentPage(perfectInfoBean.studentList)
                            }
                        }
                    }
                })
    }

    /**
     * 展现新增学员dialog
     */
    private fun showAddStudent() {
        DialogUtil.getInstance().create(this, R.layout.ui_dialog_style_1 , true , true ,0.8f)
                .show(object : AbsAdapter() {
                    override fun bindListener(onClickListener: View.OnClickListener?) {
                        this.bindText(R.id.tvTop , "学员并未在该机构报名")
                        this.bindText(R.id.tvBottom , "请添加新学员")
                        this.bindText(R.id.tvConfirm , "添加新学员")
                        this.bindListener(onClickListener, R.id.tvConfirm)
                    }

                    override fun onClick(v: View?, dialog: DialogUtil?) {
                        super.onClick(v, dialog)
                        when (v?.id) {
                            R.id.tvConfirm -> {
                                //是
                                dialog?.dismiss()
                                mClassBean?.let {
                                    SchemeDispatcher.jumpPage(this@ActiveClassActivity, "axx://attendClassRome?classCode=$mClassCode&institutionId=${it.institutionId}")
                                }
                            }
                        }
                    }
                })
    }

    /**
     * 选择学员
     */
    private fun gotoSelectStudentPage(body: List<StudentInfo>?) {
        body?.let {
            val intent = Intent(mContext, SelectStudentActivity::class.java)
            intent.putExtra("data", JSON.toJSONString(body))
            intent.putExtra("classCode", mClassCode)
            intent.putExtra("institutionId", mClassBean?.institutionId)
            startActivity(intent)
        }
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
     * 键盘显示与隐藏
     */
    private fun toggleSoftInput() {
        try {
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager ?: return
            inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (e: Exception) {}
    }
}
