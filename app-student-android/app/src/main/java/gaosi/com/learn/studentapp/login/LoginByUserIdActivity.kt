package gaosi.com.learn.studentapp.login

import android.os.Bundle
import android.view.View
import com.gaosi.passport.PassportAPI
import com.gaosi.passport.util.SharedPreferencesUtil
import com.github.mzule.activityrouter.annotation.Router
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.GSHttpResponse
import com.gsbaselib.net.callback.GSJsonCallback
import com.gsbaselib.utils.ActivityCollector
import com.gsbaselib.utils.ToastUtil
import com.gstudentlib.SDKConfig
import com.gstudentlib.base.BaseActivity
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.bean.StudentInfo
import com.gstudentlib.bean.StudentInfoBody
import com.gstudentlib.util.SchemeDispatcher
import com.lzy.okgo.model.Response
import gaosi.com.learn.R
import gaosi.com.learn.application.AppApi
import kotlinx.android.synthetic.main.activity_login_by_user_id.*
import org.json.JSONObject
import java.util.HashMap

/**
 * userId登录
 */
@Router("loginByUserId")
class LoginByUserIdActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_by_user_id)
    }

    override fun initView() {
        super.initView()
        title_bar.setLeftListener(this)
        btn_login.setOnClickListener(this)
    }

    private fun loginByUserId() {
        if (edt_userId.text.isNullOrEmpty() || edt_phone.text.isNullOrEmpty() || edt_refreshToken.text.isNullOrEmpty() || edt_token.text.isNullOrEmpty()) {
            ToastUtil.showToast("输入信息不能为空")
        } else {
            STBaseConstants.Token = edt_token.text.toString()
            STBaseConstants.businessUserId = edt_userId.text.toString()
            SharedPreferencesUtil.putString(PassportAPI.TOKEN_L, edt_refreshToken.text.toString())
            showLoadingProgressDialog()
            val params = HashMap<String, String>()
            val json = JSONObject()
            json.put("userId", edt_userId.text.toString())
            json.put("phone", edt_phone.text.toString())
            params["param"] = json.toString()
            GSRequest.startRequest(AppApi.getUserInfoByUserId, params, object : GSJsonCallback<StudentInfoBody>() {
                override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<StudentInfoBody>) {
                    if (isFinishing) {
                        return
                    }
                    dismissLoadingProgressDialog()
                    if (result.isSuccess) {
                        if (result.body != null) {
                            STBaseConstants.isBeixiao = if (result.body.beixiao == true) 1 else 0
                            STBaseConstants.changedPasswordCode = result.body.changedPasswordCode!!
                            if (result.body.students?.isNotEmpty()!!) {
                                result.body.students!![0].isBeiXiao = STBaseConstants.isBeixiao
                                checkClientId(result.body.students!![0], edt_token.text.toString())
                            }
                        } else {
                            ToastUtil.showToast(result.message)
                        }
                    } else {
                        ToastUtil.showToast(result.message)
                    }
                }

                override fun onResponseError(response: Response<*>?, code: Int, mseeage: String?) {
                    if (isFinishing) {
                        return
                    }
                    dismissLoadingProgressDialog()
                }
            })
        }
    }

    private fun checkClientId(studentInfo: StudentInfo?, token: String) {
        studentInfo ?: return
        SDKConfig.updateInfo(studentInfo, token)
        ActivityCollector.getInstance().finishAllActivity()
        SchemeDispatcher.jumpPage(this, "axx://main")
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.llTitleLeft -> finish()
            R.id.btn_login -> loginByUserId()
        }
    }
}
