package com.tinytiger.common.net.data.circle.postsend;

import com.tinytiger.common.net.data.BaseBean;

import java.util.ArrayList;

/**
 * @author zhw_luke
 * @date 2019/11/27 0027 下午 6:19
 * @Copyright 小虎互联科技
 * @doc
 * @since 5.0.0
 */
public class SelectFriend2Bean extends BaseBean {

    public Data data;

    public class Data {


        public ArrayList<FriendBean> list;
        public int total;
        public int current_page;
        public int last_page;


    }


//    "data": {
//        "qa_user": [   # 问答团
//        {
//            "user_id": 13,
//                "nickname": "Justin Bieber 03 01 生日快"
//        },
//        {
//            "user_id": 983,
//                "nickname": "Ryan你仿佛就能发你发你发你发"
//        },
//        {
//            "user_id": 3272,
//                "nickname": "用户80014594"
//        }
//        ],
//        "recommend_user": [   # 推荐用户
//        {
//            "user_id": 3603,
//                "nickname": "用户10607064"
//        }
//        ],
//        "follow_user": {   # 关注用户
//            "list": [    #关注列表-默认会返回50条记录
//            {
//                "id": 2801,
//                    "user_id": 3346,
//                    "nickname": "付费恢复恢复好"
//            },
//            {
//                "id": 2149,
//                    "user_id": 3443,
//                    "nickname": "ertrt"
//            },
//            {
//                "id": 2143,
//                    "user_id": 3394,
//                    "nickname": "测试账号测试账号"
//            }
//            ],
//            "next_page_id": 0   # [0没有下一页] [>0下一页 page 值]
//        }
//    }
}
