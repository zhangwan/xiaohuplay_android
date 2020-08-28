package com.tinytiger.common.event;


/**
 * @author zhw_luke
 * @date 2018/9/19 0019 下午 1:52
 * @doc 首页登录通知Event
 */
public class MainLoginEvent {

    /**
     * 跳转页
     */
    public String page="home";
    /**
     * 登录是否成功
     */
    public boolean logintype=true;

    public MainLoginEvent(){

    }

    public MainLoginEvent(String page) {
        this.page = page;
    }

    public MainLoginEvent(Boolean logintype) {
        this.logintype = logintype;
    }

    public MainLoginEvent(String page,Boolean logintype) {
        this.page = page;
        this.logintype = logintype;
    }

}
