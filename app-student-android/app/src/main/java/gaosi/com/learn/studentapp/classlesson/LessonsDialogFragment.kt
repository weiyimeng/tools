package gaosi.com.learn.studentapp.classlesson

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.gsbaselib.utils.LOGGER
import com.gsbaselib.utils.LangUtil
import com.gsbiloglib.builder.GSConstants.Companion.P
import com.gsbiloglib.log.GSLogUtil
import com.gstudentlib.base.STBaseConstants
import gaosi.com.learn.R
import gaosi.com.learn.bean.classlesson.LessonBean
import gaosi.com.learn.bean.classlesson.LessonData

/**
 * 讲次列表
 * Created by huangshan on 2019/2/20.
 */
class LessonsDialogFragment : BottomSheetDialogFragment() {

    private var mLessonBeans: List<LessonBean>? = null
    private var mLessonData: LessonData? = null
    private var mLessionsAdapter: LessonsAdapter? = null

    companion object {
        private const val DATA = "data"
        fun newInstance(data: LessonData?): LessonsDialogFragment =
                LessonsDialogFragment().apply {
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
        this.initData()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        rootView = View.inflate(context, R.layout.fragment_lession_dialog, null)
        val recyclerView = rootView?.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        mLessionsAdapter = LessonsAdapter(this)
        recyclerView?.adapter = mLessionsAdapter
        recyclerView?.isNestedScrollingEnabled = false
        mLessonBeans?.let {
            mLessionsAdapter?.setNewData(it)
        }
        dialog?.setContentView(rootView)
        mBehavior = BottomSheetBehavior.from(rootView?.parent as View)
        mBehavior?.skipCollapsed = true
        mBehavior?.isHideable = true
        mBehavior?.setBottomSheetCallback(mBottomSheetBehaviorCallback)
        if (dialog != null) {
            val bottomSheet = dialog?.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            bottomSheet?.layoutParams?.height = STBaseConstants.deviceInfoBean.screenHeight * 4 / 5
        }
        rootView?.post { mBehavior?.setPeekHeight(rootView?.height ?: 0) }
        val tvClose = rootView?.findViewById<TextView>(R.id.tvClose)
        tvClose?.setOnClickListener {
            dismiss()
        }
        return dialog as BottomSheetDialog
    }

    private fun initData() {
        mLessonData = arguments?.getSerializable(DATA) as LessonData?
        if(LangUtil.isNotEmpty(mLessonData?.lessonList)) {
            mLessonBeans = mLessonData?.lessonList
        }
    }

    override fun show(manager: FragmentManager?, tag: String?) {
        try {
            GSLogUtil.collectPageLog("XSD_500", P?.getPreviousRefer())
            P?.setCurrRefer("XSD_500")
            super.show(manager, tag)
        } catch (e : Exception) {
            LOGGER.log(e)
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        GSLogUtil.collectPageLog("as103", P?.getPreviousRefer())
        P?.setCurrRefer("as103")
    }
}
