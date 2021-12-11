package com.gstudentlib.view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.gsbaselib.base.log.LogUtil
import com.gsbaselib.utils.error.ErrorView
import com.gstudentlib.R

/**
 * 针对学生端的异常流信息布局
 */
class ErrorViewStudent : ErrorView {

    private var mErrorText: String? = ""
    private var mErrorTextTop: String? = ""
    private var mErrorTextBootom: String? = ""
    private var tvErrorRefresh: TextView? = null
    private var tvRemindSingle: TextView? = null
    private var llRemind: LinearLayout? = null
    private var tvRemindTop: TextView? = null
    private var tvRemindBottom: TextView? = null
    private var ivIcon: ImageView? = null

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.init(attrs)
    }

    override fun init(attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs,
                R.styleable.ErrorView)
        mErrorText = a.getString(R.styleable.ErrorView_error_text_net)
        mErrorTextTop = a.getString(R.styleable.ErrorView_error_text_uncontent_top)
        mErrorTextBootom = a.getString(R.styleable.ErrorView_error_text_uncontent_bottom)
        a.recycle()
        this.removeAllViews()
        val mRootView = LayoutInflater.from(context).inflate(R.layout.ui_error_view, this) as LinearLayout
        tvErrorRefresh = mRootView.findViewById(R.id.tvErrorRefresh)
        tvRemindSingle = mRootView.findViewById(R.id.tvRemindSingle)
        llRemind = mRootView.findViewById(R.id.llRemind)
        tvRemindTop = mRootView.findViewById(R.id.tvRemindTop)
        tvRemindBottom = mRootView.findViewById(R.id.tvRemindBottom)
        ivIcon = mRootView.findViewById(R.id.ivIcon)
        tvErrorRefresh?.setOnClickListener { v ->
            mOnClickCallBackListener?.onClick(mError, v)
        }
    }

    override fun setNetError(): Boolean {
        this.visibility = View.VISIBLE
        this.ivIcon?.setImageResource(R.drawable.icon_errorview_net)
        this.mFlagView?.visibility = View.GONE
        this.llRemind?.visibility = View.GONE
        this.tvErrorRefresh?.visibility = View.VISIBLE
        this.tvRemindSingle?.visibility = View.VISIBLE
        if(!TextUtils.isEmpty(mErrorText)) {
            tvRemindSingle?.visibility = View.VISIBLE
            tvRemindSingle?.text = mErrorText
        }else{
            tvRemindSingle?.visibility = View.GONE
        }
        return false
    }

    override fun setNoContent(): Boolean {
        this.visibility = View.VISIBLE
        this.ivIcon?.setImageResource(R.drawable.icon_errorview_book)
        this.mFlagView?.visibility = View.GONE
        this.llRemind?.visibility = View.VISIBLE
        this.tvRemindSingle?.visibility = View.GONE
        this.tvErrorRefresh?.visibility = View.GONE

        if(!TextUtils.isEmpty(mErrorTextTop)) {
            tvRemindTop?.visibility = View.VISIBLE
            tvRemindTop?.text = mErrorTextTop
        }else {
            tvRemindTop?.visibility = View.GONE
        }
        if(!TextUtils.isEmpty(mErrorTextBootom)) {
            tvRemindBottom?.visibility = View.VISIBLE
            tvRemindBottom?.text = mErrorTextBootom
        }else{
            tvRemindBottom?.visibility = View.GONE
        }
        LogUtil.d("mErrorText$mErrorText ====  mErrorTextTop$mErrorTextTop  ====  mErrorTextBootom$mErrorTextBootom")
        return false
    }

}

