package com.tinytiger.common.net.data;


import com.tinytiger.common.net.data.yungaem.InitVersionBean;

import java.util.ArrayList;

/**
 *
 */
public class InitBean extends BaseBean{
    public Data data;

    public class Data {
        public InitVersionBean update_version;

        public ArrayList<AdBean> popup;
        public  ArrayList<AdBean> open_page;

        public Config config;
       /* "update_version": { #版本更新 ,无版本更新为空
            "version": "5.0.0", #版本号
            "update_url": "https://itunes.apple.com/cn/app/%E8%80%81%E6%92%95%E9%B8%A1%E7%94%B5%E7%AB%9E/id1325045766?mt=8", #更新地址
            "update_desc": "5.0版本更新", #更新说明
            "update": 2 #更新状态 1=正常更新 2=强制更新
        }*/
    }

    public class Config {
        public String url;
        public int is_show;
    }
/*"key": "android_version",
		"val": "3.1.0",
		"type": "1"*/


}
