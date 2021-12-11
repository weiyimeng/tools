package com.gaosiedu.live.sdk.android.domain;


import java.io.Serializable;
import java.util.Date;

/**
 * @author lihongtian
 * @date 2018/9/29
 * @comment ->  OrderDeliverDomain
 */
public class OrderDeliverDomain implements Serializable{
//    @ApiModelProperty("快递单号")
    private String expressNo;
//    @ApiModelProperty("快递公司名称")
    private String expressName;
//    @ApiModelProperty("发货时间")
    private Date createTime;
    
    public String getExpressNo(){
        return expressNo;
    }
    
    public void setExpressNo(String expressNo){
        this.expressNo = expressNo;
    }
    
    public String getExpressName(){
        return expressName;
    }
    
    public void setExpressName(String expressName){
        this.expressName = expressName;
    }
    
    public Date getCreateTime(){
        return createTime;
    }
    
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }
}
