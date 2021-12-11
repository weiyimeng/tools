package com.haoke91.baselibrary.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.haoke91.baselibrary.utils.ACallBack;
import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/11/5 4:37 PM
 * 修改人：weiyimeng
 * 修改时间：2018/11/5 4:37 PM
 * 修改备注：
 */
public class QuestionLayout extends FrameLayout {
    public QuestionLayout(@NonNull Context context) {
        super(context);
        init(context);
    }
    
    
    public QuestionLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        
    }
    
    public QuestionLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    
    private void init(Context context) {
    }
    
    private Disposable mdDisposable;
    
    /**
     * 倒计时
     *
     * @param countTime
     */
    public void startTimeCount(final long countTime, final ACallBack<Long> ACallBack) {
        mdDisposable = Flowable.intervalRange(0, countTime, 0, 1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .map(new Function<Long, Long>() {
                @Override
                public Long apply(Long aLong) throws Exception {
                    return countTime - aLong - 1;
                }
            })
            .doOnNext(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {
//                    Logger.e("aLong====" + aLong);
                    ACallBack.call(aLong);
                }
            })
            .doOnComplete(new Action() {
                @Override
                public void run() throws Exception {
                    ACallBack.call(0l);
                    
                }
            })
            .subscribe();
    }
    
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mdDisposable != null && !mdDisposable.isDisposed()) {
            mdDisposable.dispose();
        }
    }
    
    /**
     * 上传答案
     */
    public void postAnswer() {
    
    }
    
    
}
