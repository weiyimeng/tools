package com.haoke91.a91edu.entities.player;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/11/29 17:00
 */
public class Student {
    private String userId;
    private String headUrl;//头像
    private String nickName;//昵称
    private String rank;//等级
    private String level;//段位
    private String levelName;//等级名称
    private String totalCount;//总问题数
    private String rightCount;//正确数
    private String rightRatio;//正确率
    private String averageConsume;//答题速度
    private String advanceRatio;//进步速度
    
    public String getUserId(){
        return userId;
    }
    
    public void setUserId(String userId){
        this.userId = userId;
    }
    
    public String getHeadUrl(){
        return headUrl;
    }
    
    public void setHeadUrl(String headUrl){
        this.headUrl = headUrl;
    }
    
    public String getNickName(){
        return nickName;
    }
    
    public void setNickName(String nickName){
        this.nickName = nickName;
    }
    
    public String getRank(){
        return rank;
    }
    
    public void setRank(String rank){
        this.rank = rank;
    }
    
    public String getLevel(){
        return level;
    }
    
    public void setLevel(String level){
        this.level = level;
    }
    
    public String getLevelName(){
        return levelName;
    }
    
    public void setLevelName(String levelName){
        this.levelName = levelName;
    }
    
    public String getTotalCount(){
        return totalCount;
    }
    
    public void setTotalCount(String totalCount){
        this.totalCount = totalCount;
    }
    
    public String getRightCount(){
        return rightCount;
    }
    
    public void setRightCount(String rightCount){
        this.rightCount = rightCount;
    }
    
    public String getRightRatio(){
        return rightRatio;
    }
    
    public void setRightRatio(String rightRatio){
        this.rightRatio = rightRatio;
    }
    
    public String getAverageConsume(){
        return averageConsume;
    }
    
    public void setAverageConsume(String averageConsume){
        this.averageConsume = averageConsume;
    }
    
    public String getAdvanceRatio(){
        return advanceRatio;
    }
    
    public void setAdvanceRatio(String advanceRatio){
        this.advanceRatio = advanceRatio;
    }
}
