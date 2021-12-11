package gaosi.com.learn.studentapp.classlesson.myclass

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
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
import gaosi.com.learn.bean.classlesson.myclass.SubjectBean
import org.greenrobot.eventbus.EventBus

/**
 * description:
 * created by huangshan on 2020/6/15 上午10:29
 */
class SelectSubjectDialogFragment : BottomSheetDialogFragment() {

    private var mHandler: Handler? = null
    private var mSubjectList: ArrayList<SubjectBean>? = null

    companion object {
        private const val DATA = "data"
        fun newInstance(data: ArrayList<SubjectBean>): SelectSubjectDialogFragment =
                SelectSubjectDialogFragment().apply {
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
        rootView = View.inflate(context, R.layout.fragment_select_subject_dialog, null)

        val recyclerView = rootView?.findViewById<RecyclerView>(R.id.recycler_view)
        val gridLayoutManager = GridLayoutManager(context, 3)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.addItemDecoration(ClassStatusDialogFragment.SpaceItemDecoration())
        val subjectAdapter = SubjectAdapter()
        subjectAdapter.setOnItemClickListener { _, _, position ->
            mSubjectList?.forEachIndexed { index, subjectBean ->
                subjectBean.isClicked = index == position
            }
            subjectAdapter.notifyDataSetChanged()
            val appEventBean = EventBean()
            appEventBean.what = AppEventType.SELECT_SUBJECT
            appEventBean.obj = mSubjectList?.get(position)
            EventBus.getDefault().post(appEventBean)
            mHandler?.postDelayed({
                dismiss()
            }, 300)
            GSLogUtil.collectClickLog(GSConstants.P?.getCurrRefer(), "XSD_555", "")
        }
        recyclerView?.adapter = subjectAdapter
        recyclerView?.isNestedScrollingEnabled = false
        subjectAdapter.setNewData(mSubjectList)

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
        mSubjectList = arguments?.getSerializable(DATA) as ArrayList<SubjectBean>
    }

    override fun show(manager: FragmentManager?, tag: String?) {
        try {
            GSLogUtil.collectPageLog("XSD_498", GSConstants.P?.getPreviousRefer())
            GSConstants.P?.setCurrRefer("XSD_498")
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

    class SubjectAdapter : BaseQuickAdapter<SubjectBean, BaseViewHolder>(R.layout.item_select_subject) {

        override fun convert(helper: BaseViewHolder?, data: SubjectBean?) {
            helper?.run {
                val tvSubject = getView<TextView>(R.id.tvSubject)
                tvSubject.layoutParams.width = (STBaseConstants.deviceInfoBean.screenWidth - TypeValue.dp2px(40F) - TypeValue.dp2px(32F)) / 3
                tvSubject.text = data?.subjectName
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
