package gaosi.com.learn.bean

import java.io.Serializable

/**
 * description: 金币商城bean
 * created by huangshan on 2019-12-18 14:50
 */
class GoldMallBean : Serializable {
    var studentId: Int? = 0
    var coin: Int? = 0
    var used: Int? = 0
    var currentIDocmageId: String? = ""//当前使用头像ID
    var coinImageLogList: ArrayList<CoinImage>? = null//头像列表
}

class CoinImage : Serializable {
    var id: Int? = 0//头像ID
    var buy: Int? = 0//是否购买 0为购买 1已购买 2已拥有
    var documentName: String? = ""//头像名称
    var doucmentUrl: String? = ""//头像链接
    var resumeDay: Int? = 0//剩余天数
    var createDate: String? = ""//购买时间
    var endDate: String? = ""//到期时间
    var usageCycle: Int? = 0
    var costCoin: Int? = 0//花费金币
    var isCurrentDoc: Int? = 0//是否为当前使用头像 0不是 1是
    var selectStatus: Boolean? = false//头像是否被点击
}

class BuyCoinImageBean : Serializable {
    var coin: Int? = 0//剩余金币
    var endDate: String? = ""//到期时间
    var usageCycle: String? = ""//使用期限
}



