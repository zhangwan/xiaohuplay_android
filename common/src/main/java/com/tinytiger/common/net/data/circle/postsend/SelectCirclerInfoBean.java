package com.tinytiger.common.net.data.circle.postsend;


/**
 * @author zhw_luke
 * @date 2019/11/27 0027 下午 6:19
 * @Copyright 小虎互联科技
 * @doc
 * @since 5.0.0
 */
public class SelectCirclerInfoBean {

        public int id;
        public String name;
        public int hots;
        public String background;


        public int position;

        public SelectCirclerInfoBean(String name) {
                this.id = -1;
                this.name = name;
                this.background = "https://cdn.tinytiger.cn/34d03343a98aefe7fa91b8c90123515.jpg";
        }
        public SelectCirclerInfoBean() {

        }

        /*"id": 3,        #圈子id
                "name": "test-",    #圈子名称
                "extra_hots": 0,    #自定义热度值
                "comment_num": 0,   #圈子评论数
                "logo": " "         #圈子图标*/




}
