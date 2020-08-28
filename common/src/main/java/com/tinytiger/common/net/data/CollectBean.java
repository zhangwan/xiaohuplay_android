package com.tinytiger.common.net.data;



/**
 *
 * @Author luke
 * @Date 2020-05-14 21:29
 * @Copyright 小虎互联科技
 * @since 1.1.0
 * @Des  是否收藏
 *
 */
public class CollectBean extends BaseBean{
    public Data data;

    public class Data {
        public int is_collect;
        // "is_collect": -1    #是否(收藏)关注 1:是 -1:否
    }


}
