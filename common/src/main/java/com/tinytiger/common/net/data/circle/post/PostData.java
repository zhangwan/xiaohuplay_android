package com.tinytiger.common.net.data.circle.post;

import java.util.ArrayList;

/**
 * @author zhw_luke
 * @date 2019/11/27 0027 下午 6:19
 * @Copyright 小虎互联科技
 * @doc 帖子bean
 * @since 5.0.0
 */
public class PostData {


    public String id;
    public String post_id;
    public String content;
    public String user_id;

    public int is_like;
    public int like_num;
    public String create_time;
    public int comment_num;
    public String nickname;

    public String avatar;
    public String medal_name;
    public String medal_image;
    public String master_id;

    public int master_type;
    //public ArrayList<PostData> son_comment_list;
    public ArrayList<PostData> reply_list;


    public String reply_nickname;
    public int reply_count;

    public int is_black;

    public String circle_id;

    public String getCircle_id() {
        if (circle_id==null){
            circle_id="0";
        }
        return circle_id;
    }

    public void setCircle_id(String circle_id) {
        this.circle_id = circle_id;
    }
    /*"id": 10752,    #评论id
            "content": "这些优秀的暗黑类游戏，比如恐怖黎明，包括游戏跟暗…", #评论内容
            "post_id": 17,
                    "user_id": 983, #被采纳的内容发布人id
            "like_num": 0,      #点赞数
            "create_time": "2020-05-03 14:36:55",   #发布时间
            "comment_num": 2,   #评论数
            "nickname": "Ryan你仿佛就能发你发你发你发", #被采纳的内容发布人昵称
            "avatar": "http://cdn.tinytiger.cn/user/user4.jpg",#被采纳的内容发布人头像
            "medal_name": "开发勋章", #勋章名称
            "medal_image": "http:\/\/cdn.tinytiger.cn\/user\/user3.jpg",#勋章logo
            "master_id": 0, #达人认证id
            "master_type": 0, #达人认证方式 0=普通 1=站内 2=站外
            "son_comment_list": [   #子评论数据集合*/

            /*"id": 10767,        #子评论id
                    "parent_user_id": 983,
                            "top_parent_id": 10752,
                            "content": "用户983第二次追加评论🍎😔❤",  #子评论内容
                    "post_id": 17,
                            "user_id": 983, #子评论发布人id
                    "like_num": 0,
                            "create_time": "2020-05-06 14:41:06",   #子评论发布时间
                    "nickname": "Ryan你仿佛就能发你发你发你发",     #子评论发布人昵称
                    "avatar": "http://cdn.tinytiger.cn/user/user4.jpg",#子评论发布人头像
                    "medal_name": "开发勋章", #勋章名称
                    "medal_image": "http:\/\/cdn.tinytiger.cn\/user\/user3.jpg", #勋章logo
                    "master_id": 0,#达人认证id
                    "master_type": 0, #达人认证方式 0=普通 1=站内 2=站外
                    "reply_nickname": null*/
}
