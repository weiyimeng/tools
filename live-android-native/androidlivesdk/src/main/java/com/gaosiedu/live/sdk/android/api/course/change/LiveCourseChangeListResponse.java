/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.course.change;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageResponse;
import com.gaosiedu.live.sdk.android.domain.*;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;

import java.math.BigDecimal;
import java.util.*;

/**
 * 课程-转班课程列表 返回值
 *
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/11/20 17:03
 * @since 2.1.0
 */
public class LiveCourseChangeListResponse extends ResponseResult {
    
    private ResultData data;
    
    public ResultData getData() {
        return data;
    }
    
    public void setData(ResultData data) {
        this.data = data;
    }
    
    
    public static class ResultData extends LiveSdkBasePageResponse {
        
        private List<CourseDomain> list;
        
        public List<CourseDomain> getList() {
            return list;
        }
        
        public void setList(List<CourseDomain> list) {
            this.list = list;
        }
        
    }
}
