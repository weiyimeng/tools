package com.haoke91.a91edu.presenter;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

import org.jetbrains.annotations.NotNull;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/6/1 下午7:37
 * 修改人：weiyimeng
 * 修改时间：2018/6/1 下午7:37
 * 修改备注：
 */
public interface IPresenter extends LifecycleObserver {
    
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate( LifecycleOwner owner);
    
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy( LifecycleOwner owner);
    
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    void onLifecycleChanged( LifecycleOwner owner,  Lifecycle.Event event);
}
