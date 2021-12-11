package com.haoke91.a91edu.entities;

/**
 * 项目名称：91edu
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/5/15 13:57
 */
public class IRoomUser{
    
    private String teacherPwd;
    private String isLive;
    private String studentPwd;
    private String userId;
    private String userName;
    private String livePalatform;
    private String playPath;
    private String serial;
    private String userType;
    
    public String getTeacherPwd() {
        return teacherPwd;
    }
    
    public void setTeacherPwd(String teacherPwd) {
        this.teacherPwd = teacherPwd;
    }
    
    public String getIsLive() {
        return isLive;
    }
    
    public void setIsLive(String isLive) {
        this.isLive = isLive;
    }
    
    public String getStudentPwd() {
        return studentPwd;
    }
    
    public void setStudentPwd(String studentPwd) {
        this.studentPwd = studentPwd;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getLivePalatform() {
        return livePalatform;
    }
    
    public void setLivePalatform(String livePalatform) {
        this.livePalatform = livePalatform;
    }
    
    public String getPlayPath() {
        return playPath;
    }
    
    public void setPlayPath(String playPath) {
        this.playPath = playPath;
    }
    
    public String getSerial() {
        return serial;
    }
    
    public void setSerial(String serial) {
        this.serial = serial;
    }
    
    public String getUserType() {
        return userType;
    }
    
    public void setUserType(String userType) {
        this.userType = userType;
    }
}
