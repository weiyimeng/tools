package gaosi.com.learn.bean.classlesson.lessondetail

import java.io.Serializable

/**
 * 作者：created by 逢二进一 on 2020/1/30 15:39
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
class TeachingMaterialBean: Serializable {

    var type: Int? = null // 教材格式 （1-json，2-pdf）
    var pdfUrl: String? = null
    var handOuts: List<Any>? = null

}