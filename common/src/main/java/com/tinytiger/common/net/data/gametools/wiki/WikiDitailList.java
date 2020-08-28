package com.tinytiger.common.net.data.gametools.wiki;

import com.tinytiger.common.net.data.BaseBean;

public class WikiDitailList extends BaseBean {

    public WikiModular data;

    public class WikiModular {
        public String id;
        public String name;
        public String img_url;
        public String content;
        public String url;
        public String cate_id;
        public int is_collect;
        public String update_time;
        public String entry_error_url;
        public String update_user_url;

        public gameModular game_info;
        public CircleInfo circle_info;

        public class gameModular {
            public String game_info;
            public String id;
            public String logo;
            public String name;
            public String one_introduce;
        }

        public class CircleInfo {
            public String id;
            public String logo;
            public String name;
        }


     /*"id": 66,       #内容id
        "name": "tem-和平精英1-content", #内容名称
        "img_url": "",  #内容图片链接
        "content": "",   #内容文本
        "cate_id": 67,
                "game_info": {  #游戏信息
                "id": 112,  #游戏id
                "logo": "https://cdn.tinytiger.cn/d327b8495", #游戏logo
                "name": "#COMPASS 战",   #游戏名称
                "one_introduce": "aaaaaaaaaaaa"     #一句话简介
            },
                    "url": "http://192.168.11/web_app/gamesSedia.html"      #分享地址
*/
    }

}
