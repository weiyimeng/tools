package com.haoke91.a91edu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.haoke91.a91edu.R;
import com.orhanobut.logger.Logger;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/19 上午11:43
 * 修改人：weiyimeng
 * 修改时间：2018/7/19 上午11:43
 * 修改备注：
 */
public class HomeScrollView extends ScrollView {
    private OnScrollListener onScrollListener;
    private float mDownPosX, mDownPosY;
    private boolean isScrolledToTop, isScrolledToBottom;
    
    public HomeScrollView(Context context) {
        this(context, null);
    }
    
    public HomeScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public HomeScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        RelativeLayout rl_scroller_classify = findViewById(R.id.rl_scroller_classify);
    }
    
    /**
     * 设置滚动接口
     *
     * @param onScrollListener
     */
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }
    
    
    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }
    
    
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener != null) {
            onScrollListener.onScroll(t);
        }
        //        if (getScrollY() == 0) {
        //            isScrolledToTop = true;
        //            isScrolledToBottom = false;
        //        } else if (getScrollY() + getHeight() - getPaddingTop() - getPaddingBottom() == getChildAt(0).getHeight()) {
        //            isScrolledToBottom = true;
        //            isScrolledToTop = false;
        //        } else {
        //            isScrolledToTop = false;
        //            isScrolledToBottom = false;
        //        }
        //     }
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //   super.onInterceptTouchEvent(ev);
        final float x = ev.getX();
        final float y = ev.getY();
        
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownPosX = x;
                mDownPosY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaX = Math.abs(x - mDownPosX);
                final float deltaY = Math.abs(y - mDownPosY);
                if (deltaX > deltaY - 50) {
                    return false;
                }
                //                Logger.e("isScrolledToTop===" + isScrolledToTop + "===isScrolledToBottom===" + isScrolledToBottom);
                //                if (!isScrolledToBottom) {
                //                    return true;
                //                }
            
        }
        // return false;
        return super.onInterceptTouchEvent(ev);
    }
    
    //    @Override
    //    public boolean onTouchEvent(MotionEvent ev) {
    //        super.onTouchEvent(ev);
    //        final int action = ev.getAction();
    //        switch (action) {
    //            case MotionEvent.ACTION_MOVE:
    //                Logger.e("isScrolledToTop===" + isScrolledToTop + "===isScrolledToBottom===" + isScrolledToBottom);
    //                if (!isScrolledToBottom) {
    //                    return true;
    //                }
    //        }
    //        return false;
    //    }
    
    /**
     * 滚动的回调接口
     */
    public interface OnScrollListener {
        /**
         * 回调方法， 返回MyScrollView滑动的Y方向距离
         *
         * @param scrollY 、
         */
        void onScroll(int scrollY);
    }
    
    
}
