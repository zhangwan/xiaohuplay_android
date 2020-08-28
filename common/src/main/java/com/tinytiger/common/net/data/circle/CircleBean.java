package com.tinytiger.common.net.data.circle;

/**
 * @author zhw_luke
 * @date 2019/11/27 0027 下午 6:19
 * @Copyright 小虎互联科技
 * @doc 圈子bean
 * @since 5.0.0
 */
public class CircleBean {


    public String id;
    public String one_introduce;
    public String name;
    public String game_id;


    public String background;
    public String logo;
    public int hots;


    public int is_show;
    public int is_deleted;
    public String create_time;
    public String update_time;

    public int add_circle_num;
    public int comment_num;

    public String post_id;
    public String circle_id;

//"is_join": 1,       #是否加入圈子 1加入 0未加入
    public int is_join;

    public int join_people;
    public int post_num;
    /* "join_people": 4, #加入人数
        "post_num": 19 #动态数*/
   /*       "id": 3, #圈子ID
            "name": "test-", #圈子名称
            "one_introduce": "", #简介
            "game_id": 0, #游戏ID

            "background": " ", #背景图片
            "logo": " ", #logo图片
            "hots": 33, #热度值
            "extra_hots": 0, #额外热度值

            "is_show": 1,
            "is_deleted": 0,
            "create_time": "-0001-11-30 00:00:00", #创建时间
            "update_time": "2020-05-02 14:53:52",

             "add_circle_num": 0, #加入人数
            "comment_num": 0 #评论数*/
}
