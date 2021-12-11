package gaosi.com.learn.studentapp.setting

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.github.mzule.activityrouter.annotation.Router
import com.gsbaselib.base.event.EventBean
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.GSHttpResponse
import com.gsbaselib.net.callback.GSJsonCallback
import com.gsbaselib.utils.StatusBarUtil
import com.gsbaselib.utils.ToastUtil
import com.gstudentlib.base.BaseActivity
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.event.EventType
import com.lzy.okgo.model.Response
import com.r0adkll.slidr.Slidr
import gaosi.com.learn.R
import gaosi.com.learn.application.AppApi
import gaosi.com.learn.application.event.AppEventType
import kotlinx.android.synthetic.main.activity_setting.title_bar
import kotlinx.android.synthetic.main.activity_update_name.*
import org.greenrobot.eventbus.EventBus
import java.util.regex.Pattern

/**
 * 作者：created by 逢二进一 on 2020/5/11 17:36
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
@Router("updateName")
class UpdateNameActivity: BaseActivity() {

    private var mChangeNameStatus: Int? = 1 //修改姓名状态 0 不可修改 1 可修改
    private var mChangeNameTimeIntervalContent: String? = null
    private var mLastChangeNameTimeContent: String? = null
    private var mName: String? = null
    private var mNewName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置右滑动返回
        Slidr.attach(this)
        setContentView(R.layout.activity_update_name)
        this.getIntentData()
        this.showUI()
    }

    override fun initView() {
        super.initView()
        title_bar.setLeftListener(this)
        tvConfirm.setOnClickListener(this)
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                mNewName = s.toString()
                checkSubmitButtonValid()
                if (!TextUtils.isEmpty(s)) {
                    ivDelete.visibility = View.VISIBLE
                    if (s.length > 8){
                        etName.setText(s.toString().substring(0 ,8))
                        etName.setSelection(8)
                        ToastUtil.showToast("输入的名字过长,请重新输入! ")
                    }
                }else {
                    ivDelete.visibility = View.GONE
                }
            }
            override fun afterTextChanged(s: Editable) {
            }
        })
        ivDelete.setOnClickListener(this)
        val filters = arrayOf<InputFilter>(EditNameFilter())
        etName.filters = filters
    }

    private fun getIntentData() {
        intent?.apply {
            mChangeNameStatus = getStringExtra("changeNameStatus").toInt()
            mChangeNameTimeIntervalContent = getStringExtra("changeNameTimeIntervalContent") ?: ""
            mLastChangeNameTimeContent = getStringExtra("lastChangeNameTimeContent") ?: ""
            mName = getStringExtra("name") ?: ""
            mNewName = mName
        }
    }

    private fun showUI() {
        if(mChangeNameStatus == 1) {
            tvTips.visibility = View.VISIBLE
            tvConfirm.visibility = View.VISIBLE
            tvConfirm.setTextColor(Color.parseColor("#A9DC35"))
            //解决名字过长问题
            if(mName?.length?:0 < 8) {
                etName.setText(mName)
                etName.setSelection(mName?.length?:0)
            }
            tvChangeNameTimeIntervalContent.text = "*$mChangeNameTimeIntervalContent"
            mHandler.postDelayed({ showSoftInput(etName) }, 300)
        }else {
            tvTips.visibility = View.GONE
            tvConfirm.visibility = View.GONE
            etName.hint = mName
            etName.isEnabled = false
            etName.isFocusable = false
            tvChangeNameTimeIntervalContent.text = "*$mChangeNameTimeIntervalContent"
            tvLastChangeNameTimeContent.text = "*$mLastChangeNameTimeContent"
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.llTitleLeft -> finish()
            R.id.tvConfirm -> {
                updateName()
                collectClickEvent("XSD_443")
            }
            R.id.ivDelete -> {
                ivDelete.visibility = View.GONE
                etName.text.clear()
            }
        }
    }

    /**
     * 提交更新姓名
     */
    private fun updateName() {
        if(mNewName == mName) {
            finish()
            return
        }
        toggleSoftInput()
        STBaseConstants.userInfo ?: return
        showLoadingProgressDialog()
        mNewName = etName.text.toString()
        val params = java.util.HashMap<String, String>()
        params["studentId"] = STBaseConstants.userId
        params["studentName"] = mNewName?:""
        params["phone"] = STBaseConstants.userInfo.phone
        GSRequest.startRequest(AppApi.changeStudentName, params, object : GSJsonCallback<String>() {
            override fun onResponseSuccess(response: Response<*>, code: Int, result: GSHttpResponse<String>) {
                if (isFinishing) {
                    return
                }
                dismissLoadingProgressDialog()
                if (showResponseErrorMessage(result) == 1) {
                    val appEventBean = EventBean()
                    appEventBean.what = AppEventType.UPDATE_NAME_SUCCESS
                    appEventBean.obj = mNewName
                    EventBus.getDefault().post(appEventBean)
                    finish()
                }
            }

            override fun onResponseError(response: Response<*>, code: Int, message: String) {
                if (isFinishing) {
                    return
                }
                dismissLoadingProgressDialog()
                ToastUtil.showToast(message + "")
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
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
        } catch (e: Exception) { }
    }

    /**
     * 检查提交按钮的合法性
     */
    private fun checkSubmitButtonValid() {
        when {
            TextUtils.isEmpty(mNewName) -> {
                tvConfirm.setTextColor(Color.parseColor("#9BA1AC"))
                tvConfirm.isClickable = false
            }
            else -> {
                tvConfirm.setTextColor(Color.parseColor("#A9DC35"))
                tvConfirm.isClickable = true
            }
        }
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
            if(TextUtils.isEmpty(source)) {
                return ""
            }
            return stringFilter(source.toString())
        }
    }

    override fun setStatusBar() {
        setStatusBar(Color.parseColor("#F8FAFD"), 0)
        StatusBarUtil.setLightMode(this)
    }

}