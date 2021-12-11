/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.exercise.list;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;

import java.util.*;


/**
 * @author sdk-generator-android request
 * @describe
 * @date 2018/08/30 17:53
 * @since 2.1.0
 */
public class LiveSccUserExerciseListRequest extends LiveSdkBasePageRequest {
    
    public static final String TAG_UNSUBMITED = "unsubmitted";//未提交
    public static final String TAG_CORRECTING = "correcting";//批改中
    public static final String TAG_CORRECTED = "corrected";//已批改
    
    private transient final String PATH = "user/exercise/list";
    
    public LiveSccUserExerciseListRequest(){
        super();
        setPath(PATH);
    }
    
    
    /**
     * 课程名称
     */
    private String courseName;
    
    /**
     * 作业id
     */
    private Integer exerciseId;
    
    /**
     * 作业结果id
     */
    private Integer exerciseResultId;
    
    /**
     * 课次名称
     */
    private String knoledgeName;
    
    /**
     * 作业名称
     */
    private String name;
    
    /**
     * 资源列表
     */
    private List resourceList;
    
    /**
     * sortParams
     */
    private String sortParams;
    
    /**
     * unsubmitted：未提交，correcting：批改中，corrected：已批该
     */
    private String status;
    
    /**
     * 用户id
     */
    private String userId;
    
    
    //属性get||set方法
    public String getCourseName(){
        return this.courseName;
    }
    
    public void setCourseName(String courseName){
        this.courseName = courseName;
    }
    
    public Integer getExerciseId(){
        return this.exerciseId;
    }
    
    public void setExerciseId(Integer exerciseId){
        this.exerciseId = exerciseId;
    }
    
    public Integer getExerciseResultId(){
        return this.exerciseResultId;
    }
    
    public void setExerciseResultId(Integer exerciseResultId){
        this.exerciseResultId = exerciseResultId;
    }
    
    public String getKnoledgeName(){
        return this.knoledgeName;
    }
    
    public void setKnoledgeName(String knoledgeName){
        this.knoledgeName = knoledgeName;
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public List getResourceList(){
        return this.resourceList;
    }
    
    public void setResourceList(List resourceList){
        this.resourceList = resourceList;
    }
    
    public String getSortParams(){
        return this.sortParams;
    }
    
    public void setSortParams(String sortParams){
        this.sortParams = sortParams;
    }
    
    public String getStatus(){
        return this.status;
    }
    
    public void setStatus(String status){
        this.status = status;
    }
    
    public String getUserId(){
        return this.userId;
    }
    
    public void setUserId(String userId){
        this.userId = userId;
    }
    
}
