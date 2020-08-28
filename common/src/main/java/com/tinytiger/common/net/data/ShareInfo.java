package com.tinytiger.common.net.data;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * 字符串解析
 */
public class ShareInfo implements Serializable {

        public String title;
        public String img;
        public String desc;

        public String shareUrl;
        public String share_url;


        public String type;
        public Drawable path;
        public String code;
        public ShareInfo() {

        }
        public ShareInfo(String title, Drawable path) {
                this.title = title;
                this.path = path;
        }

        /*"title": "给你吃桃的个人主页",
        "img": "http://p1arvhkn5.bkt.clouddn.com/img1533092882748.png",
        "desc": "TiTi电竞，最具专业的电竞社交平台",
        "shareUrl": "http://d.lsjdj.cn/download/d.php"*/


}
