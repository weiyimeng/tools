package com.gstudentlib;

import com.gsbaselib.InitBaseLib;
import com.gstudentlib.base.STBaseConstants;

/**
 * 作者：created by 逢二进一 on 2019/9/11 17:53
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
public interface GSAPI {

    String SIGAO_BASE_URL = "https://openapi.aixuexi.com/wjd/";

    String SDK_BASE_URL = "https://openapi.aixuexi.com/xsd/";

    //双师baseUrl
    String DT_BASE_URL = "https://ss.aixuexi.com/";

    //爱学习在线baseUrl
    String SDK_DEBUG_AXX_BASE_URL = "https://lol-learning-web.aixuexi.com/"; //爱学习sdk测试环境域名
    String SDK_RELEASE_AXX_BASE_URL = "https://learningzx.aixuexi.com/"; //爱学习sdk正式环境域名
    String SDK_AXX_BASE_URL = STBaseConstants.isDebug ? SDK_DEBUG_AXX_BASE_URL : SDK_RELEASE_AXX_BASE_URL;

    String BASE_URL = InitBaseLib.getInstance().getConfigManager().getBaseUrl();

    /**
     * 获取积分接口
     */
    String studentScore_scoreLevel = "studentScore/scoreLevel";

    /**
     * 获取学生信息接口
     */
    String student_studentInfo = "student/info/studentInfo";

    /**
     * 首页消息
     */
    String Report_teacherMessageCount = "report/teacherMessageCount";

    /**
     * 宝箱信息
     */
    String Treasure_getInfo = "treasure/getInfo";

    /**
     * 宝箱开启
     */
    String Treasure_open = "treasure/open/info";

    /**
     * 更新hy资源
     */
    String hyResourceUpdate = "faceUMall/getDownloadInfo";

    /**
     * 英语预习 如果观看超过90秒，落库保存记录
     */
    String englishPreparation = "studentapi/englishPreparation/insert";

    /**
     * 装扮自己页面信息
     */
    String getDressSelf = "adornOneself/info";

    /**
     * 使用道具，改变道具new状态
     */
    String userdDressSelf = "adornOneself/item/used";

    /**
     * 装扮自己-保存装扮
     */
    String saveDressSelf = "adornOneself/saveImage";

    /**
     * 信息架构调整-首页banner
     */
    String homeBanner = "newIndex/imageShow";

    /**
     * 信息架构调整-首页班级讲次列表
     */
    String newClassLesson = "newIndex/classLesson";

    /**
     * 信息架构调整-我的班级
     */
    String myClass = "studentClassList/myClass";

    /**
     * 信息架构调整-讲次详情
     */
    String myClassDetail = "lessoninfo/detail";

    /**
     * 信息架构调整-我的班级讲次
     */
    String myLessonList = "lessoninfo/list";

    /**
     * 全部班级（包含历史班级）
     */
    String allClassList = "studentClassList/classList";

    /**
     * 查询错题视频
     */
    String getErrorQuestionsVideo = "question/errorQuestions/topicUrl";

    /**
     * 入班流程优化->验证用户的班级码
     */
    String checkClassCode = "intoclass/getClassInfo";

    /**
     * 入班流程优化->完善新用户信息
     */
    String perfectStudentInfo = "intoclass/intoClass";

    /**
     * 入班流程优化->班级信息完善获取验证码
     */
    String getAttendClassValidateCode = "intoclass/getValidateCode";

    /**
     * 入班流程优化->效验验证码
     */
    String checkValidateCode = "intoclass/checkValidateCode";

    /**
     * 激活码激活三阶课
     */
    String studentClassListSctivate = "studentClassList/activate";

    /**
     * 活动弹框
     */
    String homeDialog = "newIndex/popWindow";

    /**
     * 确认性别
     */
    String genderConfirmToNet = "student/info/gender";

    /**
     * 装扮合成确认
     */
    String mergeDressConfirm = "faceUMall/combineConfirm";

    /**
     * 装扮回收
     */
    String recycleDress = "faceUMall/recover";

    /**
     * 装扮回收确认
     */
    String recycleDressConfirm = "faceUMall/recoverConfirm";

    /**
     * 情景对话列表
     */
    String sceneList = "aiScenes/listScenes";

    /**
     * 情景对话场景
     */
    String getDialogeInfo = "aiScenes/getDialogeInfo";

    /**
     * 情景对话 点击翻译、提示行为统计
     */
    String dialogueBehavior = "aiScenes/dialogueBehavior";

    /**
     * 情景对话，对话组回答数据保存
     */
    String dialogueAnswer = "aiScenes/dialogueAnswer";

    /**
     * 修改密码时，获取验证码
     */
    String getSecuretyCodeUpdate = "update/password/getValidateCode";

    /**
     * 验证手机号
     */
    String verityPhone = "update/password/validateCode";

    /**
     * 修改密码
     */
    String updatePassword = "update/password";

    /**
     * 取消修改密码弹框
     */
    String closeUpdateDialog = "update/password/close/updateModal";

    /**
     * 监测是否需要修改密码
     */
    String checkUpdatePassword = "update/password/updateStatus";

    /**
     * 获取学生信息页面
     */
    String getStudentInfo = "student/info/studentInfo";

    /**
     * 学生拥有的道具
     */
    String faceUmallInfo = "faceUMall/getMyItemsInfo";

    /**
     * 获取用户信息
     */
    String loginSuccess = "student/loginSuccess";

    /**
     * 情景对话获取场景挑战状态
     */
    String challengeTheStatus = "aiScenes/challengeTheStatus";

    /**
     * 保存学生上课记录
     */
    String axxOnLineJoin = "axxonline/lesson/join";

    /**
     * 网关token
     */
    String gatewayLogin = SDK_BASE_URL + "gateway/login";

}
