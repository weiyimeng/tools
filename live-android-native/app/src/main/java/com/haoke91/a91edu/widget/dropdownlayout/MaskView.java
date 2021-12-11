package com.haoke91.a91edu.widget.dropdownlayout;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/17 上午9:29
 * 修改人：weiyimeng
 * 修改时间：2018/7/17 上午9:29
 * 修改备注：
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by sunger on 16/4/16.
 */
public class MaskView extends View {
    public MaskView(Context context) {
        this(context, null);
    }
    
    public MaskView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public MaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(Color.parseColor("#88838685"));
        
    }
    
    //    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    //    public MaskView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    //        super(context, attrs, defStyleAttr, defStyleRes);
    //        setBackgroundColor(Color.parseColor("#88838685"));
    //    }
    
    
    public void show() {
        setAlpha(1);
        setVisibility(VISIBLE);
    }
    
    public void dissMiss() {
        setVisibility(GONE);
    }
    
}
