package com.haoke91.a91edu.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.haoke91.a91edu.R;

import java.util.Random;


/**
 * @author Vondear
 * @date 2017/3/15
 */

public class AnimationTool {
    
    /**
     * 颜色渐变动画
     *
     * @param beforeColor 变化之前的颜色
     * @param afterColor  变化之后的颜色
     */
    public static void animationColorGradient(int beforeColor, int afterColor) {
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), beforeColor, afterColor).setDuration(3000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //                textView.setTextColor((Integer) animation.getAnimatedValue());
                //listener.doSomething((Integer) animation.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }
    
    
    /**
     * 缩小动画
     *
     * @param view
     */
    public static void zoomIn(final View view, float scale, float dist) {
        view.setPivotY(view.getHeight());
        view.setPivotX(view.getWidth() / 2);
        AnimatorSet mAnimatorSet = new AnimatorSet();
        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, scale);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, scale);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", 0.0f, -dist);
        
        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX).with(mAnimatorScaleY);
        //        mAnimatorSet.play(mAnimatorScaleX).with(mAnimatorScaleY);
        mAnimatorSet.setDuration(200);
        mAnimatorSet.start();
    }
    
    /**
     * 放大动画
     *
     * @param view
     */
    public static void zoomOut(final View view, float scale) {
        view.setPivotY(view.getHeight());
        view.setPivotX(view.getWidth() / 2);
        AnimatorSet mAnimatorSet = new AnimatorSet();
        
        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", scale, 1.0f);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", scale, 1.0f);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), 0);
        
        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX).with(mAnimatorScaleY);
        //        mAnimatorSet.play(mAnimatorScaleX).with(mAnimatorScaleY);
        mAnimatorSet.setDuration(200);
        mAnimatorSet.start();
    }
    
    public static void ScaleUpDowm(View view) {
        ScaleAnimation animation = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(1200);
        view.startAnimation(animation);
    }
    
    public static void animateHeight(int start, int end, final View view) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();//根据时间因子的变化系数进行设置高度
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);//设置高度
            }
        });
        valueAnimator.start();
    }
    
    public static ObjectAnimator popup(final View view, final long duration) {
        view.setAlpha(0);
        view.setVisibility(View.VISIBLE);
        
        ObjectAnimator popup = ObjectAnimator.ofPropertyValuesHolder(view,
            PropertyValuesHolder.ofFloat("alpha", 0f, 1f),
            PropertyValuesHolder.ofFloat("scaleX", 0f, 1f),
            PropertyValuesHolder.ofFloat("scaleY", 0f, 1f));
        popup.setDuration(duration);
        popup.setInterpolator(new OvershootInterpolator());
        
        return popup;
    }
    
    public static ObjectAnimator popout(final View view, final long duration, final AnimatorListenerAdapter animatorListenerAdapter) {
        ObjectAnimator popout = ObjectAnimator.ofPropertyValuesHolder(view,
            PropertyValuesHolder.ofFloat("alpha", 1f, 0f),
            PropertyValuesHolder.ofFloat("scaleX", 1f, 0f),
            PropertyValuesHolder.ofFloat("scaleY", 1f, 0f));
        popout.setDuration(duration);
        popout.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
                if (animatorListenerAdapter != null) {
                    animatorListenerAdapter.onAnimationEnd(animation);
                }
            }
        });
        popout.setInterpolator(new AnticipateOvershootInterpolator());
        
        return popout;
    }
    
    
    public static void textScaleAnimation(TextView tab, float start, float end) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 1f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", start, end);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", start, end);
        ObjectAnimator.ofPropertyValuesHolder(tab, pvhX, pvhY, pvhZ).setDuration(100).start();
    }
    
    
    public static void showSlideLeft(final View view, final AnimationRunFinshCallBack animationRunFinshCallBack, int width) {
        
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", -view.getWidth() + width, 0);
        
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (animationRunFinshCallBack != null) {
                    animationRunFinshCallBack.finishAnimation();
                }
            }
        });
        animator.setDuration(500);
        animator.start();
    }
    
    public static void hideSlideLeft(final ViewGroup view, final AnimationRunFinshCallBack animationRunFinshCallBack, int width) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", 0, -view.getWidth() + width);
        
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (animationRunFinshCallBack != null) {
                    animationRunFinshCallBack.finishAnimation();
                }
            }
        });
        animator.setDuration(500);
        animator.start();
    }
    
    public interface AnimationRunFinshCallBack {
        void finishAnimation();
    }
    
    public static ObjectAnimator createFlyFromLtoR(final View target, float star, float end, int duration, TimeInterpolator interpolator) {
        //1.个人信息先飞出来
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(target, "translationX",
            star, end);
        anim1.setInterpolator(interpolator);
        anim1.setDuration(duration);
        return anim1;
    }
    
    public static ObjectAnimator createDismissAnimation(ViewGroup viewGroup) {
        PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat("translationY", 0, -400);
        
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.0f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(viewGroup, translationY, alpha);
        animator.setInterpolator(new LinearInterpolator());
        animator.setStartDelay(0);
        double random = Math.random();
        random = random < 0.5 ? 0.5 : random;
        animator.setDuration((long) (1500 * random));
        
        //        translationY = PropertyValuesHolder.ofFloat("translationY", -50, -100);
        //        alpha = PropertyValuesHolder.ofFloat("alpha", 0.5f, 0f);
        //        ObjectAnimator animator1 = ObjectAnimator.ofPropertyValuesHolder(viewGroup, translationY, alpha);
        //        animator1.setStartDelay(0);
        //        animator1.setDuration(1500);
        //
        //        // 复原
        //        //        ObjectAnimator fadeAnimator2 = GiftAnimationUtil.createFadeAnimator(giftFrameLayout, 0, 0, 0, 0);
        //
        //        AnimatorSet animatorSet = new AnimatorSet();
        //        animatorSet.play(animator);
        //        //        animatorSet.play(fadeAnimator2).after(animator1);
        //        animator.start();
        return animator;
    }
    
    public static ObjectAnimator scaleGiftNum(final TextView target) {
        PropertyValuesHolder anim4 = PropertyValuesHolder.ofFloat("scaleX",
            1.2f, 0.8f, 1f);
        PropertyValuesHolder anim5 = PropertyValuesHolder.ofFloat("scaleY",
            1.2f, 0.8f, 1f);
        PropertyValuesHolder anim6 = PropertyValuesHolder.ofFloat("alpha",
            1.0f, 0f, 1f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(target, anim4, anim5, anim6).setDuration(400);
        return animator;
        
    }
}
