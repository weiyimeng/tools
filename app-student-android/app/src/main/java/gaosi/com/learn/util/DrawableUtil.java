package gaosi.com.learn.util;

import android.graphics.drawable.GradientDrawable;

public class DrawableUtil {

    public static GradientDrawable createShape(int color, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(radius);//设置4个角的弧度
        drawable.setColor(color);// 设置颜色
        return drawable;
    }

}
