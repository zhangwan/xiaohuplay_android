package com.tinytiger.common.net.data.circle.postsend;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * @author zhw_luke
 * @date 2019/11/27 0027 下午 6:19
 * @Copyright 小虎互联科技
 * @doc
 * @since 5.0.0
 */
public class DraftBean implements Serializable {


    public int id;
    public String create_time;
    public String content;
    public String title;
    public String cover_url;
    public String video_url;

    public ArrayList<String> img_url;
     /*   "total": 1,     #总条数
        "per_page": 20, #每页显示条数
        "current_page": 1,  #当前页
        "last_page": 1,     #最后一页
        "data": [
        {
            "id": 1,        #帖子id
            "content": "123🍎😔❤🍎😔❤🍎😔❤",       #帖子内容
            "create_time": "2020-05-02 18:29:42"    #时间
        }
        ]*/


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
