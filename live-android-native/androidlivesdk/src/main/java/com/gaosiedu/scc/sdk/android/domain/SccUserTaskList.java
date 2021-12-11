package com.gaosiedu.scc.sdk.android.domain;

import java.io.Serializable;
import java.util.Date;

public class SccUserTaskList implements Serializable {
  /** scc_user_task_list */
  private static final long serialVersionUID = 1L;
  /** */
  private Integer id;
  /** 用户ID */
  private String userId;
  /** 任务唯一标识 */
  private String uuid;
  /** 一级目录 */
  private String rootType;
  /** 任务类型 */
  private String type;
  /** */
  private String taskName;
  /** 奖励信息； */
  private String rewardInfo;
  /** 是否使用自定义的奖励配置1支持 */
  private Integer isAct;
  /** 状态：1-已完成；0-未完成 */
  private Integer status;
  /** */
  private Integer sort;
  /** 有效开始时间当天开始时间 */
  private Date startTime;
  /** 过期时间 加上任务有效时长 */
  private Date expiryTime;
  /** 任务创建时间 */
  private Date createTime;
  /** 任务条件，Json格式 */
  private String taskCondition;

  /** @return id */
  public Integer getId() {
    return id;
  }

  /** @param id */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * 用户ID
   *
   * @return user_id 用户ID
   */
  public String getUserId() {
    return userId;
  }

  /**
   * 用户ID
   *
   * @param userId 用户ID
   */
  public void setUserId(String userId) {
    this.userId = userId == null ? null : userId.trim();
  }

  /**
   * 任务唯一标识
   *
   * @return uuid 任务唯一标识
   */
  public String getUuid() {
    return uuid;
  }

  /**
   * 任务唯一标识
   *
   * @param uuid 任务唯一标识
   */
  public void setUuid(String uuid) {
    this.uuid = uuid == null ? null : uuid.trim();
  }

  /**
   * 一级目录
   *
   * @return root_type 一级目录
   */
  public String getRootType() {
    return rootType;
  }

  /**
   * 一级目录
   *
   * @param rootType 一级目录
   */
  public void setRootType(String rootType) {
    this.rootType = rootType == null ? null : rootType.trim();
  }

  /**
   * 任务类型
   *
   * @return type 任务类型
   */
  public String getType() {
    return type;
  }

  /**
   * 任务类型
   *
   * @param type 任务类型
   */
  public void setType(String type) {
    this.type = type == null ? null : type.trim();
  }

  /** @return task_name */
  public String getTaskName() {
    return taskName;
  }

  /** @param taskName */
  public void setTaskName(String taskName) {
    this.taskName = taskName == null ? null : taskName.trim();
  }

  /**
   * 奖励信息；
   *
   * @return reward_info 奖励信息；
   */
  public String getRewardInfo() {
    return rewardInfo;
  }

  /**
   * 奖励信息；
   *
   * @param rewardInfo 奖励信息；
   */
  public void setRewardInfo(String rewardInfo) {
    this.rewardInfo = rewardInfo == null ? null : rewardInfo.trim();
  }

  /**
   * 是否使用自定义的奖励配置1支持
   *
   * @return is_act 是否使用自定义的奖励配置1支持
   */
  public Integer getIsAct() {
    return isAct;
  }

  /**
   * 是否使用自定义的奖励配置1支持
   *
   * @param isAct 是否使用自定义的奖励配置1支持
   */
  public void setIsAct(Integer isAct) {
    this.isAct = isAct;
  }

  /**
   * 状态：1-已完成；0-未完成
   *
   * @return status 状态：1-已完成；0-未完成
   */
  public Integer getStatus() {
    return status;
  }

  /**
   * 状态：1-已完成；0-未完成
   *
   * @param status 状态：1-已完成；0-未完成
   */
  public void setStatus(Integer status) {
    this.status = status;
  }

  /** @return sort */
  public Integer getSort() {
    return sort;
  }

  /** @param sort */
  public void setSort(Integer sort) {
    this.sort = sort;
  }

  /**
   * 有效开始时间当天开始时间
   *
   * @return start_time 有效开始时间当天开始时间
   */
  public Date getStartTime() {
    return startTime;
  }

  /**
   * 有效开始时间当天开始时间
   *
   * @param startTime 有效开始时间当天开始时间
   */
  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  /**
   * 过期时间 加上任务有效时长
   *
   * @return expiry_time 过期时间 加上任务有效时长
   */
  public Date getExpiryTime() {
    return expiryTime;
  }

  /**
   * 过期时间 加上任务有效时长
   *
   * @param expiryTime 过期时间 加上任务有效时长
   */
  public void setExpiryTime(Date expiryTime) {
    this.expiryTime = expiryTime;
  }

  /**
   * 任务创建时间
   *
   * @return create_time 任务创建时间
   */
  public Date getCreateTime() {
    return createTime;
  }

  /**
   * 任务创建时间
   *
   * @param createTime 任务创建时间
   */
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  /**
   * 任务条件，Json格式
   *
   * @return task_condition 任务条件，Json格式
   */
  public String getTaskCondition() {
    return taskCondition;
  }

  /**
   * 任务条件，Json格式
   *
   * @param taskCondition 任务条件，Json格式
   */
  public void setTaskCondition(String taskCondition) {
    this.taskCondition = taskCondition == null ? null : taskCondition.trim();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getClass().getSimpleName());
    sb.append(" [");
    sb.append("Hash = ").append(hashCode());
    sb.append(", id=").append(id);
    sb.append(", userId=").append(userId);
    sb.append(", uuid=").append(uuid);
    sb.append(", rootType=").append(rootType);
    sb.append(", type=").append(type);
    sb.append(", taskName=").append(taskName);
    sb.append(", rewardInfo=").append(rewardInfo);
    sb.append(", isAct=").append(isAct);
    sb.append(", status=").append(status);
    sb.append(", sort=").append(sort);
    sb.append(", startTime=").append(startTime);
    sb.append(", expiryTime=").append(expiryTime);
    sb.append(", createTime=").append(createTime);
    sb.append(", taskCondition=").append(taskCondition);
    sb.append("]");
    return sb.toString();
  }
}
