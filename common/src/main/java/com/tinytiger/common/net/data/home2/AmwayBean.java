package com.tinytiger.common.net.data.home2;


import com.tinytiger.common.net.data.gametools.AssessTagBean;
import com.tinytiger.common.net.data.gametools.comment.CommentAssessBean;

import java.util.ArrayList;

/**
 * 游戏分类
 */
public class AmwayBean {


    public String id;
    public String name;
    public String title;
    public float score;
    public String game_id;

    public String game_name;
    public String user_id;
    public String content="";
    public int like_num;

    public int comment_num;
    public int share_num;
    public String nickname=" ";
    public String avatar;
    public int master_type;

    public String medal_image;
    public String medal_name;

    public int follow;
    public String thumbnail;
    public int amway_assess_num;

    public ArrayList<AssessTagBean> viewpoint_list;

    public ArrayList<CommentAssessBean> comment_list;
    public int is_like;

    public String create_time;//#收藏时间
    public String share_url;
    public String logo;
//"is_like": 1 #是否已点赞  1是 0否

    public boolean isSelected;

    public int is_cloud_game;
    public String package_name_android;

/*"id": 14, #评价ID
            "title": "张攀test发布评价专用 (勿删)", #评价标题
            "score": 4, #评分
            "game_id": 49, #游戏ID

            "game_name": "陈子婷-英雄联盟", #游戏名
            "user_id": 13, #用户ID
            "content": "o(´^｀)o\，🍎为了更好地增加用户体验\，服务器维护时间内用户将不能正常登录APP\，维护完毕后\，用户无需重新下载客户端\，直接登录即可正常使用APP,哈哈哈哈O(∩_∩)O哈哈~狗苹果<p>OGN Entus Force在艾伦格和米拉玛地图中展现了自己的绝对统治力，场均排名6.4的他们提前锁定了一张PGC门票。", #评价内容
            "like_num": 13, #点赞数

            "comment_num": 7, #评论数
            "share_num": 1, #分享数
            "nickname": "啊啊啊啊", #用户昵称
            "avatar": "http://cdn.tinytiger.cn/user/user6.jpg", #用户头像

            "follow": -1, #关注状态: -1未关注 0已关注 1相互关注
            "thumbnail": "https://cdn.tinytiger.cn/FhGehOm9Rq4zIRpRNjl5qKj_gKgH", #游戏缩略图
            "amway_assess_num": 13, #安利数
            "tag_list": [ #评价标签列表
    {
        "name": "英雄联盟A",
            "like_num": 12
    },
    {
        "name": "电竞",
            "like_num": 10
    },
    {
        "name": "只拿一血",
            "like_num": 10
    },
    {
        "name": "送人头",
            "like_num": 1
    },
    {
        "name": "英雄联盟E",
            "like_num": 0
    }
            ],*/
}
