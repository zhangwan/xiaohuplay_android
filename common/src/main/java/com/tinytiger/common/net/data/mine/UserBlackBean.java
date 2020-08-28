package com.tinytiger.common.net.data.mine;
import com.tinytiger.common.net.data.BaseBean;

import java.util.ArrayList;


public class UserBlackBean extends BaseBean {

    public DataBean data;

    public static class DataBean {
        public ArrayList<BlackBean> data;

        public int total;
        public int current_page;
        public int last_page;

        public static class BlackBean {

            public String user_id;
            public String nickname;
            public String avatar;
            public String create_time;

            public String account;
            public String netease_id;
            public int master_type;
            public String medal_image;
        }

         /* "user_id": 1, #用户id
                "nickname": "神仙姐姐123", #昵称
                "avatar": "http://cdn.tinytiger.cn/hello", #头像
                "create_time": "2019-11-14 22:27:27" #拉黑时间*/

    }
}
