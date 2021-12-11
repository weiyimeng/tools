package gaosi.com.learn.application

/**
 * 作者：created by 逢二进一 on 2019/12/23 13:54
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
object AppApi {

    /**
     * 通过UserId 获取用户信息表
     */
    const val getUserInfoByUserId = "student/getUserInfoByUserId"

    /**
     * 扫码交作业参数
     */
    const val getQR_HomeworkInfo = "homework/qrHomeworkInfo"

    /**
     * 金币商城列表
     */
    const val goldMallList = "coinImageDoc/listCoinImageByStudentId"

    /**
     * 购买金币头像
     */
    const val buyCoinAvatar = "coinImageDoc/buyCoinImageLog"

    /**
     * 设置金币头像
     */
    const val setCoinAvatar = "coinImageDoc/setCoinImageDocAsImage"

    /**
     * 获取电子教材
     */
    const val teachingMaterial= "teachingmaterial/info"

    /**
     * 登录页面配置
     */
    const val loginPageConfig = "anonymous/loginpage/config"

    /**
     * 注册页面获取手机号验证码
     */
    const val registerGetValidateCode = "student/register/getValidateCode"

    /**
     * 完善信息页面保存
     */
    const val registerSave = "student/register/save"

    /**
     * 获取直播间url
     */
    const val getDtLive = "lessoninfo/getDtLive"

    /**
     * 获取时钟
     */
    const val getDtPlayPlanSingln = "lessoninfo/getDtPlayPlanSingln"

    /**
     * 保存双师回放状态
     */
    const val saveDtReplay = "lessoninfo/saveDtReplay"

    /**
     * 三阶课激活接口
     */
    const val courseActivation = "lessoninfo/course/activation"

    /**
     * 未读通知
     */
    const val unreadNotify = "student/notify/general"

    /**
     * 获取学生有评测报告的年级
     */
    const val testReportGrade = "evaluation/report/grade"

    /**
     * 获取评测报告班级
     */
    const val testReportList = "evaluation/report/class"

    /**
     * 保存学生查看评测报告记录
     */
    const val saveTestReportRecord = "evaluation/report/record/save"

    /**
     * 修改学生姓名 获取学员状态
     */
    const val changeStudentNameStatus = "student/info/changeStudentNameStatus"

    /**
     * 修改学生姓名
     */
    const val changeStudentName = "student/info/changeStudentName"

    /**
     * 首页学习任务
     */
    const val studyTask = "newIndex/studyTask"

    /**
     * 首页任务列表
     */
    const val todoTaskPage = "newIndex/todoTaskPage"

    /**
     * 首页外教情景对话入口
     */
    const val functionEntrance = "newIndex/functionEntrance"

    /**
     * 英语预习获取url
     */
    const val getPreEnglishResource = "englishPreparation/getResource"

    /**
     * 登录成功学生列表分页
     */
    const val getLoginSuccessStudentInfo = "student/loginSuccess/page"

    /**
     * 获取全部学科
     */
    const val getSubjectList = "studentClassList/subjectList"

    /**
     * 获取全部班级
     */
    const val getAllClass = "studentClassList/allClass"

    /**
     * 获取tol课程未绑定消息
     */
    const val courseRemind = "studentClassList/courseRemind"
}