package gaosi.com.learn.bean.active

import java.io.Serializable

/**
 * 入班流程优化->验证用户的班级码
 * Created by dingyuanzheng on 2019/05/06
 */
class ClassBean: Serializable {
        var className: String? = "" //班级名称
        var teacherName: ArrayList<String>? = null //主讲老师
        var classId: String? = "" //班级ID
        var institutionId: String? = ""//机构ID
}
