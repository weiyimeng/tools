package gaosi.com.learn.studentapp.classlesson.myclass

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gsbaselib.base.event.EventBean
import com.gsbaselib.utils.LOGGER
import com.gsbaselib.utils.TypeValue
import com.gsbiloglib.builder.GSConstants
import com.gsbiloglib.log.GSLogUtil
import com.gstudentlib.base.STBaseConstants
import gaosi.com.learn.R
import gaosi.com.learn.application.event.AppEventType
import org.greenrobot.eventbus.EventBus

/**
 * description:
 * created by huangshan on 2020/6/15 上午15:19
 */
class ClassStatusDialogFragment : BottomSheetDialogFragment() {

    private var mHandler: Handler? = null
    private var mClassStatusList: ArrayList<MyClassActivity.ClassStatus>? = null

    companion object {
        private const val DATA = "data"
        fun newInstance(data: ArrayList<MyClassActivity.ClassStatus>): ClassStatusDialogFragment =
                ClassStatusDialogFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(DATA, data)
                    }
                }
    }

    private var rootView: View? = null
    private var dialog: BottomSheetDialog? = null

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
        mHandler = Handler()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        rootView = View.inflate(context, R.layout.fragment_class_status_dialog, null)

        val recyclerView = rootView?.findViewById<RecyclerView>(R.id.recycler_view)
        val gridLayoutManager = GridLayoutManager(context, 3)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.addItemDecoration(SpaceItemDecoration())
        val classStatusAdapter = ClassStatusAdapter()
        classStatusAdapter.setOnItemClickListener { _, _, position ->
            mClassStatusList?.forEachIndexed { index, classStatus ->
                classStatus.isClicked = index == position
            }
            classStatusAdapter.notifyDataSetChanged()
            val appEventBean = EventBean()
            appEventBean.what = AppEventType.SELECT_CLASS_STATUS
            appEventBean.arg1 = mClassStatusList?.get(position)?.type ?: 0
            EventBus.getDefault().post(appEventBean)
            mHandler?.postDelayed({
                dismiss()
            }, 300)
            GSLogUtil.collectClickLog(GSConstants.P?.getCurrRefer(), "XSD_556", "")
        }
        recyclerView?.adapter = classStatusAdapter
        recyclerView?.isNestedScrollingEnabled = false
        classStatusAdapter.setNewData(mClassStatusList)

        dialog?.setContentView(rootView)
        mBehavior = BottomSheetBehavior.from(rootView?.parent as View)
        mBehavior?.skipCollapsed = true
        mBehavior?.isHideable = true
        mBehavior?.setBottomSheetCallback(mBottomSheetBehaviorCallback)
        if (dialog != null) {
            val bottomSheet = dialog?.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            bottomSheet?.layoutParams?.height = STBaseConstants.deviceInfoBean.screenHeight * 5 / 6
        }
        rootView?.post { mBehavior?.setPeekHeight(rootView?.height ?: 0) }
        val tvClose = rootView?.findViewById<TextView>(R.id.tvClose)
        tvClose?.setOnClickListener {
            dismiss()
        }
        return dialog as BottomSheetDialog
    }

    private fun initData() {
        mClassStatusList = arguments?.getSerializable(DATA) as ArrayList<MyClassActivity.ClassStatus>
    }

    override fun show(manager: FragmentManager?, tag: String?) {
        try {
            GSLogUtil.collectPageLog("XSD_499", GSConstants.P?.getPreviousRefer())
            GSConstants.P?.setCurrRefer("XSD_499")
            super.show(manager, tag)
        } catch (e: Exception) {
            LOGGER.log(e)
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        GSLogUtil.collectPageLog("as401", GSConstants.P?.getPreviousRefer())
        GSConstants.P?.setCurrRefer("as401")
    }

    class SpaceItemDecoration: RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            val position = parent.getChildAdapterPosition(view)
            when {
                position % 3 == 0 -> {
                    outRect.left = 0
                    outRect.right = TypeValue.dp2px(10f)
                }
                position % 3 == 1 -> {
                    outRect.left = TypeValue.dp2px(6f)
                    outRect.right =  TypeValue.dp2px(6f)
                }
                position % 3 == 2 -> {
                    outRect.left = TypeValue.dp2px(10f)
                    outRect.right =  0
                }
            }
        }
    }

    class ClassStatusAdapter : BaseQuickAdapter<MyClassActivity.ClassStatus, BaseViewHolder>(R.layout.item_select_subject) {

        override fun convert(helper: BaseViewHolder?, data: MyClassActivity.ClassStatus?) {
            helper?.run {
                val tvSubject = getView<TextView>(R.id.tvSubject)
                tvSubject.layoutParams.width = (STBaseConstants.deviceInfoBean.screenWidth - TypeValue.dp2px(40F) - TypeValue.dp2px(32F)) / 3
                when (data?.type) {
                    0 -> tvSubject.text = "全部"
                    2 -> tvSubject.text = "正在学"
                    3 -> tvSubject.text = "已结课"
                }
                if (data?.isClicked == true) {
                    tvSubject.setTextColor(Color.parseColor("#E6FFFFFF"))
                    tvSubject.setBackgroundResource(R.drawable.bg_todo_task_button)
                } else {
                    tvSubject.setTextColor(Color.parseColor("#E6474D6B"))
                    tvSubject.setBackgroundResource(R.drawable.bg_gray_shape_44)
                }
            }
        }
    }
}
