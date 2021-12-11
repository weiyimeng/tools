package com.haoke91.baselibrary.event;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.util.HashMap;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * 项目名称：91edu
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/5/11 上午11:10
 * 修改人：weiyimeng
 * 修改时间：2018/5/11 上午11:10
 * 修改备注：
 */
public class RxBus {
    private HashMap<String, CompositeDisposable> mSubscriptionMap;
    private static volatile RxBus mRxBus;
    private final Subject<Object> mSubject;
    private CompositeDisposable compositeDisposable;
    
    
    public static RxBus getIntanceBus() {
        if (mRxBus == null) {
            synchronized (RxBus.class) {
                if (mRxBus == null) {
                    mRxBus = new RxBus();
                }
            }
        }
        return mRxBus;
    }
    
    private RxBus() {
        mSubject = PublishSubject.create().toSerialized();
    }
    
    public void post(Object o) {
        mSubject.onNext(o);
    }
    
    /**
     * 返回指定类型的带背压的Flowable实例
     *
     * @param <T>
     * @param type
     * @return
     */
    public <T> Flowable<T> getObservable(Class<T> type) {
        return mSubject.toFlowable(BackpressureStrategy.BUFFER)
            .ofType(type);
    }
    
    /**
     * 一个默认的订阅方法
     *
     * @param <T>
     * @param type
     * @param next
     * @return
     */
    public <T> Disposable doSubscribe(Class<? extends Activity> activity, Class<T> type, Consumer<T> next) {
        Disposable subscribe = getObservable(type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(next, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    Logger.e("tag", "throwable==" + throwable);
                }
            });
        addSubscription(activity, subscribe);
        return subscribe;
    }
    
    /**
     * 是否已有观察者订阅
     *
     * @return
     */
    public boolean hasObservers() {
        return mSubject.hasObservers();
    }
    
    /**
     * 保存订阅后的disposable
     *
     * @param o
     * @param disposable
     */
    public void addSubscription(Class<? extends Activity> o, Disposable disposable) {
        //        if (compositeDisposable == null) {
        //            compositeDisposable = new CompositeDisposable();
        //        }
        //        compositeDisposable.add(disposable);
        if (mSubscriptionMap == null) {
            mSubscriptionMap = new HashMap<>();
        }
        String key = o.getName();
        if (mSubscriptionMap.get(key) != null) {
            mSubscriptionMap.get(key).add(disposable);
        } else {
            //一次性容器,可以持有多个并提供 添加和移除。
            CompositeDisposable disposables = new CompositeDisposable();
            disposables.add(disposable);
            mSubscriptionMap.put(key, disposables);
        }
    }
    
    /**
     * 取消订阅
     *
     * @param
     */
    public void unSubscribe(Class<? extends Activity> o) {
        if (mSubscriptionMap == null) {
            return;
        }
        
        String key = o.getName();
        if (!mSubscriptionMap.containsKey(key)) {
            return;
        }
        if (mSubscriptionMap.get(key) != null) {
            mSubscriptionMap.get(key).dispose();
        }
        mSubscriptionMap.remove(key);
    }
}
