package com.tinytiger.common.net.data.circle.home;

import com.tinytiger.common.net.data.BaseBean;
import com.tinytiger.common.net.data.UserInfo;
import com.tinytiger.common.net.data.circle.PostBean;

import java.util.ArrayList;

/**
 * @author zhw_luke
 * @date 2019/11/27 0027 下午 6:19
 * @Copyright 小虎互联科技
 * @doc
 * @since 5.0.0
 */
public class CircleAttentionUserBean extends BaseBean {

    public Data data;

    public class Data {
        public ArrayList<UserInfo> data;
        public int total;
        public int current_page;
        public int last_page;



/*"id": 25, #圈子ID
                "logo": "https://cdn.tinytiger.cn/94fa3cadf81a540b58ec8e10dfdedc5a.png", #logo
                "name": "与偶像团体V6 的秘", #名称
                "add_circle_num": 0, #加入人数
                "comment_num": 0 #评论数*/

    }

}
