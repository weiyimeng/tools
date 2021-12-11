package gaosi.com.learn.bean.main

import java.io.Serializable

/**
 * description:
 * created by huangshan on 2020/6/3 下午2:04
 */
class TodoTaskData : Serializable {
    var pageNum: Int? = 0
    var pageTotal: Int? = 0
    var pageSize: Int? = 0
    var itemTotal: Int? = 0
    var hasClass: Int? = 1 //是否加入过班级 1-是 0-否
    var list: ArrayList<TodoTaskListBean>? = null
}

class TodoTaskListBean: Serializable {
    var lessonId: Int? = 0 //讲次id
    var lessonNum: Int? = 0 //讲次序号
    var lessonName: String? = "" //讲次名称
    var classId: Int? = 0 //班级id
    var className: String? = "" //班级名称
    var subjectId: Int? = 0 //学科id
    var tasks: ArrayList<TaskBean>? = null
    var isActivation: Int? = 0 //是否激活三阶课 0 否 1是
}

class TaskBean : Serializable {
    var type: Int? = 0 //任务类型（自我巩固-1,英语口语练习-2,课堂回放-3,课堂报告-4,专题课-5,英语预习-6,外教课-7,英语背诵-8,外教课回放-9,预习课件-10,电子讲义-11,学情报告-12）
    var gold: Int? = 0 //任务完成最多可得金币数
    var hasNewVersion: Int? = 0 //专题课有新版本 1-有 0-无
    var finishStatus: Int? = 0 //自我巩固作业完成状态（0-有题目未答且未提交，1-全部题目已答且未提交）
    var preview: Int? = 0 //英语预习 1：旧 2：单词 3：都有，需要弹框
}