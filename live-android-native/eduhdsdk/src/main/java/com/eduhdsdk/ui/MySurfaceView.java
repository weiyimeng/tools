package com.eduhdsdk.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;

import org.tkwebrtc.SurfaceViewRenderer;

/**
 * 项目名称：talkplus
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/11/8 15:55
 */
public class MySurfaceView extends SurfaceViewRenderer {
    public MySurfaceView(Context context){
        super(context);
    }
    
    public MySurfaceView(Context context, AttributeSet attrs){
        super(context, attrs);
    }
    
    @Override
    public void draw(Canvas canvas){
        Path path = new Path();
        //用矩形表示SurfaceView宽高
        RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        //15.0f即是圆角半径
        path.addRoundRect(rect, getResources().getDisplayMetrics().density * 4.0f, getResources().getDisplayMetrics().density * 4.0f, Path.Direction.CCW);
        //裁剪画布，并设置其填充方式
        canvas.clipPath(path, Region.Op.REPLACE);
        super.draw(canvas);
    }
}
