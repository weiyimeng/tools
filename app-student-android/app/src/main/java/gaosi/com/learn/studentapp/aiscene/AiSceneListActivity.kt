package gaosi.com.learn.studentapp.aiscene

import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.PopupWindowCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.GSHttpResponse
import com.gsbaselib.net.callback.GSJsonCallback
import com.gsbiloglib.log.GSLogUtil
import com.lzy.okgo.model.Response
import android.widget.*
import com.github.mzule.activityrouter.annotation.Router
import com.gsbaselib.base.GSBaseApplication
import com.gsbaselib.base.event.EventBean
import com.gsbaselib.base.inject.GSAnnotation
import com.gsbaselib.base.log.LogUtil
import com.gsbaselib.utils.StatusBarUtil
import com.gsbaselib.utils.ToastUtil
import com.gsbaselib.utils.TypeValue
import com.gsbaselib.utils.error.ErrorView
import com.gsbaselib.utils.net.NetworkUtil
import com.gstudentlib.GSAPI
import com.gstudentlib.StatisticsDictionary
import com.gstudentlib.base.BaseActivity
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.event.EventType
import gaosi.com.learn.R
import gaosi.com.learn.bean.aiscene.SceneInfo
import gaosi.com.learn.bean.aiscene.SceneListBean
import kotlinx.android.synthetic.main.activity_ai_scene_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.abs

/**
 * Created by huangshan on 2019/8/02.
 */
@Router("aiSceneList")
@GSAnnotation(pageId = StatisticsDictionary.sceneList)
class AiSceneListActivity : BaseActivity() {

    companion object {
        var mSceneInfo: SceneInfo? = null
    }

    private var mSceneListAdapter: AiSceneListAdapter? = null
    private val mSceneLists = ArrayList<SceneInfo>()
    private val easyList = ArrayList<SceneInfo>()
    private val middleList = ArrayList<SceneInfo>()
    private val hardList = ArrayList<SceneInfo>()
    private lateinit var popupWindow: PopupWindow
    private var isPopupShow = false
    private var topImageView: ImageView? = null
    private var bottomImageView: ImageView? = null
    private var mStatus = 0
    private var mTitleHeight = 0
    private var mContentHeight = 0
    private var mTopImageHeight = 0
    private var mBottomImageHeight = 0
    private var mScrollY = 0
    private var mIsRefresh = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ai_scene_list)
        requestSceneList()
        EventBus.getDefault().register(this)
    }

    override fun initView() {
        super.initView()
        ivBack.setOnClickListener(this)
        rlSelector.setOnClickListener(this)
        evError.register(vStub)
        evError.setClickCallBackListener(object : ErrorView.OnClickCallBackListener {
            override fun onClick(status: ErrorView.ERROR, v: View) {

            }
        })
        initTopBottomImageView(STBaseConstants.deviceInfoBean.screenHeight)
        adjustStatusBarMargin(vStatusBar2)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        recycler_view.layoutManager = layoutManager
        mSceneListAdapter = AiSceneListAdapter()
        mSceneListAdapter?.setNewData(mSceneLists)
        recycler_view.adapter = mSceneListAdapter
        recycler_view.isNestedScrollingEnabled = false
    }

    private fun requestSceneList() {
        if (NetworkUtil.isConnected(GSBaseApplication.getApplication())) {
            showLoadingProgressDialog()
            val paramMap = HashMap<String, String>()
            paramMap["studentId"] = STBaseConstants.userId
            paramMap["pageNum"] = "1"
            GSRequest.startRequest(GSAPI.sceneList, paramMap, object : GSJsonCallback<SceneListBean>() {
                override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<SceneListBean>) {
                    if (isFinishing || isDestroyed) {
                        return
                    }
                    dismissLoadingProgressDialog()
                    if (showResponseErrorMessage(result) == 0) {
                        showErrorView()
                        return
                    }
                    //收集异常日志
                    if (result.body == null) {
                        GSLogUtil.collectPerformanceLog(GSLogUtil.PerformanceType.HTTP_REQUEST_ERROR, mUrl, mResponse)
                        showErrorView()
                        return
                    }
                    result.body.aiScenesInfo ?: return
                    val sceneList = result.body.aiScenesInfo as List<SceneInfo>
                    if (sceneList.isEmpty()) {
                        showErrorView()
                        return
                    }
                    evError.normalError()
                    easyList.clear()
                    middleList.clear()
                    hardList.clear()
                    sceneList.forEach {
                        when (it.scenesLevel) {
                            0 -> easyList.add(it)
                            1 -> middleList.add(it)
                            2 -> hardList.add(it)
                        }
                    }
                    if (easyList.size != 0) {
                        val sceneInfo = SceneInfo()
                        sceneInfo.scenesLevel = 0
                        easyList.add(0, sceneInfo)
                    }
                    if (middleList.size != 0) {
                        val sceneInfo = SceneInfo()
                        sceneInfo.scenesLevel = 1
                        middleList.add(0, sceneInfo)
                    }
                    if (hardList.size != 0) {
                        val sceneInfo = SceneInfo()
                        sceneInfo.scenesLevel = 2
                        hardList.add(0, sceneInfo)
                    }
                    mSceneLists.clear()
                    mSceneLists.addAll(easyList)
                    mSceneLists.addAll(middleList)
                    mSceneLists.addAll(hardList)
                    mSceneListAdapter?.setEasyList(easyList)
                    mSceneListAdapter?.setMiddleList(middleList)
                    mSceneListAdapter?.setHardList(hardList)
                    mSceneListAdapter?.notifyDataSetChanged()
                    rlSelector.visibility = View.VISIBLE
                    val viewTreeObserver = recycler_view.viewTreeObserver
                    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            //移除监听，只用于布局初始化
                            recycler_view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            LogUtil.e("recycler_view: " + recycler_view.height)
                            if (recycler_view.height >= STBaseConstants.deviceInfoBean.screenHeight) {
                                initTopBottomImageView(recycler_view.height)
                            }
                            if (!mIsRefresh) {
                                initImageView(recycler_view.height - mTopImageHeight - mBottomImageHeight)
                            }
                            scrollview.scrollTo(0, recycler_view.height)
                            if (mIsRefresh) {
                                scrollview.scrollTo(0, mScrollY)
                            }
                        }
                    })
                }

                override fun onResponseError(response: Response<*>?, p1: Int, message: String?) {
                    if (isFinishing || isDestroyed) {
                        return
                    }
                    dismissLoadingProgressDialog()
                    showErrorView()
                    message ?: return
                    ToastUtil.showToast(message)
                }
            })
        } else {
            ToastUtil.showToast("网络连接异常")
        }
    }

    private fun showErrorView() {
        evError.unContentError()
        val tvRemindSingle =  evError.findViewById<TextView>(R.id.tvRemindSingle)
        tvRemindSingle.text = "话题不存在"
        tvRemindSingle.setTextColor(Color.WHITE)
        tvRemindSingle.visibility = View.VISIBLE
    }

    /**
     * 设置头、底部图片位置
     */
    private fun initTopBottomImageView(height: Int) {
        val rlBgParam = rlBg.layoutParams
        rlBgParam.height = height
        rlBg.layoutParams = rlBgParam

        if (topImageView == null) {
            topImageView = ImageView(this@AiSceneListActivity)
            topImageView?.scaleType = ImageView.ScaleType.CENTER_CROP
            topImageView?.setImageResource(R.drawable.icon_scene_list_top)
            rlBg.addView(topImageView)
            topImageView?.let {
                mTopImageHeight = getViewHeight(it)
            }
        }
        val topParam = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        topImageView?.layoutParams = topParam

        if (bottomImageView == null) {
            bottomImageView = ImageView(this@AiSceneListActivity)
            bottomImageView?.scaleType = ImageView.ScaleType.CENTER_CROP
            bottomImageView?.setImageResource(R.drawable.icon_scene_list_bottom)
            rlBg.addView(bottomImageView)
            bottomImageView?.let {
                mBottomImageHeight = getViewHeight(it)
            }
        }
        val bottomParam = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        bottomParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        bottomImageView?.layoutParams = bottomParam
    }

    /**
     * 弹出PopupWindow
     */
    private fun showPopupWindow() {
        val layout = layoutInflater.inflate(R.layout.scene_list_popup_layout, null, false)
        val tvEasy = layout.findViewById<TextView>(R.id.tvEasy)
        val tvMiddle = layout.findViewById<TextView>(R.id.tvMiddle)
        val tvHard = layout.findViewById<TextView>(R.id.tvHard)
        tvEasy.setOnClickListener(this)
        tvMiddle.setOnClickListener(this)
        tvHard.setOnClickListener(this)
        val drawable = ContextCompat.getDrawable(this, R.drawable.bg_dress_save)
        when (mStatus) {
            0 -> {
                tvEasy.background = drawable
                tvMiddle.background = null
                tvHard.background = null
            }
            1 -> {
                tvEasy.background = null
                tvMiddle.background = drawable
                tvHard.background = null
            }
            2 -> {
                tvEasy.background = null
                tvMiddle.background = null
                tvHard.background = drawable
            }
        }
        popupWindow = PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popupWindow.isOutsideTouchable = true
        popupWindow.isTouchable = true
        popupWindow.isFocusable = true
        val contentView = popupWindow.contentView
        //需要先测量，PopupWindow还未弹出时，宽高为0
        contentView.measure(makeDropDownMeasureSpec(popupWindow.width),
                makeDropDownMeasureSpec(popupWindow.height))
        val offsetX = abs(popupWindow.contentView.measuredWidth - rlSelector.width) / 2
        PopupWindowCompat.showAsDropDown(popupWindow, rlSelector, -offsetX, 20, Gravity.START)
        isPopupShow = true
        popupWindow.setOnDismissListener {
            showArrowAnim(true)
            isPopupShow = false
        }
    }

    private fun makeDropDownMeasureSpec(measureSpec: Int): Int {
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), getDropDownMeasureSpecMode(measureSpec))
    }

    private fun getDropDownMeasureSpecMode(measureSpec: Int): Int {
        return when (measureSpec) {
            ViewGroup.LayoutParams.WRAP_CONTENT -> View.MeasureSpec.UNSPECIFIED
            else -> View.MeasureSpec.EXACTLY
        }
    }

    /**
     * 绘制imageView
     */
    private fun initImageView(height: Int) {
        LogUtil.e("initImageView: " + TypeValue.px2dp(height.toFloat()).toString())
        val imageHeight = TypeValue.dp2px(285f)
        val marginTop = TypeValue.dp2px(20f)
        val num = (height + imageHeight + marginTop) / TypeValue.dp2px(440f)
        for (index in 0 until num) {
            val param = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, imageHeight)
            val dpTop = index * TypeValue.dp2px(440f) + mTopImageHeight + marginTop
            param.setMargins(0, dpTop, 0, 0)
            val imageView = ImageView(this@AiSceneListActivity)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.layoutParams = param
            if (index % 2 == 1) {
                imageView.setImageResource(R.drawable.icon_scene_list_slice_right)
            } else {
                imageView.setImageResource(R.drawable.icon_scene_list_slice_left)
            }
            rlBg.addView(imageView)
        }
    }

    /**
     * 选择箭头动画
     */
    private fun showArrowAnim(isDown: Boolean) {
        val anim = if (isDown) {
            ObjectAnimator.ofFloat(ivArrow, "rotation", 180F, 360F)
        } else {
            ObjectAnimator.ofFloat(ivArrow, "rotation", 0F, 180F)
        }
        anim.duration = 300
        anim.start()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.ivBack -> {
                finish()
                collectClickEvent("as600_clk_return")
            }
            R.id.rlSelector -> {
                if (isPopupShow) {
                    showArrowAnim(true)
                } else {
                    showArrowAnim(false)
                }
                showPopupWindow()
            }
            R.id.tvEasy -> {
                mStatus = 0
                tvLevel.text = "简单"
                popupWindow.dismiss()
                initItemHeight()
                scrollview.scrollTo(0, recycler_view.height)
                collectClickEvent("as600_clk_simple_topic")
            }
            R.id.tvMiddle -> {
                mStatus = 1
                tvLevel.text = "中等"
                popupWindow.dismiss()
                initItemHeight()
                val height = recycler_view.height - (mTitleHeight + (easyList.size - 1) * mContentHeight + STBaseConstants.deviceInfoBean.screenHeight)
                LogUtil.e("MiddleHeight: $height")
                scrollview.scrollTo(0, height)
                scrollview.scrollY
                collectClickEvent("as600_clk_medium_topic")
            }
            R.id.tvHard -> {
                mStatus = 2
                tvLevel.text = "困难"
                popupWindow.dismiss()
                initItemHeight()
                val height = recycler_view.height - (mTitleHeight * 2 + (easyList.size + middleList.size - 2) * mContentHeight + STBaseConstants.deviceInfoBean.screenHeight)
                LogUtil.e("HardHeight: $height")
                scrollview.scrollTo(0, height)
                collectClickEvent("as600_clk_complex_topic")
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSceneSuccess(event: EventBean) {
        if(EventType.AI_SCENE_SUCCESS == event.what) {
            mIsRefresh = true
            requestSceneList()
        }
    }

    /**
     * 获取item高度
     */
    private fun initItemHeight() {
        if (mTitleHeight == 0) {
            mSceneListAdapter?.getView(0)?.let {
                mTitleHeight = getViewHeight(it)
            }
        }
        if (mContentHeight == 0) {
            mSceneListAdapter?.getView(1)?.let {
                mContentHeight = getViewHeight(it)
            }
        }
    }

    fun getScrollY(){
        mScrollY = scrollview.scrollY
        LogUtil.e("mScrollY: $mScrollY")
    }

    private fun getViewHeight(v: View): Int {
        val w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        v.measure(w, h)
        return v.measuredHeight
    }

    override fun setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, null)
        StatusBarUtil.setLightMode(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}
