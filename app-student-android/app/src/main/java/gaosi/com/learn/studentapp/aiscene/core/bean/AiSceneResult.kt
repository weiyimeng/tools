package gaosi.com.learn.studentapp.aiscene.core.bean

import java.io.Serializable

/**
 * 作者：created by 逢二进一 on 2019/8/9 15:01
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
class AiSceneResult: Serializable {

    var scenesId: String? = null //场景ID
    var taskId: String? = null //任务id
    var audioUrl: String? = null //语音文件地址
    var ocrResultXfyun: String? = null // ocr识别出来的结果
    var speechAssessment: String? = null //驰声语音测评结果
    var speechAssessmentChivox: String? = null //驰声语音测评完整结果
    var grammaticalAssessment: String? = null //语法测评结果
    var semanticAssessment: String? = null //语义测评结果
    var recommendBranchId: Int? = null //推荐分支ID

}