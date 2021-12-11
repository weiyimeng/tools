package gaosi.com.learn.bean.raw;


import com.gsbaselib.base.bean.BaseData;

/**
 * <li>description: student_studentInfo接口返回的Raw data
 * <li>author: zhengpeng
 * <li>date: 18/4/24 下午3:39
 */
public class student_studentInfoBean extends BaseData {

    /** 头像 **/
    private String avatarUrl;

    /** 金币数量 **/
    private float coinCount;

    /** 当前积分 **/
    private int medalCount;

    /** 姓名 **/
    private String name;

    /** 学校名称 **/
    private String schoolName;

    /** 性别 **/
    private int sex;

    /** 等级 **/
    private int grade;

    private String code;

    /** 是否双师 **/
    private boolean isDoubleTeacher;

    /** 我的形象 **/
    private String fashionUrl;

    /** 是否有免费抽取机会0:没有，1:有 **/
    private int freeExtractingChance;

    /** 获取下一个免费抽取装扮时间的毫秒值 **/
    private int countDown;

    /** 是否确认过性别 **/
    private boolean wheConfirmGender;

    /** 是否有学情报告入口 */
    private int isLessonReport;

    private String institutionName;

    /** 是否有2+1+X 班级 0 没有 1 有 **/
    private int hasXClass;

    /** 宝箱补偿状态(0：没有弹窗，1:已经弹过) */
    private int dropTreasure;
    /** 个人装扮上新状态(0：没有弹窗，1:已经弹过) */
    private int dropPersonaliz;

    /** 年级名称 */
    private String gradeName;

   private int logSymbol;//需要同时利用阿里云SDK,发送第二份埋点数据发送给后端，数据格式以现有格式为主，比如有请求参数

    private int teachingMaterialSwitch;//电子教材弹窗提醒开关 0-不弹窗 1-弹窗

    private int syncCourseSwitch;//寒春同步课弹框开关 0关闭 1打开

    private String syncCourseImageUrl;//寒春同步课弹框 图片url

    private String syncCourseUrl;//寒春同步课弹框 同步课url

    private int multiUser; //是否多学员 0 否 1 是

    private String diamond; //钻石

    private String studentScore;//学生金币

    private String level;//学生等级

    private int tol;//是否tol学员(1-是，0-否)

    private String gsId;//gsId

    public boolean isTol() {
        return tol == 1;
    }

    public String getGsId() {
        return gsId;
    }

    public int getMultiUser() {
        return multiUser;
    }

    public void setMultiUser(int multiUser) {
        this.multiUser = multiUser;
    }

    public String getDiamond() {
        return diamond;
    }

    public void setDiamond(String diamond) {
        this.diamond = diamond;
    }

    public String getStudentScore() {
        return studentScore;
    }

    public void setStudentScore(String studentScore) {
        this.studentScore = studentScore;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public int getLogSymbol() {
        return logSymbol;
    }

    public void setLogSymbol(int logSymbol) {
        this.logSymbol = logSymbol;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public float getCoinCount() {
        return coinCount < 0 ? 0 : coinCount;
    }

    public void setCoinCount(float coinCount) {
        this.coinCount = coinCount;
    }

    public int getMedalCount() {
        return medalCount;
    }

    public void setMedalCount(int medalCount) {
        this.medalCount = medalCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isDoubleTeacher() {
        return isDoubleTeacher;
    }

    public void setDoubleTeacher(boolean doubleTeacher) {
        isDoubleTeacher = doubleTeacher;
    }

    public String getFashionUrl() {
        return fashionUrl;
    }

    public void setFashionUrl(String fashionUrl) {
        this.fashionUrl = fashionUrl;
    }

    public int getFreeExtractingChance() {
        return freeExtractingChance;
    }

    public void setFreeExtractingChance(int freeExtractingChance) {
        this.freeExtractingChance = freeExtractingChance;
    }

    public int getCountDown() {
        return countDown;
    }

    public void setCountDown(int countDown) {
        this.countDown = countDown;
    }

    public boolean isWheConfirmGender() {
        return wheConfirmGender;
    }

    public void setWheConfirmGender(boolean wheConfirmGender) {
        this.wheConfirmGender = wheConfirmGender;
    }

    public int getIsLessonReport() {
        return isLessonReport;
    }

    public void setIsLessonReport(int isLessonReport) {
        this.isLessonReport = isLessonReport;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public int getDropTreasure() {
        return dropTreasure;
    }

    public void setDropTreasure(int dropTreasure) {
        this.dropTreasure = dropTreasure;
    }

    public int getDropPersonaliz() {
        return dropPersonaliz;
    }

    public void setDropPersonaliz(int dropPersonaliz) {
        this.dropPersonaliz = dropPersonaliz;
    }

    public void setHasXClass(int hasXClass) {
        this.hasXClass = hasXClass;
    }

    public int getHasXClass() {
        return hasXClass;
    }

    public int getTeachingMaterialSwitch() {
        return teachingMaterialSwitch;
    }

    public void setTeachingMaterialSwitch(int teachingMaterialSwitch) {
        this.teachingMaterialSwitch = teachingMaterialSwitch;
    }

    public void setSyncCourseSwitch(int syncCourseSwitch) {
        this.syncCourseSwitch = syncCourseSwitch;
    }

    public int getSyncCourseSwitch() {
        return syncCourseSwitch;
    }

    public void setSyncCourseImageUrl(String syncCourseImageUrl) {
        this.syncCourseImageUrl = syncCourseImageUrl;
    }

    public String getSyncCourseImageUrl() {
        return syncCourseImageUrl;
    }

    public void setSyncCourseUrl(String syncCourseUrl) {
        this.syncCourseUrl = syncCourseUrl;
    }

    public String getSyncCourseUrl() {
        return syncCourseUrl;
    }
}
