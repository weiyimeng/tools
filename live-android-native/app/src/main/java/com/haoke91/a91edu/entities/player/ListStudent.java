package com.haoke91.a91edu.entities.player;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/11/29 20:27
 */
public class ListStudent {
    List<Student> mStudentList;
    String tag;
    
    public List<Student> getStudentList(){
        return mStudentList;
    }
    
    public void setStudentList(List<Student> studentList){
        mStudentList = studentList;
    }
    
    public String getTag(){
        return tag;
    }
    
    public void setTag(String tag){
        this.tag = tag;
    }
}
