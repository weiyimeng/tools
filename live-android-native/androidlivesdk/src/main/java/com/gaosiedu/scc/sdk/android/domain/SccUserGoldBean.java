package com.gaosiedu.scc.sdk.android.domain;


import java.util.Date;

public class SccUserGoldBean{
  /** 本课次总金币 */
  private int totalGold;

  /** 新增字段 */
  /** scc_user_gold */
  private static final long serialVersionUID = 1L;
  /** 本科次总金币 */
  private Integer partGold;
  /** 课次id */
  private String knowledgeId;
  /** */
  private Integer id;
  /** */
  private Integer gold;
  /** */
  private String userId;
  /** */
  private Date createTime;
  /** 最后更新时间 */
  private Date updateTime;
  /** 1是正常，其他是不正常 */
  private Integer status;

  /** @return id */
  public Integer getId() {
    return id;
  }

  /** @param id */
  public void setId(Integer id) {
    this.id = id;
  }

  /** @return gold */
  public Integer getGold() {
    return gold;
  }

  /** @param gold */
  public void setGold(Integer gold) {
    this.gold = gold;
  }

  /** @return user_id */
  public String getUserId() {
    return userId;
  }

  /** @param userId */
  public void setUserId(String userId) {
    this.userId = userId == null ? null : userId.trim();
  }

  /** @return create_time */
  public Date getCreateTime() {
    return createTime;
  }

  /** @param createTime */
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  /**
   * 最后更新时间
   *
   * @return update_time 最后更新时间
   */
  public Date getUpdateTime() {
    return updateTime;
  }

  /**
   * 最后更新时间
   *
   * @param updateTime 最后更新时间
   */
  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  /**
   * 1是正常，其他是不正常
   *
   * @return status 1是正常，其他是不正常
   */
  public Integer getStatus() {
    return status;
  }

  /**
   * 1是正常，其他是不正常
   *
   * @param status 1是正常，其他是不正常
   */
  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getPartGold() {
    return partGold;
  }

  public void setPartGold(Integer partGold) {
    this.partGold = partGold;
  }

  public String getKnowledgeId() {
    return knowledgeId;
  }

  public void setKnowledgeId(String knowledgeId) {
    this.knowledgeId = knowledgeId;
  }

  public int getTotalGold() {
    return totalGold;
  }

  public void setTotalGold(int totalGold) {
    this.totalGold = totalGold;
  }
}
