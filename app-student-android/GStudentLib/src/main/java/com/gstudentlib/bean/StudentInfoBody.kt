package com.gstudentlib.bean

import java.io.Serializable

/**
 * 作者：created by 逢二进一 on 2019/7/19 15:59
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
class StudentInfoBody: Serializable {

    var beixiao: Boolean? = false
    var changedPasswordCode: Int? = 1
    var students: List<StudentInfo>? = null

}