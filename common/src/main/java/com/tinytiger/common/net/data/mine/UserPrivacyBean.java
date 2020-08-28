package com.tinytiger.common.net.data.mine;


import com.tinytiger.common.net.data.BaseBean;

public class UserPrivacyBean extends BaseBean {


    public DataBean data;

    public static class DataBean {
        public userBean user_config;
        public static class userBean {
            public int private_letter;
            public int city_show;
        }

      /*  "user_config": {
            "private_letter": 1, #1所有人 2我关注的人
            "city_show": 1 #1所有人 2我关注的人 3不显示
        }*/
    }
}
