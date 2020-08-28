package com.tinytiger.common.net.data.gametools;

import com.tinytiger.common.net.data.BaseBean;

import java.util.List;

public class GameWikiListBean  extends BaseBean {
    public Data data;

    public class Data {

            public GameInfoBean game_info;
            public List<Wiki_info> wiki_info;


    }

}
