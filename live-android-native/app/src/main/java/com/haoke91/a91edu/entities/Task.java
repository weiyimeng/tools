package com.haoke91.a91edu.entities;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/9/12 15:54
 */
public class Task {
    
    /**
     * agreeTime : 30
     * course : 1250
     * endTime : 1532521800000
     * keyId : 5672
     * knowledgeName : 【阅】巴金
     * startTime : 1532514600000
     * userId : 5904
     */
    
    private int agreeTime;
    private int courseId;
    private long endTime;
    private int keyId;//课次 or 作业id
    private String knowledgeName;//课次名称
    private long startTime;
    private int userId;
    private String content;//作业内容
    private String courseName;//课程名称
    
    
    public int getAgreeTime(){
        return agreeTime;
    }
    
    public void setAgreeTime(int agreeTime){
        this.agreeTime = agreeTime;
    }
    
    public int getCourseId(){
        return courseId;
    }
    
    public void setCourseId(int courseId){
        this.courseId = courseId;
    }
    
    public long getEndTime(){
        return endTime;
    }
    
    public void setEndTime(long endTime){
        this.endTime = endTime;
    }
    
    public int getKeyId(){
        return keyId;
    }
    
    public void setKeyId(int keyId){
        this.keyId = keyId;
    }
    
    public String getKnowledgeName(){
        return knowledgeName;
    }
    
    public void setKnowledgeName(String knowledgeName){
        this.knowledgeName = knowledgeName;
    }
    
    public long getStartTime(){
        return startTime;
    }
    
    public void setStartTime(long startTime){
        this.startTime = startTime;
    }
    
    public int getUserId(){
        return userId;
    }
    
    public void setUserId(int userId){
        this.userId = userId;
    }
    
    public String getContent(){
        return content;
    }
    
    public void setContent(String content){
        this.content = content;
    }
    
    public String getCourseName(){
        return courseName;
    }
    
    public void setCourseName(String courseName){
        this.courseName = courseName;
    }
}
