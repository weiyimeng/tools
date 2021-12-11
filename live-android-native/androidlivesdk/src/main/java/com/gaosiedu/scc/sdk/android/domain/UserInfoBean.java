package com.gaosiedu.scc.sdk.android.domain;

import java.io.Serializable;
import java.util.Map;

/** 返回结果 */
public class UserInfoBean implements Serializable {
  //    sul.user_id  userId,
  //    sul.level_name levelName,
  //    sul.level,
  //    sul.rank,
  //    sul.season,
  //    sul.head_img headImg,
  //    sul.progress_value progressValue,
  //    SUM(scr.gold_change) partGold,
  //    SUM(slr.point) partProgress

  private String userId;

  private String knowledgeId;

  private String levelName;

  private Integer level;

  private String rank;

  private Integer season;

  private String headImg;

  private Integer progressValue;

  private Integer partGold;

  private Integer partProgress;

  private Integer getGold;

  private Integer getProgress;
  /** 回答正确1 */
  private String answerFlag;
  /** 人数统计 */
  private Map<String, Object> RightCount;

  /**
   * 作业id，等第三方id
   *
   * @return
   */
  private String keyId;

  public String getKeyId() {
    return keyId;
  }

  public void setKeyId(String keyId) {
    this.keyId = keyId;
  }

  public String getAnswerFlag() {
    return answerFlag;
  }

  public void setAnswerFlag(String answerFlag) {
    this.answerFlag = answerFlag;
  }

  public Map<String, Object> getRightCount() {
    return RightCount;
  }

  public void setRightCount(Map<String, Object> rightCount) {
    RightCount = rightCount;
  }

  public Integer getGetProgress() {
    return getProgress;
  }

  public void setGetProgress(Integer getProgress) {
    this.getProgress = getProgress;
  }

  public Integer getGetGold() {

    return getGold;
  }

  public void setGetGold(Integer getGold) {
    this.getGold = getGold;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getKnowledgeId() {
    return knowledgeId;
  }

  public void setKnowledgeId(String knowledgeId) {
    this.knowledgeId = knowledgeId;
  }

  public String getLevelName() {
    return levelName;
  }

  public void setLevelName(String levelName) {
    this.levelName = levelName;
  }

  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  public String getRank() {
    return rank;
  }

  public void setRank(String rank) {
    this.rank = rank;
  }

  public Integer getSeason() {
    return season;
  }

  public void setSeason(Integer season) {
    this.season = season;
  }

  public String getHeadImg() {
    return headImg;
  }

  public void setHeadImg(String headImg) {
    this.headImg = headImg;
  }

  public Integer getProgressValue() {
    return progressValue;
  }

  public void setProgressValue(Integer progressValue) {
    this.progressValue = progressValue;
  }

  public Integer getPartGold() {
    return partGold;
  }

  public void setPartGold(Integer partGold) {
    this.partGold = partGold;
  }

  public Integer getPartProgress() {
    return partProgress;
  }

  public void setPartProgress(Integer partProgress) {
    this.partProgress = partProgress;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getClass().getSimpleName());
    sb.append(" [");
    sb.append("Hash = ").append(hashCode());
    sb.append(", userId=").append(userId);
    sb.append(", knowledgeId=").append(knowledgeId);
    sb.append(", levelName=").append(levelName);
    sb.append(", level=").append(level);
    sb.append(", rank=").append(rank);
    sb.append(", season=").append(season);
    sb.append(", headImg=").append(headImg);
    sb.append(", progressValue=").append(progressValue);
    sb.append(", partGold=").append(partGold);
    sb.append(", partProgress=").append(partProgress);
    sb.append(", getGold=").append(getGold);
    sb.append(", getProgress=").append(getProgress);
    sb.append(", answerFlag=").append(answerFlag);
    sb.append("]");
    return sb.toString();
  }
}
