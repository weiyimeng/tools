package gaosi.com.learn.bean.main

import java.io.Serializable

/**
 * description: 首页权限入口
 * created by huangshan on 2020/6/5 上午10:46
 */
class FunctionEntranceBean: Serializable {
    var foreignStatus: Int? = 0 //外教入口状态  0 没有权限 1 没有通知 2 有通知
    var englishWhiteList: Int? = 0 //0 不是英语情景对话白名单， 1是英语情景对话白名单
}