/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.course.calendar.month;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
 * @author sdk-generator-android request
 * @describe
 * @date 2018/08/31 10:00
 * @since 2.1.0
 */
public class LiveSccUserCourseCalendarMonthRequest extends LiveSdkBaseRequest {
    
    private transient final String PATH = "user/course/calendar/month";
    
    public LiveSccUserCourseCalendarMonthRequest(){
        super();
        setPath(PATH);
    }
    
    
    /**
     * 开始时间
     */
    private Long start;
    
    
    /**
     * 用户id
     */
    private String userId;
    
    
    //属性get||set方法
    
    public Long getStart(){
        return start;
    }
    
    public void setStart(Long start){
        this.start = start;
    }
    
    public String getUserId(){
        return this.userId;
    }
    
    public void setUserId(String userId){
        this.userId = userId;
    }
    
    
}
