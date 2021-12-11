package com.haoke91.a91edu.presenter;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

import com.haoke91.a91edu.entities.CourseDetail;
import com.haoke91.a91edu.model.BaseModel;
import com.haoke91.a91edu.view.BaseView;
import com.orhanobut.logger.Logger;

import org.jetbrains.annotations.NotNull;

/**
 * 项目名称：91edu
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/5/14 19:30
 */
public class BasePresenter<T extends BaseModel, V extends BaseView> implements IPresenter {
    public T mModel;
    public V mView;
    
    public BasePresenter(T model, V view) {
        this.mModel = model;
        this.mView = view;
    }
    
    
    @Override
    public void onCreate(LifecycleOwner owner) {
    }
    
    @Override
    public void onDestroy(LifecycleOwner owner) {
    
    }
    
    @Override
    public void onLifecycleChanged(LifecycleOwner owner, Lifecycle.Event event) {
    
    }
    
    public void log(String log) {
        Log.e("play===",log);
    }
    
    public boolean isSuccess(String code) {
        return code != null && code.equalsIgnoreCase("success");
    }
}
