package com.tencent.liteav.demo.play.view;

/**
 * Created by yuejiaoli on 2018/7/7.
 * 清晰度
 */

public class TCVideoQulity implements Comparable<TCVideoQulity>{

    public int index;
    public String name;
    public String title;
    public String url;
    public int bitrate;
    public TCVideoQulity() {
    }

    public TCVideoQulity(int index, String title, String url) {
        this.index = index;
        this.title = title;
        this.url = url;
    }

    public TCVideoQulity(int index, String name, String title, String url) {
        this.index = index;
        this.name = name;
        this.title = title;
        this.url = url;
    }

    @Override
    public int compareTo(TCVideoQulity var1) {
        return var1.bitrate-this.bitrate;
    }
}
