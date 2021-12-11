package gaosi.com.learn.studentapp.dresscity

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.view.View
import com.github.mzule.activityrouter.annotation.Router
import com.gsbaselib.base.GSBaseFragment
import com.gsbaselib.base.adapter.CommentVpAdapter
import com.gsbaselib.base.event.EventBean
import com.gsbaselib.base.log.LogUtil
import com.gsbaselib.utils.*
import com.gsbaselib.utils.dialog.AbsAdapter
import com.gsbaselib.utils.dialog.DialogUtil
import com.gsbiloglib.log.GSLogUtil
import com.gstudentlib.StatisticsDictionary
import com.gstudentlib.base.BaseActivity
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.event.EventType
import com.gstudentlib.util.SchemeDispatcher
import gaosi.com.learn.R
import kotlinx.android.synthetic.main.activity_dress_city.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject

/**
 * 裝扮城与装扮自己
 */
@Router("dressCity")
class DressCityActivity : BaseActivity(), ViewPager.OnPageChangeListener {

    private var mDressName: String? = ""
    private var mFromPage: String? = ""
    private var coinCount: String? = "0"
    private var position: String? = "0"
    private var mCurrPosition: Int = 0
    private var mFragmentList = ArrayList<GSBaseFragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dress_city)
        EventBus.getDefault().register(this)
    }

    override fun initView() {
        super.initView()
        val params = rlTitle.layoutParams
        params.height = TypeValue.dp2px(44f) + ViewUtil.getStatusBarHeight()
        rlTitle.layoutParams = params
        this.getIntentData()
        this.initData()
        ivTitleLeft.setOnClickListener(this)
        ivTitleRight.setOnClickListener(this)
        llDressSelf.setOnClickListener(this)
        llDressCenter.setOnClickListener(this)
    }

    private fun getIntentData() {
        intent.apply {
            coinCount = getStringExtra("coinCount") ?: "0"
            mDressName = getStringExtra("dressName") ?: ""
            mFromPage = getStringExtra("fromPage") ?: ""
            position = getStringExtra("position") ?: "0"
        }
    }

    private fun initData() {
        mFragmentList.add(DressCitySelfFragment.instance(coinCount ?: "0", mDressName
                ?: "", mFromPage ?: ""))
        mFragmentList.add(DressCityCenterFragment.instance(coinCount ?: "0", mDressName
                ?: "", mFromPage ?: ""))
        vpDress.offscreenPageLimit = mFragmentList.size
        vpDress.adapter = CommentVpAdapter(supportFragmentManager, mFragmentList)
        vpDress.addOnPageChangeListener(this)
        if (!TextUtils.isEmpty(position) && 2 > position?.toInt() ?: 0) {
            vpDress.currentItem = position?.toInt() ?: 0
        }
    }

    /**
     * 点击问好显示说明
     */
    private fun showExplain() {
        DressCityCenterFragment.levelForSplitVO ?: return
        DialogUtil.getInstance().create(this@DressCityActivity, R.layout.activity_dress_center_explain)
                .show(object : AbsAdapter() {
                    override fun bindListener(onClickListener: View.OnClickListener) {
                        var rule = "1. B级装扮：%s装扮碎片可合成 \n2. A级装扮：%s装扮碎片可合成 \n3. S级装扮：%s装扮碎片可合成 \n4. S限定装扮：%s装扮碎片可合成"
                        val b = DressCityCenterFragment.levelForSplitVO?.levelForSplitVO?.B ?: 0
                        val a = DressCityCenterFragment.levelForSplitVO?.levelForSplitVO?.A ?: 0
                        val s = DressCityCenterFragment.levelForSplitVO?.levelForSplitVO?.S ?: 0
                        val ss = DressCityCenterFragment.levelForSplitVO?.levelForSplitVO?.SS ?: 0
                        rule = String.format(rule, b, a, s, ss)
                        this.bindText(R.id.tvRule, rule)
                        this.bindListener(onClickListener, R.id.tvKnow)
                    }

                    override fun onClick(v: View, dialog: DialogUtil) {
                        super.onClick(v, dialog)
                        when (v.id) {
                            R.id.tvKnow -> dialog.dismiss()
                        }
                    }
                })
    }

    /**
     * 抽取装扮
     */
    fun goExtracting() {
        GSLogUtil.collectClickLog(pageId, "as_clk_mine_call_ufo", "")
        val json = JSONObject()
        json.put("userId", STBaseConstants.userId)
        json.put("pageName", "dressSelf")
        val s = "axx://rnPage?url=%s&param=%s"
        val url = String.format(s, "ExtractDressUp", json.toString())
        SchemeDispatcher.jumpPage(this, url)
        //首页判断是哪个页面过来的（空的话意味着）
        mFromPage?.let {
            if ("extracting" == it) {//如果来源是抽取装扮页面，此时关闭上个页面
                ActivityCollector.getInstance().getLastPageByNum(1)?.finish()
            }
        }
    }

    /**
     * 改变page
     */
    fun changePage(position: Int) {
        when {
            position > 1 -> vpDress.currentItem = 0
            position < 0 -> vpDress.currentItem = 0
            else -> vpDress.currentItem = position
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onChooseDressEvent(event: EventBean) {
        LogUtil.w("收到抽取装扮事件")
        if (EventType.CHOOSE_DRESS == event.what) {
            try {
                val json = event.obj as com.alibaba.fastjson.JSONObject
                mDressName = json.getString("dressName")
                coinCount = json.getString("coinCount")
                if (!TextUtils.isEmpty(mDressName)) {
                    getDressCitySelfFragment().refreshData(coinCount ?: "0", mDressName ?: "")
                    getDressCityCenterFragment().refreshData(coinCount ?: "0", mDressName ?: "")
                    changePage(0)
                }
            } catch (e: Exception) {
                LOGGER.log(e)
            }
        } else if (EventType.MERGE_DRESS == event.what) {
            getDressCitySelfFragment().initData()
            getDressCityCenterFragment().requestFaceUMall()
        }
    }

    /**
     * 更新免费次数
     */
    fun updateFreeNumber(firestFreeNumber: Int) {
        getDressCitySelfFragment().updateFreeNumber(firestFreeNumber)
        getDressCityCenterFragment().updateFreeNumber(firestFreeNumber)
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        this.mCurrPosition = position
        tvDressSelf.setTextColor(if (0 == position) Color.parseColor("#ffffff") else Color.parseColor("#aaffffff"))
        tvDressCenter.setTextColor(if (1 == position) Color.parseColor("#ffffff") else Color.parseColor("#aaffffff"))
        vDressSelf.visibility = if (0 == position) View.VISIBLE else View.INVISIBLE
        vDressCenter.visibility = if (1 == position) View.VISIBLE else View.INVISIBLE
    }

    fun getDressCitySelfFragment(): DressCitySelfFragment {
        return mFragmentList[0] as DressCitySelfFragment
    }

    fun getDressCityCenterFragment(): DressCityCenterFragment {
        return mFragmentList[1] as DressCityCenterFragment
    }

    override fun getPageId(): String {
        return if (0 == mCurrPosition) {
            StatisticsDictionary.dressSelf
        } else {
            StatisticsDictionary.dressCenter
        }
    }

    override fun onBackPressed() {
        getDressCitySelfFragment().onBackPressed(mCurrPosition)
    }

    override fun setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, null)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.ivTitleLeft -> onBackPressed()
            R.id.ivTitleRight -> showExplain()
            R.id.llDressSelf -> vpDress.currentItem = 0
            R.id.llDressCenter -> vpDress.currentItem = 1
            else -> {
            }
        }
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