package gaosi.com.learn.bean.classlesson.lessondetail

import java.io.Serializable

/**
 * description:
 * created by huangshan on 2020-03-18 20:26
 */
class TestReport : Serializable {
    var link: String? = "" //报告链接
    var type: Int? = 1 //1期中 2期末
}