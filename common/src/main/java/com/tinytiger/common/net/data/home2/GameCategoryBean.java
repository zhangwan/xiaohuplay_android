package com.tinytiger.common.net.data.home2;




import com.tinytiger.common.net.data.BaseBean;

import java.util.ArrayList;

/**
 * 游戏分类
 */
public class GameCategoryBean extends BaseBean {

    public ArrayList<GameCategory> data;
    public class GameCategory {
        public String game_id;
        public String game_name;
       /* "game_id": 1, #分类的游戏ID
            "game_name": "啦啦啦" #分类名称*/
    }

}
