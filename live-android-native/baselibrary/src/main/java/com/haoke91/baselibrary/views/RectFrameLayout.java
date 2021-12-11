package com.haoke91.baselibrary.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.haoke91.baselibrary.R;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/9 下午1:19
 * 修改人：weiyimeng
 * 修改时间：2018/7/9 下午1:19
 * 修改备注：
 */
public class RectFrameLayout extends FrameLayout {
    float aFloat;
    
    public RectFrameLayout(Context context) {
        super(context);
    }
    
    public RectFrameLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RectFrameLayout);
        aFloat = typedArray.getFloat(R.styleable.RectFrameLayout_scaleToHeight, 1);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i = MeasureSpec.makeMeasureSpec((int) (MeasureSpec.getSize(widthMeasureSpec) * aFloat), MeasureSpec.getMode(widthMeasureSpec));
        super.onMeasure(widthMeasureSpec, i);
    }
}
