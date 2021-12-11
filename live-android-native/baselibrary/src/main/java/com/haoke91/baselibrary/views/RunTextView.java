package com.haoke91.baselibrary.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug;

/**
 * @author vondear
 * @date 2016/6/28
 */
public class RunTextView extends android.support.v7.widget.AppCompatTextView {
    public RunTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public RunTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public RunTextView(Context context) {
        super(context);
    }
    
    @Override
    @ViewDebug.ExportedProperty(category = "focus")
    public boolean isFocused() {
        return true;
    }
}