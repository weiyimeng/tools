package gaosi.com.learn.bean.dress;

import java.io.Serializable;

/**
 * Created by test on 2018/5/29.
 */

public class FaceUMyItem implements Serializable {

    /** 数量 **/
    private int count;

    /** 已召唤一次 **/
    private String countStr;

    /** 是否被使用 0未使用过 1使用过 2 正在装扮 **/
    private int toBeUsed;

    /** 等级排序 **/
    private int levelSort;

    /** id **/
    private int itemId;

    /** 名字 **/
    private String name;

    /** 编码 **/
    private String code;

    /** 编码 **/
    private String smallCode;

    /** 等级 **/
    private String level;

    /** 等级名称 **/
    private int levelNum;

    /** 种类 **/
    private String category;

    /** 描述 **/
    private String itemDescription;

    /** 性别 **/
    private int sex;

    /** 代替编码 **/
    private String substituteCode;

    /** 默认装扮 **/
    private int defaultDress;

    /** 取消装扮 **/
    private int cancelDress;

    /** 下载地址 **/
    private String url;

    /** 版本号 **/
    private String version;

    /** 概率 **/
    private String rate;

    /** 该学生当前碎片数量 **/
    private String myDressSplit;

    /** 合成该装扮所需碎片数 **/
    private String levelForSplit;

    public String getSmallCode() {
        return smallCode;
    }

    public void setSmallCode(String smallCode) {
        this.smallCode = smallCode;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCountStr() {
        return countStr;
    }

    public void setCountStr(String countStr) {
        this.countStr = countStr;
    }

    public int getToBeUsed() {
        return toBeUsed;
    }

    public void setToBeUsed(int toBeUsed) {
        this.toBeUsed = toBeUsed;
    }

    public int getLevelSort() {
        return levelSort;
    }

    public void setLevelSort(int levelSort) {
        this.levelSort = levelSort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSubstituteCode() {
        return substituteCode;
    }

    public void setSubstituteCode(String substituteCode) {
        this.substituteCode = substituteCode;
    }

    public int getDefaultDress() {
        return defaultDress;
    }

    public void setDefaultDress(int defaultDress) {
        this.defaultDress = defaultDress;
    }

    public int getCancelDress() {
        return cancelDress;
    }

    public void setCancelDress(int cancelDress) {
        this.cancelDress = cancelDress;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(int levelNum) {
        this.levelNum = levelNum;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public void setLevelForSplit(String levelForSplit) {
        this.levelForSplit = levelForSplit;
    }

    public String getLevelForSplit() {
        return levelForSplit;
    }

    public void setMyDressSplit(String myDressSplit) {
        this.myDressSplit = myDressSplit;
    }

    public String getMyDressSplit() {
        return myDressSplit;
    }
}
