/*
 * Copyright (c) 2016,gaosiedu.com
 */
package com.gaosiedu.live.sdk.android.domain;

import java.io.Serializable;

/**
 * 字典数据
 *
 * @author lixun
 * @describe
 * @date 2017/7/25 17:19
 * @since 2.1.0
 */
public class DictionaryDomain implements Serializable {
    public DictionaryDomain(String dicName, String dicValue) {
        this.dicName = dicName;
        this.dicValue = dicValue;
    }
    
    private String dicTypeValue;
    
    /**
     * 字典名
     */
    private String dicName;
    
    /**
     * 字典值
     */
    private String dicValue;
    
    public String getDicValue() {
        return dicValue;
    }
    
    public void setDicValue(String dicValue) {
        this.dicValue = dicValue;
    }
    
    public String getDicName() {
        return dicName;
    }
    
    public void setDicName(String dicName) {
        this.dicName = dicName;
    }
    
    public String getDicTypeValue() {
        return dicTypeValue;
    }
    
    public void setDicTypeValue(String dicTypeValue) {
        this.dicTypeValue = dicTypeValue;
    }
}
