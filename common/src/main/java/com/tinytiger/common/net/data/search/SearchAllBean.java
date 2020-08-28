package com.tinytiger.common.net.data.search;


import com.tinytiger.common.net.data.BaseBean;
import com.tinytiger.common.net.data.circle.CircleBean;
import com.tinytiger.common.net.data.circle.PostBean;

import java.util.ArrayList;

/**
 *
 */
public class SearchAllBean extends BaseBean {
    public Data data;

    public class Data {
        public ArrayList<SearchGameInfo> game;

        public ArrayList<SearchNewsInfo> content;

        public ArrayList<SearchUserInfo> user;

        public ArrayList<CircleBean> circle;

        public ArrayList<PostBean> post;

        public ArrayList<SearchGameInfo> game_category;

        public String shareUrl;
    }

}
