package com.tinytiger.common.net.data.user;


import com.tinytiger.common.net.data.BaseBean;
import com.tinytiger.common.net.data.circle.CircleBean;
import com.tinytiger.common.net.data.circle.PostBean;

import java.util.ArrayList;

/**
 *
 * @author zhw_luke
 * @date 2020/5/19 0019 下午 1:53
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 用户主页-动态
 */
public class UserDynamicList extends BaseBean {
    public Data data;

    public class Data {
        public int current_page;
        public int last_page;

        public ArrayList<UserDynamicBean> data;
    }


    /*"id": 122, #帖子ID
                "create_time": "2020-05-12 14:32:19", #发布时间
                "type": 2, #类型:1=评价 2=帖子
                "answer_id": 0, #-1=动态贴 0=问答贴(未采纳) 大于0=问答贴(已采纳的id)
                "content": "测试帖子置顶", #帖子内容
                "img_url": [ #帖子图片(最多9张)
                    "https://cdn.tinytiger.cn/FoNlqfZCXqupLIldN8DtZssJztdT"
                            ],
                            "circle_list": [ #圈子列表
    {
        "circle_id": 25,
            "name": "与偶像团字体V6 的秘"
    },
    {
        "circle_id": 26,
            "name": "古龍群俠傳《遊戲酒吧"
    }
                ]*/

    /* "id": 2642134, #评价ID
                "create_time": "2020-05-12 16:13:39", #发布时间
                "type": 1, #类型:1=评价 2=帖子
                "game_id": 2, #游戏ID

                "game_name": "张晓伟永恒钻石", #游戏名称
                "logo": "https://cdn.tinytiger.cn/FhGqoYuPzmYY3lq4uCfkF1gsEYUN", #游戏logo
                "title": "合并",  #标题
                "content": "萨达是打发点撒范德萨第三方的撒范德萨啊啊第三方的说法", #内容
                "score": 3 #评分*/

}
