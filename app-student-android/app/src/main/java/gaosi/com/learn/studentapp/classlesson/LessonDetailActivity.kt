package gaosi.com.learn.studentapp.classlesson

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ImageSpan
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import com.github.mzule.activityrouter.annotation.Router
import com.gsbaselib.InitBaseLib
import com.gsbaselib.base.GSBaseFragment
import com.gsbaselib.base.event.EventBean
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.GSHttpResponse
import com.gsbaselib.net.callback.GSJsonCallback
import com.gsbaselib.utils.*
import com.gsbaselib.utils.dialog.AbsAdapter
import com.gsbaselib.utils.dialog.DialogUtil
import com.gsbiloglib.builder.GSConstants.Companion.P
import com.gsbiloglib.log.GSLogUtil
import com.gstudentlib.GSAPI
import com.gstudentlib.base.BaseActivity
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.event.EventType
import com.gstudentlib.util.SchemeDispatcher
import com.gstudentlib.util.SystemUtil
import com.lzy.okgo.model.Response
import gaosi.com.learn.R
import gaosi.com.learn.bean.classlesson.LessonBean
import gaosi.com.learn.bean.classlesson.LessonData
import gaosi.com.learn.bean.raw.GiftInfoBean
import gaosi.com.learn.studentapp.classlesson.status.CourseTypeId
import gaosi.com.learn.studentapp.classlesson.status.SubjectId
import gaosi.com.learn.studentapp.dresscity.adapter.FragmentAdapter
import gaosi.com.learn.view.AxxEmptyView
import gaosi.com.learn.view.CenterAlignImageSpan
import kotlinx.android.synthetic.main.activity_lession_detail.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*
import kotlin.collections.HashMap

/**
 * 讲次详情页面
 */
@Router("lessonDetail")
class LessonDetailActivity : BaseActivity(), TabLayout.OnTabSelectedListener {

    private var mCurrentPage = 0
    private var mClassId: String = ""
    private var curLessonId: String = "" //当前lessonId
    private var mFragments: ArrayList<GSBaseFragment>? = ArrayList()
    private var mCommentVpAdapter: FragmentAdapter? = null
    //讲次列表
    private var mLessonData: LessonData? = null
    //请求宝箱的信息
    private var giftInfoBean: GiftInfoBean? = null
    //宝箱是否全部显示出来
    private var mIsTreasureChestAll = false
    //自动隐藏宝箱
    private var autoHideRunnable: Runnable? = null
    //异常流状态
    private var mEmptyType: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lession_detail)
        EventBus.getDefault().register(this)
    }

    override fun initView() {
        super.initView()
        title_bar.setLeftListener(this)
        ivMore.setOnClickListener(this)
        this.getIntentData()
        this.requestClassLesson()
        mEmptyView?.setClickCallBackListener(object : AxxEmptyView.OnClickCallBackListener {
            override fun onClick() {
                when (mEmptyType) {
                    1 -> {
                        requestClassLesson()
                    }
                }
            }
        })
    }

    /**
     * 获取页面传输数据
     */
    private fun getIntentData() {
        intent?.apply {
            mClassId = intent.getStringExtra("classId") ?: ""
            curLessonId = intent.getStringExtra("lessonId") ?: ""
        }
    }

    /**
     * 获取讲次列表
     */
    private fun requestClassLesson() {
        this.setEmptyStatus(0)
        showLoadingProgressDialog()
        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        paramMap["classId"] = mClassId
        paramMap["onlyLessonList"] = "2"
        if(!TextUtils.isEmpty(curLessonId)) {
            paramMap["lessonId"] = curLessonId
        }
        GSRequest.startRequest(GSAPI.myLessonList, paramMap, object : GSJsonCallback<LessonData>() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<LessonData>) {
                if (isFinishing || isDestroyed) {
                    return
                }
                dismissLoadingProgressDialog()
                //收集异常日志
                if (showResponseErrorMessage(result) != 1) {
                    dismissLoadingProgressDialog()
                    setEmptyStatus(1)
                    GSLogUtil.collectPerformanceLog(GSLogUtil.PerformanceType.HTTP_REQUEST_ERROR, mUrl, mResponse)
                    return
                }
                if(result.body.lessonList.isNullOrEmpty()) {
                    dismissLoadingProgressDialog()
                    setEmptyStatus(1)
                    return
                }
                mLessonData = result.body
                showData()
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                if (isFinishing || isDestroyed) {
                    return
                }
                dismissLoadingProgressDialog()
                message?: return
                setEmptyStatus(1)
            }
        })
    }

    /**
     * 数据展示
     */
    private fun showData() {
        mFragments?.clear()
        tlLessonNum.removeAllTabs()
        tlLessonNum.clearOnTabSelectedListeners()
        mLessonData?.let {
            val spannableString = SpannableString("      ${it.className}")

            val periodDrawable = ContextCompat.getDrawable(mContext, CourseTypeId.getCourseTypeLabel(it.courseTypeId))
            periodDrawable?.setBounds(0, 0, TypeValue.dp2px(44F), TypeValue.dp2px(20F))
            val imageSpan1 = CenterAlignImageSpan(periodDrawable)
            spannableString.setSpan(imageSpan1, 0, 2, ImageSpan.ALIGN_BASELINE)

            val subjectDrawable = ContextCompat.getDrawable(mContext, SubjectId.getSubjectLabel(it.subjectId))
            subjectDrawable?.setBounds(0, 0, TypeValue.dp2px(20F), TypeValue.dp2px(20F))
            val imageSpan = CenterAlignImageSpan(subjectDrawable)
            spannableString.setSpan(imageSpan, 3, 5, ImageSpan.ALIGN_BASELINE)

            tvClassName.text = spannableString

            if(it.showClassTime == 0) {
                tvClassBeginTimeEndTime.visibility = View.VISIBLE
                tvClassBeginTimeEndTime.text = "暂未开课"
            }else {
                if(it.beginTime == null || it.endTime == null) {
                    tvClassBeginTimeEndTime.visibility = View.GONE
                }else {
                    val beginDataTimeString = DateUtil.longToString(it.beginTime?:0, "yyyy年MM月dd日")
                    val endDataTimeString = DateUtil.longToString(it.endTime?:0, "yyyy年MM月dd日")
                    tvClassBeginTimeEndTime.text = "$beginDataTimeString-$endDataTimeString"
                    tvClassBeginTimeEndTime.visibility = View.VISIBLE
                }
            }

            for (index in 0 until it.lessonList!!.size) {
                val lessonDetailFragment = LessonDetailFragment.instance()
                lessonDetailFragment.setCurrPosition(index)
                mFragments?.add(lessonDetailFragment)
                tlLessonNum.addTab(tlLessonNum.newTab())
            }
            mCommentVpAdapter = mFragments?.let { it -> FragmentAdapter(supportFragmentManager, it) }
            vpContent.adapter = mCommentVpAdapter
            vpContent.offscreenPageLimit = mFragments?.size ?: 0
            tlLessonNum.setupWithViewPager(vpContent)
            ivMore.visibility = View.VISIBLE
            vLine.visibility = View.VISIBLE
            var position = 0
            //设置自定义视图
            for (i in 0 until tlLessonNum.tabCount) {
                val tab = tlLessonNum.getTabAt(i)
                tab?.text = "第${i + 1}讲"
                if(it.lessonList!![i].lessonId == it.curLessonId) {
                    position = i
                }
            }
            tlLessonNum.addOnTabSelectedListener(this)
            if(position == 0) {
                (mFragments?.get(position) as LessonDetailFragment).requestMyClassDetail()
            }else {
                vpContent.currentItem = position
            }
            if (1 == it.haveTreasureBox) {
                requestBoxInfo(it.haveHomeWorkSize ?: 0)
            } else {
                hideTreasureChest()
            }
        }
    }

    /**
     * 获取宝箱信息
     */
    private fun requestBoxInfo(haveHomeWorkSize: Int) {
        val params = HashMap<String, String>()
        params["classId"] = mClassId
        params["studentId"] = STBaseConstants.userId
        params["haveHomeWorkSize"] = haveHomeWorkSize.toString() + ""
        GSRequest.startRequest(GSAPI.Treasure_getInfo, params, object : GSJsonCallback<GiftInfoBean>() {
            override fun onResponseSuccess(response: Response<*>, code: Int, result: GSHttpResponse<GiftInfoBean>) {
                if (isFinishing || isDestroyed) {
                    return
                }
                if (result.body != null) {
                    giftInfoBean = result.body
                    //如果可显示的话显示小球
                    if (giftInfoBean?.display == true) {
                        showTreasureChest()
                    } else {
                        hideTreasureChest()
                    }
                } else {
                    hideTreasureChest()
                }
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                if (isFinishing || isDestroyed) {
                    return
                }
                hideTreasureChest()
            }
        })
    }

    /**
     * 获取当前对象数据
     * @param position
     * @return
     */
    fun getData(position: Int): LessonBean? {
        mLessonData?.lessonList?.let {
            return if (position < it.size) {
                it[position]
            } else null
        }
        return null
    }

    private fun showRewardDialog(count: Int) {
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

    /**
     * 显示宝箱
     */
    fun showTreasureChest() {
        rlTreasureChest.visibility = View.VISIBLE
        gvTreasureChest.refreshGifeView(giftInfoBean?.offset ?: 0)
        showTreasureChestHalf()
        //如果宝箱可打开进行左右抖动提醒用户 因为动画显示需要时间，所以延迟500ms执行
        if (giftInfoBean?.open == true && giftInfoBean?.surprised == true) {
            showTreasureChestAll()
            mHandler.postDelayed(Runnable {
                if (!isFinishing) return@Runnable
                ivTreasureChest.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake_left_top_right_bottom))
            }, 500)
        }
        rlTreasureChest.setOnClickListener {
            if(mIsTreasureChestAll) {
                openTreasureChest(giftInfoBean)
            }
            if (giftInfoBean?.open == false || giftInfoBean?.surprised == false) {
                autoHideRunnable?.let {
                    mHandler.removeCallbacks(it)
                }
                autoHideRunnable = autoAllToHalfRunnable()
                mHandler.postDelayed(autoHideRunnable , 3000)
                if(!mIsTreasureChestAll) {
                    this.showTreasureChestAll()
                }
            }
        }
    }

    /**
     * 隐藏宝箱
     */
    fun hideTreasureChest() {
        rlTreasureChest.visibility = View.GONE
    }

    /**
     * 宝箱全部显示
     */
    private fun showTreasureChestAll() {
        mIsTreasureChestAll = true
        val objectAnimator = ObjectAnimator.ofFloat(rlTreasureChest, "translationX", TypeValue.dp2px(52f) / 2f, 0f)
                .setDuration(300)
        objectAnimator.interpolator = DecelerateInterpolator()
        objectAnimator.start()
        ObjectAnimator.ofFloat(rlTreasureChest, "alpha", 0.3f, 1f)
                .setDuration(300)
                .start()
    }

    /**
     * 宝箱显示一半
     */
    private fun showTreasureChestHalf() {
        mIsTreasureChestAll = false
        val objectAnimator = ObjectAnimator.ofFloat(rlTreasureChest, "translationX", 0f, TypeValue.dp2px(52f) / 2f)
                .setDuration(300)
        objectAnimator.interpolator = DecelerateInterpolator()
        objectAnimator.start()
        ObjectAnimator.ofFloat(rlTreasureChest, "alpha", 1f, 0.3f)
                .setDuration(300)
                .start()
    }

    /**
     * 展开宝箱
     */
    private fun openTreasureChest(giftInfoBean: GiftInfoBean?) {
        ObjectAnimator.ofFloat(rlTreasureChest, "scaleX", 1f, 1.2f, 1f)
                .setDuration(300)
                .start()
        ObjectAnimator.ofFloat(rlTreasureChest, "scaleY", 1f, 1.2f, 1f)
                .setDuration(300)
                .start()
        //弹出宝箱
        try {
            val inner = JSONObject()
            inner.put("allStar", giftInfoBean?.allStar)
            inner.put("level", giftInfoBean?.level)
            inner.put("open", giftInfoBean?.open)
            inner.put("offset", giftInfoBean?.offset)
            inner.put("surprised", giftInfoBean?.surprised)
            inner.put("treasureCount", giftInfoBean?.treasureCount)
            inner.put("classId", mClassId)
            val outer = JSONObject()
            outer.put("TC__TYPE", "TC_GLOD_BOX")
            outer.put("param", inner)
            val suffix = STBaseConstants.suffixPopup
            SchemeDispatcher.openH5Dialog(this, InitBaseLib.getInstance().configManager.h5ServerUrl + suffix, SystemUtil.generateDefautJsonStr(outer, suffix))
            collectClickEvent("as_clk_homework_gold_box")
        } catch (e: Exception) {
            LOGGER.log("context", e)
        }
    }

    /**
     * 自动隐藏宝箱功能
     */
    private fun autoAllToHalfRunnable(): Runnable {
        return Runnable {
            showTreasureChestHalf()
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.llTitleLeft -> finish()
            R.id.ivBack -> finish()
            R.id.ivMore -> {
                if (null != mLessonData) {
                    collectClickEvent("XSD_557")
                    LessonsDialogFragment.newInstance(mLessonData).show(supportFragmentManager, "dialog")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mLessonData?.let {
            (mFragments?.get(mCurrentPage) as LessonDetailFragment).requestMyClassDetail()
            if (1 == it.haveTreasureBox) {
                requestBoxInfo(it.haveHomeWorkSize ?: 0)
            } else {
                hideTreasureChest()
            }
        }
    }

    /**
     * 选取讲次成功
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onClickLessonEvent(event: EventBean) {
        if (EventType.CLICK_CHANGE_LESSON == event.what) {
            if (null != event.obj) {
                collectClickEvent("XSD_530")
                val mLessonId = event.obj.toString()
                for(index in mLessonData?.lessonList?.indices!!) {
                    if(mLessonData?.lessonList?.get(index)?.lessonId == mLessonId) {
                        vpContent.currentItem = index
                        break
                    }
                }
            }
        } else if (EventType.GET_REWARD == event.what) {
            this.showRewardDialog(event.arg1)
            (mFragments?.get(mCurrentPage) as LessonDetailFragment).requestMyClassDetail()
        }else if (event.what == EventType.GET_GLOD_COINS) {
            giftInfoBean?.let {
                val params = java.util.HashMap<String, String>()
                params["classId"] = mClassId
                params["level"] = giftInfoBean?.level.toString()
                params["studentId"] = STBaseConstants.userId
                params["type"] = giftInfoBean?.type.toString()
                params["sgdia"] = giftInfoBean?.sgdia.toString()
                GSRequest.startRequest(GSAPI.Treasure_open, params, object : GSJsonCallback<GiftInfoBean>() {
                    override fun onResponseSuccess(response: Response<*>, code: Int, result: GSHttpResponse<GiftInfoBean>) {
                        if (isFinishing || isDestroyed) {
                            return
                        }
                    }
                    override fun onResponseError(response: Response<*>, code: Int, message: String) {
                        if (isFinishing || isDestroyed) {
                            return
                        }
                        ToastUtil.showToast(message + "")
                    }
                })
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

    /**
     * 将执行权限代理到Fragment
     */
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        super.onPermissionsGranted(requestCode, perms)
        mLessonData?.let {
            (mFragments?.get(mCurrentPage) as LessonDetailFragment).onPermissionsGranted(requestCode , perms)
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        super.onPermissionsDenied(requestCode, perms)
        /**
         * 若是在权限弹窗中，用户勾选了'NEVER ASK AGAIN.'或者'不在提示'，且拒绝权限。
         * 这时候，需要跳转到设置界面去，让用户手动开启。
         */
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this)
                    .setTitle("权限申请")
                    .setRationale("若不允许, 你将无法使用直播互动、提交作业等功能")
                    .setPositiveButton("去设置")
                    .setNegativeButton("取消")
                    .build()
                    .show()
        }
    }

    override fun setStatusBar() {
        setStatusBar(Color.parseColor("#F8FAFD"), 0)
        StatusBarUtil.setLightMode(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        tab?: return
        mCurrentPage = tab?.position
        (mFragments?.get(mCurrentPage) as LessonDetailFragment).requestMyClassDetail()
    }
}
