package com.haoke91.baselibrary.views.emptyview;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.blankj.utilcode.util.ToastUtils;
import com.haoke91.baselibrary.R;


/**
 * Created by santalu on 09/08/2017.
 */

public class EmptyView extends LinearLayout {
    
    private final EmptyViewBuilder builder;
    
    public LinearLayout container;
    public View progressBar;
    private ImageView imageView;
    private TextView titleView;
    private TextView textView;
    private TextView button;
    private LottieAnimationView lottieView;
    public EmptyView(Context context) {
        super(context);
        builder = new EmptyViewBuilder(this);
    }
    
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        builder = new EmptyViewBuilder(this, attrs);
    }
    
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        builder = new EmptyViewBuilder(this, attrs);
    }
    
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (child.getVisibility() == VISIBLE) {
            builder.include(child);
        }
    }
    
    @Override
    public void setOnClickListener(@Nullable View.OnClickListener onClickListener) {
        builder.setOnClickListener(onClickListener);
    }
    
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflate(getContext(), R.layout.empty_view, this);
        container = findViewById(R.id.empty_layout);
        imageView = findViewById(R.id.empty_icon);
        textView = findViewById(R.id.empty_text);
        titleView = findViewById(R.id.empty_title);
        button = findViewById(R.id.empty_button);
        button.setMovementMethod(LinkMovementMethod.getInstance());
        progressBar = findViewById(R.id.empty_progress_bar);
        lottieView=findViewById(R.id.lottieView);
    }
    
    public EmptyViewBuilder builder() {
        return builder;
    }
    
    public EmptyViewBuilder content() {
        return builder.setState(EmptyViewBuilder.CONTENT);
    }
    
    public EmptyViewBuilder loading() {
        return builder.setState(EmptyViewBuilder.LOADING);
    }
    
    public EmptyViewBuilder empty() {
        return builder.setState(EmptyViewBuilder.EMPTY);
    }
    
    public EmptyViewBuilder error() {
        return builder.setState(EmptyViewBuilder.ERROR);
    }
    
    public EmptyViewBuilder error(Throwable t) {
        Error error = Error.get(t);
        return error(error);
    }
    
    public EmptyViewBuilder error(Error error) {
        return error()
            .setErrorTitle(error.getTitle(getContext()))
            .setErrorText(error.getMessage(getContext()));
    }
    
    @EmptyViewBuilder.State
    public int state() {
        return builder.state;
    }
    
    public void showContent() {
        content().show();
    }
    
    public void showLoading() {
        loading().show();
    }
    
    public void showEmpty() {
        empty().show();
    }
    
    public void showError() {
        error().show();
    }
    
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void show() {
        // start animation
        //        if (builder.transition != null) {
        //            TransitionManager.beginDelayedTransition(this, builder.transition);
        //        }
        
        switch (builder.state) {
            case EmptyViewBuilder.CONTENT:
                container.setVisibility(View.VISIBLE);
                progressBar.setVisibility(GONE);
                imageView.setVisibility(GONE);
                lottieView.cancelAnimation();
                titleView.setVisibility(GONE);
                textView.setVisibility(GONE);
                button.setVisibility(GONE);
                setChildVisibility(VISIBLE);
                
                setContainer(Color.TRANSPARENT);
                break;
            case EmptyViewBuilder.EMPTY:
                container.setVisibility(View.VISIBLE);
                progressBar.setVisibility(GONE);
                lottieView.cancelAnimation();
                imageView.setVisibility(VISIBLE);
                titleView.setVisibility(VISIBLE);
                textView.setVisibility(VISIBLE);
                button.setVisibility(VISIBLE);
                setChildVisibility(GONE);
                
                setContainer(builder.emptyBackgroundColor);
                setIcon(builder.emptyDrawable, builder.emptyDrawableTint);
                setTitle(builder.emptyTitle, builder.emptyTitleTextColor);
                setText(builder.emptyText, builder.emptyTextColor);
                //                setButton(builder.emptyButtonText, builder.emptyButtonTextColor, builder.emptyButtonBackgroundColor);
                break;
            case EmptyViewBuilder.ERROR:
                container.setVisibility(View.VISIBLE);
                progressBar.setVisibility(GONE);
                lottieView.cancelAnimation();
    
                imageView.setVisibility(VISIBLE);
                titleView.setVisibility(VISIBLE);
                textView.setVisibility(VISIBLE);
                button.setVisibility(VISIBLE);
                setChildVisibility(GONE);
                
                setContainer(builder.errorBackgroundColor);
                setIcon(builder.errorDrawable, builder.errorDrawableTint);
                setTitle(builder.errorTitle, builder.errorTitleTextColor);
                setText(builder.errorText, builder.errorTextColor);
                setButton(builder.errorButtonText, builder.errorButtonTextColor, builder.errorButtonBackgroundColor);
                break;
            case EmptyViewBuilder.LOADING:
                container.setVisibility(View.VISIBLE);
                progressBar.setVisibility(VISIBLE);
                lottieView.playAnimation();
    
                imageView.setVisibility(GONE);
                titleView.setVisibility(VISIBLE);
                textView.setVisibility(VISIBLE);
                button.setVisibility(GONE);
                setChildVisibility(GONE);
                
                setContainer(builder.loadingBackgroundColor);
                setProgressBar(builder.loading, builder.loadingDrawableTint);
                setIcon(builder.loadingDrawable, builder.loadingDrawableTint);
                setTitle(builder.loadingTitle, builder.loadingTitleTextColor);
                setText(builder.loadingText, builder.loadingTextColor);
                //                Rect rect = new Rect();
                //                ((View)getParent()).getLocalVisibleRect(rect);
                //                if (rect.bottom - rect.top > 100) {
                //                    int y = (int) ((rect.bottom - rect.top - 200 * getResources().getDisplayMetrics().density) / 2);
                //                    progressBar.setY(y < 0 ? 0 : y);
                //                }
                break;
        }
    }
    
    private void setChildVisibility(int visibility) {
        for (View view : builder.children) {
            view.setVisibility(visibility);
        }
    }
    
    private void setContainer(@ColorInt int backgroundColor) {
        container.setGravity(builder.gravity);
        container.setBackgroundColor(backgroundColor);
    }
    
    private void setProgressBar(@EmptyViewBuilder.LoadingType int style, @ColorInt int tint) {
        if (progressBar.getVisibility() != VISIBLE) {
            return;
        }
        if (style == EmptyViewBuilder.NONE) {
            progressBar.setVisibility(GONE);
            return;
        }
        progressBar.setVisibility(VISIBLE);
        //        if (tint != 0) {
        //            Drawable drawable = progressBar.getIndeterminateDrawable();
        //            if (drawable != null) {
        //                drawable.setColorFilter(tint, Mode.SRC_ATOP);
        //            }
        //        }
    }
    
    private void setIcon(Drawable drawable, @ColorInt int tint) {
        if (imageView.getVisibility() != VISIBLE) {
            return;
        }
        if (drawable == null) {
            imageView.setVisibility(GONE);
            return;
        }
        imageView.setVisibility(VISIBLE);
        imageView.setImageDrawable(drawable);
        imageView.setColorFilter(tint);
    }
    
    private void setTitle(CharSequence text, @ColorInt int textColor) {
        if (titleView.getVisibility() != VISIBLE) {
            return;
        }
        if (TextUtils.isEmpty(text)) {
            titleView.setVisibility(GONE);
            return;
        }
        titleView.setVisibility(VISIBLE);
        titleView.setText(text);
        titleView.setTextColor(textColor);
        titleView.setTypeface(builder.font);
        if (builder.titleTextSize != 0) {
            EmptyUtils.setTextSize(titleView, builder.titleTextSize);
        }
    }
    
    private void setText(CharSequence text, @ColorInt int textColor) {
        if (textView.getVisibility() != VISIBLE) {
            return;
        }
        if (TextUtils.isEmpty(text)) {
            textView.setVisibility(GONE);
            return;
        }
        textView.setVisibility(VISIBLE);
        textView.setText(text);
        textView.setTextColor(textColor);
        textView.setTypeface(builder.font);
        if (builder.textSize != 0) {
            EmptyUtils.setTextSize(textView, builder.textSize);
        }
    }
    
    private void setButton(CharSequence text,
                           @ColorInt int textColor,
                           @ColorInt int backgroundColor) {
        if (button.getVisibility() != VISIBLE) {
            return;
        }
        if (TextUtils.isEmpty(text)) {
            button.setVisibility(GONE);
            return;
        }
        button.setVisibility(VISIBLE);
        button.setText(text);
        //        button.setTextColor(textColor);
        button.setTypeface(builder.font);
        if (builder.buttonTextSize != 0) {
            EmptyUtils.setTextSize(button, builder.buttonTextSize);
        }
        button.setBackgroundColor(backgroundColor);
        //        button.setOnClickListener(builder.onClickListener);
    }
}
