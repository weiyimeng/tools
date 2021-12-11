/*
 * Copyright (c) 2016,gaosiedu.com
 */
package com.gaosiedu.live.sdk.android.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangjunle
 * @describe
 * @date 2018/1/4 18:02
 * @since 2.1.0
 */
public class ContentDomain implements Serializable {
    /**
     *
     */
    private int id;

    /**
     * 内容名称（如:首页收心课广告），作为前端展现的字段和编辑内容是查找识别的依据
     */
    private String name;

    /**
     * 关联图片路径
     */
    private String img;

    /**
     * 关联链接地址
     */
    private String url;

    /**
     * 内容类型（1：广告，2：动态板块，3：推荐课程......）
     */
    private int type;

    /**
     * 状态（-1：已被逻辑删除前后端不展示，1：正常，2：暂停使用【前端不可见，而后端可见】)
     */
    private int status;

    /**
     * 可用平台（用平台缩写表示中间以“,”分割，如：pc,mobile，app，app-ios，app-android）
     */
    private String platform;

    /**
     * 开始使用时间，用于配置自动发布，如果为null表示即刻开始
     */
    private Date startTime;

    /**
     * 结束时间，用于控制自动下架，如为null表示永久不下架
     */
    private Date endTime;

    /**
     * 在同一类型中的排序，升序排序
     */
    private int sort;

    /**
     * 内容标签
     */
    private String flag;

    /**
     * 删除标识（1,未删除，0删除）
     */
    private int deleteFlag;


    /**
     * 热门课程中的关联课程id 以  ， 分开
     */
    private String courseId;

    /**
     * 课程的所属位置，该属性是content_location中的id相互对应
     */
    private int location;

    /**
     * 跳转位置
     */
    private Integer targetType;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public Integer getTargetType() {
        return targetType;
    }

    public void setTargetType(Integer targetType) {
        this.targetType = targetType;
    }
}
