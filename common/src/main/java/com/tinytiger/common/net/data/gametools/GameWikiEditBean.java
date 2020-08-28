package com.tinytiger.common.net.data.gametools;

import com.tinytiger.common.net.data.BaseBean;

import java.util.List;

/**
 * @author lmq001
 * @date 2020/06/01 16:03
 * @copyright 小虎互联科技
 * @doc
 */
public class GameWikiEditBean extends BaseBean {

    public Data data;

    public class Data {
        public int current_page;
        public int last_page;
        public int per_page;
        public int total;
        public List<Wiki_info> wiki_info;
    }
}
