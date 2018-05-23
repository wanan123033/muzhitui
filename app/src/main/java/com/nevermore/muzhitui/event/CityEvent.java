package com.nevermore.muzhitui.event;

/**
 * Created by hehe on 2016/5/21.
 */
public class CityEvent {
    private int type;
    private String cityName;

    public CityEvent(String cityName,int type) {
        this.cityName = cityName;
        this.type = type;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String toString(){
        return cityName;
    }

    public int getType() {
        return type;
    }
}
