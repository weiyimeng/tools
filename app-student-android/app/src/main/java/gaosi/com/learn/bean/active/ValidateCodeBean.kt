package gaosi.com.learn.bean.active

import com.gstudentlib.bean.StudentInfo
import java.io.Serializable

/**
 * 入班流程优化->班级信息完善获取验证码
 * Created by dingyuanzheng on 2019/05/07
 */
class ValidateCodeBean: Serializable {
        var status: Int? = null // 0发送失败  1 发送成功
        var studentInfoVO: List<StudentInfo>? = null
}
