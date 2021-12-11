/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.course.list;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;

import java.util.*;


/**
 * @author sdk-generator-android request
 * @describe
 * @date 2018/08/31 10:00
 * @since 2.1.0
 */
public class LiveSccUserCourseListRequest  extends LiveSdkBasePageRequest {

    private transient final String PATH = "user/course/list";

    public LiveSccUserCourseListRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 课程id
     */
    private Integer courseId;

    /**
     * 搜索的课程名称
     */
    private String courseName;

    /**
     * 结束时间
     */
    private Long end;

    /**
     * id
     */
    private Integer id;

    /**
     * 用户订单id
     */
    private Integer orderId;


    /**
     * 搜索的结束时间
     */
    private String searchEndTime;

    /**
     * 排序
     */
    private Integer searchSort;

    /**
     * 搜索的开始时间
     */
    private String searchStartTime;

    /**
     * 用户课程状态
     */
    private Integer searchStatus;

    /**
     * sortParams
     */
    private String sortParams;

    /**
     * 开始时间
     */
    private Long start;

    /**
     * 课程科目
     */
    private Integer subject;

    /**
     * 课程学期
     */
    private Integer term;

    /**
     * 获取的总条数  add lht
     */
    private Integer totalCount;

    /**
     * 用户id
     */
    private String userId;


    //属性get||set方法
    public Integer getCourseId() {
        return this.courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Long getEnd() {
        return this.end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getSearchEndTime() {
        return this.searchEndTime;
    }

    public void setSearchEndTime(String searchEndTime) {
        this.searchEndTime = searchEndTime;
    }

    public Integer getSearchSort() {
        return this.searchSort;
    }

    public void setSearchSort(Integer searchSort) {
        this.searchSort = searchSort;
    }

    public String getSearchStartTime() {
        return this.searchStartTime;
    }

    public void setSearchStartTime(String searchStartTime) {
        this.searchStartTime = searchStartTime;
    }

    public Integer getSearchStatus() {
        return this.searchStatus;
    }

    public void setSearchStatus(Integer searchStatus) {
        this.searchStatus = searchStatus;
    }

    public String getSortParams() {
        return this.sortParams;
    }

    public void setSortParams(String sortParams) {
        this.sortParams = sortParams;
    }

    public Long getStart() {
        return this.start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Integer getSubject() {
        return this.subject;
    }

    public void setSubject(Integer subject) {
        this.subject = subject;
    }

    public Integer getTerm() {
        return this.term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public Integer getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
