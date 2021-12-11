package com.haoke91.a91edu.entities;

import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.haoke91.im.mqtt.entities.Message;

import java.util.List;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/12/5 19:46
 */
public class GetImHistoryResponse extends ResponseResult {
    private List<Message> data;
    
    public List<Message> getData(){
        return data;
    }
    
    public void setData(List<Message> data){
        this.data = data;
    }
}
