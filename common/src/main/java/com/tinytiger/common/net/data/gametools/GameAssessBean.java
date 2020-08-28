package com.tinytiger.common.net.data.gametools;

import com.tinytiger.common.net.data.BaseBean;

import java.util.ArrayList;

/**
 *
 * @author zhw_luke
 * @date 2019/11/27 0027 下午 6:19
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc
 */
public class GameAssessBean extends BaseBean {

    public Data data;

    public class Data{
        public String id;
        public String user_id;
        public String title;
        public float score;

        public String content;
        public String assess_radar_chart_url;
        public int comment_num;
        public int like_num;
        public int collect_num;

        public int share_num;
        public String create_time;
        public String thumbnail;
        public String background;
        public String name;

        public int assess_num;
        public String netease_id;
        public String nickname;
        public String avatar;

        public int is_mutual=-1;
        public int is_like=-1;
        public int is_collect=-1;
        public ArrayList<AssessTagBean> viewpoint_list;

        public int is_black;
        public String view_log_id;

        public String share_url;
        public String logo;
        public String medal_name;
        public String medal_image;

        public int master_type;
        /*"id": 14,           #评价id
        "user_id": 13,      #评价人id
        "title": "张攀test发布评价专用 (勿删)", #评价标题
        "score": 4, #评价得分

        "content": "<p>分类：</p>", #评价正文
        "comment_num": 1,   #评价评论数
        "like_num": 11,     #评价点赞数
        "collect_num": 0,   #评价收藏数

        "share_num": 0,     #评价分享数
        "create_time": "2020-02-26 22:17:16", #评价评价时间
        "background": "https://cdn.tinyt",   #评价所属游戏的背景图
        "name": "英雄联盟1",        #评价所属游戏名称

        "assess_num": 0,        #游戏下的评价数
        "netease_id": "8c60ffb7fe167c8a", #评价人云信账号
        "nickname": "啊啊啊啊",   #评价昵称
        "avatar": "http://cdn.tinytiger.cn/user/user1.jpg", #评价头像

        "is_mutual": -1, #(登录才有此字段) 登录用户与评价人的关注关系 1:互相关注 0:已关注 -1:未关注 -2:自己
        "is_like": -1, # (登录才有此字段)登录者是否赞过此评价	-1:否1:是
        "is_collect": -1, # (登录才有此字段)登录者是否赞过此评价	-1:否1:是
        "assess_tag": [
        {
            "id": 112,  #评价标签id
            "name": "电竞", #评价名称
            "is_like": 1    #登录人是否点赞过 -1:否1:是
        },
        {
            "id": 142,
                "name": "只拿一血",
                "is_like": -1
        }
        ],
                "is_black": -1, (登录才有此字段)内容作者是否在登录用户的黑名单里1:是,-1:否
        "view_log_id": "5" #评价浏览记录的id*/



    }

}
