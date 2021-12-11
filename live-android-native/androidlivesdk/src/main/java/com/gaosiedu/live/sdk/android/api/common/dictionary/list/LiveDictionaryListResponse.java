/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.common.dictionary.list;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.DictionaryDomain;

import java.io.Serializable;
import java.util.List;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/08/29 16:04
 * @since 2.1.0
 */
public class LiveDictionaryListResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }


    public static class ResultData implements Serializable{

        /**
        * list
        */
        private  List<DictionaryDomain> list;

        //属性get||set方法



        public List<DictionaryDomain> getList() {
            return this.list;
        }

        public void setList(List<DictionaryDomain> list) {
            this.list = list;
        }

    }
}
