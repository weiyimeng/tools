package com.gaosiedu.scc.sdk.android.domain;

import java.util.Date;

/** 用户试题数据返回结果 */
public class SccUserWrongQuestionBean {

  private int id;

  /** 用户id */
  private String userId;

  /** 科目id */
  private int subjectId;

  /** 科目名称 */
  private String subjectName;

  /** 年级id */
  private int gradeId;

  /** 年级名称 */
  private String gradeName;

  /** 试题id */
  private int questionId;

  /** 错题原因 */
  private String questionWrongReason;

  /** 错题来源 */
  private String quetionSource;

  /** 用户答案 */
  private String userAnswer;
  /** 用户答案图片 */
  private String userAnswerImg;

  /** 用户作答时间 */
  private Date userAnswerTime;

  /** 是否收藏。0 ：未收藏；1：收藏 */
  private Short collect;

  /** 是否订正。0：未订正；1：订正 */
  private Short correct;

  /** 教师id */
  private String teacherId;

  /** 订正次数 */
  private int correctNum;

  /** 是否删除。0删除，1正常 */
  private Short delStatus;

  /** 类型（1：单选；2：多选；3：填空；） */
  private int questionType;

  /** 题干 */
  private String questionStem;

  /** 试题内容（json） */
  private String questionContent;

  /** 试题答案 */
  private String questionAnswer;

  /** 试题解析 */
  private String questionAnalysis;

  /** 试题来源 */
  private String questionSource;

  /** 试题创建时间 */
  private Date questionCreatetime;

  /** @return id */
  public int getId() {
    return id;
  }

  /** @param id */
  public void setId(int id) {
    this.id = id;
  }
  /**
   * 用户id
   *
   * @return user_id 用户id
   */
  public String getUserId() {
    return userId;
  }

  /**
   * 用户id
   *
   * @param userId 用户id
   */
  public void setUserId(String userId) {
    this.userId = userId == null ? null : userId.trim();
  }

  /**
   * 科目id
   *
   * @return subject_id 科目id
   */
  public int getSubjectId() {
    return subjectId;
  }

  /**
   * 科目id
   *
   * @param subjectId 科目id
   */
  public void setSubjectId(int subjectId) {
    this.subjectId = subjectId;
  }

  /**
   * 科目名称
   *
   * @return subject_name 科目名称
   */
  public String getSubjectName() {
    return subjectName;
  }

  /**
   * 科目名称
   *
   * @param subjectName 科目名称
   */
  public void setSubjectName(String subjectName) {
    this.subjectName = subjectName == null ? null : subjectName.trim();
  }

  /**
   * 年级id
   *
   * @return grade_id 年级id
   */
  public int getGradeId() {
    return gradeId;
  }

  /**
   * 年级id
   *
   * @param gradeId 年级id
   */
  public void setGradeId(int gradeId) {
    this.gradeId = gradeId;
  }

  /**
   * 年级名称
   *
   * @return grade_name 年级名称
   */
  public String getGradeName() {
    return gradeName;
  }

  /**
   * 年级名称
   *
   * @param gradeName 年级名称
   */
  public void setGradeName(String gradeName) {
    this.gradeName = gradeName == null ? null : gradeName.trim();
  }

  /**
   * 试题id
   *
   * @return question_id 试题id
   */
  public int getQuestionId() {
    return questionId;
  }

  /**
   * 试题id
   *
   * @param questionId 试题id
   */
  public void setQuestionId(int questionId) {
    this.questionId = questionId;
  }

  /**
   * 错题原因
   *
   * @return question_wrong_reason 错题原因
   */
  public String getQuestionWrongReason() {
    return questionWrongReason;
  }

  /**
   * 错题原因
   *
   * @param questionWrongReason 错题原因
   */
  public void setQuestionWrongReason(String questionWrongReason) {
    this.questionWrongReason = questionWrongReason == null ? null : questionWrongReason.trim();
  }

  /**
   * 错题来源
   *
   * @return quetion_source 错题来源
   */
  public String getQuetionSource() {
    return quetionSource;
  }

  /**
   * 错题来源
   *
   * @param quetionSource 错题来源
   */
  public void setQuetionSource(String quetionSource) {
    this.quetionSource = quetionSource == null ? null : quetionSource.trim();
  }

  /**
   * 用户答案
   *
   * @return user_answer 用户答案
   */
  public String getUserAnswer() {
    return userAnswer;
  }

  /**
   * 用户答案
   *
   * @param userAnswer 用户答案
   */
  public void setUserAnswer(String userAnswer) {
    this.userAnswer = userAnswer == null ? null : userAnswer.trim();
  }

  public String getUserAnswerImg() {
    return userAnswerImg;
  }

  public void setUserAnswerImg(String userAnswerImg) {
    this.userAnswerImg = userAnswerImg;
  }

  /**
   * 用户作答时间
   *
   * @return user_answer_time 用户作答时间
   */
  public Date getUserAnswerTime() {
    return userAnswerTime;
  }

  /**
   * 用户作答时间
   *
   * @param userAnswerTime 用户作答时间
   */
  public void setUserAnswerTime(Date userAnswerTime) {
    this.userAnswerTime = userAnswerTime;
  }

  /**
   * 是否收藏。0 ：未收藏；1：收藏
   *
   * @return collect 是否收藏。0 ：未收藏；1：收藏
   */
  public Short getCollect() {
    return collect;
  }

  /**
   * 是否收藏。0 ：未收藏；1：收藏
   *
   * @param collect 是否收藏。0 ：未收藏；1：收藏
   */
  public void setCollect(Short collect) {
    this.collect = collect;
  }

  /**
   * 是否订正。0：未订正；1：订正
   *
   * @return correct 是否订正。0：未订正；1：订正
   */
  public Short getCorrect() {
    return correct;
  }

  /**
   * 是否订正。0：未订正；1：订正
   *
   * @param correct 是否订正。0：未订正；1：订正
   */
  public void setCorrect(Short correct) {
    this.correct = correct;
  }

  /**
   * 教师id
   *
   * @return teacher_id 教师id
   */
  public String getTeacherId() {
    return teacherId;
  }

  /**
   * 教师id
   *
   * @param teacherId 教师id
   */
  public void setTeacherId(String teacherId) {
    this.teacherId = teacherId == null ? null : teacherId.trim();
  }

  /**
   * 订正次数
   *
   * @return correct_num 订正次数
   */
  public int getCorrectNum() {
    return correctNum;
  }

  /**
   * 订正次数
   *
   * @param correctNum 订正次数
   */
  public void setCorrectNum(int correctNum) {
    this.correctNum = correctNum;
  }

  /**
   * 是否删除。0删除，1正常
   *
   * @return del_status 是否删除。0删除，1正常
   */
  public Short getDelStatus() {
    return delStatus;
  }

  /**
   * 是否删除。0删除，1正常
   *
   * @param delStatus 是否删除。0删除，1正常
   */
  public void setDelStatus(Short delStatus) {
    this.delStatus = delStatus;
  }

  /**
   * 类型（1：单选；2：多选；3：填空；）
   *
   * @return question_type 类型（1：单选；2：多选；3：填空；）
   */
  public int getQuestionType() {
    return questionType;
  }

  /**
   * 类型（1：单选；2：多选；3：填空；）
   *
   * @param questionType 类型（1：单选；2：多选；3：填空；）
   */
  public void setQuestionType(int questionType) {
    this.questionType = questionType;
  }

  /**
   * 题干
   *
   * @return question_stem 题干
   */
  public String getQuestionStem() {
    return questionStem;
  }

  /**
   * 题干
   *
   * @param questionStem 题干
   */
  public void setQuestionStem(String questionStem) {
    this.questionStem = questionStem == null ? null : questionStem.trim();
  }

  /**
   * 试题内容（json）
   *
   * @return question_content 试题内容（json）
   */
  public String getQuestionContent() {
    return questionContent;
  }

  /**
   * 试题内容（json）
   *
   * @param questionContent 试题内容（json）
   */
  public void setQuestionContent(String questionContent) {
    this.questionContent = questionContent == null ? null : questionContent.trim();
  }

  /**
   * 试题答案
   *
   * @return question_answer 试题答案
   */
  public String getQuestionAnswer() {
    return questionAnswer;
  }

  /**
   * 试题答案
   *
   * @param questionAnswer 试题答案
   */
  public void setQuestionAnswer(String questionAnswer) {
    this.questionAnswer = questionAnswer == null ? null : questionAnswer.trim();
  }

  /**
   * 试题解析
   *
   * @return question_analysis 试题解析
   */
  public String getQuestionAnalysis() {
    return questionAnalysis;
  }

  /**
   * 试题解析
   *
   * @param questionAnalysis 试题解析
   */
  public void setQuestionAnalysis(String questionAnalysis) {
    this.questionAnalysis = questionAnalysis == null ? null : questionAnalysis.trim();
  }

  /**
   * 试题来源
   *
   * @return question_source 试题来源
   */
  public String getQuestionSource() {
    return questionSource;
  }

  /**
   * 试题来源
   *
   * @param questionSource 试题来源
   */
  public void setQuestionSource(String questionSource) {
    this.questionSource = questionSource == null ? null : questionSource.trim();
  }

  /**
   * 试题创建时间
   *
   * @return question_createtime 试题创建时间
   */
  public Date getQuestionCreatetime() {
    return questionCreatetime;
  }

  /**
   * 试题创建时间
   *
   * @param questionCreatetime 试题创建时间
   */
  public void setQuestionCreatetime(Date questionCreatetime) {
    this.questionCreatetime = questionCreatetime;
  }
}
