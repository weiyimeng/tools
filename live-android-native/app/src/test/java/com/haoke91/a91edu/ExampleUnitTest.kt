package com.haoke91.a91edu

import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.utils.Md5Utils
import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    @Test
    fun md5(){
        val signparams = LinkedHashMap<String, String>()
        //        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        val currentTime = System.currentTimeMillis()
        //  String random = getRandom();
        signparams.put("appId", "91hakeapi")
        signparams.put("random", "f41d9779-7268-4c32-8633-24c12e287a7e")
        signparams.put("stamp", "1547019059539")
        var keyArray = arrayOfNulls<String>(signparams.size)
        keyArray = signparams.keys.toTypedArray()
        Arrays.sort(keyArray)
        val sortedParam = StringBuffer()
        for (key in keyArray) {
            sortedParam.append(key)
            sortedParam.append("=")
            sortedParam.append(signparams.get(key))
            sortedParam.append("&")
        }
    
//        val sortedSignParam = getSortedParam(signparams)
        //只对加密的参数添加key做加密
        val keyString = StringBuffer(sortedParam)
        keyString.append("uri=")
        keyString.append("")
        keyString.append("&appKey=")
        keyString.append("4161C4F2BC3A5AE54E9E814C4A7F3F3D")
        val sign = Md5Utils.MD5(keyString.toString())
        print(sign)
    }
}
