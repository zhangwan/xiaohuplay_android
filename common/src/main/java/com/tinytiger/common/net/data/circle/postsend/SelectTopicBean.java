package com.tinytiger.common.net.data.circle.postsend;


/**
 * @author zhw_luke
 * @date 2019/11/27 0027 下午 6:19
 * @Copyright 小虎互联科技
 * @doc
 * @since 5.0.0
 */
public class SelectTopicBean  {

        public int id;
        public String name;


        public SelectTopicBean(int id) {
                this.id = id;
        }

        public SelectTopicBean(String name) {
                this.id = -1;
                this.name = name;
        }

        public SelectTopicBean() {

        }
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
