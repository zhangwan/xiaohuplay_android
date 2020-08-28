package com.tinytiger.common.net.data.home;


/**
 *
 */
public class NewsInfoBean {


    public String id;
    public String title;
    public int type;
    public String user_id;

    public String cover;
    public String video_url;
    public int comment_num;
    public String introduce;

    public int like_num;
    public int view_num;
    public long video_length;
    public String nickname;

    public String avatar;
    public int is_mutual=-1;
    public int is_like=-1;
    public int is_collect;

    public String create_time;

    public String share_url;

               /* "id": 39,           #内容id
                "title": "7777777", #内容标题
                "type": 2,          #类型1为图文 2为视频
                "user_id": 3,       #发布人id

                "cover": "http://cdn.tinytiger.cn/user/user1.jpg",      #封面地址
                "video_url": "http://cdn.tinytiger.cn/user/user1.jpg",  #视频地址url
                "comment_num": 0,           #评论数
                "introduce": "陈子婷文章视频简介-属于非精选资讯",       #视频简介或文章内容

                "like_num": 0,              #点赞数
                "view_num": 100,            #浏览次数
                "video_length": 100,                    #视频时长
                "nickname": "chenzitingchenziting",     #发布人昵称

                "avatar": "http://cdn.tinytiger.cn/user/user1.jpg", #发布人头像
                "is_mutual": -1,    #(登录才有此字段)登录用户与发布人的关注关系 1:互相关注 0:已关注 -1:未关注 -2:自己
                "is_like": -1       #(登录才有此字段)登录用户是否点赞该内容1:是,-1:否*/
    //  "is_collect": -1    #(登录才有此字段)登录用户是否收藏该内容 1:是,-1:否
}
