/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.course.change;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;

import java.util.*;


/**
 * 课程-转班课程列表 请求接口
 *
 * @author sdk-generator-android request
 * @describe
 * @date 2018/11/20 17:03
 * @since 2.1.0
 */
public class LiveCourseChangeListRequest extends LiveSdkBasePageRequest {
    
    private transient final String PATH = "course/change";
    
    public LiveCourseChangeListRequest() {
        super();
        setPath(PATH);
    }
    
    
    /**
     *
     */
    private int courseId;
    
    
    /**
     * 用户id
     */
    private int userId;
    
    
    //属性get||set方法
    public int getCourseId() {
        return this.courseId;
    }
    
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
    
    
    public int getUserId() {
        return this.userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
}
