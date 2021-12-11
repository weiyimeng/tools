package gaosi.com.learn.bean.login

import com.gsbaselib.base.bean.BaseData

/**
 * Created by dingyz on 2018/12/19.
 */
data class LoginResponse(
        /**
         * 是否是北校用户：1是，0不是
         */
        var isBeiXiao: Int,
        /**
         * 是否修改过密码：1是，0不是
         */
        var changedPasswordCode: Int

) : BaseData()