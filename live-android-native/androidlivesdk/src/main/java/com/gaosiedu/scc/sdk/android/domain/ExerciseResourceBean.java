package com.gaosiedu.scc.sdk.android.domain;

import java.io.Serializable;

public class ExerciseResourceBean implements Serializable {

    /**
     * 资源id,资源路径
     */
    private String resource;


    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

}