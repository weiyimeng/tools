package gaosi.com.learn.bean.dress.dressself

import java.io.Serializable

/**
 * Created by dingyz on 2018/11/29.
 */
class DressSelfFaceUMyItemCategory : Serializable {

    var categoryId: Int = 0
    var code: String? = ""
    var num: Int = 0
    var assistCode: String? = ""
    var iconUrl: String? = ""
    var iconLightUrl: String? = ""
    var remark: String? = ""
    var defaultCategory: Int = 0 // 是否默认 0 可以取消 1 不可以取消
    var itemCount: Int = 0
    var myItemCount: Int = 0
    var faceUMyItem: ArrayList<DressSelfFaceUMyItem>? = null

}