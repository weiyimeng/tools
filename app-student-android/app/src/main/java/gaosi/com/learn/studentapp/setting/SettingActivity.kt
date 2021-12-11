package gaosi.com.learn.studentapp.setting

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.gaosi.passport.Passport
import com.gaosi.passport.PassportAPI
import com.gaosi.passport.bean.BaseResponseBean
import com.github.mzule.activityrouter.annotation.Router
import com.gsbaselib.InitBaseLib
import com.gsbaselib.utils.ActivityCollector
import com.gsbaselib.utils.FileUtil
import com.gsbaselib.utils.StatusBarUtil
import com.gsbaselib.utils.ToastUtil
import com.gsbaselib.utils.update.IOnlyCheckNAUpdate
import com.gsbaselib.utils.update.UpdateUtil
import com.gstudentlib.SDKConfig
import com.gstudentlib.base.BaseActivity
import com.gstudentlib.util.SchemeDispatcher
import com.gstudentlib.util.SystemUtil
import com.r0adkll.slidr.Slidr
import gaosi.com.learn.BuildConfig
import gaosi.com.learn.R
import gaosi.com.learn.studentapp.login.LoginActivity
import gaosi.com.learn.studentapp.login.UpdatePasswordActivity
import gaosi.com.learn.studentapp.main.MainActivity
import kotlinx.android.synthetic.main.activity_setting.*
import org.json.JSONException
import org.json.JSONObject

/**
 * 设置页面
 * Created by dingyz on 2018/12/18.
 */
@Router("doSetting")
class SettingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置右滑动返回
        Slidr.attach(this)
        setContentView(R.layout.activity_setting)
        this.showStudentInfo()
    }

    override fun initView() {
        super.initView()
        title_bar.setLeftListener(this)
        btnExit.setOnClickListener(this)
        llAbout.setOnClickListener(this)
        llFeedback.setOnClickListener(this)
        llPrivacyPolicy.setOnClickListener(this)
        llUserService.setOnClickListener(this)
        llUpdatePsw.setOnClickListener(this)
        llVersion.setOnClickListener(this)
    }

    /**
     * 展示用户信息
     */
    private fun showStudentInfo() {
        tvVersion.text = getString(R.string.current_version, BuildConfig.VERSION_NAME)
    }

    /**
     * 更新密码
     */
    private fun goUpdatePsw() {
        val intent = Intent(this, UpdatePasswordActivity::class.java)
        startActivity(intent)
    }

    /**
     * 帮助反馈
     */
    private fun goFeedback() {
        val params = JSONObject()
        val suffix = "feedback.web.js"
        try {
            params.put("callback", "0")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        SystemUtil.gotoWebPage(this, InitBaseLib.getInstance().configManager.h5ServerUrl + suffix, SystemUtil.generateDefautJsonStr(params, suffix))
    }

    /**
     * 隐私政策
     */
    private fun goPrivacy() {
        var url = "axx://commonWeb?url=%s"
        val filePath = FileUtil.getJsBundleSaveFilePath("agreement_privacy.html")
        url = String.format(url, "file://$filePath")
        SchemeDispatcher.jumpPage(this, url)
    }

    /**
     * 用户协议
     */
    private fun goUserService() {
        var url = "axx://commonWeb?url=%s"
        val filePath = FileUtil.getJsBundleSaveFilePath("agreement_userService.html")
        url = String.format(url, "file://$filePath")
        SchemeDispatcher.jumpPage(this, url)
    }

    /**
     * 关于我们
     */
    private fun goAbout() {
        val params = JSONObject()
        val suffix = "about.web.js"
        try {
            params.put("callback", "0")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        SystemUtil.gotoWebPage(this, InitBaseLib.getInstance().configManager.h5ServerUrl + suffix, SystemUtil.generateDefautJsonStr(params, suffix))
    }

    /**
     * 退出登录
     */
    private fun exit() {
        val builder = AlertDialog.Builder(this@SettingActivity)
        builder.setMessage("确认退出登录吗？")
                .setNegativeButton("取消") { dialog, _ ->
                    dialog.dismiss()
                }.setPositiveButton("确定") { dialog, _ ->
                    val intent = Intent(this, LoginActivity::class.java)
                    PassportAPI.instance.logOutApp(object : Passport.Callback<String> {
                        override fun onSuccess(response: BaseResponseBean<String>) {
                        }
                        override fun onError(message: String?, code: Int?) {
                        }
                    })
                    mHandler.postDelayed({
                        SDKConfig.exitLogin()
                        startActivity(intent)
                        ActivityCollector.getInstance().finishActivity(SettingActivity::class.java)
                        ActivityCollector.getInstance().finishActivity(MainActivity::class.java)
                        dialog.dismiss()
                    }, 800)
                }
        builder.show()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.llTitleLeft -> finish()
            R.id.btnExit -> {
                exit()
                collectClickEvent("as302_clk_tuichu")
            }
            R.id.llAbout -> {
                goAbout()
                collectClickEvent("as302_clk_guanyu")
            }
            R.id.llFeedback -> {
                goFeedback()
                collectClickEvent("as302_clk_kefu")
            }
            R.id.llPrivacyPolicy -> {
                goPrivacy()
                collectClickEvent("XSD_357")
            }
            R.id.llUserService -> {
                goUserService()
                collectClickEvent("XSD_421")
            }
            R.id.llUpdatePsw -> {
                goUpdatePsw()
                collectClickEvent("as302_clk_mima")
            }
            R.id.llVersion -> {
                UpdateUtil.getInstance()
                        .inject(this)
                        .setListener(object : IOnlyCheckNAUpdate {
                            override fun onUpdating() {
                            }

                            override fun onUpdateFail() {
                            }

                            override fun onUpdateSuccess(context: Context?) {
                            }

                            override fun onNoUpdate() {
                                ToastUtil.showToast("当前是最新版本")
                            }
                        }).startCheckUpdate()
                collectClickEvent("as302_clk_banben")
            }
        }
    }

    override fun setStatusBar() {
        setStatusBar(Color.parseColor("#F8FAFD"), 0)
        StatusBarUtil.setLightMode(this)
    }

}
