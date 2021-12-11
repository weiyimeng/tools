package gaosi.com.learn.studentapp.main

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.text.Html
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import com.chivox.cube.util.FileHelper
import com.crashlytics.android.Crashlytics
import com.gaosi.englishhomework.event.EnglishHomeworkEventType
import com.gaosi.englishrecite.event.EnglishReciteEventType
import com.gaosi.preclass.event.PreClassEventType
import com.gaosi.rn.base.GSReactManager
import com.gaosi.specialcourse.event.SpecialCourseEventType
import com.gaosi.teacheronline.event.TeacherOnLineEventType
import com.gaosi.weex.event.WeexEventType
import com.github.mzule.activityrouter.annotation.Router
import com.gsbaselib.InitBaseLib
import com.gsbaselib.base.bean.BaseData
import com.gsbaselib.base.event.EventBean
import com.gsbaselib.base.inject.GSAnnotation
import com.gsbaselib.base.log.LogUtil
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.GSHttpResponse
import com.gsbaselib.net.callback.GSJsonCallback
import com.gsbaselib.utils.DateUtil
import com.gsbaselib.utils.LOGGER
import com.gsbaselib.utils.SharedPreferenceUtil
import com.gsbaselib.utils.StatusBarUtil
import com.gsbaselib.utils.dialog.AbsAdapter
import com.gsbaselib.utils.dialog.DialogUtil
import com.gsbaselib.utils.update.bean.UpdateEventBean
import com.gsbaselib.utils.update.bean.UpdateType
import com.gsbiloglib.builder.GSConstants
import com.gsbiloglib.log.GSLogUtil
import com.gstudentlib.GSAPI
import com.gstudentlib.SDKConfig
import com.gstudentlib.StatisticsDictionary
import com.gstudentlib.base.BaseActivity
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.base.STBaseFragment
import com.gstudentlib.event.EventType
import com.gstudentlib.event.UpdateHyResourceEvent
import com.gstudentlib.util.SchemeDispatcher
import com.gstudentlib.util.SystemUtil
import com.gstudentlib.util.hy.HyConfig
import com.lzy.okgo.model.Response
import com.umeng.message.PushAgent
import gaosi.com.learn.R
import gaosi.com.learn.application.StatisticsApi
import gaosi.com.learn.application.event.AppEventType
import gaosi.com.learn.bean.main.ActivityDialogBean
import gaosi.com.learn.push.PushEvent
import gaosi.com.learn.push.PushEventDispatcher
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

/**
 * 爱学习主页
 */
@Router("main")
@GSAnnotation(pageId = StatisticsDictionary.homePage)
class MainActivity : BaseActivity() {

    companion object {
        private const val PERMISSION_REQUEST = 12
        private const val RESOURCE_PERMISSION_REQUEST = 13
        var tabTag = 0
    }

    private var mFragments: ArrayList<STBaseFragment> = ArrayList()
    private var mStudyFragment: StudyFragment? = null
    private var mMineFragment: MineFragment? = null
    private var mIsVisible = true
    private var mIsNormalOpenDialog = true
    private var currentFragment: String? = null
    private var mIsChecked = true

    /**
     * 通过阻塞方式监听资源下载的状态
     * 在资源不需要更新的情况下请求权限检查app更新
     * 在资源需要更新的情况下，请求权限下载资源，同时检查app版本更新
     */
    private var mUpdateHyResourceEvent: UpdateHyResourceEvent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EventBus.getDefault().register(this)
        initUserInfo()
        initFragment()
        initBottom()
        loadProvisionFile()
        unZipNativeVoiceRes()
        openResourceListener()
        showDialog()
        showHomeDialog()
        tabTag = 0
        dealWithPushEvent()
        setUmengAlias()
    }

    private fun initUserInfo() {
        try {
            if (STBaseConstants.userInfo?.parentTel1 != null) {
                SharedPreferenceUtil.setStringDataIntoSP("userInfo", "userPhone", STBaseConstants.userInfo.parentTel1)
            }
            if (STBaseConstants.userInfo?.path != null) {
                SDKConfig.setCustomization(STBaseConstants.userInfo.path)
            }
            //Firebase Crashlytics 设置用户ID
            Crashlytics.setUserIdentifier(STBaseConstants.userId)
        } catch (e: Exception) {
            LOGGER.log(e)
        }
    }

    /**
     * 初始化dialog
     */
    private fun showDialog() {
        //现在的策略是正常弹，当出现更新的时候将所有的弹框取消，弹出更新
        mHandler.postDelayed({ showChangePasswordDialog() }, 1500)
    }

    private fun showHomeDialog() {
        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        GSRequest.startRequest(GSAPI.homeDialog, paramMap, object : GSJsonCallback<ActivityDialogBean>() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<ActivityDialogBean>) {
                if (isFinishing || isDestroyed) {
                    return
                }
                //收集异常日志
                if (result.body == null) {
                    collectPerformanceLog(GSLogUtil.PerformanceType.HTTP_REQUEST_ERROR, mUrl, mResponse)
                    return
                }
                val activityDialogBean = result.body
                if (activityDialogBean?.allPopwindowVOs?.hasOpen == 1) {
                    if (mIsVisible) {
                        try {
                            val json = JSONObject()
                            json.put("popName", activityDialogBean.allPopwindowVOs?.popName)
                            json.put("url", activityDialogBean.allPopwindowVOs?.androidUrl)
                            val suffix = "homeActivityAlert.web.js"
                            SchemeDispatcher.openH5Dialog(this@MainActivity, InitBaseLib.getInstance().configManager.h5ServerUrl + suffix, SystemUtil.generateDefautJsonStr(json, suffix))
                            mIsNormalOpenDialog = true
                        } catch (e: Exception) {
                            LOGGER.log(e)
                        }
                    } else {
                        mIsNormalOpenDialog = false
                    }
                }
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {}
        })
    }

    private fun openResourceListener() {
        Thread(Runnable {
            LogUtil.w("开始读取阻塞队列")
            for (i in 0 until HyConfig.takeSize) {
                LogUtil.w("阻塞队列===$i")
                //读取阻塞线程内部数据
                mUpdateHyResourceEvent = HyConfig.takeEvent()
                if (mUpdateHyResourceEvent != null) {
                    //如果不需要更新此时将终结线程
                    if (!STBaseConstants.isResourceUpdate) {
                        LogUtil.w("资源不需要更新")
                        if (mUpdateHyResourceEvent?.shouldUpdate == false) {
                            requestPermissionsDownLoadH5Resource()
                        }
                        break
                    }
                    //此时说明需要更新,同时进行权限验证
                    if (mUpdateHyResourceEvent?.shouldUpdate == true && mUpdateHyResourceEvent?.finishUpdate == false) {
                        if (mUpdateHyResourceEvent?.updateStatus == HyConfig.NONE) {
                            LogUtil.w("资源需要更新")
                            requestPermissionsDownLoadResource()
                        } else if (mUpdateHyResourceEvent?.updateStatus == HyConfig.DOWN_SUCCESS) {
                            LogUtil.w("资源需要更新-下载成功")
                        } else if (mUpdateHyResourceEvent?.updateStatus == HyConfig.DOWN_ERROR) {
                            LogUtil.w("资源需要更新-下载失败")
                            downLoadResourceSuccess()
                            break
                        }
                    }
                    //此时说明更新完成
                    if (mUpdateHyResourceEvent?.finishUpdate == true) {
                        if (mUpdateHyResourceEvent?.updateStatus == HyConfig.ZIP_SUCCESS) {
                            LogUtil.w("资源需要更新-更新完成-解压成功")
                        } else if (mUpdateHyResourceEvent?.updateStatus == HyConfig.ZIP_ERROR) {
                            LogUtil.w("资源需要更新-更新完成-解压失败")
                        }
                        downLoadResourceSuccess()
                        break
                    }
                } else {
                    requestPermissionsDownLoadH5Resource()
                    break
                }
            }
        }).start()
    }

    //请求权限更新页面资源
    private fun requestPermissionsDownLoadH5Resource() {
        runOnUiThread {
            if (!hasPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                requestPermissions("存储权限申请", PERMISSION_REQUEST, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else {
                checkUpdate()
            }
        }
    }

    //请求权限下载资源
    private fun requestPermissionsDownLoadResource() {
        runOnUiThread {
            if (!hasPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                LogUtil.w("资源需要更新-申请权限")
                requestPermissions("存储权限申请", RESOURCE_PERMISSION_REQUEST, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else {
                LogUtil.w("资源需要更新-开始更新")
                HyConfig.downloadHyResource(mUpdateHyResourceEvent?.zipUrl)
            }
        }
    }

    //资源下载完成并且解压完成，此时再进行app版本检查
    private fun downLoadResourceSuccess() {
        runOnUiThread {
            LogUtil.w("资源需要更新-更新完成")
            checkUpdate()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == RESOURCE_PERMISSION_REQUEST) {
            if (perms.size == 1) {
                LogUtil.w("资源需要更新-申请权限-开始更新")
                HyConfig.downloadHyResource(mUpdateHyResourceEvent?.zipUrl)
            } else {
                STBaseConstants.isResourceUpdate = false
                HyConfig.putEvent(shouldUpdate = false, finishUpdate = false, zipUrl = null, updateStatus = HyConfig.NONE)
            }
        } else if (requestCode == PERMISSION_REQUEST) {
            if (perms.size == 1) {
                checkUpdate()
            }
        } else if (requestCode == 101 && perms.contains(Manifest.permission.CAMERA) && perms.contains(Manifest.permission.RECORD_AUDIO)) {
            mStudyFragment?.setAxxLiveListener()
        } else if (requestCode == 102 && perms.contains(Manifest.permission.CAMERA) && perms.contains(Manifest.permission.RECORD_AUDIO)) {
            mStudyFragment?.setDtLiveListener()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == RESOURCE_PERMISSION_REQUEST) {
            STBaseConstants.isResourceUpdate = false
            HyConfig.putEvent(shouldUpdate = true, finishUpdate = false, zipUrl = null, updateStatus = HyConfig.NONE)
            LogUtil.w("下载资源权限申请异常")

            val builder = AlertDialog.Builder(this)
            val alertDialog = builder.create()
            alertDialog.setTitle("温馨提示")
            alertDialog.setMessage("爱学习缺少存储权限将无法更新新版应用!\n请在手机设置--权限管理中开启权限!")
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定") { _, _ -> alertDialog?.dismiss() }
            alertDialog.show()
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.colorAccent))
        }
    }

    private fun initFragment() {
        mFragments.clear()
        mStudyFragment = StudyFragment()
        mFragments.add(mStudyFragment!!)
        currentFragment = mStudyFragment?.pageId
        mMineFragment = MineFragment()
        mFragments.add(mMineFragment!!)
        main_vp.offscreenPageLimit = 2
        val mAdapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return mFragments[position]
            }

            override fun getCount(): Int {
                return mFragments.size
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {}
        }
        main_vp?.adapter = mAdapter
    }

    /**
     * 首先将tabtag设置为点击页面
     * 其次关闭当前切换过程中的所有弹框
     * 最后请求切换后的数据
     */
    private fun initBottom() {
        rl_title.visibility = View.GONE
        rb_home_tab.setOnClickListener {
            tabTag = StudyFragment.PAGE_INDEX
            main_vp?.currentItem = tabTag
            if (StudyFragment.mIsShowBackToTop) {
                changeStudyTabStyle(R.drawable.tab_back_to_top, "回到顶部")
                tvTabStudy.setTextColor(ContextCompat.getColor(this, R.color.tab_text_selected_color))
                ivTabMine.background = ContextCompat.getDrawable(this,R.drawable.icon_mine)
                tvTabMine.setTextColor(ContextCompat.getColor(this, R.color.tab_text_unselected_color))
            } else {
                if (!mIsChecked) {
                    changeTabStyle(true)
                }
            }
            if (mIsChecked && StudyFragment.mIsShowBackToTop) {
                mStudyFragment?.scrollToTop()
            }
            mIsChecked = true
        }
        rb_my_tab.setOnClickListener {
            if (tabTag == MineFragment.PAGE_INDEX) {
                return@setOnClickListener
            }
            mIsChecked = false
            mStudyFragment?.stopScroll()
            tabTag = MineFragment.PAGE_INDEX
            main_vp?.currentItem = tabTag
            changeTabStyle(false)
        }

        //在切换完成以后进行弹框消失，然后将tabTag置为当前页面id
        main_vp?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {}

            override fun onPageSelected(i: Int) {
                DialogUtil.getInstance().dismiss()//为了避免在切换的过程中出现弹框
                LogUtil.w("onPageSelected====关闭完成")
                when (i) {
                    StudyFragment.PAGE_INDEX -> {
                        collectPageLog(mStudyFragment?.pageId, currentFragment)
                        currentFragment = mStudyFragment?.pageId
                    }
                    MineFragment.PAGE_INDEX -> {
                        mMineFragment?.adjustShowExplain()
                        collectPageLog(mMineFragment?.pageId, currentFragment)
                        currentFragment = mMineFragment?.pageId
                    }
                }
            }

            override fun onPageScrollStateChanged(i: Int) {}
        })
    }

    /**
     * 设置tab样式
     */
    private fun changeTabStyle(studySelect: Boolean) {
        tvTabStudy.text = "学习"
        if (studySelect) {
            ivTabStudy.background = ContextCompat.getDrawable(this, R.drawable.app_anim_tab_study)
            val animationDrawable = ivTabStudy.background as AnimationDrawable
            if (!animationDrawable.isRunning) {
                animationDrawable.start()
            }
            tvTabStudy.setTextColor(ContextCompat.getColor(this, R.color.tab_text_selected_color))
            ivTabMine.background = ContextCompat.getDrawable(this,R.drawable.icon_mine)
            tvTabMine.setTextColor(ContextCompat.getColor(this, R.color.tab_text_unselected_color))
        } else {
            ivTabMine.background = ContextCompat.getDrawable(this, R.drawable.app_anim_tab_mine)
            val animationDrawable = ivTabMine.background as AnimationDrawable
            if (!animationDrawable.isRunning) {
                animationDrawable.start()
            }
            tvTabMine.setTextColor(ContextCompat.getColor(this, R.color.tab_text_selected_color))
            ivTabStudy.background = ContextCompat.getDrawable(this,R.drawable.icon_study)
            tvTabStudy.setTextColor(ContextCompat.getColor(this, R.color.tab_text_unselected_color))
        }
    }

    public fun changeStudyTabStyle(resId: Int, text: String) {
        ivTabStudy.background = ContextCompat.getDrawable(this, resId)
        tvTabStudy.text = text
    }

    private fun dealWithPushEvent() {
        if (intent == null || intent.extras == null) {
            return
        }
        val event = intent.extras?.getSerializable(PushEventDispatcher.PUSH_EVENT) as PushEvent?
        PushEventDispatcher.dispatcher(this, event)
    }

    override fun onResume() {
        super.onResume()
        if (!mIsVisible) {
            if (!mIsNormalOpenDialog) {
                this.showHomeDialog()
            }
            mIsVisible = true
            if (tabTag == StudyFragment.PAGE_INDEX && mStudyFragment?.isVisible == true) {//在首页才刷新
//                mStudyFragment.onRefresh()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            existDialog()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    /**
     * 首次打开APP，未修改密码弹窗提示
     */
    private fun showChangePasswordDialog() {
        if (!shouldShowPasswordDialog()) {
            //已经修改过密码，不弹对话框
            //如果是北校用户，不弹修改密码弹框
            checkNotificationEnabled()
            return
        }
        STBaseConstants.changedPasswordCode = 1
        DialogUtil.getInstance().create(this, R.layout.ui_change_password_dialog)
                .show(object : AbsAdapter() {
                    override fun bindListener(onClickListener: View.OnClickListener) {
                        val text = "为了您的<font color='#A9DC35'>账号安全</font>，如果您尚未修改过密码，请尽快修改账户的默认密码~"
                        this.bindText(R.id.tv_content, Html.fromHtml(text))
                        this.bindListener(onClickListener, R.id.btn_change_password)
                        this.bindListener(onClickListener, R.id.tv_cancel)
                    }

                    override fun onClick(v: View?, dialog: DialogUtil?) {
                        super.onClick(v, dialog)
                        when (v?.id) {
                            R.id.btn_change_password -> {
                                dialog?.dismiss()
                                val scheme = "axx://updatePassword?type=" + SharedPreferenceUtil.getStringValueFromSP("userInfo", "loginType", "1")
                                SchemeDispatcher.jumpPage(this@MainActivity, scheme)
                            }
                            R.id.tv_cancel -> dialog?.dismiss()
                        }
                    }

                    override fun onDismiss() {
                        super.onDismiss()
                        //当修改密码弹框消失后，可以弹出宝箱升级通告
                        checkNotificationEnabled()
                        val params = HashMap<String, String>()
                        params["username"] = STBaseConstants.userInfo.phone
                        GSRequest.startRequest(GSAPI.closeUpdateDialog, params, object : GSJsonCallback<BaseData>() {
                            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<BaseData>) {}
                            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {}
                        })
                    }
                })
    }

    /**
     * 检查消息通知是否开启
     */
    private fun checkNotificationEnabled() {
        if (isNotificationEnabled) {
            //弹框消失后，可以弹出宝箱升级通告
            mStudyFragment?.onMainActivityDialogCompleted()
            mMineFragment?.onMainActivityDialogCompleted()
        } else {
            val lastShowTime = SharedPreferenceUtil.getStringValueFromSP("axxuserInfo",
                    "showOpenNotificationTime", "")
            if (!TextUtils.isEmpty(lastShowTime)) {
                try {
                    val lastShowDate = DateUtil.stringToDate(lastShowTime, DateUtil.SIMPLE_FORMAT)
                    val nowDate = Date()
                    val date = DateUtil.getNextDate(lastShowDate, 7)
                    if (nowDate.before(date)) {
                        mStudyFragment?.onMainActivityDialogCompleted()
                        mMineFragment?.onMainActivityDialogCompleted()
                        return
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            collectPageLog(StatisticsApi.pushPermissionOpenDialog)
            DialogUtil.getInstance().create(this, R.layout.ui_open_notification)
                    .show(object : AbsAdapter() {
                        override fun bindListener(onClickListener: View.OnClickListener) {
                            this.bindListener(onClickListener, R.id.ivClose)
                            this.bindListener(onClickListener, R.id.tvRightNowOpen)
                        }

                        override fun onClick(v: View?, dialog: DialogUtil?) {
                            super.onClick(v, dialog)
                            when (v?.id) {
                                R.id.tvRightNowOpen -> {
                                    GSLogUtil.collectClickLog(GSConstants.P?.getCurrRefer(), "XSD_347", "")
                                    dialog?.dismiss()
                                    gotoSet()
                                }
                                R.id.ivClose -> {
                                    GSLogUtil.collectClickLog(GSConstants.P?.getCurrRefer(), "XSD_348", "")
                                    dialog?.dismiss()
                                }
                            }
                        }

                        override fun onDismiss() {
                            super.onDismiss()
                            collectPageLog()
                            //弹框消失后，可以弹出宝箱升级通告
                            mStudyFragment?.onMainActivityDialogCompleted()
                            mMineFragment?.onMainActivityDialogCompleted()
                            val date = Date()
                            SharedPreferenceUtil.setStringDataIntoSP("axxuserInfo",
                                    "showOpenNotificationTime",
                                    DateUtil.dateToString(date, DateUtil.SIMPLE_FORMAT))
                        }
                    })
        }
    }

    private fun shouldShowPasswordDialog(): Boolean {
        //已经修改过密码，不弹对话框
        //如果是北校用户，不弹修改密码弹框
        val flag = STBaseConstants.changedPasswordCode == 1 || STBaseConstants.hasLogin() && STBaseConstants.userInfo.isBeiXiao == 1
        return !flag
    }

    override fun setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, null)
        StatusBarUtil.setLightMode(this)
    }

    /**
     * 代码不加入驰声会加载不成功
     * 经测试即使不申请存储权限也可以使用
     * 驰声需要的资源文件
     */
    private fun loadProvisionFile() {
        FileHelper.extractProvisionOnce(this@MainActivity, STBaseConstants.provisionFilename)
    }

    private fun unZipNativeVoiceRes() {
        LogUtil.w("unzip start")
        val pDialog = ProgressDialog(this)
        pDialog.setCanceledOnTouchOutside(false)
        pDialog.setMessage("解压资源文件中...")
        pDialog.show()
        val service = Executors.newSingleThreadExecutor()
        service.submit({
            FileHelper.extractResourceOnce(this@MainActivity, "vad.zip")
            pDialog.dismiss()
        }, true)
    }

    /**
     * 设置Umeng标识
     */
    private fun setUmengAlias() {
        PushAgent.getInstance(this).setAlias(STBaseConstants.userId, if (InitBaseLib.getInstance().configManager.isRelease) STBaseConstants.Umeng_Push_Type else STBaseConstants.Umeng_Push_Type_Test) { isSuccess, message -> LogUtil.d(if (isSuccess) "设置umeng tag成功" else "设置umeng tag失败") }
    }

    /**
     * 判断是否开启通知功能
     * @return
     */
    private val isNotificationEnabled: Boolean
        get() {
            return try {
                NotificationManagerCompat.from(this).areNotificationsEnabled()
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

    /**
     * 去设置
     */
    private fun gotoSet() {
        val intent = Intent()
        when {
            Build.VERSION.SDK_INT >= 26 -> {
                // android 8.0引导
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                intent.putExtra("android.provider.extra.APP_PACKAGE", packageName)
            }
            Build.VERSION.SDK_INT >= 21 -> {
                // android 5.0-7.0
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                intent.putExtra("app_package", packageName)
                intent.putExtra("app_uid", applicationInfo.uid)
            }
            else -> {
                // 其他
                intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                intent.data = Uri.fromParts("package", packageName, null)
            }
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onPause() {
        super.onPause()
        mIsVisible = false
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRnUpdateEvent(event: UpdateEventBean) {
        LogUtil.d("开始检查：接收到RN升级成功事件")
        try {
            if (event.type == UpdateType.RN_UPDATE) {
                GSReactManager.clear()
            }
        } catch (e: Exception) {
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: EventBean) {
        when (event.what) {
            AppEventType.INTO_CLASS_COMPLETE,
            PreClassEventType.TASK_COMPLETE,
            SpecialCourseEventType.TASK_COMPLETE,
            EnglishReciteEventType.TASK_COMPLETE,
            WeexEventType.TASK_COMPLETE,
            EnglishHomeworkEventType.TASK_COMPLETE,
            EnglishHomeworkEventType.ENGLISH_PREVIEW_COMPLETE,
            TeacherOnLineEventType.TASK_COMPLETE -> {
                //入班成功、任务完成,刷新列表
                mStudyFragment?.completeTodoTask()
            }
            EventType.GET_REWARD -> {
                //英语预习奖励事件
                showEnglishPreviewRewardDialog(event.arg1)
            }
        }
    }

    private fun showEnglishPreviewRewardDialog(count: Int) {
        DialogUtil.getInstance().create(this, R.style.reward_DialogStyle, R.layout.dialog_lession_reward, true, true, 0.8f, true)
                .show(object : AbsAdapter() {
                    override fun bindListener(onClickListener: View.OnClickListener?) {
                        this.bindListener(onClickListener, R.id.ll_reward)
                        bindText(R.id.tv_gold_count, "+$count")
                    }

                    override fun onClick(v: View?, dialog: DialogUtil) {
                        dialog.dismiss()
                    }
                })
    }

    override fun getResources(): Resources {
        val resources = super.getResources()
        val configuration = resources.configuration
        configuration.fontScale = 1.0f
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return resources
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
