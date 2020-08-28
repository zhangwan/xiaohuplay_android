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
public class SelectCircler1Bean extends BaseBean {

    public ArrayList<Data> data;

    public class Data {
        public String id;
        public String name;
        public ArrayList<SelectCirclerInfoBean> circle_info;


 /*"id": 6,        #分类id
            "name": "热门",   #分类名称
            "circle_info": [    #该分类下的圈子集合
        {
            "id": 1,        #圈子id
            "name": "圈子名称1",    #圈子分类
            "logo": " " #圈子图标
        }
            ]*/
    }

}
