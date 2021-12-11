package com.haoke91.a91edu.entities;

import java.io.Serializable;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/8/3 16:39
 */
public class Exam implements Serializable {
    public int id;//编号
    public int questionType;//题型    1.单选  2.多选  3.填空
    public String questionStem;//题干
    public String questionContent;//选项
    
    public int getId(){
        return id;
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public int getQuestionType(){
        return questionType;
    }
    
    public void setQuestionType(int questionType){
        this.questionType = questionType;
    }
    
    public String getQuestionStem(){
        return questionStem;
    }
    
    public void setQuestionStem(String questionStem){
        this.questionStem = questionStem;
    }
    
    public String getQuestionContent(){
        return questionContent;
    }
    
    public void setQuestionContent(String questionContent){
        this.questionContent = questionContent;
    }
}
