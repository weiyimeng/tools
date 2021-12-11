package gaosi.com.learn.bean.dress.dressself

import java.io.Serializable

/**
 * Created by dingyz on 2018/11/29.
 */
class DressSelfData : Serializable {

    var uploadToken: String? = ""
    var firestFreeNumber: Int? = 0
    var faceUMyFace: DressSelfFaceUMyFace? = null
    var faceUMyItemCategory: ArrayList<DressSelfFaceUMyItemCategory>? = null

}