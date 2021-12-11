package gaosi.com.learn.bean.login

import com.gstudentlib.bean.StudentInfo
import java.io.Serializable

/**
 * 作者：created by 逢二进一 on 2020/6/5 16:59
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
class StudentInfoPage: Serializable {

    var pageNum: Int? = 0
    var pageSize: Int? = 0
    var pageTotal: Int? = 0
    var itemTotal: Int? = 0
    var list: List<StudentInfo>? = null

}