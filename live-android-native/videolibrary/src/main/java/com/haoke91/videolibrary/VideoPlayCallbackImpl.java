package com.haoke91.videolibrary;

import android.view.View;

import com.haoke91.videolibrary.model.VodRspData;

/**
 * 项目名称：91edu
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/5/14 上午10:19
 * 修改人：weiyimeng
 * 修改时间：2018/5/14 上午10:19
 * 修改备注：
 */
public interface VideoPlayCallbackImpl {
    void onBeginPlay();
    
    void onPlayFinish();
    
    void onBack();
    
    void onSwitchPageType();
    
    /**
     * 其他可能的点击事件
     *
     * @param view
     */
    void onClick(View view);
    
}
