package com.haoke91.a91edu.entities

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/12/5 7:54 PM
 * 修改人：weiyimeng
 * 修改时间：2018/12/5 7:54 PM
 * 修改备注：
 */
class UpLoadResultBean {
    
    /**
     * body : {"businessKey":"HAOKE","key":"B:1019:K/1543939200/b84c3b31e4954d8faf2a540131073684","url":"https://img-test.aixuexi.com/B:1019:K/1543939200/b84c3b31e4954d8faf2a540131073684"}
     * errorCode : 0
     * errorMessage :
     * status : 1
     */
    
    var body: BodyBean? = null
    var errorCode: Int = 0
    var errorMessage: String? = null
    var status: Int = 0
    
    class BodyBean {
        /**
         * businessKey : HAOKE
         * key : B:1019:K/1543939200/b84c3b31e4954d8faf2a540131073684
         * url : https://img-test.aixuexi.com/B:1019:K/1543939200/b84c3b31e4954d8faf2a540131073684
         */
        
        var businessKey: String? = null
        var key: String? = null
        var url: String? = null
    }
}
