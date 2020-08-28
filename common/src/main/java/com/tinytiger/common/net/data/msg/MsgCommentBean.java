package com.tinytiger.common.net.data.msg;


/**
 *
 */
public class MsgCommentBean {


    public int id;
    public String create_time;
    public int has_badge;
    public String top_parent_user_id;
    public String content;
    public int like_num;
    public int comment_num;
    public int is_like=-1;
    public int  is_black=-1;

    public int  is_author;
    public int  is_lz;
   //  "is_author": 1,  #是否是内容作者1:是,-1:否
   //             "is_lz": 1,  #是否是楼主1:是,-1:否
   public int   top_parent_id;

    public UserBean replysUserinfo;
    public UserBean commentUserinfo;

    public String game_id;

    public MsgCommentBean() {

    }
    public MsgCommentBean(int id, String create_time, String content, UserBean replysUserinfo) {
        this.id = id;
        this.create_time = create_time;
        this.content = content;
        this.replysUserinfo = replysUserinfo;
    }

/* "id": 281,          #评论id
            "user_id": 3446,    #评论人id
            "parent_id": 0,
             "top_parent_id": 0,

             "parent_user_id": 3298,
             "top_parent_user_id": 0,
             "create_time": "2019-12-04 22:51:17",       #评论时间
            "has_badge": 0,         #是否展示徽章 1:是  0:否

            "content": "发财",        #评论内容
            "like_num": 0,          #点赞数
            "comment_num": 0,       #子评论条数
            "is_like": -1,           #是否点赞该内容1:是,-1:否

            "replysUserinfo": {
                 "user_id": 3328,
                "nickname": "洗衣液",   #评论人昵称
                "netease_id": "084e491c1f29138a3585fb6599dd4309",   #评论人云信id
                "avatar": "https://cdn.tinytiger.cn/FrjIGvkcAYz__jlBv_dKR0MxHOwp",  #评论人头像*/
    /* "id": 676,  #评论id
        "user_id": 3451,
         "parent_id": 666,
         "parent_user_id": 3471,

          "create_time": "2019-12-14 11:44:33",
         "top_parent_user_id": 3298,
          "has_badge": 0,         #是否展示徽章 1:是  0:否
          "content":"666啥663啥啥366试停服中\，请等待2小时   全部              楼上两个函数",  #评论内容

                "like_num": 0,  #点赞数
                "is_like": -1,  #是否点赞该内容1:是,-1:否

     "commentUserinfo": {#被回复人       [回复的一级评论时无此字段, 回复的子级评论时有此字段]
        "user_id": 3471,             #id
        "nickname": "用户_6801",    #昵称
        "netease_id": "c8179fb25982c6e",        #云信id
        "avatar": "http://cdn.tinytiger.cn/user/user4.jpg"
    },
    "replysUserinfo": {#回复人
        "user_id": 3451,           #id
        "netease_id": "c8179fb25982c6e",        #云信id
        "nickname": "小傻逼",    #昵称
        "avatar": "http://cdn.tinytiger.cn/user/user4.jpg"*/


}
