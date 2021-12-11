package com.haoke91.a91edu.entities;

import com.gaosiedu.live.sdk.android.domain.CommonStatus;

import java.math.BigDecimal;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/8/1 下午7:04
 * 修改人：weiyimeng
 * 修改时间：2018/8/1 下午7:04
 * 修改备注：
 */
public class MulitOrderBean<T extends CommonStatus> {
    public static final int head = 1;
    public static final int body = 2;
    public static final int tail = 3;
    
    public MulitOrderBean(int type) {
        this.type = type;
    }
    
    private int type;
    private T date;
    
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public T getDate() {
        return date;
    }
    
    public void setDate(T date) {
        this.date = date;
    }
    
    
    public static class OrderHead extends CommonStatus {
        //        public String orderNo;
        //        public int status;
    }
    
    public static class OrderTail extends CommonStatus {
        //        public String orderNo;
        //        public int status;
        
        public BigDecimal money;
    }
    
}
