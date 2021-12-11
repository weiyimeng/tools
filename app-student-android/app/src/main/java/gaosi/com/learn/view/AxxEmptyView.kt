package gaosi.com.learn.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import gaosi.com.learn.R
import kotlinx.android.synthetic.main.ui_common_empty.view.*

/**
 * description:
 * created by huangshan on 2020/6/12 下午3:32
 */
class AxxEmptyView : LinearLayout {

    private var mOnClickCallBackListener: OnClickCallBackListener? = null

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    private fun initView() {
        View.inflate(context, R.layout.ui_common_empty, this)
        setEmptyVisibility(showImg = true, showText = true)
        button.setOnClickListener {
            mOnClickCallBackListener?.onClick()
        }
    }

    fun setEmptyIcon(resId: Int) {
        ivEmpty.setImageResource(resId)
    }

    fun setEmptyTitleText(text: String) {
        tvEmptyTitle.text = text
    }

    fun setEmptyText(text: String) {
        tvEmpty.text = text
    }

    fun setButtonText(text: String) {
        button.text = text
    }

    fun setEmptyVisibility(showImg: Boolean = true, showText: Boolean = true, showButton: Boolean = false, showEmptyTitle: Boolean = false) {
        if (showImg) {
            ivEmpty.visibility = View.VISIBLE
        } else {
            ivEmpty.visibility = View.GONE
        }
        if (showText) {
            tvEmpty.visibility = View.VISIBLE
        } else {
            tvEmpty.visibility = View.GONE
        }
        if (showButton) {
            button.visibility = View.VISIBLE
        } else {
            button.visibility = View.GONE
        }
        if (showEmptyTitle) {
            tvEmptyTitle.visibility = View.VISIBLE
        } else {
            tvEmptyTitle.visibility = View.GONE
        }
    }

    interface OnClickCallBackListener {
        fun onClick()
    }

    fun setClickCallBackListener(onClickCallBackListener: OnClickCallBackListener) {
        this.mOnClickCallBackListener = onClickCallBackListener
    }
}