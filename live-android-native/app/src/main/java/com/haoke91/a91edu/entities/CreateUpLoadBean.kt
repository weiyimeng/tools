package com.haoke91.a91edu.entities

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/12/5 5:22 PM
 * 修改人：weiyimeng
 * 修改时间：2018/12/5 5:22 PM
 * 修改备注：
 * @version
 */
class CreateUpLoadBean {
    lateinit var businessKey: String
    lateinit var uploadFiles: ArrayList<FileInfo>
    
     class FileInfo {
        constructor(fileNameOrSuffix: String, amount: Int) {
            this.fileNameOrSuffix = fileNameOrSuffix
            this.amount = amount
        }
        
        var fileNameOrSuffix: String
        var amount: Int = 1
    }
}
