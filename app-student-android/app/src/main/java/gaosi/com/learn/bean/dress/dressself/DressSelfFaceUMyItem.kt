package gaosi.com.learn.bean.dress.dressself

import java.io.Serializable

/**
 * Created by dingyz on 2018/11/29.
 */
class DressSelfFaceUMyItem : Serializable {

    var count: Int = 0
    var toBeUsed: Int = 0 //0 首次收取到，未装扮过 2 正在使用中
    var itemId: Int = 0
    var name: String? = ""
    var code: String? = ""
    var level: String? = ""
    var levelNum: Int = 0
    var category: String? = ""
    var assistCategory: String? = ""
    var itemDescription: String? = ""
    var sex: Int = 0
    var substituteCode: String? = ""
    var defaultDress: Int = 0
    var cancelDress: Int = 0
    var url: String? = ""
    var version: String? = ""
    var rate: Float = 0.0f
    var smallCode: String? = ""
    var smallCodeUrl: String? = ""

}