package com.haoke91.a91edu.utils;

import android.graphics.Rect;
import android.view.TouchDelegate;
import android.view.View;

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/10/10 下午2:13
 * 修改人：weiyimeng
 * 修改时间：2018/10/10 下午2:13
 * 修改备注：
 */
public class ViewUtils {
    /**
     * 增加控件的可点击范围，最大范围只能是父布局所包含的的区域
     */
    public static void addDefaultScreenArea(final View view, final int top, final int bottom, final int left, final int right) { // 增大checkBox的可点击范围
        final View parent = (View) view.getParent();
        parent.post(new Runnable() {
            public void run() {
                
                Rect bounds = new Rect();
                view.setEnabled(true);
                view.getHitRect(bounds);
                
                bounds.top -= top;
                bounds.bottom += bottom;
                bounds.left -= left;
                bounds.right += right;
                
                TouchDelegate touchDelegate = new TouchDelegate(bounds, view);
                
                if (View.class.isInstance(view.getParent())) {
                    ((View) view.getParent()).setTouchDelegate(touchDelegate);
                }
            }
        });
    }
}
