package com.tinytiger.common.net.data.gametools.comment;


import com.tinytiger.common.net.data.UserInfo;

import java.util.ArrayList;

/**
 * @author zhw_luke
 * @date 2019/11/27 0027 下午 6:19
 * @Copyright 小虎互联科技
 * @doc 评论
 * @since 5.0.0
 */
public class CommentAssessBean {

    public String is_top;
    public int id;
    public String user_id;
    public String nickname = "";

    public String medal_image;
    public String medal_name;
    public String netease_id;
    public String parent_id;
    public String avatar;
    public String create_time;

    public int is_like;
    public String lz_user_id;
    public String content = "";
    public int comment_user_num;

    public int like_num;
    public int comment_num;
    public int  master_type;
    public ArrayList<CommentAssessBean> replys;

    public UserInfo commentUserinfo;
    public UserInfo replysUserinfo;

        /*"commentUserinfo" : {
        "user_id": 31,             #id
        "nickname": "用户_6801",    #昵称
        "netease_id": "c8179fb25982c6e",        #云信id
    }
                    "replysUserinfo": {
        "user_id": 31,
                "nickname": "用户_6801",
                "netease_id": "c8179fb25982c6e",
    },*/


         /*"is_top": 2,        #(查看二级评论||未登录时 无此字段)是否置顶 1:否,2:是
                "id": 3049,         #评论id
                "user_id": 3,       #评论人id
                "nickname": "小傻逼",  #评论人昵称

                "netease_id": "c8179fb25982c6e",        #评论人云信id
                "parent_id": 0,
                        "avatar": "http://cdn.tinytiger.cn/user/user2.jpg", #评论人头像
                "create_time": "2019-11-16 13:53:04",   #评论时间

                "is_like": -1,  #是否点赞该内容1:是,-1:否
                "lz_user_id": 3298,  #楼主user_id
                "content":"777722222试停服中\，请等待2小时", #评论内容
                "comment_user_num": 0,  #评论的用户数

                "like_num": 0,  #点赞数
                "comment_num": 0,   #二级评论数      [查看二级评论时 无此字段]
            "is_like": -1       #(登录才有此字段)登录用户是否点赞该内容1:是,-1:否
                "replys": []    #二级评论列表      [查看二级评论时 无此字段]*/

       /*  "is_top": 1,
                 "id": 2,
                 "user_id": 983,
                 "nickname": "Ryan",

                 "parent_id": 0,
                 "avatar": "http://cdn.tinytiger.cn/user/user1.jpg",
                 "create_time": "2019-11-15 16:34:59",
                 "is_like": -1,  #是否点赞该内容1:是,-1:否
                "lz_user_id": 3298,  #楼主user_id
                "content": "xxx",
                        "like_num": 0,
                        "comment_num": 4,*/


}
