package com.gaosiedu.live.sdk.android.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CourseTemplateDomain  implements Serializable {
    //private String templateId="";
    private List<TemplateProperty> templatePropertyList=new ArrayList<>();

/*    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }*/

    public List<TemplateProperty> getTemplatePropertyList() {
        return templatePropertyList;
    }

    public void setTemplatePropertyList(List<TemplateProperty> templatePropertyList) {
        this.templatePropertyList = templatePropertyList;
    }

    //对模板数据的一个封装
    public static class TemplateProperty implements Serializable{
        private int type;

        private String name;

        private String showName;

        private String value;

        private int sort;

        private int templateType;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getTemplateType() {
            return templateType;
        }

        public void setTemplateType(int templateType) {
            this.templateType = templateType;
        }

        public String getShowName() {
            return showName;
        }

        public void setShowName(String showName) {
            this.showName = showName;
        }
    }
}
