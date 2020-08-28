package com.tinytiger.common.net.data.search;

import java.util.ArrayList;

/**
 * 游戏
 */
public class SearchGameInfo {

        public String id;
        public String name;
        public String background;
        public String one_introduce;

        public String logo;

        public String cate_name;

        public ArrayList<cateTwoLevel> cate_two_level;


        public class cateTwoLevel{
                public String cate_name;
        }

/*
"id": 1,                           ＃游戏id
                "name": "dota",                     #游戏名称
                "background": "http://www.baidu.com",           #背景图
                "one_introduce": "gfgf",                    #一句话简介
                "logo": "http://www.baidu.com"                   #游戏logo
*/


}
