package gaosi.com.learn.studentapp.dresscity

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.gsbaselib.base.GSBaseApplication
import com.gsbaselib.base.GSBaseFragment
import com.gsbaselib.base.log.LogUtil
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.GSHttpResponse
import com.gsbaselib.net.callback.GSJsonCallback
import com.gsbaselib.net.callback.GSStringCallback
import com.gsbaselib.utils.ToastUtil
import com.gsbaselib.utils.customevent.OnNoDoubleClickListener
import com.gsbaselib.utils.dialog.AbsAdapter
import com.gsbaselib.utils.dialog.DialogUtil
import com.gsbaselib.utils.glide.ImageLoader
import com.gsbaselib.utils.net.NetworkUtil
import com.gsbiloglib.log.GSLogUtil
import com.gstudentlib.GSAPI
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.base.STBaseFragment
import com.lzy.okgo.model.Response
import gaosi.com.learn.R
import gaosi.com.learn.bean.dress.FaceUMallItem
import gaosi.com.learn.bean.dress.FaceUMallResponse
import gaosi.com.learn.bean.dress.RecycleDressBean
import gaosi.com.learn.bean.dress.Split
import gaosi.com.learn.studentapp.dresscity.adapter.FragmentAdapter
import gaosi.com.learn.studentapp.dresscity.fragment.DressCenterFragment
import kotlinx.android.synthetic.main.activity_dress_city_center.*
import org.json.JSONObject
import java.util.*

/**
 * 装扮城页面
 */
class DressCityCenterFragment : STBaseFragment(), TabLayout.OnTabSelectedListener {

    private var mFaceUMallResponse: FaceUMallResponse? = null
    private var mCommentVpAdapter: FragmentAdapter? = null
    private var mFragments: ArrayList<GSBaseFragment>? = ArrayList()
    private var coinCount: String? = "0"
    private var mDressName: String? = ""
    private var mFromPage: String? = ""
    private var isFirstLoad: Boolean = true
    private var mCurrPosition: Int = 0

    companion object {
        private const val COIN_COUNT = "coinCount"
        private const val DRESS_NAME = "dressName"
        private const val FROM_PAGE = "fromPage"
        var levelForSplitVO: Split? = null
        fun instance(coinCount: String, mDressName: String, mFromPage: String): DressCityCenterFragment {
            return DressCityCenterFragment().apply {
                arguments = Bundle().apply {
                    putString(COIN_COUNT, coinCount)
                    putString(DRESS_NAME, mDressName)
                    putString(FROM_PAGE, mFromPage)
                }
            }
        }
    }

    override fun initView(inflater: LayoutInflater?, container: ViewGroup?): View {
        return inflater?.inflate(R.layout.activity_dress_city_center, container, false) as View
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coinCount = arguments?.getString(COIN_COUNT)
        mDressName = arguments?.getString(DRESS_NAME)
        mFromPage = arguments?.getString(FROM_PAGE)
        LogUtil.d("DressCityCenterFragment:$coinCount  $mDressName  $mFromPage")
        tvExtractionNewDress.setOnClickListener(object : OnNoDoubleClickListener() {
            override fun onNoDoubleClick(p0: View?) {
                (activity as DressCityActivity).goExtracting()
            }
        })
        tvRecycleDress.setOnClickListener(object : OnNoDoubleClickListener() {
            override fun onNoDoubleClick(p0: View?) {
                requestRecycleDress()
                collectClickEvent("as1004_clk_recycle")
            }
        })
        this.requestFaceUMall()
    }

    /**
     * 加载装扮mall
     */
    private fun loadFaceUMall() {
        this.reset()
        mFaceUMallResponse?.faceUMyItemCategory?.let {
            for (i in 0 until it.size) {
                val dressCenterFragment = DressCenterFragment.instance()
                dressCenterFragment.setCurrPosition(i)
                mFragments?.add(dressCenterFragment)
                tlDress.addTab(tlDress.newTab())
            }
            mCommentVpAdapter = mFragments?.let { it1 -> FragmentAdapter(childFragmentManager, it1) }
            vpDress.adapter = mCommentVpAdapter
            vpDress.offscreenPageLimit = mFragments?.size ?: 0
            tlDress.setupWithViewPager(vpDress)
            //设置自定义视图
            for (i in 0 until tlDress.tabCount) {
                val tab = tlDress.getTabAt(i)
                tab?.customView = this.createView(i)
            }
            tlDress.addOnTabSelectedListener(this)
            adapterPosition()
            if (mCurrPosition < tlDress.tabCount) {
                vpDress.currentItem = mCurrPosition
                tlDress.getTabAt(mCurrPosition)?.select()
            } else {
                this.mCurrPosition = 0
            }
        }
    }

    /**
     * 创建tab view
     * @param position
     * @return
     */
    private fun createView(position: Int): View {
        val faceUMallItem = mFaceUMallResponse?.faceUMyItemCategory?.get(position)
        val container = LayoutInflater.from(activity).inflate(R.layout.activity_dress_center_fm_tab_item, null)
        val tvTitle = container.findViewById<TextView>(R.id.tvTitle)
        val iviIcon = container.findViewById<ImageView>(R.id.iviIcon)
        container.tag = iviIcon
        faceUMallItem?.let {
            tvTitle.text = it.myItemCount.toString() + "/" + it.itemCount.toString()
            ImageLoader.setNetImageResource(iviIcon, if (position == 0) it.iconLightUrl else it.iconUrl)
        }
        tvTitle.setTextColor(tlDress?.tabTextColors)
        return container
    }

    /**
     * 重置
     */
    private fun reset() {
        mFragments?.clear()
        tlDress.removeAllTabs()
        tlDress.clearOnTabSelectedListeners()
        vLine.visibility = View.VISIBLE
    }

    /**
     * 请求道具
     */
    fun requestFaceUMall() {
        vLine.visibility = View.INVISIBLE
        val params = HashMap<String, String>()
        params["studentId"] = STBaseConstants.userId
        GSRequest.startRequest(GSAPI.faceUmallInfo, params, object : GSJsonCallback<FaceUMallResponse>() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<FaceUMallResponse>) {
                if (activity == null || activity?.isFinishing == true) {
                    return
                }
                isFirstLoad = false
                if (result.body == null) {
                    GSLogUtil.collectPerformanceLog(GSLogUtil.PerformanceType.HTTP_REQUEST_ERROR, mUrl, mResponse)
                    return
                }
                mFaceUMallResponse = result.body
                levelForSplitVO = mFaceUMallResponse?.levelForSplitVO
                loadFaceUMall()
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                if (activity == null || activity?.isFinishing == true) {
                    return
                }
                isFirstLoad = false
                ToastUtil.showToast("网络未连接")
            }
        })
    }

    /**
     * 分析抽出的
     */
    private fun adapterPosition() {
        mFaceUMallResponse?.faceUMyItemCategory?.let {
            for (i in 0 until it.size) {
                if (TextUtils.isEmpty(mDressName)) {
                    break
                }
                if (it[i].assistCode == mDressName) {
                    mCurrPosition = i
                    break
                }
            }
            mDressName = ""
        }
    }

    /**
     * 刷新数据
     */
    fun refreshData(coinCount: String, mDressName: String) {
        if (!isFirstLoad) {
            this.mDressName = mDressName
            this.coinCount = coinCount
            this.requestFaceUMall()
        }
    }

    /**
     * 获取当前对象数据
     * @param position
     * @return
     */
    fun getData(position: Int): FaceUMallItem? {
        mFaceUMallResponse?.faceUMyItemCategory?.let {
            return if (position < it.size) {
                it[position]
            } else null
        }
        return null
    }

    fun updateFreeNumber(num: Int) {
        if (num > 0) {
            tvFreeNum.visibility = View.VISIBLE
            tvFreeNum.text = "免费$num" + "次"
        } else {
            tvFreeNum.visibility = View.GONE
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        tab.customView?.tag ?: return
        mCurrPosition = tab.position
        val faceUMallItem = mFaceUMallResponse?.faceUMyItemCategory?.get(tab.position)
        val iviIcon = tab.customView?.tag as ImageView
        ImageLoader.setImageViewResource(iviIcon, faceUMallItem?.iconLightUrl ?: "")
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
        tab.customView?.tag ?: return
        val faceUMallItem = mFaceUMallResponse?.faceUMyItemCategory?.get(tab.position)
        val iviIcon = tab.customView?.tag as ImageView
        ImageLoader.setNetImageResource(iviIcon, faceUMallItem?.iconUrl ?: "")
    }

    override fun onTabReselected(tab: TabLayout.Tab) {}

    /**
     * 回收装扮接口
     */
    private fun requestRecycleDress() {
        if (NetworkUtil.isConnected(GSBaseApplication.getApplication())) {
            val paramMap = HashMap<String, String>()
            paramMap["studentId"] = STBaseConstants.userId
            GSRequest.startRequest(GSAPI.recycleDress, paramMap, object : GSJsonCallback<RecycleDressBean>() {
                override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<RecycleDressBean>) {
                    if (activity?.isFinishing == true || activity?.isDestroyed == true) {
                        return
                    }
                    //收集异常日志
                    if (result.body == null) {
                        GSLogUtil.collectPerformanceLog(GSLogUtil.PerformanceType.HTTP_REQUEST_ERROR, mUrl, mResponse)
                        return
                    }
                    showRecycleDialog(result.body)
                }

                override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                    message ?: return
                    ToastUtil.showToast(message)
                }
            })
        } else {
            ToastUtil.showToast("网络错误")
        }
    }

    /**
     * 回收装扮确认接口
     */
    private fun requestRecycleDressConfirm() {
        if (NetworkUtil.isConnected(GSBaseApplication.getApplication())) {
            val paramMap = HashMap<String, String>()
            paramMap["studentId"] = STBaseConstants.userId
            GSRequest.startRequest(GSAPI.recycleDressConfirm, paramMap, object : GSStringCallback() {
                override fun onResponseSuccess(response: Response<*>?, code: Int, result: String) {
                    if (activity?.isFinishing == true || activity?.isDestroyed == true) {
                        return
                    }
                    val json = JSONObject(result)
                    val status = json.optInt("status")
                    if (status == 1) {
                        DialogUtil.getInstance().dismiss()
                        ToastUtil.showToast("回收成功")
                        requestFaceUMall()
                    } else {
                        ToastUtil.showToast("网络错误")
                    }
                }

                override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                    message ?: return
                    ToastUtil.showToast(message)
                }
            })
        } else {
            ToastUtil.showToast("网络错误")
        }
    }

    private fun showRecycleDialog(data: RecycleDressBean?) {
        if (data?.recoverDressSplit == "0") {
            DialogUtil.getInstance().create(context, R.layout.ui_recycle_dress_unable)
                    .show(object : AbsAdapter() {
                        override fun bindListener(onClickListener: View.OnClickListener?) {
                            findViewById<TextView>(R.id.tvNeedFragment).visibility = View.GONE
                            bindText(R.id.tvMyFragment, "我的装扮碎片：${data?.myDressSplit}")
                            bindListener(onClickListener, R.id.tvKnow)
                        }

                        override fun onClick(v: View?, dialog: DialogUtil?) {
                            super.onClick(v, dialog)
                            when (v?.id) {
                                R.id.tvKnow -> dialog?.dismiss()
                            }
                        }
                    })
        } else {
            DialogUtil.getInstance().create(context, R.layout.ui_recycle_dress)
                    .show(object : AbsAdapter() {
                        override fun bindListener(onClickListener: View.OnClickListener?) {
                            bindText(R.id.tvMyFragment, "我的装扮碎片：${data?.myDressSplit}")
                            bindText(R.id.tvNeedFragment, "回收可得装扮碎片：${data?.recoverDressSplit}")
                            bindListener(onClickListener, R.id.tvCancel)
                            bindListener(onClickListener, R.id.tvConfirm)
                        }

                        override fun onClick(v: View?, dialog: DialogUtil?) {
                            super.onClick(v, dialog)
                            when (v?.id) {
                                R.id.tvConfirm -> {
                                    requestRecycleDressConfirm()
                                    collectClickEvent("as1004_recycle_clk_sure")
                                }
                                R.id.tvCancel -> dialog?.dismiss()
                            }
                        }
                    })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mFragments?.clear()
        this.mFragments = null
        this.mFaceUMallResponse = null
        LogUtil.w("页面销毁")
    }
}