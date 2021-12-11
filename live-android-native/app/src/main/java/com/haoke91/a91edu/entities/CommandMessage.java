package com.haoke91.a91edu.entities;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/12/5 19:50
 */
public class CommandMessage {
    
    /**
     * time : 123456
     * sendUserId : 12119
     * sendRoleId : 12
     * receiveUserId :
     * receiveSubgroupId :
     * type :
     * value :
     * flag :
     * expiryTime : 123465
     * arriveTime : 1321546
     * cmdData : 1001
     */
    
    private long time;
    private String sendUserId;
    private String sendRoleId;
    private String receiveUserId;
    private String receiveSubgroupId;
    private String type;
    private String value;
    private String flag;
    private long expiryTime;
    private long arriveTime;
    private String cmdData;
    
    public long getTime(){
        return time;
    }
    
    public void setTime(long time){
        this.time = time;
    }
    
    public String getSendUserId(){
        return sendUserId;
    }
    
    public void setSendUserId(String sendUserId){
        this.sendUserId = sendUserId;
    }
    
    public String getSendRoleId(){
        return sendRoleId;
    }
    
    public void setSendRoleId(String sendRoleId){
        this.sendRoleId = sendRoleId;
    }
    
    public String getReceiveUserId(){
        return receiveUserId;
    }
    
    public void setReceiveUserId(String receiveUserId){
        this.receiveUserId = receiveUserId;
    }
    
    public String getReceiveSubgroupId(){
        return receiveSubgroupId;
    }
    
    public void setReceiveSubgroupId(String receiveSubgroupId){
        this.receiveSubgroupId = receiveSubgroupId;
    }
    
    public String getType(){
        return type;
    }
    
    public void setType(String type){
        this.type = type;
    }
    
    public String getValue(){
        return value;
    }
    
    public void setValue(String value){
        this.value = value;
    }
    
    public String getFlag(){
        return flag;
    }
    
    public void setFlag(String flag){
        this.flag = flag;
    }
    
    public long getExpiryTime(){
        return expiryTime;
    }
    
    public void setExpiryTime(long expiryTime){
        this.expiryTime = expiryTime;
    }
    
    public long getArriveTime(){
        return arriveTime;
    }
    
    public void setArriveTime(long arriveTime){
        this.arriveTime = arriveTime;
    }
    
    public String getCmdData(){
        return cmdData;
    }
    
    public void setCmdData(String cmdData){
        this.cmdData = cmdData;
    }
}
