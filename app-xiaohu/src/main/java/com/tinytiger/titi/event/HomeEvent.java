package com.tinytiger.titi.event;


/**
 *
 * @author zhw_luke
 * @date 2020/4/16 0016 上午 11:05
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc 首页切换
 */
public class HomeEvent {

    public int page;

    public HomeEvent(int page) {
        this.page = page;
    }
}
