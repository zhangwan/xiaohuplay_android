package com.tinytiger.common.net.data.gametools.wiki;

import com.tinytiger.common.net.data.BaseBean;

import java.util.ArrayList;

public class WikiStatusList extends BaseBean {

    public WikiStatus data;

    public class WikiStatus {
        public int user_status;
        public WikiList list;

        public class WikiList {
            public int total;
            public ArrayList<WikiBeam> data;
        }


/*"wiki_status": 0,  //0百科未开启  1百科已经开启
        "user_status": 1,  //0当前用户未点亮百科    1当前用户已经点亮百科
        "list": {           //wiki_status=0 有值
            "total": 1,     //已经点亮百科的人数
                    "per_page": 20,
                    "current_page": 1,
                    "last_page": 1,
                    "data": [
            {
                "avatar": "http://cdn.tinytiger.cn/user/user5.jpg"   //头像集合
            },
            {
                "avatar": "http://cdn.tinytiger.cn/user/user6.jpg"   //头像集合
            }
            ]
        }*/
    }
    public class WikiBeam {
        public String avatar;
        public String user_id;
    }
}
