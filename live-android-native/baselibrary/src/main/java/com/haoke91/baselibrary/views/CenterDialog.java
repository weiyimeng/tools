package com.haoke91.baselibrary.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.haoke91.baselibrary.R;


public abstract class CenterDialog extends Dialog {
    
    
    public CenterDialog(Context context, @LayoutRes int layout) {
        super(context, R.style.CommonDialog);
        //        setCanceledOnTouchOutside(true);
        //        getWindow().getAttributes().width = -1;
        //        getWindow().getAttributes().height = -2;
        //        getWindow().getAttributes().y = 0;
        //        getWindow().setGravity(Gravity.CENTER_VERTICAL);
        //        getWindow().setAttributes(getWindow().getAttributes());
        getWindow().setWindowAnimations(R.style.DialogAnim);
        if (context instanceof Activity) {
            setOwnerActivity((Activity) context);
        }
        //        this.context = context;
        View view = LayoutInflater.from(context).inflate(layout, null);
        setCanceledOnTouchOutside(true);
        setContentView(view);
        initView(view);
    }
    
    public abstract void initView(View view);
    
    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = -1;
        lp.y = 0;
        getWindow().setAttributes(lp);
    }
}
