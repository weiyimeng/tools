package com.eduhdsdk.tools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2018/9/4/004.
 */

public class VolumeView extends View {


    private int mPressXIndex = 5;
    //几个音量条
    private int mTotal = 4;
    //默认音量
    private int mPrecess = 1;

    private Rect mRect;
    //音量宽度
    private int mRectWidth = 5;
    //条高度
    private int mRectHeight = 100;
    //条与条间隔距离
    private int space = 6;
    private int mTop = 0;
    private int mBottom = mTop + mRectHeight;
    //条与条间隔距离
    private int mPaddingX = 3;
    private Paint mPaint;

    public VolumeView(Context context) {
        super(context);
        init();
    }

    public VolumeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VolumeView(Context context, AttributeSet attrs,
                      int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 1; i <= mTotal; i++) {
            drawRect(i, canvas);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(4*5 + 4*6,heightMeasureSpec);
    }

    public void setIndex(int index) {
        mPrecess = index;
        invalidate();
    }

    /**
     * @param i
     * @param canvas
     */
    private void drawRect(int i, Canvas canvas) {
        int color = i <= mPrecess ? Color.parseColor("#FFDC54") : Color.parseColor("#ffffff");
        mPaint.setColor(color);
        int startX = getLeft(i);
        RectF rectF=new RectF();
        rectF.left = getLeft(i);
        rectF.top = mTop;
        rectF.right = startX + mRectWidth;
        rectF.bottom = mTop + this.getHeight();
//        LogUtils.isLogE("YF",mTop + this.getHeight()+"");
        canvas.drawRoundRect(rectF,2,2,mPaint);
//        canvas.drawRect(getLeft(i), mTop, startX + mRectWidth, mBottom, mPaint);
    }

    private int getLeft(int i) {
        return (i - 1) * (mRectWidth + space) + mPaddingX;
    }

    public void setProcess(int process) {
        this.mPrecess = process;
        invalidate();
    }

    public void setTotal(int total) {
        this.mTotal = total;
        invalidate();
    }

    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                handleX(x);
                break;
            case MotionEvent.ACTION_MOVE:
                handleX(x);
                break;
            case MotionEvent.ACTION_UP:
                handleX(x);
                break;
        }
        return true;
    }
*/
    private void handleX(float x) {
        int index = getIndexByX(x);

        if (index != mPressXIndex) {
            mPressXIndex = index;

            mPrecess = mPressXIndex;
            if (mListener != null) {
                mListener.onProcessChanged(mPressXIndex);
            }
        }
        /*invalidate();*/
    }

    public interface OnProcessListener {
        void onProcessChanged(int process);
    }

    private OnProcessListener mListener;

    public void setOnProcessListener(OnProcessListener listener) {
        mListener = listener;
    }

    private int getIndexByX(float x) {
        int index = (int) (x - mPaddingX);
        if (index <= 0) {
            return 0;
        }
        index = index / (space + mRectWidth) + 1;
        return index;
    }




}