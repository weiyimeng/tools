package com.haoke91.a91edu.widget.dropdownlayout;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/17 上午9:28
 * 修改人：weiyimeng
 * 修改时间：2018/7/17 上午9:28
 * 修改备注：
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by sunger on 16/4/16.
 */
public class DropDownLayout extends RelativeLayout implements ViewTreeObserver.OnGlobalLayoutListener {
    
    private MaskView mMaskView;
    private MenuLayout mMenuLayout;
    
    public DropDownLayout(Context context) {
        this(context, null);
    }
    
    public DropDownLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public DropDownLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }
    
    private void findChildView() {
        for (int i = 0; i < getChildCount(); i++) {
            if (mMaskView != null && mMenuLayout != null) {
                break;
            }
            View childItem = getChildAt(i);
            if (childItem instanceof MaskView) {
                mMaskView = (MaskView) childItem;
            } else if (childItem instanceof MenuLayout) {
                mMenuLayout = (MenuLayout) childItem;
            }
        }
    }
    
    @Override
    public void onGlobalLayout() {
        findChildView();
        if (mMaskView == null || mMenuLayout == null) {
            throw new IllegalArgumentException("you layout must be contain  MaskView MenuLayout");
        }
        
        if (Build.VERSION.SDK_INT < 16) {
            removeLayoutListenerPre16();
        } else {
            removeLayoutListenerPost16();
        }
        mMenuLayout.setOnAnimationEndListener(new MenuLayout.onAnimationEndListener() {
            @Override
            public void onAnimationChange(float value) {
                mMaskView.setAlpha(value * 10);
            }
            
            
            @Override
            public void onAnimationEnd(boolean isShow) {
                isAnimationEnd = true;
                if (!isShow) {
                    mMaskView.dissMiss();
                }
            }
            
            @Override
            public void onAnimationStart() {
                isAnimationEnd = false;
                
            }
        });
        mMenuLayout.setVisibility(GONE);
        mMaskView.setVisibility(GONE);
        mMaskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
        
    }
    
    boolean isAnimationEnd = true;
    
    @SuppressWarnings("deprecation")
    private void removeLayoutListenerPre16() {
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }
    
    @TargetApi(16)
    private void removeLayoutListenerPost16() {
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }
    
    
    public void showMenuAt(int position) {
        //        if (!isAnimationEnd) {
        //            return;
        //        }
        if (mMenuLayout.isShow() && mMenuLayout.getCurrentItem() == position) {
            mMaskView.dissMiss();
            mMenuLayout.dissMiss();
        } else {
            mMaskView.show();
            mMenuLayout.show();
            mMenuLayout.setCurrentItem(position);
        }
    }
    
    public void closeMenu() {
        mMaskView.dissMiss();
        mMenuLayout.dissMiss();
        
    }
    
    
}
