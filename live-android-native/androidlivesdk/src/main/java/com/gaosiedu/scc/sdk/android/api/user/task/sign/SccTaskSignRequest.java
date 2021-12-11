/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.task.sign;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
 * @author sdk-generator-android request
 * @describe
 * @date 2018/09/11 15:44
 * @since 2.1.0
 */
public class SccTaskSignRequest extends LiveSdkBaseRequest {
    
    private transient final String PATH = "user/task/sign";
    
    public SccTaskSignRequest(){
        super();
        setPath(PATH);
    }
    
    
    /**
     * async
     */
    private Boolean async;
    
    /**
     * courseId
     */
    private String courseId;
    
    /**
     * homeWorkLevel
     */
    private String homeWorkLevel;
    
    /**
     * keyId
     */
    private Integer keyId;
    
    /**
     * knowledgeId
     */
    private String knowledgeId;
    
    /**
     * type
     */
    private String type;
    
    /**
     * userId
     */
    private String userId;
    
    
    //属性get||set方法
    public Boolean getAsync(){
        return this.async;
    }
    
    public void setAsync(Boolean async){
        this.async = async;
    }
    
    public String getCourseId(){
        return this.courseId;
    }
    
    public void setCourseId(String courseId){
        this.courseId = courseId;
    }
    
    public String getHomeWorkLevel(){
        return this.homeWorkLevel;
    }
    
    public void setHomeWorkLevel(String homeWorkLevel){
        this.homeWorkLevel = homeWorkLevel;
    }
    
    public Integer getKeyId(){
        return this.keyId;
    }
    
    public void setKeyId(Integer keyId){
        this.keyId = keyId;
    }
    
    public String getKnowledgeId(){
        return this.knowledgeId;
    }
    
    public void setKnowledgeId(String knowledgeId){
        this.knowledgeId = knowledgeId;
    }
    
    public String getType(){
        return this.type;
    }
    
    public void setType(String type){
        this.type = type;
    }
    
    public String getUserId(){
        return this.userId;
    }
    
    public void setUserId(String userId){
        this.userId = userId;
    }
    
}
