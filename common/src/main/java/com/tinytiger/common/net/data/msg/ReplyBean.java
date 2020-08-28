package com.tinytiger.common.net.data.msg;


/**
 *
 */
public class ReplyBean {


    public int id;
    public int comment_id;
    public int top_parent_id;
    public int content_id;
    public String user_id;
    public int parent_user_id;
    public String content;
    public String create_time;
    public int is_read;
    public String avatar;
    public String nickname;
    public String medal_image;
    public String medal_name;

    public int  master_type;

    public String cover;
    public String cover_content;

    public int type;

    public int  count;

    public int is_comment;
    public String comment_content;


    public String game_id;
    public String  assess_id;

    public String  post_id;
    public String video_url;

//"is_read": 0,                       # 是否显示红点 0为显示 1为不显示


    /*     "content_id": 1,                                # 作品ID
                "user_id": 3083,                                # 评论人ID
                "parent_user_id": 3083,                         # 被评论人ID
                "comment_id": 3128,                             # 评论ID
                "create_time": "2019-11-19 14:58:01",           # 评论时间
                "avatar": [                                     # 多个头像则为多人评论
                    "http://cdn.tinytiger.cn/user/user1.jpg",
                            "http://cdn.tinytiger.cn/user/user1.jpg",
                            "http://cdn.tinytiger.cn/user/user1.jpg"
                            ],
                            "nickname": "chenzitingchenziting",             # 评论人昵称
                "cover": "https://ss0.bdstatic.com/9bA1vGfa2gU2pMbfm9GUKT-w/timg?wisealaddin&sec=1573724377&di=a81ee16d3c2d62d4cedc7e41cdc8d447&quality=100&size=f242_182&src=http%3A%2F%2Fvdposter.bdstatic.com%2F196852f3d58bf9a766e1a6a199e54420.jpeg" # 作品封面
                "type": 2                                # 1=图文,2=视频
                "content": "休闲鞋"                        # 评论内容*/



//"type": 2                                # 1=图文,2=视频

            /*"parent_id": 0,                         # parent=0时为评论作品
            "content_id": 3,                        # 作品ID
            "user_id": 3,                           # 评论人ID
            "parent_user_id": 1,                    # 被评论人ID

            "content":"测试停服中\，请等待2小时                 楼上两个函数",   # 评论内容
            "create_time": "2019-11-16 12:22:56",                                # 评论时间
            "avatar": [                                                          # 多个头像则为多人评论
                "http://cdn.tinytiger.cn/user/user2.jpg",
                        "http://cdn.tinytiger.cn/user/user2.jpg",
                        "http://cdn.tinytiger.cn/user/user2.jpg"
                        ],
                        "nickname": "小傻逼",                                                 # 评论人昵称
            "cover": "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2018939532,1617516463&fm=26&gp=0.jpg"   # 作品封面*/



}
