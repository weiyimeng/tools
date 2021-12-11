package gaosi.com.learn.bean.qr

import java.io.Serializable

/**
 * 作者：created by 逢二进一 on 2019/12/23 14:27
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
class QrHomeworkInfoBean: Serializable {

    var studentCode: String? = null //学生Code
    var classId: String? = null //班级id
    var lessonId: String? = null //讲次id
    var flag: Int? = null //标签
    var status: Int? = null //状态值 0没有扫码权限 1 正常 2 没有加入班级 3存在两个班级 4作业未开放 5作业已提交
    var action: Int? = null //提交作业途径 0讲次详情页进sdk 1扫码进入sdk
    var stars: Int? = null //星星数量
    var questionsCount: Int? = null //题目总数
    var haveAnswerCount: Int? = null //已提交的题目数
    var topicType: Int? = null //题目类型 0 有客观题也有客观题； 1：只有客观题 ；2：只有主观题
    var lessonNum: Int? = null
    var lessonName: String? = null

}