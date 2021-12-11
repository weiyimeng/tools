package com.haoke91.a91edu.entities.player;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/12/3 16:54
 */
public class Answer {
    private String choose;
    private String chooseContent;
    private boolean isSelected=false;
    
    public boolean isSelected(){
        return isSelected;
    }
    
    public void setSelected(boolean selected){
        isSelected = selected;
    }
    
    public String getChoose(){
        return choose;
    }
    
    public void setChoose(String choose){
        this.choose = choose;
    }
    
    public String getChooseContent(){
        return chooseContent;
    }
    
    public void setChooseContent(String chooseContent){
        this.chooseContent = chooseContent;
    }
}
