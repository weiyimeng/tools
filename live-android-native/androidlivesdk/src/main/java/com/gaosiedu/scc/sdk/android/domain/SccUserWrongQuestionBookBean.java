package com.gaosiedu.scc.sdk.android.domain;

/**
 * Created by wangjia on 2018/8/17.
 */
public class SccUserWrongQuestionBookBean {

    /**
     * 科目id
     */
    private int subjectId;

    /**
     * 科目名称
     */
    private String subjectName;
    /**
     * 该科目下的错题数
     */
    private int wrongQuestionsNum;
    /**
     * 该科目下的错题订正数
     */
    private int correctNum;

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getWrongQuestionsNum() {
        return wrongQuestionsNum;
    }

    public void setWrongQuestionsNum(int wrongQuestionsNum) {
        this.wrongQuestionsNum = wrongQuestionsNum;
    }

    public int getCorrectNum() {
        return correctNum;
    }

    public void setCorrectNum(int correctNum) {
        this.correctNum = correctNum;
    }
}
