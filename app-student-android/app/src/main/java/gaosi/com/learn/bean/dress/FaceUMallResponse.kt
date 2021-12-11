package gaosi.com.learn.bean.dress

import com.gsbaselib.base.bean.BaseData
import java.io.Serializable

import java.util.ArrayList

/**
 * Created by test on 2018/5/29.
 */

class FaceUMallResponse : BaseData() {

    var faceUMyFaceImageUrl: String? = null

    /**  自己拥有的道具  */
    var faceUMyItemCategory: ArrayList<FaceUMallItem>? = null

    var levelForSplitVO: Split? = null//合成所需碎片数
}

class Split : Serializable {
    var myDressSplit: Int? = 0
    var levelForSplitVO: LevelForSplit? = null
}

class LevelForSplit : Serializable {
    var A: Int? = 0
    var B: Int? = 0
    var S: Int? = 0
    var SS: Int? = 0
}
