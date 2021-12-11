package com.haoke91.a91edu.widget.dropdownlayout

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView

import com.haoke91.a91edu.R
import kotlinx.android.synthetic.main.layout_classify_course.view.*

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/17 上午11:18
 * 修改人：weiyimeng
 * 修改时间：2018/7/17 上午11:18
 * 修改备注：
 */
class ClassifyCourseView : FrameLayout {
    private val onClickListener = OnClickListener { v ->
        resetStatus()
        when (v.id) {
            R.id.tv_all_course -> {
                val drawable = context.resources.getDrawable(R.mipmap.course_icon_upward)
                drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
                tv_all_course.setCompoundDrawables(null, null, drawable, null)
                tv_all_course.setTextColor(Color.parseColor("#75c82b"))
                onClassifyChangeListener!!.onClassifyChange(0)
                
            }
            R.id.tv_all_garden -> {
                val drawable = context.resources.getDrawable(R.mipmap.course_icon_upward)
                drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
                tv_all_garden.setCompoundDrawables(null, null, drawable, null)
                tv_all_garden.setTextColor(Color.parseColor("#75c82b"))
                onClassifyChangeListener!!.onClassifyChange(1)
                
                
            }
            R.id.tv_all_classify -> {
                val drawable = context.resources.getDrawable(R.mipmap.course_icon_upward)
                drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
                tv_all_classify.setCompoundDrawables(null, null, drawable, null)
                tv_all_classify.setTextColor(Color.parseColor("#75c82b"))
                onClassifyChangeListener!!.onClassifyChange(2)
                
            }
        }
    }
    
    private var onClassifyChangeListener: ClassifyChangeListener? = null
    
    constructor(context: Context) : super(context)
    
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        View.inflate(context, R.layout.layout_classify_course, this)
        tv_all_course.setOnClickListener(onClickListener)
        tv_all_garden.setOnClickListener(onClickListener)
        tv_all_classify.setOnClickListener(onClickListener)
        
    }
    
    fun setOnClassifyChangeListener(ClassifyChangeListener: ClassifyCourseView.ClassifyChangeListener) {
        this.onClassifyChangeListener = ClassifyChangeListener
    }
    
    interface ClassifyChangeListener {
        fun onClassifyChange(position: Int)
    }
    
    fun setText(vararg tittle: String) {
        if (tittle.size == 2) {
            if (!TextUtils.isEmpty(tittle[0])) {
                tv_all_course.text = tittle[0]
            }
            if (!TextUtils.isEmpty(tittle[1])) {
                tv_all_garden.text = tittle[1]
            }
            fl_all_classify.visibility = View.GONE
        } else if (tittle.size == 3) {
            //            ((TextView) findViewById(R.id.tv_all_course)).setText(tittle[0]);
            //            ((TextView) findViewById(R.id.tv_all_garden)).setText(tittle[1]);
            if (!TextUtils.isEmpty(tittle[0])) {
                tv_all_course.text = tittle[0]
            }
            if (!TextUtils.isEmpty(tittle[1])) {
                tv_all_garden.text = tittle[1]
            }
            if (!TextUtils.isEmpty(tittle[2])) {
                tv_all_classify.text = tittle[2]
                
            }
        }
    }
    
    fun resetStatus() {
        val drawable = context.resources.getDrawable(R.mipmap.course_icon_down)
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        tv_all_course.setCompoundDrawables(null, null, drawable, null)
        tv_all_course.setTextColor(Color.parseColor("#363636"))
        
        tv_all_garden.setCompoundDrawables(null, null, drawable, null)
        tv_all_garden.setTextColor(Color.parseColor("#363636"))
        
        tv_all_classify.setCompoundDrawables(null, null, drawable, null)
        tv_all_classify.setTextColor(Color.parseColor("#363636"))
        
        
    }
}
