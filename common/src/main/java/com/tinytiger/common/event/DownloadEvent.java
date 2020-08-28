package com.tinytiger.common.event;


import java.io.Serializable;

/**
 * @author zhw_luke
 * @date 2019/12/15 0015 下午 4:00
 * @Copyright 小虎互联科技
 * @doc 下载url数据
 * @since 2.2.0
 */
public class DownloadEvent implements Serializable {

    /**
     * 0 下载
     * 1 下载中
     * 2 下载暂停
     * 3 下载完成
     * 4 下载失败
     */
    public int type = 0;
    public String url;
    public String name;

    /**
     * 下载进度0-100
     */
    public int schedule;


    public DownloadEvent() {

    }

    public DownloadEvent(String name, String url) {
        this.name = name;
        this.url = url;
    }


    public DownloadEvent(int type, String name, String url) {
        this.type = type;
        this.name = name;
        this.url = url;
    }

    public DownloadEvent(int type, String name, String url,int schedule) {
        this.type = type;
        this.name = name;
        this.url = url;
        this.schedule = schedule;
    }
}
