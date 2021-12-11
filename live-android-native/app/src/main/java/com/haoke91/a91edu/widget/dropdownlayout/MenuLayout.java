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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;


import com.haoke91.a91edu.R;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by sunger on 16/4/16.
 */
public class MenuLayout extends RelativeLayout {
    private Animation animationIn;
    private Animation animationOut;
    private FragmentManager fragmentManager;
    private List<Fragment> fragments;
    private int currentItem = 0;
    
    public MenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public MenuLayout(Context context) {
        this(context, null);
    }
    
    public MenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
        post(new Runnable() {
            @Override
            public void run() {
                height = getHeight();
            }
        });
    }
    
    //    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MenuLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
        post(new Runnable() {
            @Override
            public void run() {
                height = getHeight();
                //               / Logger.e("height===1111===" + height);
                
            }
        });
    }
    
    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }
    
    private int getMenuId() {
        if (getId() == NO_ID) {
            setId(R.id.menu_id);
        }
        return getId();
    }
    
    private void hideLastFragment(FragmentTransaction transaction) {
        for (int i = 0; i < fragments.size(); i++) {
            if (i != currentItem || !fragments.get(i).isHidden()) {
                transaction.hide(fragments.get(i));
            }
        }
    }
    
    public int getCurrentItem() {
        return currentItem;
    }
    
    public void setCurrentItem(int position) {
        currentItem = position;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideLastFragment(transaction);
        transaction.show(fragments.get(position));
        transaction.commit();
    }
    
    
    public void bindFragments(List<Fragment> fragmentList) {
        this.fragments = fragmentList;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (Fragment frg : fragments) {
            transaction.add(getMenuId(), frg);
        }
        transaction.commit();
    }
    
    int height;
    
    public void show() {
        //  showOrDimss(0, 1, true);
        if (animationIn == null) {
            animationIn = AnimationUtils.loadAnimation(getContext(), R.anim.menu_in);
        }
        
        
        setVisibility(VISIBLE);
        setAnimation(animationIn);
        animationIn.start();
    }
    
    
    private void showOrDimss(int start, int end, final boolean show) {
        final ViewGroup.LayoutParams layoutParams = getLayoutParams();
        ValueAnimator animator = ValueAnimator.ofFloat(start, end);
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                layoutParams.height = (int) ((float) animation.getAnimatedValue() * height);
                Logger.e("layoutParams.height====" + layoutParams.height);
                requestLayout();
                if (!show) {
                    onAnimationEndListener.onAnimationChange((Float) animation.getAnimatedValue());
                    
                }
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!show) {
                    setVisibility(GONE);
                }
                onAnimationEndListener.onAnimationEnd(show);
            }
            
            @Override
            public void onAnimationStart(Animator animation, boolean isReverse) {
                onAnimationEndListener.onAnimationStart();
                
            }
            
            @Override
            public void onAnimationStart(Animator animation) {
                if (show) {
                    setVisibility(VISIBLE);
                }
            }
        });
        animator.start();
    }
    
    public void dissMiss() {
        //  showOrDimss(1, 0, false);
        
        //        if (animationOut == null) {
        //            animationOut = AnimationUtils.loadAnimation(getContext(), R.anim.menu_out);
        //        }
        setVisibility(GONE);
        if (closeMenuListner != null) {
            closeMenuListner.closeMenu();
        }
        //        setAnimation(animationOut);
        //        animationOut.start();
    }
    
    
    public boolean isShow() {
        return getVisibility() == VISIBLE;
    }
    
    public void setOnAnimationEndListener(MenuLayout.onAnimationEndListener onAnimationEndListener) {
        this.onAnimationEndListener = onAnimationEndListener;
    }
    
    onAnimationEndListener onAnimationEndListener;
    
    public interface onAnimationEndListener {
        void onAnimationChange(float value);
        
        void onAnimationEnd(boolean isShow);
        
        void onAnimationStart();
    }
    
    private CloseMenuListener closeMenuListner;
    
    public void setCloseMenuListner(CloseMenuListener closeMenuListner) {
        this.closeMenuListner = closeMenuListner;
    }
    
    public interface CloseMenuListener {
        void closeMenu();
    }
}
