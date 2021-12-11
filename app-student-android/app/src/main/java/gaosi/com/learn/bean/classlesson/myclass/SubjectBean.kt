package gaosi.com.learn.bean.classlesson.myclass

import java.io.Serializable

/**
 * description: 学科
 * created by huangshan on 2020/6/16 下午7:51
 */
data class SubjectBean(
        var subjectId: Int? = 0, //学科ID
        var subjectName: String? = "", //学科名称
        var isClicked: Boolean = false //是否点击
) : Serializable