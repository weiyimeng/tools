package com.gaosiedu.live.sdk.android.domain;

import java.io.Serializable;

public class ExerciseResourceDomain implements Serializable {
    /**
     *
     */
    private int id;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 课程练习/作业结果id
     */
    private int courseExerciseResultId;

    /**
     * 资源id,资源路径
     */
    private String resource;

    /**
     * 资源类型(wx:微信资源,original:文件服务器端存放的原始素材,result:文件服务器端存放的批改结果的素材)
     */
    private String type;

    /**
     * 状态(1:正常,-1:删除)
     */
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCourseExerciseResultId() {
        return courseExerciseResultId;
    }

    public void setCourseExerciseResultId(int courseExerciseResultId) {
        this.courseExerciseResultId = courseExerciseResultId;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
