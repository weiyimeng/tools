package com.haoke91.videolibrary.presenter;

import com.haoke91.videolibrary.presenter.views.BaseView;
import com.orhanobut.logger.Logger;

/**
 * 项目名称：91edu
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/5/14 19:30
 */
public class BasePresenter<V extends BaseView> {
    public V mView;
    
    public BasePresenter(V view) {
        this.mView = view;
    }
    
    public void log(String log) {
        Logger.e(log);
    }
}
