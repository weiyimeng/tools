package gaosi.com.learn.studentapp.aiscene.core.bean

import java.io.Serializable

/**
 * 作者：created by 逢二进一 on 2019/8/12 10:53
 * 邮箱：dingyuanzheng@gaosiedu.com
 * 10000 驰声接口调用失败
 * 10001 讯飞接口调用失败
 * 10003 AILIB接口调用失败
 * 10004 存储接口调用失败
 * 10005 服务器内部错误
 * 10006 接口参数错误
 */
class AiSceneResultBean: Serializable {

    var code: String? = null
    var message: String? = null
    var data: AiSceneResult? = null

}