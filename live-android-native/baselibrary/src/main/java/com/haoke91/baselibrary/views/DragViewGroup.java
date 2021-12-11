package com.haoke91.baselibrary.views;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

/**
 * Created by frank on 2017/6/20.
 */

public class DragViewGroup extends FrameLayout {
    private static final String TAG = "DragViewGroup";
    
    private boolean isEnableDrag = true;
    private ViewDragHelper mDragHelper;
    
    private float mDragOriLeft;
    private float mDragOriTop;
    
    public DragViewGroup(Context context) {
        this(context, null);
    }
    
    public DragViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public DragViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        
        mDragHelper = ViewDragHelper.create(this, 0.5f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                //                if (child.getTag() != null && "drag".equalsIgnoreCase(child.getTag().toString())){
                //                    return true;
                //                }
                return true;
            }
            
            @Override
            public void onViewCaptured(View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
                mDragOriLeft = capturedChild.getLeft();
                mDragOriTop = capturedChild.getTop();
                Log.d(TAG, "onViewCaptured: left:" + mDragOriLeft
                    + " top:" + mDragOriTop);
            }
            
            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                super.onEdgeDragStarted(edgeFlags, pointerId);
                Log.d(TAG, "onEdgeDragStarted: " + edgeFlags);
                mDragHelper.captureChildView(getChildAt(getChildCount() - 1), pointerId);
            }
            
            @Override
            public void onEdgeTouched(int edgeFlags, int pointerId) {
                super.onEdgeTouched(edgeFlags, pointerId);
                Log.d(TAG, "onEdgeTouched: " + edgeFlags);
            }
            
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return left;
            }
            
            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }
            
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                
                View child = getChildAt(0);
                if (child != null && child == releasedChild){
                    mDragHelper.flingCapturedView(getPaddingLeft(), getPaddingTop(),
                        getWidth() - getPaddingRight() - child.getWidth(),
                        getHeight() - getPaddingBottom() - child.getHeight());
                } else{
                    
                    mDragHelper.settleCapturedViewAt((int) mDragOriLeft, (int) mDragOriTop);
                    
                }
                invalidate();
            }
            
            @Override
            public int getViewHorizontalDragRange(View child) {
                return 100;
            }
            
            @Override
            public int getViewVerticalDragRange(View child) {
                return 100;
            }
        });
        
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.DIRECTION_HORIZONTAL);
        
    }
    
    
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragHelper != null && mDragHelper.continueSettling(true)){
            invalidate();
        }
    }
    
    
    public void testSmoothSlide(boolean isReverse) {
        if (mDragHelper != null){
            View child = getChildAt(1);
            if (child != null){
                if (isReverse){
                    mDragHelper.smoothSlideViewTo(child,
                        getLeft(), getTop());
                } else{
                    mDragHelper.smoothSlideViewTo(child,
                        getRight() - child.getWidth(),
                        getBottom() - child.getHeight());
                }
                invalidate();
            }
        }
    }
    
    public void enableDrag(Boolean enableDrag) {
        this.isEnableDrag = enableDrag;
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isShouldHideKeyboard(getChildAt(0), event) && isEnableDrag){
            mDragHelper.processTouchEvent(event);
            return true;
        } else{
            return false;
        }
        
        
    }
    
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null){
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                top = l[1],
                bottom = top + v.getHeight(),
                right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                && event.getY() > top && event.getY() < bottom){
                return false;
            } else{
                return true;
            }
        }
        return false;
    }
    
}
