package com.tinytiger.titi.data.circle;


/**
 * @author zhw_luke
 * @date 2019/11/27 0027 下午 6:19
 * @Copyright 小虎互联科技
 * @doc
 * @since 5.0.0
 */
public class ImageBean {

    public int type=0;

    public String url="";
    public ImageBean() {

    }
    public ImageBean(int type) {
        this.type = type;
    }
    public ImageBean(String url) {
        this.url = url;
    }
}
