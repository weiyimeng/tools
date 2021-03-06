package com.android.dz.pullrecyclerview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.dz.pullrecyclerview.R;

public class AnimatorView extends RelativeLayout {

    private View mView;
    private LinearLayout mLayout;
    private TextView mTextView;
    private int mMove;
    private Path mPath;
    private Paint mBackPaint;
    private int mHeight;
    private int mLayoutHeight;
    private int mLayoutWidth;

    public AnimatorView(Context context) {
        super(context);
        mPath = new Path();
        mBackPaint = new Paint();
        mBackPaint.setAntiAlias(true);
        mBackPaint.setStyle(Paint.Style.FILL);
        mBackPaint.setColor(0xffF3F3F3);

        mView = View.inflate(context, R.layout.animator_hot, null);
        mLayout = (LinearLayout) mView.findViewById(R.id.animator_ll);
        mTextView = (TextView) mView.findViewById(R.id.animator_text);
        addView(mView);
    }

    public AnimatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mHeight = getHeight();
        mLayoutHeight = mLayout.getHeight();
        mLayoutWidth = mLayout.getWidth();
    }

    public void setRefresh(int width){
        mMove += width;
        if(mMove < 0){
            mMove = 0;
        }else if(mMove > DZStickyNavLayouts.maxWidth){
            mMove = DZStickyNavLayouts.maxWidth;
        }
        mView.getLayoutParams().width = mMove;
        mView.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;

        if(mMove > DZStickyNavLayouts.maxWidth / 2){
            mTextView.setText("????????????");
        }else{
            mTextView.setText("????????????");
        }
        requestLayout();
    }

    public void setRelease(){
        mMove = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        float marginTop = (mHeight - mLayoutHeight) / 2;
        // ?????????x??????????????????y??????
        mPath.moveTo(mMove - mLayoutWidth, marginTop);
        // ????????????x?????????????????????y??????????????????x??????????????????y??????
        mPath.quadTo(0,  mHeight / 2, mMove - mLayoutWidth, mLayoutHeight + marginTop);
        canvas.drawPath(mPath, mBackPaint);
    }
}
