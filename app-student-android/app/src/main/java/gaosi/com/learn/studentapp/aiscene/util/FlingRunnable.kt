package gaosi.com.learn.studentapp.aiscene.util

import android.content.Context
import android.widget.Scroller
import com.gsbaselib.utils.ViewUtil.post


/**
 * 作者：created by 逢二进一 on 2019/8/22 14:18
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
class FlingRunnable: Runnable {

    private var mInitX: Int = 0
    private var mMinX: Int = 0
    private var mMaxX: Int = 0
    private var mVelocityX: Int = 0
    private var mScroller: Scroller? = null

    fun FlingRunnable(context: Context) {
        this.mScroller = Scroller(context, null, false)
    }

    override fun run() {
        // 如果已经结束，就不再进行
        if (mScroller?.computeScrollOffset() == false) {
            return
        }

        // 计算偏移量
        val currX = mScroller?.currX
        val diffX = mInitX - currX!!
        // 用于记录是否超出边界，如果已经超出边界，则不再进行回调，即使滚动还没有完成
        if(diffX != 0) {
            if (mScroller?.isFinished == false) {
                post(this)
            }
            mInitX = currX
        }
    }

    fun start(initX: Int,
              velocityX: Int,
              minX: Int,
              maxX: Int) {
        this.mInitX = initX
        this.mMaxX = maxX
        this.mMinX = minX
        this.mVelocityX = velocityX
        // 先停止上一次的滚动
        if (mScroller?.isFinished == false) {
            mScroller?.abortAnimation()
        }
        // 开始 fling
        mScroller?.fling(initX, 0, velocityX,
                0, 0, maxX, 0, 0)
        post(this)
    }

    fun stop() {
        if (mScroller?.isFinished == false) {
            mScroller?.abortAnimation()
        }
    }

}