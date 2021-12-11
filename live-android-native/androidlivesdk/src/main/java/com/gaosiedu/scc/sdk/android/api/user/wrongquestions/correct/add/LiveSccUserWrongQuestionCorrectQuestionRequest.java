package com.gaosiedu.scc.sdk.android.api.user.wrongquestions.correct.add;


import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;


/**
 * @author sdk-generator-android request
 * @describe
 * @date 2018/08/28 19:15
 * @since 2.1.0
 */
public class LiveSccUserWrongQuestionCorrectQuestionRequest  extends LiveSdkBaseRequest {

    private final String PATH = "user/wrongquestions/correct/add";

    public LiveSccUserWrongQuestionCorrectQuestionRequest() {
        super();
        setPath(PATH);
    }

    /**
     * 订正错题的图片路径
     */
    private String correctProcessImgPath;

    /**
     * 订正错题的答案
     */
    private String correctQuestionAnswer;

    /**
     * userId
     */
    private String userId;

    /**
     * 用户错题id
     */
    private int userWrongQuestionId;


    //属性get||set方法

    public String getCorrectProcessImgPath() {
        return correctProcessImgPath;
    }

    public void setCorrectProcessImgPath(String correctProcessImgPath) {
        this.correctProcessImgPath = correctProcessImgPath;
    }

    public String getCorrectQuestionAnswer() {
        return correctQuestionAnswer;
    }

    public void setCorrectQuestionAnswer(String correctQuestionAnswer) {
        this.correctQuestionAnswer = correctQuestionAnswer;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getUserWrongQuestionId() {
        return userWrongQuestionId;
    }

    public void setUserWrongQuestionId(int userWrongQuestionId) {
        this.userWrongQuestionId = userWrongQuestionId;
    }
}