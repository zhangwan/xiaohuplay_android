package com.tinytiger.common.view.picker.bean;

import com.zyyoona7.wheel.IWheelEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * @author Tamas
 * create at 2019/11/26 0026
 * Email: ljw_163mail@163.com
 * description:
 */
public class CityEntity implements IWheelEntity, Serializable {

    //国家
    public static final String LEVEL_COUNTRY = "country";
    //省
    public static final String LEVEL_PROVINCE = "province";
    //市
    public static final String LEVEL_CITY = "city";
    //区
    public static final String LEVEL_DISTRICT = "district";
    private static final long serialVersionUID = -5287604550171284524L;

    private String citycode;
    private String adcode;
    private String name;
    private String center;
    private String level;
    private List<CityEntity> districts;


    public CityEntity(String name) {
        this.name = name;
    }

    public CityEntity(String name, List<CityEntity> districts) {
        this.name = name;
        this.districts = districts;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<CityEntity> getDistricts() {
        return districts == null ? new ArrayList<CityEntity>(1) : districts;
    }

    public void setDistricts(List<CityEntity> districts) {
        this.districts = districts;
    }

    @Override
    public String toString() {
        return "CityEntity{" +
                "citycode='" + citycode + '\'' +
                ", adcode='" + adcode + '\'' +
                ", name='" + name + '\'' +
                ", center='" + center + '\'' +
                ", level='" + level + '\'' +
                ", districts=" + districts +
                '}';
    }

    @Override
    public String getWheelText() {
        return name == null ? "" : name;
    }
}
