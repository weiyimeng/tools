package gaosi.com.learn.bean.raw;

/**
 * Created by mrz on 18/5/3.
 */
public class Lesson implements ILesson{
    private String lessonId;
    private String num;//题目数量
    private String lessonName;

    private String correctionSubjectiveStar;//第三颗星 主观题星星
    private String correctionObjectiveStar;//第二课星  客观题星
    private String stars;
    private String submitStar;// 提交星  代表第一颗星，"1"代表提交过
    private String questionsCount;

    private String hwStatus;
    private int status; // 0.未开课，1.开课, 2.结课未超过7天，3.已退班未超过7天，4.结课超过7天，5.退班超过7天

    private String myselfCorrect;
    private String flag;//0不需要删除。 1需要删除重交 2已重判 3已重交
    private String englishFlag;//班型标识
    private String topicType;

    private int havaEnglishSpeech;//是否有英语语音作业(0:没有，1:有)

    private int englishQuestionsCount;//英语作业题目数量

    private int englishSpeechStar;//英语语音星

    private int havaHomeworkSpeech;//是否有课后作业

    private int haveLiveClass;//是否有直播课（0：没有，1：有）

    private int liveClassStatus;//直播课状态 0：无直播，1：未开始 2：正在直播 3：已经结束 4：敬请期待 5：直播回放

    private String liveClassInfo;//直播课信息

    private int liveRoomId;//直播间地址id

    private int haveXQuestion;//是否有定制练习(0:没有，1:有)

    private int haveAnswerCount;//课后作业已经答了多少到题

    private boolean isShow = false;//是否展开

    private int aiHomeworkCorrection;// 0 不是智能批改白名单 1 是智能批改白名单

    private int submitSource; //0 正常提交 1 智能批改提交

    private String classTypeId; //班型ID

    private int goldCoinsNum; //金币数

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getEnglishFlag() {
        return englishFlag;
    }

    public void setEnglishFlag(String englishFlag) {
        this.englishFlag = englishFlag;
    }

    public int getHavaEnglishSpeech() {
        return havaEnglishSpeech;
    }

    public void setHavaEnglishSpeech(int havaEnglishSpeech) {
        this.havaEnglishSpeech = havaEnglishSpeech;
    }

    public int getEnglishQuestionsCount() {
        return englishQuestionsCount;
    }

    public void setEnglishQuestionsCount(int englishQuestionsCount) {
        this.englishQuestionsCount = englishQuestionsCount;
    }

    public int getEnglishSpeechStar() {
        return englishSpeechStar;
    }

    public void setEnglishSpeechStar(int englishSpeechStar) {
        this.englishSpeechStar = englishSpeechStar;
    }

    public int getHavaHomeworkSpeech() {
        return havaHomeworkSpeech;
    }

    public void setHavaHomeworkSpeech(int havaHomeworkSpeech) {
        this.havaHomeworkSpeech = havaHomeworkSpeech;
    }

    public int getHaveLiveClass() {
        return haveLiveClass;
    }

    public void setHaveLiveClass(int haveLiveClass) {
        this.haveLiveClass = haveLiveClass;
    }

    public int getLiveClassStatus() {
        return liveClassStatus;
    }

    public void setLiveClassStatus(int liveClassStatus) {
        this.liveClassStatus = liveClassStatus;
    }

    public String getLiveClassInfo() {
        return liveClassInfo;
    }

    public void setLiveClassInfo(String liveClassInfo) {
        this.liveClassInfo = liveClassInfo;
    }

    public int getLiveRoomId() {
        return liveRoomId;
    }

    public void setLiveRoomId(int liveRoomId) {
        this.liveRoomId = liveRoomId;
    }

    public int getHaveXQuestion() {
        return haveXQuestion;
    }

    public void setHaveXQuestion(int haveXQuestion) {
        this.haveXQuestion = haveXQuestion;
    }

    public int getHaveAnswerCount() {
        return haveAnswerCount;
    }

    public void setHaveAnswerCount(int haveAnswerCount) {
        this.haveAnswerCount = haveAnswerCount;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getNum() {
        return num;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public String getLessonName() {
        return lessonName;
    }



    public void setCorrectionSubjectiveStar(String correctionSubjectiveStar) {
        this.correctionSubjectiveStar = correctionSubjectiveStar;
    }

    public String getCorrectionSubjectiveStar() {
        return correctionSubjectiveStar;
    }

    public void setCorrectionObjectiveStar(String correctionObjectiveStar) {
        this.correctionObjectiveStar = correctionObjectiveStar;
    }

    public String getCorrectionObjectiveStar() {
        return correctionObjectiveStar;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getStars() {
        return stars;
    }

    public void setSubmitStar(String submitStar) {
        this.submitStar = submitStar;
    }

    public String getSubmitStar() {
        return submitStar;
    }

    public void setQuestionsCount(String questionsCount) {
        this.questionsCount = questionsCount;
    }

    public String getQuestionsCount() {
        return questionsCount;
    }

    public void setHwStatus(String hwStatus) {
        this.hwStatus = hwStatus;
    }

    public String getHwStatus() {
        return hwStatus;
    }

    public void setMyselfCorrect(String myselfCorrect) {
        this.myselfCorrect = myselfCorrect;
    }

    public String getMyselfCorrect() {
        return myselfCorrect;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    /**
     *
     * @return 0不需要删除。 1需要删除重交 2已重判 3已重交
     */
    public String getFlag() {
        return flag;
    }

    public void setTopicType(String topicType) {
        this.topicType = topicType;
    }

    /**
     *
     * @return 题目类型0 有客观题也有主观题，1只有客观题 2只有主观题
     */
    public String getTopicType() {
        return topicType;
    }

    public String getClassTypeId() {
        return classTypeId;
    }

    public void setClassTypeId(String classTypeId) {
        this.classTypeId = classTypeId;
    }

    public int getSubmitSource() {
        return submitSource;
    }

    public void setSubmitSource(int submitSource) {
        this.submitSource = submitSource;
    }

    public int getAiHomeworkCorrection() {
        return aiHomeworkCorrection;
    }

    public void setAiHomeworkCorrection(int aiHomeworkCorrection) {
        this.aiHomeworkCorrection = aiHomeworkCorrection;
    }

    public int getGoldCoinsNum() {
        return goldCoinsNum;
    }

    public void setGoldCoinsNum(int goldCoinsNum) {
        this.goldCoinsNum = goldCoinsNum;
    }
}
