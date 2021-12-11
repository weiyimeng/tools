package gaosi.com.learn.studentapp.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.gaosi.homework.jump.HomeworkOptions
import com.gaosi.homework.jump.JumpDoWrongBookBuilder
import com.gsbaselib.InitBaseLib
import com.gsbaselib.base.inject.GSAnnotation
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.GSHttpResponse
import com.gsbaselib.net.callback.GSJsonCallback
import com.gsbaselib.net.callback.GSStringCallback
import com.gsbaselib.utils.ActivityCollector
import com.gsbaselib.utils.LangUtil
import com.gsbaselib.utils.ToastUtil
import com.gsbaselib.utils.TypeValue
import com.gsbaselib.utils.dialog.AbsAdapter
import com.gsbaselib.utils.dialog.DialogUtil
import com.gsbaselib.utils.glide.ImageLoader
import com.gsbiloglib.log.GSLogUtil
import com.gstudentlib.GSAPI
import com.gstudentlib.StatisticsDictionary
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.base.STBaseFragment
import com.gstudentlib.util.SchemeDispatcher
import com.gstudentlib.util.SystemUtil
import com.gstudentlib.util.hy.HyConfig
import com.lzy.okgo.model.Response
import gaosi.com.learn.R
import gaosi.com.learn.application.AppApi
import gaosi.com.learn.bean.main.NotifyStatusBean
import gaosi.com.learn.bean.raw.student_studentInfoBean
import gaosi.com.learn.studentapp.dresscity.DressCityActivity
import gaosi.com.learn.studentapp.login.UserListActivity
import gaosi.com.learn.studentapp.setting.SettingActivity
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * 首页我的
 */
@GSAnnotation(pageId = StatisticsDictionary.mine)
class MineFragment : STBaseFragment() {

    companion object {
        const val NO_SEX = -1
        const val WOMAN = 0
        const val MAN = 1
        const val TIME = 300
        //页面page索引
        const val PAGE_INDEX = 1
        const val CHOOSE_USER_PAGE_REQUEST = 40001
    }
    private var ufoAnimaSize = 4
    //判断首页是否已经完成弹框逻辑
    private var isMainActivityCompleted: Boolean = false
    private var isFisrtVisiable: Boolean = false
    /**
     * 请求头部信息
     */
    private var mStudentInfoBean: student_studentInfoBean? = null
    private var mStudentScore: String? = null

    /**
     * 控制性别的选择器是否可以弹起
     * mFlag 因为StudyFragment在点击头像的调用了check方法，因此会导致radioGroup回调3次，为了避免重复执行故定义变量
     * 当用户切换tab时需要对其进行请求
     */
    private var mFlag = true

    /**
     * 展示性别选择弹框
     */
    private var llMan: RelativeLayout? = null
    private var llWoman: RelativeLayout? = null

    /**
     * 选择性别动画
     * isFirstSelectSex用于区分当前加载动画的类型
     */
    private var mCurrSelectSex: Int = 0
    private var isFirstSelectSex = true//用于标记用户是否已经选择了性别（临时性的）
    //用户等级
    private var mLevel: String = "0"

    override fun initView(inflater: LayoutInflater?, container: ViewGroup?): View? {
        return inflater?.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.adjustStatusBarMargin(vStatusBar)
        init()
        ufoAnima()
    }

    private fun init() {
        cvGoldMall.setOnClickListener(this)
        cvMasonryMall.setOnClickListener(this)
        rlExtracting.setOnClickListener(this)
        rlDressCenter.setOnClickListener(this)
        llErrorBook.setOnClickListener(this)
        llMyStudentInfo.setOnClickListener(this)
        ivHeader.setOnClickListener(this)
        llConstactUs.setOnClickListener(this)
        llSetting.setOnClickListener(this)
        tvChangeStudent.setOnClickListener(this)
        rlTestReportNotify.setOnClickListener(this)
        rlMyFashion.setOnClickListener(this)
    }

    private fun requestStudentInfo() {
        val params = HashMap<String, String>()
        params["studentId"] = STBaseConstants.userId
        GSRequest.startRequest(GSAPI.getStudentInfo, params, object : GSJsonCallback<student_studentInfoBean>() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<student_studentInfoBean>) {
                if (activity == null || activity?.isFinishing == true) {
                    return
                }
                if (result.body == null) {
                    mFlag = true
                    GSLogUtil.collectPerformanceLog(GSLogUtil.PerformanceType.HTTP_REQUEST_ERROR, mUrl, mResponse)
                    return
                }
                mStudentInfoBean = result.body as student_studentInfoBean
                mStudentInfoBean?.let {
                    STBaseConstants.logSymbol = it.logSymbol
                    tvName.text = it.name
                    tvName.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                    STBaseConstants.userInfo.truthName = it.name //用户可能存在空值
                    ImageLoader.setCircleImageViewResource(ivHeader, it.avatarUrl + "?imageView2/2/w/300", R.drawable.icon_default_header)
                    tvFreeChance.visibility = if (it.freeExtractingChance == 0) View.INVISIBLE else View.VISIBLE
                    setGold(it.coinCount)
                    if(it.diamond == "0") {
                        llDiamonde.visibility = View.GONE
                    }else {
                        tvDiamonde.text = it.diamond
                        llDiamonde.visibility = View.VISIBLE
                    }
                    tvLv.text = "Lv" + it.level
                    mLevel = it.level
                    mStudentScore = it.studentScore
                    if (it.multiUser == 1) {
                        tvChangeStudent.visibility = View.VISIBLE
                    } else {
                        tvChangeStudent.visibility = View.GONE
                    }
                    if (it.isTol) {
                        cvMasonryMall.visibility = View.VISIBLE
                    } else {
                        cvMasonryMall.visibility = View.GONE
                    }
                    if (!TextUtils.isEmpty(it.fashionUrl)) {
                        pbFashion.visibility = View.VISIBLE
                        Glide.with(activity!!).load(it.fashionUrl).listener(object :RequestListener<Drawable>{
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                pbFashion.visibility = View.INVISIBLE
                                return false
                            }
                            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                pbFashion.visibility = View.INVISIBLE
                                return false
                            }
                        }).into(ivFashion)
                    }
                    if (STBaseConstants.userInfo != null) {
                        STBaseConstants.userInfo.institutionName = it.institutionName
                        STBaseConstants.userInfo.path = it.avatarUrl
                    }
                    //true是弹窗去确认性别
                    if (it.isWheConfirmGender) {
                        showSexSelector()
                    } else {
                        mFlag = true
                    }
                }
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                if (activity == null || activity?.isFinishing == true) {
                    return
                }
                mFlag = true
                ToastUtil.show(activity, "网络未连接")
            }
        })
    }

    /**
     * 获取测评报告未读状态
     */
    private fun requestTestReportNotify() {
        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        paramMap["types"] = "1" //未读通知类型（1-评测报告)
        GSRequest.startRequest(AppApi.unreadNotify, paramMap, object : GSJsonCallback<ArrayList<NotifyStatusBean>>() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<ArrayList<NotifyStatusBean>>) {
                if (activity == null || activity?.isFinishing == true) {
                    return
                }
                if (result.body == null) {
                    GSLogUtil.collectPerformanceLog(GSLogUtil.PerformanceType.HTTP_REQUEST_ERROR, mUrl, mResponse)
                    return
                }
                val list = result.body
                if (!list.isNullOrEmpty()) {
                    val notifyStatusBean = list[0]
                    if (notifyStatusBean.notify == 1) {
                        vTestReportNotify.visibility = View.VISIBLE
                    } else {
                        vTestReportNotify.visibility = View.GONE
                    }
                }
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {

            }
        })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.cvGoldMall -> {
                goGoldMall()
                GSLogUtil.collectClickLog(pageId, "XSD_332", "")
            }
            R.id.cvMasonryMall -> {
                mStudentInfoBean?.gsId?.let {
                    goMasonryMall(it)
                    GSLogUtil.collectClickLog(pageId, "XSD_608", "")
                }
            }
            R.id.rlTestReportNotify -> {
                goTestReport()
                GSLogUtil.collectClickLog(pageId, "XSD_402", "")
            }
            R.id.llErrorBook -> {
                goErrorBook()
            }
            R.id.ivHeader -> {
                goMyHeader()
                GSLogUtil.collectClickLog(pageId, "as_clk_mine_face", "")
            }
            R.id.llMyStudentInfo -> {
                goMyCoins()
                GSLogUtil.collectClickLog(pageId, "XSD_528", "")
            }
            R.id.rlMyFashion -> {
                goDressCity(0)
                GSLogUtil.collectClickLog(pageId, "as_clk_mine_dress_self", "")
            }
            R.id.rlDressCenter -> {
                goDressCity(1)
                GSLogUtil.collectClickLog(pageId, "as_clk_mine_dress_shop", "")
            }
            R.id.rlExtracting -> {
                goExtracting()
                GSLogUtil.collectClickLog(pageId, "as_clk_mine_call_ufo", "")
            }
            R.id.llConstactUs -> {
                goQiYu()
                GSLogUtil.collectClickLog(pageId, "as102_clk_kefu2", "")
            }
            R.id.llSetting -> {
                goSetting()
                GSLogUtil.collectClickLog(pageId, "as_clk_mine_setting", "")
            }
            R.id.tvChangeStudent -> {
                gotoUserListPage()
                collectClickEvent("as302_clk_qiehuan")
            }
            else -> {
            }
        }
    }

    /**
     * 首页弹框结束
     */
    fun onMainActivityDialogCompleted() {
        isMainActivityCompleted = true
        mStudentInfoBean ?: return
        //前面已判空，这可以用!!
        if (mStudentInfoBean!!.isWheConfirmGender) {
            showSexSelector()
        }
    }

    fun adjustShowExplain() {
        if (!mFlag) return
        mFlag = false
        requestStudentInfo()
        requestTestReportNotify()
    }

    private fun showSexSelector() {
        mCurrSelectSex = NO_SEX//重置性别为-1
        isFirstSelectSex = true//重置是否头次显示性别选择
        if (MainActivity.tabTag != PAGE_INDEX || !isMainActivityCompleted) {//必须是当前tab为我的页面,同时其他弹框要弹出完成,如果不是在当前页面，或者首页没有完成都被驳回
            mFlag = true
            return
        }
        DialogUtil.getInstance().create(activity, R.layout.fragment_home_sex_selector)
                .show(object : AbsAdapter() {
                    override fun bindListener(onClickListener: View.OnClickListener?) {
                        this.bindListener(onClickListener, R.id.tvComfirm, R.id.llMan, R.id.llWoman)
                        //前面已判空，这可以用!!
                        this.bindText(R.id.tvContent, "为了更好的为您服务，请确认" + (if (mStudentInfoBean != null) mStudentInfoBean!!.name + "同学的" else "") + "性别，性别一旦确定，将无法修改哦！")
                        llMan = findViewById(R.id.llMan)
                        llWoman = findViewById(R.id.llWoman)
                    }

                    override fun onClick(v: View?, dialog: DialogUtil?) {
                        super.onClick(v, dialog)
                        when (v?.id) {
                            R.id.tvComfirm -> {
                                //当用户点击确定的时候判断用户有没有选择性别
                                if (mCurrSelectSex == NO_SEX) {
                                    //前面已判空，这可以用!!
                                    activity?.let { ToastUtil.showToast(it, "请选择" + (if (mStudentInfoBean != null) mStudentInfoBean!!.name + "同学的" else "") + "性别！", 150) }
                                    return
                                }
                                dialog?.dismiss()
                                //将性别传送到网上
                                val params = HashMap<String, String>()
                                params["studentId"] = STBaseConstants.userId
                                params["gender"] = mCurrSelectSex.toString()
                                GSRequest.startRequest(GSAPI.genderConfirmToNet, params, object : GSStringCallback() {

                                    override fun onResponseSuccess(response: Response<*>?, code: Int, result: String) {
                                        if (activity == null || activity?.isFinishing == true) {
                                            return
                                        }
                                        requestStudentInfo()//当用户设置完成性别之后要重新请求一下
                                    }

                                    override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                                        if (activity == null || activity?.isFinishing == true) {
                                            return
                                        }
                                        requestStudentInfo()//当用户设置完成性别之后要重新请求一下
                                    }
                                })
                            }
                            R.id.llMan -> clickAnima(MAN)
                            R.id.llWoman -> clickAnima(WOMAN)
                        }
                    }

                    override fun onDismiss() {
                        super.onDismiss()
                        mFlag = true
                    }
                })
    }

    /**
     * 设置金币
     *
     * @param gold
     */
    private fun setGold(gold: Float) {
        if (gold > 999999) {
            tvScore.text = getString(R.string.coin99)
        } else {
            tvScore.text = LangUtil.parseNumberToString(gold, "0")
        }
    }

    /**
     * ufo小飞船上下缓慢漂浮动画
     */
    private fun ufoAnima() {
        ObjectAnimator.ofFloat(llUfo, View.TRANSLATION_Y, 0f, (-TypeValue.dp2px(ufoAnimaSize.toFloat())).toFloat(), 0f).apply {
            duration = 2000
            interpolator = DecelerateInterpolator()
            repeatCount = -1
            start()
        }
    }

    private fun clickAnima(mSelectSex: Int) {
        if (mCurrSelectSex != NO_SEX && mCurrSelectSex == mSelectSex) return
        this.mCurrSelectSex = mSelectSex
        when (mCurrSelectSex) {
            MAN -> {
                if (isFirstSelectSex) {
                    animaSelect(llMan)
                } else {
                    animaSelect(llMan)
                    animaUnSelect(llWoman)
                }
                llMan?.isSelected = true
                llWoman?.isSelected = false
            }
            WOMAN -> {
                if (isFirstSelectSex) {
                    animaSelect(llWoman)
                } else {
                    animaSelect(llWoman)
                    animaUnSelect(llMan)
                }
                llWoman?.isSelected = true
                llMan?.isSelected = false
            }
            else -> {
            }
        }
        isFirstSelectSex = false
    }

    /**
     * 性别选中动画
     */
    private fun animaSelect(animaView: View?) {
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(ObjectAnimator.ofFloat(animaView, "scaleX", 1f, 1.13f), ObjectAnimator.ofFloat(animaView, "scaleY", 1f, 1.13f))
        animatorSet.duration = TIME.toLong()
        animatorSet.interpolator = AccelerateDecelerateInterpolator()
        animatorSet.start()
    }

    /**
     * 性别取消选中动画
     */
    private fun animaUnSelect(animaView: View?) {
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(ObjectAnimator.ofFloat(animaView, "scaleX", 1.13f, 1f), ObjectAnimator.ofFloat(animaView, "scaleY", 1.13f, 1f))
        animatorSet.duration = TIME.toLong()
        animatorSet.interpolator = AccelerateDecelerateInterpolator()
        animatorSet.start()
    }

    /**
     * 修改头像
     */
    private fun goMyHeader() {
        val params = JSONObject()
        val suffix = "myHead.web.js"
        try {
            params.put("callback", "0")
            mStudentInfoBean?.let { params.put("coinCount", it.coinCount) }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        SystemUtil.gotoWebPage(activity, InitBaseLib.getInstance().configManager.h5ServerUrl + suffix, SystemUtil.generateDefautJsonStr(params, suffix))
    }

    /**
     * 金币商城
     */
    private fun goGoldMall() {
        val url = "axx://GoldMall"
        SchemeDispatcher.jumpPage(activity, url)
    }

    /**
     * 钻石商城
     */
    private fun goMasonryMall(gsId: String) {
        val s = "axx://DiamondMarket?axgStudentId=%s&gsId=%s"
        val url = String.format(s, STBaseConstants.userId, gsId)
        SchemeDispatcher.jumpPage(activity, url)
    }

    /**
     * 测评报告
     */
    private fun goTestReport() {
        val url = "axx://testReport"
        SchemeDispatcher.jumpPage(mContext as Activity, url)
    }

    /**
     * 我的积分与金币
     */
    private fun goMyCoins() {
        val json = JSONObject()
        json.put("userId", STBaseConstants.userId)
        val s = "axx://rnPage?url=%s&param=%s"
        val url = String.format(s, "myGold", json.toString())
        SchemeDispatcher.jumpPage(activity, url)
    }

    /**
     * 联系客服
     */
    private fun goQiYu() {
        SchemeDispatcher.gotoQiniu(activity , null)
    }

    /**
     * 设置
     */
    private fun goSetting() {
        val mIntent = Intent(activity, SettingActivity::class.java)
        mIntent.putExtra("coinCount", mStudentInfoBean?.coinCount.toString())
        mIntent.putExtra("avatarUrl", mStudentInfoBean?.avatarUrl ?: "")
        mIntent.putExtra("name", mStudentInfoBean?.name ?: "")
        mIntent.putExtra("schoolName", mStudentInfoBean?.schoolName ?: "")
        mIntent.putExtra("sex", mStudentInfoBean?.sex)
        mIntent.putExtra("level", mLevel)
        startActivity(mIntent)
    }

    /**
     * 装扮自己与装扮城
     */
    private fun goDressCity(position: Int) {
        if (!STBaseConstants.isResourceUpdateSuccess) {
            ToastUtil.show(activity, "装扮资源正在更新")
            return
        }
        mStudentInfoBean?.let {
            val mIntent = Intent(activity, DressCityActivity::class.java)
            mIntent.putExtra("fromPage", "mine")
            mIntent.putExtra("position", position.toString())
            mIntent.putExtra("coinCount", it.coinCount.toString())
            startActivity(mIntent)
        }
    }

    /**
     * 抽取装扮
     */
    private fun goExtracting() {
        if (!STBaseConstants.isResourceUpdateSuccess) {
            ToastUtil.show(activity, "装扮资源正在更新")
            return
        }
        val json = JSONObject()
        json.put("userId", STBaseConstants.userId)
        json.put("pageName", "mine")
        val s = "axx://rnPage?url=%s&param=%s"
        val url = String.format(s, "ExtractDressUp", json.toString())
        SchemeDispatcher.jumpPage(activity, url)
    }

    /**
     * 错题本
     */
    private fun goErrorBook() {
        HomeworkOptions.instance
                .with(activity)
                .pad(pageId)
                .start(JumpDoWrongBookBuilder())
    }

    /**
     * 去往用户列表
     */
    private fun gotoUserListPage() {
        val intent = Intent(mContext, UserListActivity::class.java)
        startActivityForResult(intent, CHOOSE_USER_PAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_USER_PAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            ActivityCollector.getInstance().finishAllActivity()
            MainActivity.tabTag = 0
            HyConfig.updateHyResource()//重新执行一次更新操作
            val intent = Intent(mContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isFisrtVisiable) {
            requestStudentInfo()
            requestTestReportNotify()
        }
        isFisrtVisiable = true
    }

    override fun onPause() {
        super.onPause()
        if(MainActivity.tabTag == PAGE_INDEX) {
            DialogUtil.getInstance().dismiss()
        }
    }
}
