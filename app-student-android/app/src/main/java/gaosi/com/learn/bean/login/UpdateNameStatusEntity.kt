package gaosi.com.learn.bean.login

import java.io.Serializable

/**
 * 作者：created by 逢二进一 on 2020/5/12 13:21
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
class UpdateNameStatusEntity: Serializable {

    var changeNameStatus: Int? = null // 修改姓名状态 0 不可修改 1 可修改
    var lastChangeNameTimeContent: String? = null //最后一次修改姓名时间文案
    var changeNameTimeIntervalContent: String? = null //修改姓名时间间隔文案
    var id: String? = null //studentId

}