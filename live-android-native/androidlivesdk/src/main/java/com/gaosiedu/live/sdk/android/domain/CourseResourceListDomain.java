/*
 * Copyright (c) 2016,gaosiedu.com
 */
package com.gaosiedu.live.sdk.android.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author duankaiyang
 * @describe
 * @date 2017/8/2 15:44
 * @since 2.1.0
 */
public class CourseResourceListDomain implements Serializable{
    private List<CourseResourceDomain> courseResourceDomains;

    public List<CourseResourceDomain> getCourseResourceDomains() {
        return courseResourceDomains;
    }

    public void setCourseResourceDomains(List<CourseResourceDomain> courseResourceDomains) {
        this.courseResourceDomains = courseResourceDomains;
    }
}