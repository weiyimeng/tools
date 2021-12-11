package com.gaosiedu.scc.sdk.android.domain;

import java.io.Serializable;
import java.util.Date;

public class SccUserWrongQuestionCorrectBean implements Serializable {
    /**
     *
     */
    private int id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 问题id
     */
    private int questionId;

    /**
     * 错题本id
     */
    private int userQuestionId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 订正答案
     */
    private String correctAnswer;

    /**
     * 解题步骤图片路径
     */
    private String correctProcessImg;

    /**
     * 订正次数.从1开始。
     */
    private int correctNum;

    /**
     * 讲评状态。1：请求讲评；2：等待讲评;3：教师已讲评
     */
    private int commentStatus;

    /**
     * 教师讲评时间
     */
    private Date commentTime;

    /**
     * 讲评教师id
     */
    private String commentTeacherId;

    /**
     * 讲评内容
     */
    private String commentContent;

    /**
     * scc_user_wrong_question_correct
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 用户id
     * @return user_id 用户id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 用户id
     * @param userId 用户id
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * 问题id
     * @return question_id 问题id
     */
    public int getQuestionId() {
        return questionId;
    }

    /**
     * 问题id
     * @param questionId 问题id
     */
    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    /**
     * 错题本id
     * @return user_question_id 错题本id
     */
    public int getUserQuestionId() {
        return userQuestionId;
    }

    /**
     * 错题本id
     * @param userQuestionId 错题本id
     */
    public void setUserQuestionId(int userQuestionId) {
        this.userQuestionId = userQuestionId;
    }

    /**
     * 创建时间
     * @return create_time 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 订正答案
     * @return correct_answer 订正答案
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * 订正答案
     * @param correctAnswer 订正答案
     */
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer == null ? null : correctAnswer.trim();
    }

    /**
     * 解题步骤图片路径
     * @return correctProcessImg 解题步骤图片路径
     */
    public String getCorrectProcessImg() {
        return correctProcessImg;
    }

    /**
     * 解题步骤图片路径
     * @param correctProcessImg 解题步骤图片路径
     */
    public void setCorrectProcessImg(String correctProcessImg) {
        this.correctProcessImg = correctProcessImg == null ? null : correctProcessImg.trim();
    }

    /**
     * 订正次数.从1开始。
     * @return correct_num 订正次数.从1开始。
     */
    public int getCorrectNum() {
        return correctNum;
    }

    /**
     * 订正次数.从1开始。
     * @param correctNum 订正次数.从1开始。
     */
    public void setCorrectNum(int correctNum) {
        this.correctNum = correctNum;
    }

    /**
     * 讲评状态。1：请求讲评；2：等待讲评;3：教师已讲评
     * @return comment_status 讲评状态。1：请求讲评；2：等待讲评;3：教师已讲评
     */
    public int getCommentStatus() {
        return commentStatus;
    }

    /**
     * 讲评状态。1：请求讲评；2：等待讲评;3：教师已讲评
     * @param commentStatus 讲评状态。1：请求讲评；2：等待讲评;3：教师已讲评
     */
    public void setCommentStatus(int commentStatus) {
        this.commentStatus = commentStatus;
    }

    /**
     * 教师讲评时间
     * @return comment_time 教师讲评时间
     */
    public Date getCommentTime() {
        return commentTime;
    }

    /**
     * 教师讲评时间
     * @param commentTime 教师讲评时间
     */
    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    /**
     * 讲评教师id
     * @return comment_teacher_id 讲评教师id
     */
    public String getCommentTeacherId() {
        return commentTeacherId;
    }

    /**
     * 讲评教师id
     * @param commentTeacherId 讲评教师id
     */
    public void setCommentTeacherId(String commentTeacherId) {
        this.commentTeacherId = commentTeacherId == null ? null : commentTeacherId.trim();
    }

    /**
     * 讲评内容
     * @return comment_content 讲评内容
     */
    public String getCommentContent() {
        return commentContent;
    }

    /**
     * 讲评内容
     * @param commentContent 讲评内容
     */
    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent == null ? null : commentContent.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", questionId=").append(questionId);
        sb.append(", userQuestionId=").append(userQuestionId);
        sb.append(", createTime=").append(createTime);
        sb.append(", correctAnswer=").append(correctAnswer);
        sb.append(", correctNum=").append(correctNum);
        sb.append(", commentStatus=").append(commentStatus);
        sb.append(", commentTime=").append(commentTime);
        sb.append(", commentTeacherId=").append(commentTeacherId);
        sb.append(", commentContent=").append(commentContent);
        sb.append("]");
        return sb.toString();
    }
}
