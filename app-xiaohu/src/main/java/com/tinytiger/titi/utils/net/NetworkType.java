package com.tinytiger.titi.utils.net;

/*
 * @author Tamas
 * create at 2019/12/5 0005
 * Email: ljw_163mail@163.com
 * description:
 */
public enum  NetworkType {

    NETWORK_WIFI("WiFi"),
    NETWORK_4G("4G"),
    NETWORK_3G("3G"),
    NETWORK_UNKNOWN("Unknown"),
    NETWORK_NO("No network");

    private String desc;
    NetworkType(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return desc;
    }
}
