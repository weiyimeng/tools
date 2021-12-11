package com.gstudentlib.view

import android.content.Context
import android.graphics.drawable.Drawable
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
class JoinClassViewStudent : LinearLayout {

    private var mOnClickCallBackListener: OnClickCallBackListener? = null
    private var mError = ERROR.NORMAL
    private var mFlagView: View? = null

    private var mErrorText: String? = ""
    private var mErrorTextTop1: String? = ""
    private var mErrorTextBootom1: String? = ""
    private var mErrorTextTop2: String? = ""
    private var mErrorTextBootom2: String? = ""

    private var ivIcon: ImageView? = null //图片
    private var tvErrorRefresh: TextView? = null//点击刷新
    private var tvRemindSingle: TextView? = null //网络加载失败

    private var llRemind: LinearLayout? = null//提醒1 线性布局
    private var tvRemindTop: TextView? = null//提醒1 top
    private var tvRemindBottom: TextView? = null//提醒1 bottom

    private var llJoinClass: LinearLayout? = null//提醒2 线性布局
    private var tvJoinClassTop: TextView? = null//提醒2 top
    private var tvJoinClassBottom: TextView? = null//提醒2 bottom

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs,
                R.styleable.JoinClassErrorView)
        mErrorText = a.getString(R.styleable.JoinClassErrorView_stu_error_text_net)
        mErrorTextTop1 = a.getString(R.styleable.JoinClassErrorView_stu_error_text_uncontent_top1)
        mErrorTextBootom1 = a.getString(R.styleable.JoinClassErrorView_stu_error_text_uncontent_bottom1)
        mErrorTextTop2 = a.getString(R.styleable.JoinClassErrorView_stu_error_text_uncontent_top2)
        mErrorTextBootom2 = a.getString(R.styleable.JoinClassErrorView_stu_error_text_uncontent_bottom2)
        a.recycle()

        this.removeAllViews()
        val mRootView = LayoutInflater.from(context).inflate(R.layout.ui_error_view, this) as LinearLayout

        tvErrorRefresh = mRootView.findViewById(R.id.tvErrorRefresh)
        tvRemindSingle = mRootView.findViewById(R.id.tvRemindSingle)

        llRemind = mRootView.findViewById(R.id.llRemind)
        tvRemindTop = mRootView.findViewById(R.id.tvRemindTop)
        tvRemindBottom = mRootView.findViewById(R.id.tvRemindBottom)

        llJoinClass = mRootView.findViewById(R.id.llJoinClass)
        tvJoinClassTop = mRootView.findViewById(R.id.tvJoinClassTop)
        tvJoinClassBottom = mRootView.findViewById(R.id.tvJoinClassBottom)
        ivIcon = mRootView.findViewById(R.id.ivIcon)
        tvErrorRefresh?.setOnClickListener { v ->
            mOnClickCallBackListener?.onClick(mError, v)
        }
    }

    /**
     * 注冊与其显示相反的view
     * @param mFlagView
     */
    fun register(mFlagView: View) {
        this.mFlagView = mFlagView
        this.updateStatus(mError)
    }

    /**
     * 更新异常流状态
     * @param error
     */
    fun updateStatus(error: ERROR) {
        if (mFlagView == null) {
            throw RuntimeException("unregister mFlagView, please register!")
        }
        this.mError = error
        when (error) {
            ERROR.NORMAL -> {
                this.visibility = View.GONE
                this.mFlagView?.visibility = View.VISIBLE
            }
            ERROR.NET -> {
                if (setNetError()) return
            }
            ERROR.UNCONTENT -> {
                if (setNoContent()) return
            }
            ERROR.JOINCLASS -> {
                if (setJoinContent()) return
            }
            ERROR.NULL -> {
                this.visibility = View.GONE
                this.mFlagView?.visibility = View.GONE
            }
        }
        this.requestLayout()
    }

    /**
     * 设置事件监听
     * @param onClickCallBackListener
     */
    open fun setClickCallBackListener(onClickCallBackListener: OnClickCallBackListener) {
        this.mOnClickCallBackListener = onClickCallBackListener
    }

    /**
     * 异常流状态
     * 可以根据业务场景进行追加
     */
    enum class ERROR {
        NORMAL, NET, UNCONTENT , JOINCLASS , NULL
    }

    /**
     * 事件监听
     */
    interface OnClickCallBackListener {
        fun onClick(status: ERROR, v: View)
    }

    /**
     * 网络异常
     */
    open fun netError() {
        this.updateStatus(ERROR.NET)
    }

    /**
     * 正常
     */
    open fun normalError() {
        this.updateStatus(ERROR.NORMAL)
    }

    /**
     * 没有数据
     */
    open fun unContentError() {
        this.updateStatus(ERROR.UNCONTENT)
    }

    /**
     * 全部不显示
     */
    open fun nullError() {
        this.updateStatus(ERROR.NULL)
    }

    /**
     * 没有班级
     */
    open fun unClassError() {
        this.updateStatus(ERROR.JOINCLASS)
    }

    fun setNetError(): Boolean {
        this.visibility = View.VISIBLE
        this.ivIcon?.visibility = View.VISIBLE
        this.ivIcon?.setImageResource(R.drawable.icon_errorview_net)
        this.mFlagView?.visibility = View.GONE
        this.llRemind?.visibility = View.GONE
        this.tvErrorRefresh?.visibility = View.VISIBLE
        this.tvRemindSingle?.visibility = View.VISIBLE
        this.tvErrorRefresh?.text = "点击刷新"
        if(!TextUtils.isEmpty(mErrorText)) {
            tvRemindSingle?.visibility = View.VISIBLE
            tvRemindSingle?.text = mErrorText
        }else{
            tvRemindSingle?.visibility = View.GONE
        }
        return false
    }

    fun setJoinContent(): Boolean {
        this.visibility = View.VISIBLE
        this.ivIcon?.visibility = View.GONE
        this.mFlagView?.visibility = View.GONE
        this.llRemind?.visibility = View.GONE
        this.tvRemindSingle?.visibility = View.GONE

        this.llJoinClass?.visibility = View.VISIBLE
        this.tvErrorRefresh?.visibility = View.VISIBLE
        this.tvErrorRefresh?.text = "加入班级"
        if(!TextUtils.isEmpty(mErrorTextTop2)) {
            tvJoinClassTop?.visibility = View.VISIBLE
            tvJoinClassTop?.text = mErrorTextTop2
        }else {
            tvJoinClassTop?.visibility = View.GONE
        }
        if(!TextUtils.isEmpty(mErrorTextBootom2)) {
            tvJoinClassBottom?.visibility = View.VISIBLE
            tvJoinClassBottom?.text = mErrorTextBootom2
        }else{
            tvJoinClassBottom?.visibility = View.GONE
        }
        return false
    }

    fun setNoContent(): Boolean {
        this.visibility = View.VISIBLE
        this.mFlagView?.visibility = View.GONE
        this.tvRemindSingle?.visibility = View.GONE
        this.tvErrorRefresh?.visibility = View.GONE
        this.llJoinClass?.visibility = View.GONE

        this.ivIcon?.visibility = View.VISIBLE
        this.llRemind?.visibility = View.VISIBLE

        if(!TextUtils.isEmpty(mErrorTextTop1)) {
            tvRemindTop?.visibility = View.VISIBLE
            tvRemindTop?.text = mErrorTextTop1
        }else {
            tvRemindTop?.visibility = View.GONE
        }
        if(!TextUtils.isEmpty(mErrorTextBootom1)) {
            tvRemindBottom?.visibility = View.VISIBLE
            tvRemindBottom?.text = mErrorTextBootom1
        }else{
            tvRemindBottom?.visibility = View.GONE
        }
        return false
    }
}

