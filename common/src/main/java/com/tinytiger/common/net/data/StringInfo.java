package com.tinytiger.common.net.data;

import java.io.Serializable;

/**
 * 字符串
 */
public class StringInfo implements Serializable {

        public String title;
        public int id;
        public Boolean type;
/*"title": "给你吃桃的个人主页",
        "img": "http://p1arvhkn5.bkt.clouddn.com/img1533092882748.png",
        "desc": "TiTi电竞，最具专业的电竞社交平台",
        "shareUrl": "http://d.lsjdj.cn/download/d.php"*/

        public StringInfo() {
        }
        public StringInfo(String title, int id) {
                this.title = title;
                this.id = id;
                this.type = false;
        }
}
