package com.haoke91.a91edu.widget.calendar;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;
import com.orhanobut.logger.Logger;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/7/13 17:18
 */
public class MyMonthView extends MonthView {
    
    //购买了未直播  / 已完成直播  /当日直播  /预约直播课   /选中状态
    private String[] colors={"#E7E7E7", "#E3F4D5", "#75C82B", "#F5A623", "#75C82B"};
    private int mRadius;
    
    public MyMonthView(Context context) {
        super(context);
        //        //兼容硬件加速无效的代码
        //        setLayerType(View.LAYER_TYPE_SOFTWARE, mSelectedPaint);
        //        //4.0以上硬件加速会导致无效
        //        mSelectedPaint.setMaskFilter(new BlurMaskFilter(25, BlurMaskFilter.Blur.SOLID));
    }
    
    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) * 9 / 32;
//        mCurDayTextPaint.setColor(Color.parseColor("#FFFFFF"));
        mSchemePaint.setStyle(Paint.Style.STROKE);
    }
    
    @Override
    protected void onLoopStart(int x, int y) {
    
    }
    
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        mSelectedPaint.setColor(Color.parseColor(colors[4]));
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        return false;
    }
    
    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        //        canvas.drawCircle(cx, cy, mRadius, mSchemePaint);
        mSchemePaint.setStrokeWidth(2 * getResources().getDisplayMetrics().density);
        String scheme = calendar.getScheme();
        Logger.e("==="+scheme);
        if(scheme==null || scheme.trim().length()==0){
            return;
        }
        if("01".equals(scheme)){
            mSchemePaint.setStyle(Paint.Style.FILL);
            mSchemePaint.setColor(Color.parseColor(colors[0]));
            canvas.drawCircle(cx,cy,mRadius,mSchemePaint);
        }else if("02".equals(scheme)){
            mSchemePaint.setStyle(Paint.Style.FILL);
            mSchemePaint.setColor(Color.parseColor(colors[1]));
            canvas.drawCircle(cx,cy,mRadius,mSchemePaint);
        }else if("03".equals(scheme)){
            mSchemePaint.setStyle(Paint.Style.STROKE);
            mSchemePaint.setColor(Color.parseColor(colors[2]));
            canvas.drawCircle(cx,cy,mRadius,mSchemePaint);
        }else {
            mSchemePaint.setStyle(Paint.Style.FILL);
            mSchemePaint.setColor(Color.parseColor(colors[3]));
            canvas.drawCircle(cx,cy,mRadius,mSchemePaint);
        }
//        canvas.drawLine(x + mItemWidth / 4, y + mItemHeight, x + mItemWidth * 3 / 4, y + mItemHeight, mSchemePaint);//横线
        //对勾
//        Path path = new Path();
//        path.moveTo(x + mItemWidth * 2 / 6, y + mItemHeight * 5 / 6);
//        path.lineTo(cx, y + mItemHeight);
//        path.lineTo(x + mItemWidth * 5 / 6, y + mItemHeight * 4 / 6);
//        canvas.drawPath(path, mSchemePaint);
    }
    
    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine + y;
        int cx = x + mItemWidth / 2;
        
        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                cx,
                baselineY,
                mSelectTextPaint);
        } else if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                cx,
                baselineY,
                calendar.isCurrentDay() ? mCurDayTextPaint :
                    calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);
            
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
                calendar.isCurrentDay() ? mCurDayTextPaint :
                    calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }
    }
}
