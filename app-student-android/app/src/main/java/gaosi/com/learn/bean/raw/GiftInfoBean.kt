package gaosi.com.learn.bean.raw

import com.gsbaselib.base.bean.BaseData

/**
 * Created by test on 2018/5/11.
 * 宝箱球显示信息
 */

class GiftInfoBean : BaseData() {
    var id: String? = ""//开启宝箱的流水Id，不用传
    var level: Int = 0//返回的宝箱的级别，作为开启宝箱的传参
    var type: String? = ""//宝箱奖励类型，开启宝箱的传参
    var sgdia: String? = ""//宝箱奖励值
    var open: Boolean = false//与surprised做与运算，看此宝箱是否要开启
    var offset: Int = 0//偏移量
    var allStar: Int = 0//所有星数
    var surprised: Boolean = false//与open做与运算，看此宝箱是否要开启
    var display: Boolean = false//false不显示，true显示
    var treasureCount: Int = 0//已开启的宝箱数
}
