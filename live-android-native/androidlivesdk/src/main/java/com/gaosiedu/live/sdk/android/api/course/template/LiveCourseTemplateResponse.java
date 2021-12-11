/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.course.template;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/08/29 19:15
 * @since 2.1.0
 */
public class LiveCourseTemplateResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }


    public static class ResultData{

        /**
        * templatePropertyList
        */
        private  List<CourseTemplateDomain.TemplateProperty> templatePropertyList;

        //属性get||set方法



        public List<CourseTemplateDomain.TemplateProperty> getTemplatePropertyList() {
            return this.templatePropertyList;
        }

        public void setTemplatePropertyList(List<CourseTemplateDomain.TemplateProperty> templatePropertyList) {
            this.templatePropertyList = templatePropertyList;
        }

    }
}
