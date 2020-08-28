package com.tinytiger.common.net.data.user;


import com.tinytiger.common.net.data.BaseBean;

import java.util.ArrayList;


/**
 *关注/取消用户
 */
public class FriendListBean extends BaseBean {
    public Data data;

    public class Data{
        public int current_page;
        public int last_page;

        public ArrayList<FriendBean> data;



    }


}
