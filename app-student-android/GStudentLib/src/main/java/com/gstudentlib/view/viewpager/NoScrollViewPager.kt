package com.gstudentlib.view.viewpager

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * 作者：created by 逢二进一 on 2019/9/17 11:16
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
class NoScrollViewPager : ViewPager {

    private var isScrollable = false

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    fun setScrollable(scrollable: Boolean) {
        isScrollable = scrollable
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        try {
            return isScrollable && super.onTouchEvent(ev)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        try {
            return isScrollable && super.onInterceptTouchEvent(ev)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
        return false
    }

}