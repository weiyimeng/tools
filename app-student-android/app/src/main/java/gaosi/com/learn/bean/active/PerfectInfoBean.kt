package gaosi.com.learn.bean.active

import com.gstudentlib.bean.StudentInfo
import java.io.Serializable

/**
 * 入班流程-完善新用户信息
 * Created by dingyuanzheng on 2019/05/07
 */
class PerfectInfoBean: Serializable {

        var changedPasswordCode: Int? = 0// 密码是否修改0：未修改 1：已修改
        var isBeiXiao: Int? = null //是否是北校学生0：不是 1：是
        var intoStatus: Int? = null // 0学生不对 1 入班成功   2入班失败
        var studentId: String? = "" //学生ID
        var userId: String? = "" //业务ID
        var axxProof: String? = "" //业务token
        var refreshToken: String? = "" //刷新token
        var businessUserId: List<String>? = null // 业务id
        var studentList: List<StudentInfo>? = null //学员列表
        var student: StudentInfo? = null //当前入班成功的学员信息

}
