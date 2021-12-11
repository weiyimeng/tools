package gaosi.com.learn.studentapp.active

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.alibaba.fastjson.JSON
import com.gaosi.passport.Passport
import com.gaosi.passport.PassportAPI
import com.gaosi.passport.bean.BaseResponseBean
import com.gaosi.passport.bean.UserSwitchResponseBean
import com.github.mzule.activityrouter.annotation.Router
import com.gsbaselib.base.inject.GSAnnotation
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
import com.gstudentlib.util.SchemeDispatcher
import com.gstudentlib.util.hy.HyConfig
import com.gstudentlib.util.rxcheck.IRxCheckStatus
import com.gstudentlib.util.rxcheck.IRxCheckStatusListener
import com.gstudentlib.util.rxcheck.RxCheckNetStatus
import com.gstudentlib.util.rxcheck.RxCheckStatusMaster
import com.lzy.okgo.model.Response
import com.r0adkll.slidr.Slidr
import gaosi.com.learn.R
import gaosi.com.learn.bean.active.PerfectInfoBean
import gaosi.com.learn.studentapp.active.adapter.SelectStudentAdapter
import gaosi.com.learn.studentapp.login.LoginActivity
import gaosi.com.learn.studentapp.main.MainActivity
import kotlinx.android.synthetic.main.activity_select_student.*

/**
 * 作者：created by 逢二进一 on 2019/5/8 17:08
 * 邮箱：dingyuanzheng@gaosiedu.com
 */

@Router("selectStudentRome")
@GSAnnotation(pageId = StatisticsDictionary.selectStudentRome)
class SelectStudentActivity: BaseActivity() {

    private var dataList: List<StudentInfo>? = null
    private var mClassCode: String? = ""
    private var mValidateCode: String? = ""
    private var mPhone: String? = ""
    private var mStudent: StudentInfo? = null
    private var mInstitutionId: String? = ""
    private var mType: String? = "" //0未登录新增， 1未登录选择 2 已登录当前正在登录的绑定 3 已登录新增 4 已登录选择

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_student)
        // 设置右滑动返回
        Slidr.attach(this)
        this.getIntentData()
        this.initData()
        this.checkSubmitButtonValid()
    }

    override fun initView() {
        super.initView()
        title_bar.setLeftListener(this)
        btn_login.setOnClickListener(this)
    }

    private fun getIntentData() {
        if(intent.hasExtra("data")) {
            val data = intent.getStringExtra("data")
            dataList = JSON.parseArray(data, StudentInfo::class.java)
        }else {
            ToastUtil.showToast("参数传输错误")
            finish()
            return
        }
        intent?.apply {
            mClassCode = getStringExtra("classCode") ?: ""
            mValidateCode = getStringExtra("validateCode") ?: ""
            mPhone = getStringExtra("phone") ?: ""
            mInstitutionId = getStringExtra("institutionId") ?: ""
        }
    }

    private fun initData() {
        val selectStudentAdapter = SelectStudentAdapter()
        recycler_view.adapter = selectStudentAdapter
        selectStudentAdapter.addFooterView(footerView)
        recycler_view.layoutManager = LinearLayoutManager(this)
        selectStudentAdapter.setOnItemClickListener { _, _, position ->
            for(i in 0 until dataList!!.size) {
                dataList?.get(i)?.isSelectStatus = false
                if(i == position) {
                    dataList?.get(position)?.isSelectStatus = true
                    mStudent = dataList?.get(position)
                }
            }
            checkSubmitButtonValid()
            selectStudentAdapter.notifyDataSetChanged()
        }
        dataList?.let {
            selectStudentAdapter.setNewData(it)
        }
    }

    private val footerView: View
        get() {
            val view = View.inflate(this, R.layout.footer_select_student, null) as View
            view.findViewById<LinearLayout>(R.id.llAttendClass)?.setOnClickListener(this)
            return view
        }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.llTitleLeft -> finish()
            R.id.btn_login -> {
                bindClass()
                collectClickEvent("as1016_clk_chooseuser_enter")
            }
            R.id.llAttendClass -> {
                if(STBaseConstants.hasLogin()) {
                    SchemeDispatcher.jumpPage(this, "axx://attendClassRome?classCode=$mClassCode&institutionId=$mInstitutionId")
                }else {
                    SchemeDispatcher.jumpPage(this, "axx://attendClassRome?classCode=$mClassCode&phone=$mPhone&validateCode=$mValidateCode&institutionId=$mInstitutionId")
                }
                collectClickEvent("as1016_clk_chooseuser_createuserinfo")
            }
        }
    }

    /**
     * 绑定班级
     * 登录情况下type==1
     * 传输当前的studentId即可
     * 未登录情况下type==0
     * 传输当前的studentId以及当前的手机号验证码用于验证
     */
    private fun bindClass() {
        if(mStudent == null) {
            ToastUtil.showToast("请选择学员")
            return
        }
        RxCheckStatusMaster
                .addCheckStatus(RxCheckNetStatus())
                .check(object : IRxCheckStatusListener {
                    override fun onCheckPass() {
                        showLoadingProgressDialog()
                        val paramMap = HashMap<String, String>()
                        paramMap["classCode"] = mClassCode?:""
                        paramMap["institutionId"] = mInstitutionId?:""
                        if(STBaseConstants.hasLogin()) {
                            mType = "4"
                            paramMap["phone"] = STBaseConstants.userInfo.parentTel1
                            paramMap["studentId"] = mStudent?.id?:""
                            paramMap["type"] = mType?:""
                        }else {
                            mType = "1"
                            paramMap["phone"] = mPhone?:""
                            paramMap["validateCode"] = mValidateCode?:""
                            paramMap["studentId"] = mStudent?.id?:""
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
                                                DialogUtil.getInstance().create(this@SelectStudentActivity ,R.layout.activity_into_class_tips)
                                                        .show(object : AbsAdapter(){
                                                            override fun bindListener(onClickListener: View.OnClickListener?) {
                                                                this.bindListener(onClickListener, R.id.tvConfirm)
                                                            }
                                                            override fun onClick(v: View?, dialog: DialogUtil?) {
                                                                super.onClick(v, dialog)
                                                                when (v?.id) {
                                                                    R.id.tvConfirm -> {
                                                                        dialog?.dismiss()
                                                                        val intent = Intent(this@SelectStudentActivity, LoginActivity::class.java)
                                                                        startActivity(intent)
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
                                                        val intent = Intent(this@SelectStudentActivity, LoginActivity::class.java)
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
     * 检查提交按钮的合法性
     */
    private fun checkSubmitButtonValid() {
        //判断用户名和密码是否合法，只有在合法的情况下，才能点击
        val flag = (mStudent != null)
        btn_login.isClickable = flag

        var drawable = ContextCompat.getDrawable(this@SelectStudentActivity, R.drawable.corner_security_code_pressed)
        if (flag) {
            drawable = ContextCompat.getDrawable(this@SelectStudentActivity, R.drawable.corner_security_code)
        }
        btn_login.background = drawable
    }

    /**
     * 通过userId进行登录
     */
    private fun loginForUser(studentInfo: StudentInfo, isForce: Boolean) {
        showLoadingProgressDialog()
        //开始进行userid登录，isForce 第一次传false
        PassportAPI.instance.userSwitch(studentInfo.userId, isForce, object : Passport.Callback<UserSwitchResponseBean> {
            override fun onSuccess(response: BaseResponseBean<UserSwitchResponseBean>) {
                if (isFinishing) {
                    return
                }
                dismissLoadingProgressDialog()
                if (response.status == 0) {
                    if (response.errorCode == PassportAPI.CODE_1003) {//如果登录1003代表此用户已经在其它设备登录，此时弹框
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
}