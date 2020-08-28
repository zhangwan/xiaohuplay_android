package com.tinytiger.common.net.data.msg;


import com.tinytiger.common.net.data.BaseBean;

/**
 *
 */
public class UserKitBean extends BaseBean {
    public DataBean data;

    public static class DataBean {
        public UserConfig user_config;
        public UserConfig token_user_config;
        public UserRelation user_relation;

        public static class UserConfig {
            public String user_id;
            public int private_letter;
            public int city_show;
        }

        public static class UserRelation {
            public int follow;
        }

    }


/*"user_config": { #对方用户配置
        "private_letter": 1, #私信配置 1所有人 2我关注的人
        "city_show": 1 #城市显示配置 1所有人 2我关注的人 3不显示
    },
            "token_user_config": { #登录用户配置
        "private_letter": 2,#私信配置 1所有人 2我关注的人
        "city_show": 1 #城市显示配置 1所有人 2我关注的人 3不显示
    },*/
 /*"user_config": { #用户配置
        "private_letter": 1, #私信配置 1所有人 2我关注的人
        "city_show": 1 #城市显示配置 1所有人 2我关注的人 3不显示
    },
            "user_relation": { #用户关系
        "follow": 0 #0未关注 1已关注 2相互关注
    }
*/

}
