package gaosi.com.learn.studentapp.login

import android.graphics.Color
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.alibaba.fastjson.JSON
import com.gaosi.passport.Passport
import com.gaosi.passport.PassportAPI
import com.gaosi.passport.bean.BaseResponseBean
import com.gaosi.passport.bean.UserSwitchResponseBean
import com.github.mzule.activityrouter.annotation.Router
import com.gsbaselib.base.GSBaseConstants
import com.gsbaselib.base.inject.GSAnnotation
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.GSHttpResponse
import com.gsbaselib.net.callback.GSJsonCallback
import com.gsbaselib.utils.StatusBarUtil
import com.gsbaselib.utils.ToastUtil
import com.gsbaselib.utils.dialog.AbsAdapter
import com.gsbaselib.utils.dialog.DialogUtil
import com.gsbaselib.utils.glide.ImageLoader
import com.gstudentlib.SDKConfig
import com.gstudentlib.StatisticsDictionary
import com.gstudentlib.base.BaseActivity
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.bean.StudentInfo
import com.lzy.okgo.model.Response
import gaosi.com.learn.R
import gaosi.com.learn.application.AppApi
import gaosi.com.learn.bean.login.StudentInfoBody
import gaosi.com.learn.view.AxxEmptyView
import gaosi.com.learn.view.AxxLoadMoreView
import kotlinx.android.synthetic.main.activity_user_list.*

/**
 * 用户列表
 * Created by huangshan on 2018/9/14.
 */
@Router("switchUser")
class UserListActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {

    private var pageNum = 1
    private var mUserListAdapter: UserListAdapter? = null
    private var loginCount = 0
    //异常流状态
    private var mEmptyType: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)
        this.initData()
        this.requestStudentInfo()
    }

    private fun initData() {
        if(STBaseConstants.hasLogin()) {
            title_bar.visibility = View.VISIBLE
        }else {
            title_bar.visibility = View.INVISIBLE
        }
        title_bar.setLeftListener(this)
        mUserListAdapter = UserListAdapter()
        mUserListAdapter?.run {
            val axxLoadMoreView = AxxLoadMoreView()
            axxLoadMoreView.setLoadEndId(0)
            setLoadMoreView(axxLoadMoreView)
            mUserListAdapter?.setOnLoadMoreListener({
                requestStudentInfo()
            }, recycler_view)
        }
        recycler_view.adapter = mUserListAdapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        mUserListAdapter?.setOnItemClickListener { _, _, position ->
            val student = mUserListAdapter?.data?.get(position)
            loginForUser(student!! , false)
        }
        srRefresh.setOnRefreshListener(this)
        mEmptyView?.setClickCallBackListener(object : AxxEmptyView.OnClickCallBackListener {
            override fun onClick() {
                when (mEmptyType) {
                    1 -> {
                        onRefresh()
                    }
                }
            }
        })
        srRefresh.isRefreshing = true
    }

    /**
     * 获取学员信息
     */
    private fun requestStudentInfo() {
        this.setEmptyStatus(0)
        val params = HashMap<String, String>()
        params["pageNum"] = pageNum.toString()
        if(STBaseConstants.hasLogin()) {
            params["curStudentId"] = STBaseConstants.userId
        }
        GSRequest.startRequest(AppApi.getLoginSuccessStudentInfo, params, object : GSJsonCallback<StudentInfoBody>() {

            override fun onResponseSuccess(response: Response<*>, code: Int, result: GSHttpResponse<StudentInfoBody>) {
                if (isFinishing || isDestroyed) {
                    return
                }
                srRefresh.isRefreshing = false
                mUserListAdapter?.setEnableLoadMore(true)
                if (showResponseErrorMessage(result) == 0) {
                    setEmptyStatus(1)
                    return
                }
                if (result.isSuccess) {
                    if (result.body != null && result.body.studentPage != null && !result.body.studentPage?.list.isNullOrEmpty()) {
                        STBaseConstants.isBeixiao = if (result.body.beixiao == true) 1 else 0
                        STBaseConstants.changedPasswordCode = result.body.changedPasswordCode?:0
                        result.body.studentPage?.let {
                            val mStudentInfos = it.list
                            mStudentInfos?.let {
                                if (it.size > 1) {
                                    for (i in it.indices) {
                                        it[i].isBeiXiao = STBaseConstants.isBeixiao
                                    }
                                }
                                if (pageNum == 1) {
                                    mUserListAdapter?.setNewData(it)
                                } else {
                                    mUserListAdapter?.addData(it)
                                }
                            }
                            if(pageNum >= it.pageTotal?:0) {
                                mUserListAdapter?.loadMoreEnd()
                            }else {
                                pageNum++
                                mUserListAdapter?.loadMoreComplete()
                            }
                        }
                    }
                }
            }
            override fun onResponseError(response: Response<*>, code: Int, message: String) {
                if (isFinishing || isDestroyed) {
                    return
                }
                setEmptyStatus(1)
                ToastUtil.showToast(message + "")
                if (pageNum != 1) {
                    mUserListAdapter?.loadMoreFail()
                }
                srRefresh.isRefreshing = false
                mUserListAdapter?.setEnableLoadMore(true)
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
                        saveUserInfo(studentInfo)
                    }
                } else {
                    STBaseConstants.Token = PassportAPI.instance.getToken_S()
                    saveUserInfo(studentInfo)
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
                                if(loginCount > 0 && STBaseConstants.hasLogin()) {
                                    kickOut("您的账号已经在其它设备登录，请重新登录！")
                                    return
                                }
                                loginCount ++
                                dialog?.dismiss()
                                loginForUser(studentInfo, true)
                            }
                        }
                    }
                })
    }

    private fun saveUserInfo(student: StudentInfo?) {
        if (student == null || TextUtils.isEmpty(student.id)) {
            return
        }
        SDKConfig.updateInfo(student , GSBaseConstants.Token)
        setResult(RESULT_OK)
        finish()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.llTitleLeft -> {
                finish()
            }
        }
    }

    /**
     * 设置异常流view
     */
    private fun setEmptyStatus(emptyType: Int) {
        mEmptyType = emptyType
        when (mEmptyType) {
            0 -> {
                mEmptyView.setEmptyVisibility(showImg = false, showText = false, showButton = false)
            }
            1 -> {
                mEmptyView.setEmptyIcon(R.drawable.icon_net_error)
                mEmptyView.setEmptyText("网络加载失败")
                mEmptyView.setButtonText("点击刷新")
                mEmptyView.setEmptyVisibility(showImg = true, showText = true, showButton = true)
            }
        }
    }

    override fun setStatusBar() {
        setStatusBar(Color.parseColor("#F8FAFD"), 0)
        StatusBarUtil.setLightMode(this)
    }

    override fun onBackPressed() {
        if(STBaseConstants.hasLogin()) {
            super.onBackPressed()
        }
    }

    override fun onRefresh() {
        pageNum = 1
        mUserListAdapter?.setEnableLoadMore(false)
        requestStudentInfo()
    }
}
