package com.tinytiger.common.net.data.circle.post;

import com.tinytiger.common.net.data.BaseBean;
import com.tinytiger.common.net.data.UserInfo;
import com.tinytiger.common.net.data.circle.CircleBean;

import java.util.ArrayList;

/**
 * @author zhw_luke
 * @date 2019/11/27 0027 下午 6:19
 * @Copyright 小虎互联科技
 * @doc
 * @since 5.0.0
 */
public class PostDetailsBean extends BaseBean {

    public Data data;

    public class Data {

        public String id;
        public String create_time;
        public String content;
        public int view_num;

        public String user_id;
        public int comment_num;
        public int like_num;
        public ArrayList<String> img_url;

        public int answer_id;
        public ArrayList<CircleBean> circle_post;
        public UserInfo user_info;
        public int is_collect;

        public int is_like;
        public int is_mutual;
        public PostData answer_info;
        public String share_url;
        public int is_black;
        public PostData son_comment_list;

        //关联主圈的id
        public String circle_id;

        //#关联的模块名称
        public String modular_name;
        //#关联的模块id
        public String modular_id;
        //#关联的主圈子名称
        public String circle_name;
        //参数回答人数
        public String participate_num;

        //视频地址
        public String video_url;
        //帖子状态 1:通过 0:待审核 -1:未通过
        public String status;

        public Long video_length;


        //标题
        public String title;

        public String getCircle_id() {
            if (circle_id==null){
                circle_id="0";
            }
            return circle_id;
        }

        public void setCircle_id(String circle_id) {
            this.circle_id = circle_id;
        }

        /* "id": 12,       #帖子详情
        "create_time": "2020-05-03 11:42:46",   #发布时间
        "content": "再再在发一个正式问答帖 不发草稿啦再来再来3🍎😔❤🍎😔❤🍎😔❤", #帖子内容
        "view_num": 2,      #浏览数
        "user_id": 13,
        "comment_num": 0,   #评论数
        "like_num": 0,      #点赞数
        "img_url": "[\"https://cdn.tinytiger.cn/FrMQzoH\",\"https://cdn.tinytiger.cn/FgfhVW\"]", #图片集合 json

        "answer_id": 0, -1:普通(动态)帖  0:问答帖(未采纳) 大于0:问答帖(已采纳的id)
        "circle_post": [    #话题圈集合
        {
            "id": 2,    #圈子id
            "name": "test-圈子名称2(勿删)"    #圈子名称
        },
        {
            "id": 3,
                "name": "test-"
        }
        ],
            "user_info": {  #发布人信息
            "user_id": 13,      #发布人id
            "nickname": "Justin Bieber 03 01 生日快乐💍🌸", #发布人昵称
            "netease_id": "6de78d29ee28a",   #发布人云信id
            "avatar": "http://cdn.tinytiger.cn/user/user4.jpg",  ##发布人头像
            "medal_name": "", #勋章名称
            "medal_image": "",#勋章logo
            "master_id": 0, #达人认证id
            "master_type": 0 #达人认证方式 0=普通 1=站内 2=站外
        },
        "is_collect": 0,    #登录才有此字段 是否收藏(关注) 1:是 0:否
        "is_like": 0,       #登录才有此字段 是否点赞1:是 0:否
        "answer_info": {   #问答帖才有此字段 被采纳的回答信息 (当is_black=1时 只有user_id,nickname,avatar,is_black字段)
            "id": 10752,    #评论id
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
            "son_comment_list": [   #子评论数据集合
            {
                "id": 10767,        #子评论id
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
                "reply_nickname": null
            },
            {
                "id": 10766,
                    "parent_user_id": 983,
                    "top_parent_id": 10752,
                    "content": "用户983第二次追加评论🍎😔❤🍎😔❤🍎😔❤",
                    "post_id": 17,
                    "user_id": 983,
                    "like_num": 0,
                    "create_time": "2020-05-04 14:41:06",
                    "nickname": "Ryan你仿佛就能发你发你发你发",
                    "avatar": "http://cdn.tinytiger.cn/user/user4.jpg",
                    "medal_name": "开发勋章",
                    "medal_image": "http:\/\/cdn.tinytiger.cn\/user\/user3.jpg",
                    "master_id": 0,
                    "master_type": 0,
                    "reply_nickname": null
            }
            ],
            "is_black": -1      #被采纳问答的作者是否被帖子作者拉黑 1:是 ,-1:否
        },
                "share_url": "http://192.168.1.241//post_info_url" #分享地址*/


    }

}
