package com.gaosiedu.scc.sdk.android.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class SccLiveRoomExtdataConfigWithBLOBs implements Serializable {
  /** scc_live_room_extdata_config */
  private static final long serialVersionUID = 1L;
  /** 题干 */
  private String trunk;
  /** 选项 */
  private String chooses;

  /** 删除题板时的ids */
  private String ids;
  /** 删除题板时id的集合 */
  private List<String> listIds;
  /** 删除题板时的lmcids */
  private String lmcIds;
  /** 删除题板时lmcid的集合 */
  private List<String> listLmcIds;
  /** 题板所消耗成长值 */
  private Integer totalProgress;
  /** */
  private Integer id;
  /** */
  private String knowledgeId;
  /** 题目类型 */
  private String type;
  /** 困难程度 */
  private String difficulty;
  /** 问题 */
  private String answer;
  /** 状态 */
  private String status;
  /** */
  private Date createTime;
  /** 兼容老版lmc,对应习题在lmc的id */
  private Integer lmcId;
  /** 习题发送次数 */
  private Integer sendTimes;

  /**
   * 题干
   *
   * @return trunk 题干
   */
  public String getTrunk() {
    return trunk;
  }

  /**
   * 题干
   *
   * @param trunk 题干
   */
  public void setTrunk(String trunk) {
    this.trunk = trunk == null ? null : trunk.trim();
  }

  /**
   * 选项
   *
   * @return chooses 选项
   */
  public String getChooses() {
    return chooses;
  }

  /**
   * 选项
   *
   * @param chooses 选项
   */
  public void setChooses(String chooses) {
    this.chooses = chooses == null ? null : chooses.trim();
  }

  /** @return id */
  public Integer getId() {
    return id;
  }

  /** @param id */
  public void setId(Integer id) {
    this.id = id;
  }

  /** @return knowledge_id */
  public String getKnowledgeId() {
    return knowledgeId;
  }

  /** @param knowledgeId */
  public void setKnowledgeId(String knowledgeId) {
    this.knowledgeId = knowledgeId == null ? null : knowledgeId.trim();
  }

  public String getIds() {
    return ids;
  }

  public void setIds(String ids) {
    this.ids = ids;
  }

  public List<String> getListIds() {
    return listIds;
  }

  public void setListIds(List<String> listIds) {
    this.listIds = listIds;
  }

  public String getLmcIds() {
    return lmcIds;
  }

  public void setLmcIds(String lmcIds) {
    this.lmcIds = lmcIds;
  }

  public List<String> getListLmcIds() {
    return listLmcIds;
  }

  public void setListLmcIds(List<String> listLmcIds) {
    this.listLmcIds = listLmcIds;
  }

  public Integer getTotalProgress() {
    return totalProgress;
  }

  public void setTotalProgress(Integer totalProgress) {
    this.totalProgress = totalProgress;
  }

  /**
   * 题目类型
   *
   * @return type 题目类型
   */
  public String getType() {
    return type;
  }

  /**
   * 题目类型
   *
   * @param type 题目类型
   */
  public void setType(String type) {
    this.type = type == null ? null : type.trim();
  }

  /**
   * 困难程度
   *
   * @return difficulty 困难程度
   */
  public String getDifficulty() {
    return difficulty;
  }

  /**
   * 困难程度
   *
   * @param difficulty 困难程度
   */
  public void setDifficulty(String difficulty) {
    this.difficulty = difficulty == null ? null : difficulty.trim();
  }

  /**
   * 问题
   *
   * @return answer 问题
   */
  public String getAnswer() {
    return answer;
  }

  /**
   * 问题
   *
   * @param answer 问题
   */
  public void setAnswer(String answer) {
    this.answer = answer == null ? null : answer.trim();
  }

  /**
   * 状态
   *
   * @return status 状态
   */
  public String getStatus() {
    return status;
  }

  /**
   * 状态
   *
   * @param status 状态
   */
  public void setStatus(String status) {
    this.status = status == null ? null : status.trim();
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
   * 兼容老版lmc,对应习题在lmc的id
   *
   * @return lmc_id 兼容老版lmc,对应习题在lmc的id
   */
  public Integer getLmcId() {
    return lmcId;
  }

  /**
   * 兼容老版lmc,对应习题在lmc的id
   *
   * @param lmcId 兼容老版lmc,对应习题在lmc的id
   */
  public void setLmcId(Integer lmcId) {
    this.lmcId = lmcId;
  }

  /**
   * 习题发送次数
   *
   * @return send_times 习题发送次数
   */
  public Integer getSendTimes() {
    return sendTimes;
  }

  /**
   * 习题发送次数
   *
   * @param sendTimes 习题发送次数
   */
  public void setSendTimes(Integer sendTimes) {
    this.sendTimes = sendTimes;
  }

}
