package com.gaosiedu.scc.sdk.android.domain;

import java.io.Serializable;

/**
 * @author chengsa
 * @date 2018/11/7
 * @comment 直播部分-课次详情基础信息
 */
public class LiveKnowledgeBaseBean implements Serializable {

    /**
     * 课程id
     */
    private int courseId;

    /**
     * 课次id
     */
    private int knowlageId;

    /**
     * 课次名称
     */
    private String knowlageName;


    /**
     * 课次开始时间
     */
    private long ckStartTime;

    /**
     * 课次结束时间
     */
    private long ckEndTime;

    /**
     * 服务器类型
     */
    private String serverType;

    /**
     * 直播平台类型
     */
    private String livePlatform;
    /**
     * 老师id
     */
    private String teacherId;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getKnowlageId() {
        return knowlageId;
    }

    public void setKnowlageId(int knowlageId) {
        this.knowlageId = knowlageId;
    }

    public String getKnowlageName() {
        return knowlageName;
    }

    public void setKnowlageName(String knowlageName) {
        this.knowlageName = knowlageName;
    }

    public long getCkStartTime() {
        return ckStartTime;
    }

    public void setCkStartTime(long ckStartTime) {
        this.ckStartTime = ckStartTime;
    }

    public long getCkEndTime() {
        return ckEndTime;
    }

    public void setCkEndTime(long ckEndTime) {
        this.ckEndTime = ckEndTime;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public String getLivePlatform() {
        return livePlatform;
    }

    public void setLivePlatform(String livePlatform) {
        this.livePlatform = livePlatform;
    }
    
    public String getTeacherId(){
        return teacherId;
    }
    
    public void setTeacherId(String teacherId){
        this.teacherId = teacherId;
    }
}
