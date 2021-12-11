package com.haoke91.a91edu.entities;


import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

/**
 * TODO<json数据源>
 *
 * @author: 小嵩
 * @date: 2017/3/16 15:36
 */

public class AddressBean implements IPickerViewData {
    
    
    /**
     * name : 省份
     * city : [{"name":"北京市","area":["东城区","西城区","崇文区","宣武区","朝阳区"]}]
     */
    
    private String name;
    private List<CityBean> citys;
    private int num;
    
    public int getNum() {
        return num;
    }
    
    public void setNum(int num) {
        this.num = num;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public List<CityBean> getCityList() {
        return citys;
    }
    
    public void setCityList(List<CityBean> city) {
        this.citys = city;
    }
    
    // 实现 IPickerViewData 接口，
    // 这个用来显示在PickerView上面的字符串，
    // PickerView会通过IPickerViewData获取getPickerViewText方法显示出来。
    @Override
    public String getPickerViewText() {
        return this.name;
    }
    
    
    public static class CityBean implements IPickerViewData{
        /**
         * name : 城市
         * area : ["东城区","西城区","崇文区","昌平区"]
         */
        
        private String name;
        private List<AreaBean> areas;
        private int num;
        
        public int getNum() {
            return num;
        }
        
        public void setNum(int num) {
            this.num = num;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public List<AreaBean> getArea() {
            return areas;
        }
        
        public void setArea(List<AreaBean> area) {
            this.areas = area;
        }
    
        @Override
        public String getPickerViewText() {
            return this.name;
        }
    }
    
    public static class AreaBean implements IPickerViewData{
        private String name;
        private int num;
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public int getNum() {
            return num;
        }
        
        public void setNum(int num) {
            this.num = num;
        }
    
        @Override
        public String getPickerViewText() {
            return this.name;
        }
    }
}
