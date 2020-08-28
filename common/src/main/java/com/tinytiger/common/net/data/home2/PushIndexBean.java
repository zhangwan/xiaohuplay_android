package com.tinytiger.common.net.data.home2;


import com.tinytiger.common.net.data.BaseBean;
import com.tinytiger.common.net.data.center.MineGameBean;

import java.util.ArrayList;

/**
 *
 * @author zhw_luke
 * @date 2020/4/17 0017 上午 10:00
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc 首页游戏
 */
public class PushIndexBean extends BaseBean {

    public Data data;

    public class Data {
        public String indexNoveltyGameCateName;
        public int indexNoveltyGameCateId;
        public ArrayList<MineGameBean> indexRecommendedGame;
        public ArrayList<MineGameBean> indexNoveltyGame;
        public ArrayList<MineGameBean> indexGoodGame;
    }

}
