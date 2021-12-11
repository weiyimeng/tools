package gaosi.com.learn.studentapp.main

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.TaskStackBuilder
import com.alibaba.fastjson.JSON
import com.chivox.cube.util.FileHelper
import com.gaosi.passport.PassportAPI
import com.gsbaselib.base.log.LogUtil
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.GSHttpResponse
import com.gsbaselib.net.callback.GSJsonCallback
import com.gsbaselib.utils.LOGGER
import com.gsbaselib.utils.ToastUtil
import com.gsbaselib.utils.update.IResUpdateListener
import com.gsbaselib.utils.update.UpdateUtil
import com.gsbaselib.utils.update.bean.UpdateEventBean
import com.gsbaselib.utils.update.bean.UpdateType
import com.gstudentlib.GSAPI
import com.gstudentlib.SDKConfig
import com.gstudentlib.base.BaseActivity
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.bean.StudentInfo
import com.gstudentlib.bean.StudentInfoBody
import com.gstudentlib.util.SchemeDispatcher
import com.gstudentlib.util.SystemUtil
import com.lzy.okgo.model.Response
import gaosi.com.learn.BuildConfig
import gaosi.com.learn.bean.login.LoginSuccessRequest
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.IOException
import java.util.HashMap
import java.util.concurrent.Executors

/**
 * 北校交作业 流程分发
 */
class IntentParseActivity : BaseActivity() {

    private var mUserId = ""
    private var mToken = ""
    private var mClassId = ""
    private var mLessonId = ""
    private var mStudentInfo: StudentInfo? = null
    private var mIsUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        showLoadingProgressDialog()
        registerUpdate()
        loadProvisionFile()
        unZipNativeVoiceRes()
        getUserInfo()
    }

    /**
     * 获取用户信息
     */
    private fun getUserInfo() {
        if (intent.action == Intent.ACTION_VIEW) {
            val data = intent.data
            if (data != null) {
                try {
                    mUserId = data.getQueryParameter("userId")
                    mToken = data.getQueryParameter("token")
                    //判断本地userId Token 和传递的是否一致（当前处于登录状态）
                    if (mUserId == STBaseConstants.userId && mToken == STBaseConstants.Token) {
                        if (STBaseConstants.hasLogin()) {
                            mStudentInfo = JSON.parseObject(STBaseConstants.UserInfo, StudentInfo::class.java)
                            SDKConfig.updateInfo(mStudentInfo , mToken)
                            if (mIsUpdate) {
                                dismissLoadingProgressDialog()
                                externalJump()
                            }
                        }
                    } else {
                        STBaseConstants.Token = mToken
                        val params = HashMap<String, String>()
                        val loginSuccessRequest = LoginSuccessRequest()
                        loginSuccessRequest.studentId = mUserId
                        try {
                            params["param"] = GSRequest.getConverterFactory().ObjectToStringConverter().convert(loginSuccessRequest)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        GSRequest.startRequest(GSAPI.loginSuccess, params, object : GSJsonCallback<StudentInfoBody>() {
                            override fun onResponseSuccess(response: Response<*>, code: Int, result: GSHttpResponse<StudentInfoBody>) {
                            if (isFinishing) {
                                return
                            }
                            if (result.isSuccess) {
                                if (result.body != null && result.body.students != null && result.body.students?.size != 0) {
                                    STBaseConstants.isBeixiao = 1
                                    STBaseConstants.changedPasswordCode = result.body.changedPasswordCode?:1
                                    val studentInfo = result.body.students?.get(0)
                                    mStudentInfo = studentInfo
                                    SDKConfig.updateInfo(mStudentInfo , mToken)
                                    if (mIsUpdate) {
                                        dismissLoadingProgressDialog()
                                        externalJump()
                                    }
                                }else {
                                        ToastUtil.showToast("获取用户信息失败")
                                    }
                                } else {
                                    ToastUtil.showToast("获取用户信息失败")
                                }
                            }
                            override fun onResponseError(response: Response<*>, code: Int, message: String) {
                                ToastUtil.showToast(message + "")
                            }
                        })
                    }
                } catch (e: Exception) {
                    ToastUtil.showToast("参数错误")
                    LOGGER.log(e)
                    finish()
                }
            }
        }
    }

    /**
     * intent解析
     */
    private fun parseIntent(data: Uri): Intent? {
        val mIntent: Intent? = null
        try {
            mClassId = data.getQueryParameter("classId")
            mLessonId = data.getQueryParameter("lessonId")
            val url = "axx://lessonDetail?classId=$mClassId&lessonId=$mLessonId"
            LogUtil.d("url: $url")
            SchemeDispatcher.jumpPage(this, url)
        } catch (e: Exception) {
            ToastUtil.showToast("跳转失败,请检查爱学习APP是否为新版本")
            LOGGER.log(e)
            finish()
        }
        return mIntent
    }

    private fun externalJump() {
        try {
            if (intent.action == Intent.ACTION_VIEW) {
                val data = intent.data
                if (data != null) {
                    //当用户从北校app过来的情况下，关闭passport的使用，此时会清除passport里面的所有数据
                    //因为是关闭passport的使用，所以在启动校验的时候将不再进行token验证，同时在入班的情况将不再进行token刷新以及token替换
                    PassportAPI.instance.setUsePassport(false)
                    val resultIntent = parseIntent(data)
                    resultIntent?.let {
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        if (SystemUtil.isLaunchedActivity(this, MainActivity::class.java)) {
                            startActivity(it)
                        } else {
                            TaskStackBuilder.create(this)
//                                .addParentStack(resultIntent.component)
                                    .addNextIntent(it)
                                    .startActivities()
                        }
                    }
                }
                finish()
            }
        } catch (e: Exception) {
            LOGGER.log(e)
            finish()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRnUpdateEvent(event: UpdateEventBean) {
        try {
            if (event.type == UpdateType.UPDATE_SUCCESS) {
                mIsUpdate = true
                if (mStudentInfo != null) {
                    dismissLoadingProgressDialog()
                    externalJump()
                    mIsUpdate = false
                }
            }
        } catch (e: Exception) {
            LOGGER.log(e)
        }
    }

    /**
     * 代码不加入驰声会加载不成功
     * 经测试即使不申请存储权限也可以使用
     * 驰声需要的资源文件
     */
    private fun loadProvisionFile() {
        FileHelper.extractProvisionOnce(this@IntentParseActivity, STBaseConstants.provisionFilename)
    }

    private fun unZipNativeVoiceRes() {
        LogUtil.w("unzip start")
        val pDialog = ProgressDialog(this)
        pDialog.setCanceledOnTouchOutside(false)
        pDialog.setMessage("解压资源文件中...")
        pDialog.show()
        val service = Executors.newSingleThreadExecutor()
        service.submit({
            FileHelper.extractResourceOnce(this@IntentParseActivity, "vad.zip")
            pDialog.dismiss()
        }, true)
    }

    /**
     * 注冊升级方案
     */
    private fun registerUpdate() {
        //初始化升级更新配置
        UpdateUtil.getInstance()
                .inject(this)
                .register("2", "bd0f3ba42dc727662a6a", BuildConfig.APPLICATION_ID)
                .setListener(object : IResUpdateListener {
                    override fun onUpdating() {
                        LogUtil.e("update-->onUpdating")
                    }

                    override fun onUpdateFail() {
                        LogUtil.e("update-->onUpdateFail")
                    }

                    override fun onUpdateSuccess(context: Context) {
                        LogUtil.e("update-->onUpdateSuccess")
                    }
                }).startCheckWebResource()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}
