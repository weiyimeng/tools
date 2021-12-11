package gaosi.com.learn.studentapp.goldmall

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Rect
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.GSStringCallback
import com.gsbaselib.utils.LOGGER
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
import gaosi.com.learn.R
import gaosi.com.learn.application.AppApi
import gaosi.com.learn.bean.CoinImage
import org.json.JSONObject
import java.util.HashMap

/**
 * Created by huangshan on 2019/12/18.
 */
class UsableAvatarDialogFragment : BottomSheetDialogFragment() {

    private lateinit var rootView: View
    private var dialog: BottomSheetDialog? = null
    private var mGoldMallListAdapter: GoldMallListAdapter? = null
    private var mAvatarUrl: String = ""
    private var mUsableCoinImageList: ArrayList<CoinImage>? = null
    //当前选择的头像
    private var mCoinImage: CoinImage? = null

    companion object {
        private const val CURRENT = "current"
        private const val DATA = "data"
        fun newInstance(current: CoinImage?, data: ArrayList<CoinImage>): UsableAvatarDialogFragment =
                UsableAvatarDialogFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(CURRENT, current)
                        putSerializable(DATA, data)
                    }
                }
    }

    private var mBehavior: BottomSheetBehavior<*>? = null
    private val mBottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            //禁止拖拽，
            if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                //设置为收缩状态
                mBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
        initData()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        rootView = View.inflate(context, R.layout.fragment_usable_avatar_dialog, null)
        val ivAvatar = rootView.findViewById<RoundCornerImageView>(R.id.ivAvatar)
        ImageLoader.setImageViewResource(ivAvatar, mAvatarUrl, R.drawable.icon_default_placeholder)
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerView)
        mGoldMallListAdapter = GoldMallListAdapter(2)
        val gridLayoutManager = GridLayoutManager(context, 3)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.addItemDecoration(SpaceItemDecoration())
        recyclerView.adapter = mGoldMallListAdapter
        recyclerView.isNestedScrollingEnabled = false
        mGoldMallListAdapter?.setNewData(mUsableCoinImageList)
        mGoldMallListAdapter?.setOnItemClickListener { _, _, position ->
            mCoinImage = mUsableCoinImageList?.get(position)
            mUsableCoinImageList?.forEach {
                it.selectStatus = it == mUsableCoinImageList?.get(position)
            }
            ImageLoader.setCircleImageViewResource(ivAvatar, mCoinImage?.doucmentUrl
                    ?: "", R.drawable.icon_default_placeholder)
            mGoldMallListAdapter?.notifyDataSetChanged()
        }
        dialog?.setContentView(rootView)
        dialog?.setOnKeyListener(object : DialogInterface.OnKeyListener {
            override fun onKey(dialogInterface: DialogInterface?, keyCode: Int, keyEvent: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true
                }
                return false
            }
        })
        mBehavior = BottomSheetBehavior.from(rootView.parent as View)
        mBehavior?.skipCollapsed = true
        mBehavior?.isHideable = true
        mBehavior?.setBottomSheetCallback(mBottomSheetBehaviorCallback)
        if (dialog != null) {
            val bottomSheet = dialog?.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            bottomSheet?.layoutParams?.height = STBaseConstants.deviceInfoBean.screenHeight * 2 / 3
        }
        rootView.post { mBehavior?.setPeekHeight(rootView.height) }
        val tvCancel = rootView.findViewById<TextView>(R.id.tvCancel)
        tvCancel?.setOnClickListener {
            dismiss()
        }
        val tvUse = rootView.findViewById<TextView>(R.id.tvUse)
        tvUse?.setOnClickListener {
            requestSetAvatar()
            GSLogUtil.collectClickLog("XSD_333", "XSD_336", "")
        }
        return dialog as BottomSheetDialog
    }

    private fun initData() {
        mCoinImage = arguments?.getSerializable(CURRENT) as CoinImage
        mAvatarUrl = mCoinImage?.doucmentUrl ?: ""
        mUsableCoinImageList = arguments?.getSerializable(DATA) as ArrayList<CoinImage>
    }

    private fun requestSetAvatar() {
        RxCheckStatusMaster
                .addCheckStatus(RxCheckNetStatus())
                .check(object : IRxCheckStatusListener {
                    override fun onCheckPass() {
                        val params = HashMap<String, String>()
                        params["studentId"] = STBaseConstants.userId
                        params["docId"] = mCoinImage?.id.toString()
                        GSRequest.startRequest(AppApi.setCoinAvatar, params, object : GSStringCallback() {
                            override fun onResponseSuccess(response: Response<*>?, code: Int, result: String) {
                                val json = JSONObject(result)
                                val status = json.optInt("status")
                                val message = json.optString("message")
                                if (status == 1) {
                                    dismiss()
                                    //更新金币商城列表数据
                                    ToastUtil.showToast("设置成功")
                                    (activity as GoldMallActivity).changeData(mCoinImage)
                                } else {
                                    ToastUtil.showToast(message)
                                }
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

    inner class SpaceItemDecoration : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            val position = parent.getChildAdapterPosition(view)
            when {
                position % 3 == 0 -> {
                    outRect.left = TypeValue.dp2px(15f)
                    outRect.right = 0
                }
                position % 3 == 1 -> {
                    outRect.left = TypeValue.dp2px(5f)
                    outRect.right = TypeValue.dp2px(5f)
                }
                position % 3 == 2 -> {
                    outRect.left = 0
                    outRect.right = TypeValue.dp2px(10f)
                }
            }
            outRect.bottom = TypeValue.dp2px(10f)
        }
    }

    override fun show(manager: FragmentManager?, tag: String?) {
        try {
            super.show(manager, tag)
        } catch (e: Exception) {
            LOGGER.log(e)
        }
    }

}
