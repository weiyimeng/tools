package com.haoke91.baselibrary.utils;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/12/12 19:07
 */
public interface ICallBack<T,V> {
    V onPrev(T t);
    
    void call(T t,int p);
}
