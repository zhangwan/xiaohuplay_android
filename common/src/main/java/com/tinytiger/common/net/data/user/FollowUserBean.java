package com.tinytiger.common.net.data.user;


import com.tinytiger.common.net.data.BaseBean;


/**
 *关注/取消用户
 */
public class FollowUserBean extends BaseBean {
    public Data data;

    public class Data{
        public int is_mutual=-1;

       //  "is_mutual": -1	#关注状态 1:互相关注 0:已关注 -1:未关注
    }


}
