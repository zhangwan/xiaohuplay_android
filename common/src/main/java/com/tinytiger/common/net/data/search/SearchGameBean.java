package com.tinytiger.common.net.data.search;


import com.tinytiger.common.net.data.BaseBean;

import java.util.ArrayList;

/**
 *
 */
public class SearchGameBean extends BaseBean {
    public Data data;

    public class Data{

        public ArrayList<SearchGameInfo> data;
        public int total;
        public int current_page;
        public int last_page;

        public ArrayList<SearchGameInfo> game_category;


         /*"total": 2,
                 "per_page": 15,     #每页显示数量
        "current_page": 1,  ＃当前页
        "last_page": 1,*/
    }


}
