package gaosi.com.learn.application.event

import java.io.Serializable

/**
 * 作者：created by 逢二进一 on 2020/5/12 14:17
 * 邮箱：dingyuanzheng@gaosiedu.com
 * 自我巩固 10XXX
 * 口语练习 20XXX
 * 口语背诵 30XXX
 * app 40XXX
 * RNStudent 50XXX
 * 课前预习 60XXX
 * 口语练习 70XXX
 * 专题课 80XXX
 * 在线外教 90XXX
 * weex 100xxx
 */
object AppEventType: Serializable {

    //修改姓名成功
    val UPDATE_NAME_SUCCESS =40001

    //我的班级 选择学科
    val SELECT_SUBJECT = 40002

    //我的班级 选择班级状态
    val SELECT_CLASS_STATUS = 40003

    //入班完成
    val INTO_CLASS_COMPLETE = 40004

}