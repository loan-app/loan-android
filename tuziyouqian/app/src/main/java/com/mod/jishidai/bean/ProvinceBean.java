package com.mod.jishidai.bean;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

/**
 * @author 周竹
 * @file AreaBean
 * @brief
 * @date 2018/4/26 下午12:54
 * Copyright (c) 2017
 * All rights reserved.
 */
public class ProvinceBean implements IPickerViewData {

    /**
     * name : 省份
     * city : [{"name":"北京市","area":["东城区","西城区","崇文区","宣武区","朝阳区"]}]
     */

    public String name;
    public List<CityBean> city;


    public List<CityBean> getCityList() {
        return city;
    }


    // 实现 IPickerViewData 接口，
    // 这个用来显示在PickerView上面的字符串，
    // PickerView会通过IPickerViewData获取getPickerViewText方法显示出来。
    @Override
    public String getPickerViewText() {
        return this.name;
    }


    public static class CityBean {
        /**
         * name : 城市
         * area : ["东城区","西城区","崇文区","昌平区"]
         */

        public String name;
        public List<String> area;

    }
}
