package com.haoke91.videolibrary.videoplayer;

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/10/29 下午3:14
 * 修改人：weiyimeng
 * 修改时间：2018/10/29 下午3:14
 * 修改备注：
 */
public interface LiveMessageCallBack {
    void onSendMessage(String message);
    
    void onDanmuToggle();
    
    void onPraise();
    
    void onFlower();
}
