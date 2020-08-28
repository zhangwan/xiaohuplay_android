package com.tinytiger.common.net.data.user;


import com.tinytiger.common.net.data.BaseBean;


/**
 *
 */
public class LikeBean extends BaseBean {
    public Data data;

    public class Data{
        public int is_like;
        //"is_like" : 1   是否点赞了   1:点赞了 -1:未点赞
        public int is_collect;
    }


}
