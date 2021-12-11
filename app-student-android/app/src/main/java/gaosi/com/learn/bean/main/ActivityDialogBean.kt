package gaosi.com.learn.bean.main

import java.io.Serializable

class ActivityDialogBean : Serializable {
    var studentId: Int? = 0
    var allPopwindowVOs: PopWindowParam? = null
}

class PopWindowParam: Serializable {
    var popName: String? = ""
    //是否弹出 1弹 2不弹
    var hasOpen: Int? = 0
    var androidUrl: String? = "" //android下载链接
}