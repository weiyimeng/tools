/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.course.transfer.list;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;

import java.util.*;


/**
 * 课程-调课列表 请求接口
 *
 * @author sdk-generator-android request
 * @describe
 * @date 2018/11/09 15:38
 * @since 2.1.0
 */
public class LiveCourseTransferListRequest extends LiveSdkBasePageRequest {
    
    private transient final String PATH = "course/transfer";
    
    public LiveCourseTransferListRequest() {
        super();
        setPath(PATH);
    }
    
    
    /**
     *
     */
    private Integer courseId;
    
    /**
     * sortParams
     */
    private String sortParams;
    
    /**
     *
     */
    private Integer userId;
    
    private int displayOrder;
    
    public int getDisplayOrder() {
        return displayOrder;
    }
    
    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    //属性get||set方法
    public Integer getCourseId() {
        return this.courseId;
    }
    
    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
    
    public String getSortParams() {
        return this.sortParams;
    }
    
    public void setSortParams(String sortParams) {
        this.sortParams = sortParams;
    }
    
    public Integer getUserId() {
        return this.userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
}
