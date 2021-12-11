package com.haoke91.a91edu.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.orhanobut.logger.Logger;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/7/23 17:33
 */
public class CanLocationScrollView extends ScrollView {
    private Scroller mScroller;
    int startY;
    public boolean canScrollChoose = true;
    private boolean isTouching = false;
    public boolean isFlying = false;
    private int lastY = 0;
    
    public CanLocationScrollView(Context context) {
        super(context);
    }
    
    public CanLocationScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }
    
    public CanLocationScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
    }
    
    public void canScollView(View outView, View inView) {
        //        this.mOutView = outView;
        //        this.mInView = inView;
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //        switch (ev.getAction()) {
        //            case MotionEvent.ACTION_DOWN:
        //                startY = (int) ev.getY();
        //                isTouching = true;
        //                break;
        //            case MotionEvent.ACTION_MOVE:
        //        Log.e("tag", "dispatchTouchEvent=====ACTION_MOVE===");
        //                canScrollChoose = true;
        //                isTouching = true;
        //                isFlying = true;
        //                break;
        //            case MotionEvent.ACTION_UP:
        //            case MotionEvent.ACTION_CANCEL:
        //                isTouching = false;
        //                break;
        //            default:
        //                isTouching = true;
        //                break;
        //        }
        return super.dispatchTouchEvent(ev);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                isTouching = true;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("tag", "ACTION_MOVE===");
                canScrollChoose = true;
                isTouching = true;
                isFlying = true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isTouching = false;
                break;
            default:
                isTouching = true;
                break;
        }
        return super.onTouchEvent(ev);
    }
    
    @Override
    public void computeScroll() {
        if (!isTouching() && mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
        super.computeScroll();
    }
    
    //调用此方法滚动到目标位置  duration滚动时间
    public void smoothScrollToSlow(int fx, int fy, int duration) {
        isFlying = false;
        int dx = fx - getScrollX();//mScroller.getFinalX();  普通view使用这种方法
        int dy = fy - getScrollY();  //mScroller.getFinalY();
        smoothScrollBySlow(dx, dy, duration);
    }
    
    //调用此方法设置滚动的相对偏移
    public void smoothScrollBySlow(int dx, int dy, int duration) {
        
        //设置mScroller的滚动偏移量
        mScroller.startScroll(getScrollX(), getScrollY(), dx, dy, duration);//scrollView使用的方法（因为可以触摸拖动）
        //        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy, duration);  //普通view使用的方法
        invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
    }
    
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(l, t, oldl, oldt);
        }
    }
    
    
    /**
     * 是否在手动滑动
     *
     * @return
     */
    public boolean isTouching() {
        return isTouching;
    }
    //
    //    public boolean isFlying(){
    //        return isFlying;
    //    }
    
    public interface OnScrollListener {
        void onScroll(int x, int y, int oldx, int oldy);
    }
    
    private OnScrollListener mOnScrollListener;
    
    public void setOnScrollListener(OnScrollListener scrollListener) {
        this.mOnScrollListener = scrollListener;
    }
}
