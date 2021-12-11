package gaosi.com.learn.view

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

import java.lang.ref.WeakReference

/**
 * 自动滚动viewpager
 * Created by huangshan on 2018/10/08.
 */

class AutoViewPager : ViewPager {

    private var mSwitchTask: SwitchTask? = null
    private var mTurning: Boolean = false
    private var mAutoTurningTime: Long = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        mSwitchTask = SwitchTask(this)
    }

    override fun setCurrentItem(item: Int) {
        if (currentItem != item) {
            setCurrentItem(item, true)
        }
    }

    fun start(autoTurningTime: Long) {
        if (mTurning) {
            return
        }
        this.mAutoTurningTime = autoTurningTime
        mTurning = true
        postDelayed(mSwitchTask, autoTurningTime)
    }

    fun stop() {
        mTurning = false
        removeCallbacks(mSwitchTask)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        try {
            if (currentItem == 0 && childCount == 0) {
                return false
            }
            return super.onTouchEvent(ev)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        try {
            if (currentItem == 0 && childCount == 0) {
                return false
            }
            return super.onInterceptTouchEvent(ev)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
        return false
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        try {
            val action = ev?.action
            if (currentItem == 0 && childCount == 0) {
                return false
            }
            if (mAutoTurningTime != 0L) {
                if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                        || action == MotionEvent.ACTION_OUTSIDE) {
                    start(mAutoTurningTime)
                } else if (action == MotionEvent.ACTION_DOWN) {
                    stop()
                }
            }
            return super.dispatchTouchEvent(ev)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
        return false
    }

    private inner class SwitchTask internal constructor(autoViewPager: AutoViewPager) : Runnable {

        private val mReference: WeakReference<AutoViewPager> = WeakReference(autoViewPager)

        override fun run() {
            val mAutoViewPager = mReference.get()
            if (mAutoViewPager != null && mAutoViewPager.mTurning) {
                val page = mAutoViewPager.currentItem
                mAutoViewPager.adapter?.let {
                    if (it.count == 0) {
                        return
                    }
                    if (page == it.count - 1) {
                        mAutoViewPager.currentItem = 0
                    } else {
                        mAutoViewPager.currentItem = page + 1
                    }
                    mAutoViewPager.postDelayed(mAutoViewPager.mSwitchTask, mAutoViewPager.mAutoTurningTime)
                }
            }
        }
    }
}
