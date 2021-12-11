/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.content.banner;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

import java.math.BigDecimal;
import java.util.*;

/**  首页banner图 返回值
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/12/07 16:09
 * @since 2.1.0
 */
public class LiveContentBannerResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }


    public static class ResultData{

        /**
        * contentDomains
        */
        private  List<ContentDomain> contentDomains;

        //属性get||set方法



        public List<ContentDomain> getContentDomains() {
            return this.contentDomains;
        }

        public void setContentDomains(List<ContentDomain> contentDomains) {
            this.contentDomains = contentDomains;
        }

    }
}