package gaosi.com.learn.studentapp.main.guide


import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatDialog
import android.support.v7.app.AppCompatDialogFragment
import android.view.*

import gaosi.com.learn.R
import kotlinx.android.synthetic.main.fragment_guide.*


/**
 * 信息架构改版 引导
 */
class GuideFragment : AppCompatDialogFragment() {

    private var dialog: AppCompatDialog? = null
    private var mList = ArrayList<Int>()
    private var imageAdapter: ImagePagerAdapter? = null

    companion object {
        private const val DATA = "data"
        fun newInstance(data: ArrayList<Int>): GuideFragment =
                GuideFragment().apply {
                    arguments = Bundle().apply {
                        putIntegerArrayList(DATA, data)
                    }
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogFullScreen)
        mList = arguments?.getIntegerArrayList(DATA) as ArrayList<Int>
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as AppCompatDialog
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setOnKeyListener(DialogInterface.OnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return@OnKeyListener true
            }
            false
        })
        imageAdapter = ImagePagerAdapter(mList)
        imageAdapter?.setOnItemClickListener(object : ImagePagerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                if (position == mList.size - 1) {
                    dismiss()
                } else {
                    view_pager.setCurrentItem(position + 1, false)
                }
            }
        })

        return dialog as AppCompatDialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_guide, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_pager.adapter = imageAdapter
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        if (activity is DialogInterface.OnDismissListener) {
            (activity as DialogInterface.OnDismissListener).onDismiss(dialog)
        }
    }
}
