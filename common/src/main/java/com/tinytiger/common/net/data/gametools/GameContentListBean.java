package com.tinytiger.common.net.data.gametools;

import com.tinytiger.common.net.data.BaseBean;
import com.tinytiger.common.net.data.gametools.GameInfoBean;
import com.tinytiger.common.net.data.gametools.Wiki_info;
import com.tinytiger.common.net.data.home.GameBean;
import com.tinytiger.common.net.data.home2.NewsBean;

import java.util.List;

public class GameContentListBean extends BaseBean {
    public Data data;

    public class Data {

        public GameInfoBean game_info;
        public GameContentList content_info;

    }

}
