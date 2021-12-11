package gaosi.com.learn.studentapp.goldmall

import android.os.Bundle
import android.view.View
import com.github.mzule.activityrouter.annotation.Router
import com.gsbaselib.utils.dialog.AbsAdapter
import com.gsbaselib.utils.dialog.DialogUtil
import com.gstudentlib.base.BaseActivity
import gaosi.com.learn.R
import kotlinx.android.synthetic.main.activity_gold_mall.*
import android.view.animation.LinearInterpolator
import android.animation.ObjectAnimator
import android.content.res.Resources
import android.graphics.*
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.animation.Animation
import android.widget.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.GSHttpResponse
import com.gsbaselib.net.callback.GSJsonCallback
import com.gsbaselib.utils.SharedPreferenceUtil
import com.gsbaselib.utils.ToastUtil
import com.gsbaselib.utils.TypeValue
import com.gsbaselib.utils.glide.ImageLoader
import com.gsbiloglib.log.GSLogUtil
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.util.rxcheck.IRxCheckStatus
import com.gstudentlib.util.rxcheck.IRxCheckStatusListener
import com.gstudentlib.util.rxcheck.RxCheckNetStatus
import com.gstudentlib.util.rxcheck.RxCheckStatusMaster
import com.gstudentlib.view.RoundCornerImageView
import com.lzy.okgo.model.Response
import gaosi.com.learn.application.AppApi
import gaosi.com.learn.bean.BuyCoinImageBean
import gaosi.com.learn.bean.CoinImage
import gaosi.com.learn.bean.GoldMallBean
import gaosi.com.learn.util.OnItemDoubleClickCheckListener
import gaosi.com.learn.view.AxxEmptyView
import java.util.HashMap

/**
 * Created by huangshan on 2019/12/17.
 */
@Router("GoldMall")
class GoldMallActivity : BaseActivity() {

    private var mRlCoinInfo: RelativeLayout? = null
    private var tvCoin: TextView? = null
    private var mTvHeadArea: TextView? = null
    private var mCheckBox: CheckBox? = null
    private var mCanClick = true
    private var mGoldMallListAdapter: GoldMallListAdapter? = null
    private var mGoldMallBean: GoldMallBean? = null
    //头像列表
    private var mCoinImageList: ArrayList<CoinImage>? = null
    //可使用头像列表
    private var mUsableCoinImageList: ArrayList<CoinImage> = ArrayList()
    private var  mEmptyView: AxxEmptyView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gold_mall)
        checkAndRequest()
        isShowExplainDialog()
    }

    override fun initView() {
        super.initView()
        title_bar.setLeftListener(this)
        title_bar.setRightIconListener(this)
        initAdapter()

    }

    /**
     * 空页面
     */
    private val getEmptyView: View?
        get() {
            mEmptyView = AxxEmptyView(this)
            val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(0, TypeValue.dp2px(100f), 0 ,0)
            params.gravity = Gravity.CENTER
            mEmptyView?.layoutParams = params
            mEmptyView?.setClickCallBackListener(object : AxxEmptyView.OnClickCallBackListener {
                override fun onClick() {
                    checkAndRequest()
                }
            })
            return mEmptyView
        }

    private fun setEmptyStatus(emptyType: Int) {
        mGoldMallListAdapter?.setNewData(null)
        mGoldMallListAdapter?.removeAllFooterView()
        mGoldMallListAdapter?.addFooterView(getEmptyView)
        when (emptyType) {
            1 -> {
                mEmptyView?.setEmptyIcon(R.drawable.icon_net_error)
                mEmptyView?.setEmptyText(getString(R.string.error_text_net))
                mEmptyView?.setButtonText(getString(R.string.click_refresh))
                mEmptyView?.setEmptyVisibility(showImg = true, showText = true, showButton = true)
                mTvHeadArea?.visibility = View.INVISIBLE
                mCheckBox?.visibility = View.INVISIBLE
            }
            2 -> {
                mEmptyView?.setEmptyVisibility(showImg = true, showText = true, showButton = false)
                mEmptyView?.setEmptyIcon(R.drawable.icon_avatar_list_empty)
                mEmptyView?.setEmptyText(getString(R.string.no_avatars_yet))
                mTvHeadArea?.visibility = View.VISIBLE
                mCheckBox?.visibility = View.VISIBLE
            }
        }
    }

    private fun initAdapter() {
        mGoldMallListAdapter = GoldMallListAdapter(1)
        mGoldMallListAdapter?.addHeaderView(headerView)
        val gridLayoutManager = GridLayoutManager(this, 3)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(SpaceItemDecoration())
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = mGoldMallListAdapter
        mGoldMallListAdapter?.onItemClickListener = object : OnItemDoubleClickCheckListener() {
            override fun onDoubleClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                adapter?.data ?: return
                if (!mCanClick) {
                    return
                }
                val dataList = adapter.data
                if ((dataList[position] as CoinImage).buy == 0) {
                    showBuyAvatarDialog((dataList[position] as CoinImage))
                } else {
                    //底部弹框
                    mUsableCoinImageList.forEach {
                        it.selectStatus = it == (dataList[position] as CoinImage)
                    }
                    UsableAvatarDialogFragment.newInstance((dataList[position] as CoinImage), mUsableCoinImageList).show(supportFragmentManager, "dialog")
                }
            }
        }
    }

    /**
     * headerView
     */
    private val headerView: View
        get() {
            val view = View.inflate(this, R.layout.header_gold_mall, null)
            tvCoin = view.findViewById(R.id.tvCoin)
            mRlCoinInfo = view.findViewById(R.id.rlCoinInfo)
            mTvHeadArea = view.findViewById(R.id.tvHeadArea)
            mCheckBox = view.findViewById(R.id.checkBox)
            mCheckBox?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if (mUsableCoinImageList.isNullOrEmpty()) {
                        //显示还未购买头像空页面
                        setEmptyStatus(2)
                    }
                    mGoldMallListAdapter?.setNewData(mUsableCoinImageList)
                    collectClickEvent("XSD_335")
                } else {
                    if (!mCoinImageList.isNullOrEmpty()) {
                        mGoldMallListAdapter?.removeAllFooterView()
                        mGoldMallListAdapter?.setNewData(mCoinImageList)
                    }
                }
            }
            return view
        }

    private fun checkAndRequest() {
        RxCheckStatusMaster
                .addCheckStatus(RxCheckNetStatus())
                .check(object : IRxCheckStatusListener {
                    override fun onCheckPass() {
                        requestGoldMallList()
                    }

                    override fun onCheckUnPass(iRxCheckStatus: IRxCheckStatus?) {
                        iRxCheckStatus?.let {
                            if (it is RxCheckNetStatus) {
                                setEmptyStatus(1)
                                ToastUtil.showToast("网络异常，请检查您的网络")
                            }
                        }
                    }
                })
    }

    private fun requestGoldMallList() {
        showLoadingProgressDialog()
        val params = HashMap<String, String>()
        params["studentId"] = STBaseConstants.userId
        GSRequest.startRequest(AppApi.goldMallList, GSRequest.GET, params, object : GSJsonCallback<GoldMallBean>() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<GoldMallBean>) {
                if (isFinishing || isDestroyed) {
                    return
                }
                dismissLoadingProgressDialog()
                if (showResponseErrorMessage(result) == 0) {
                    setEmptyStatus(1)
                    return
                }
                if (result.body == null) {
                    setEmptyStatus(1)
                    GSLogUtil.collectPerformanceLog(GSLogUtil.PerformanceType.HTTP_REQUEST_ERROR, mUrl, mResponse)
                    return
                }
                recyclerView.visibility = View.VISIBLE
                mGoldMallBean = result.body
                tvCoin?.text = result.body.coin.toString()
                mCoinImageList = result.body.coinImageLogList
                if (!mCoinImageList.isNullOrEmpty()) {
                    mTvHeadArea?.visibility = View.VISIBLE
                    mCheckBox?.visibility = View.VISIBLE
                    mGoldMallListAdapter?.setNewData(mCoinImageList)
                    mUsableCoinImageList.clear()
                    mCoinImageList?.forEach {
                        if (it.buy == 1 || it.buy == 2) {
                            mUsableCoinImageList.add(it)
                        }
                    }
                } else {
                    setEmptyStatus(1)
                }
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                if (isFinishing || isDestroyed) {
                    return
                }
                dismissLoadingProgressDialog()
                setEmptyStatus(1)
                message ?: return
                ToastUtil.showToast(message)
            }
        })
    }

    private fun isShowExplainDialog() {
        val isShowGoldMallExplainDialog = SharedPreferenceUtil
                .getBooleanValueFromSP("userInfo", STBaseConstants.userId + "_isShowGoldMallExplainDialog", false)
        if (!isShowGoldMallExplainDialog) {
            mHandler.postDelayed({
                showExplainDialog()
                SharedPreferenceUtil
                        .setBooleanDataIntoSP("userInfo", STBaseConstants.userId + "_isShowGoldMallExplainDialog", true)
            }, 500)
        }
    }

    /**
     * 显示说明弹框
     */
    private fun showExplainDialog() {
        DialogUtil.getInstance().create(this, R.layout.ui_gold_mall_explain_dialog)
                .show(object : AbsAdapter() {
                    override fun bindListener(onClickListener: View.OnClickListener) {
                        bindListener(onClickListener, R.id.tvKnow)
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
     * 购买弹框
     */
    private fun showBuyAvatarDialog(coinImage: CoinImage?) {
        DialogUtil.getInstance().create(this, R.layout.ui_buy_avatar_dialog)
                .show(object : AbsAdapter() {
                    override fun bindListener(onClickListener: View.OnClickListener) {
                        val ivAvatar = findViewById<RoundCornerImageView>(R.id.ivAvatar)
                        ImageLoader.setCircleImageViewResource(ivAvatar, coinImage?.doucmentUrl
                                ?: "", R.drawable.icon_default_placeholder)
                        val textStr = "当前账户余额 " + mGoldMallBean?.coin + " 金币,"
                        val spannableString = SpannableStringBuilder(textStr)
                        val startIndex = textStr.indexOf("额")
                        val endIndex = textStr.indexOf("金币")
                        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#FFC836")), startIndex + 1, endIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
                        bindText(R.id.tvRemainGold, spannableString)
                        bindText(R.id.tvPrice, coinImage?.costCoin.toString() + "/" + coinImage?.usageCycle + "天")
                        bindText(R.id.tvConfirm, coinImage?.costCoin.toString() + " 金币购买")
                        bindListener(onClickListener, R.id.tvConfirm)
                        bindListener(onClickListener, R.id.tvCancel)
                    }

                    override fun onClick(v: View, dialog: DialogUtil) {
                        super.onClick(v, dialog)
                        when (v.id) {
                            R.id.tvConfirm -> {
                                requestBuyAvatar(coinImage?.id, coinImage?.costCoin, coinImage?.doucmentUrl, coinImage?.documentName, coinImage?.usageCycle)
                                when (coinImage?.costCoin ?: 0) {
                                    150 -> collectClickEvent("XSD_337")
                                    500 -> collectClickEvent("XSD_338")
                                }
                            }
                            R.id.tvCancel -> dialog.dismiss()
                        }
                    }
                })
    }

    /**
     * 购买成功弹框
     */
    private fun showBuyAvatarSuccessDialog(doucmentUrl: String?, documentName: String?, usageCycle: Int?, endDate: String?) {
        var objectAnimator: ObjectAnimator? = null
        DialogUtil.getInstance().create(this, R.layout.ui_buy_avatar_success_dialog)
                .show(object : AbsAdapter() {
                    override fun bindListener(onClickListener: View.OnClickListener) {
                        val ivAvatar = findViewById<RoundCornerImageView>(R.id.ivAvatar)
                        ImageLoader.setCircleImageViewResource(ivAvatar, doucmentUrl
                                ?: "", R.drawable.icon_default_placeholder)
                        bindText(R.id.tvAvatarTitle, "恭喜获得" + documentName + " (" + usageCycle + "天)")
                        bindText(R.id.tvEndDate, endDate + "到期")
                        val ivBg = findViewById<ImageView>(R.id.ivBg)
                        objectAnimator = ObjectAnimator.ofFloat(ivBg, View.ROTATION, 0.0f, 360.0f)
                        objectAnimator?.duration = 4000
                        objectAnimator?.repeatCount = Animation.INFINITE
                        objectAnimator?.interpolator = LinearInterpolator()
                        objectAnimator?.start()
                        bindListener(onClickListener, R.id.btnConfirm)
                    }

                    override fun onClick(v: View, dialog: DialogUtil) {
                        super.onClick(v, dialog)
                        when (v.id) {
                            R.id.btnConfirm -> {
                                objectAnimator?.cancel()
                                dialog.dismiss()
                            }
                        }
                    }
                })
    }

    private fun requestBuyAvatar(id: Int?, cost: Int?, doucmentUrl: String?, documentName: String?, usageCycle: Int?) {
        RxCheckStatusMaster
                .addCheckStatus(RxCheckNetStatus())
                .check(object : IRxCheckStatusListener {
                    override fun onCheckPass() {
                        val params = HashMap<String, String>()
                        params["studentId"] = STBaseConstants.userId
                        params["docId"] = id.toString()
                        params["cost"] = cost.toString()
                        GSRequest.startRequest(AppApi.buyCoinAvatar, params, object : GSJsonCallback<BuyCoinImageBean>() {
                            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<BuyCoinImageBean>) {
                                if (isFinishing || isDestroyed) {
                                    return
                                }
                                if (showResponseErrorMessage(result) == 0) {
                                    return
                                }
                                result.body ?: return
                                DialogUtil.getInstance().dismiss()
                                tvCoin?.text = result.body.coin.toString()
                                //重新请求列表数据
                                requestGoldMallList()
                                mCanClick = false
                                mHandler.postDelayed({
                                    showBuyAvatarSuccessDialog(doucmentUrl, documentName, usageCycle, result.body.endDate)
                                    mCanClick = true
                                }, 500)
                            }

                            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                                message ?: return
                                ToastUtil.showToast(message)
                            }
                        })
                    }

                    override fun onCheckUnPass(iRxCheckStatus: IRxCheckStatus?) {
                        iRxCheckStatus?.let {
                            if (it is RxCheckNetStatus) {
                                ToastUtil.showToast("网络异常，请检查您的网络")

                            }
                        }
                    }
                })
    }

    /**
     * 选择头像后，更新数据
     */
    fun changeData(coinImage: CoinImage?) {
        mUsableCoinImageList.clear()
        mCoinImageList?.forEach {
            if (it == coinImage) {
                it.isCurrentDoc = 1
            } else {
                it.isCurrentDoc = 0
            }
            if (it.buy == 1 || it.buy == 2) {
                mUsableCoinImageList.add(it)
            }
        }
        mGoldMallListAdapter?.notifyDataSetChanged()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.llTitleLeft -> finish()
            R.id.ivTitleRight -> {
                showExplainDialog()
                collectClickEvent("XSD_334")
            }
        }
    }

    inner class SpaceItemDecoration : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            val position = parent.getChildAdapterPosition(view)
            when {
                position % 3 == 0 -> {
                    outRect.left = 0
                    outRect.right = TypeValue.dp2px(10f)
                }
                position % 3 == 1 -> {
                    outRect.left = TypeValue.dp2px(15f)
                    outRect.right = 0
                }
                position % 3 == 2 -> {
                    outRect.left = TypeValue.dp2px(5f)
                    outRect.right = TypeValue.dp2px(5f)
                }
            }
            if (position != 0) {
                outRect.bottom = TypeValue.dp2px(10f)
            } else {
                //第一个
                outRect.left = 0
                outRect.right = 0
                outRect.bottom = 0
            }
            //无可使用数据时
            if (mCoinImageList.isNullOrEmpty() || (mGoldMallListAdapter?.itemCount == 2 && mCheckBox?.isChecked == true && mUsableCoinImageList.size == 0)) {
                outRect.left = 0
                outRect.right = 0
                outRect.bottom = 0
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
}
