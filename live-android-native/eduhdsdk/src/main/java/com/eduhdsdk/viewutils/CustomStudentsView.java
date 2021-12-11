package com.eduhdsdk.viewutils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/12/22 17:04
 */
public class CustomStudentsView extends RelativeLayout {
    private int startX, startY;
    
    public CustomStudentsView(Context context) {
        super(context);
    }
    
    public CustomStudentsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("touchEvent===",event.getAction()+"");
        if (MotionEvent.ACTION_DOWN == event.getAction()){
            startX = (int) event.getX();
            startY = (int) event.getY();
        } else if (MotionEvent.ACTION_MOVE == event.getAction()){
            int deltaX = (int) (event.getX() - startX);
            int deltaY = (int) (event.getY() - startY);
            if (deltaX < -100 && Math.abs(-deltaX + deltaY) > 0){
                ObjectAnimator.ofFloat(this, "translationX", 0, -this.getWidth());
            }
        }
        return super.onTouchEvent(event);
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.e("touchEvent===",event.getAction()+"");
        if (MotionEvent.ACTION_DOWN == event.getAction()){
            startX = (int) event.getX();
            startY = (int) event.getY();
        } else if (MotionEvent.ACTION_MOVE == event.getAction()){
            int deltaX = (int) (event.getX() - startX);
            int deltaY = (int) (event.getY() - startY);
            if (deltaX < -100 && Math.abs(-deltaX + deltaY) > 0){
                ObjectAnimator.ofFloat(this, "translationX", 0, -this.getWidth());
            }
        }
        return super.onInterceptTouchEvent(event);
    }
}
