package com.tinytiger.common.net.data.gametools;

import com.tinytiger.common.net.data.BaseBean;
import com.tinytiger.common.net.data.circle.CircleBean;
import com.tinytiger.common.net.data.video.CommentListBean;

import java.util.ArrayList;
import java.util.List;

public class GameInfoDetailBean extends BaseBean {
    public Data data;

    public class Data {

        public GameInfoBean game_info;
        public ArrayList<String> viewpoint_list;

        public String tag_cloud_url;//  #云标签 H5url
        public String app_game_details;// #游戏详情页 H5url
        public String review;// #点评详情页 H5 url
        public String comment_details;//  #游戏详情评论页 H5 url


        public List<RecommendVideoBean> recommended_video;

        public GameAssessInfo assess_info;

        public  int show_viewpoint_list;

        public CircleBean circle_info;

        public ArrayList<GameCate> game_cate;

    }
    public class GameCate {
        public String id;
        public String cate_name;
    }


 /*"circle_info": {  #3.3.0 新增圈子信息
        "id": 74, #圈子id
        "name": "lxl-绝地求生啦22", #圈子名称
        "logo": "https:\/\/cdn.tinytiger.cn\/d4178b938bf6a925697e96e379b2187.jpg", #圈子logo
        "join_people": 4, #加入人数
        "post_num": 19 #动态数
    },*/

/*     "show_tag_list": 1, #是否显示词云图:0否 1是 （兼容旧版）
            "show_viewpoint_list": 1, #是否显示词云图:0否 1是 （新版）
            "tag_cloud_url": "http://192.168.1.241/web_app/gamesShare/tagCloud.html",  #云标签 H5 url
        "app_game_details": "http://192.168.1.241/web_app/gamesShare/gameDetails.html",  #游戏详情页 H5 url
        "comment_details": "http://192.168.1.241/web_app/gamesShare/commentDetails.html",  #评价分享 H5 url
        "review": "http://192.168.1.241/web_app/gamesShare/review.html",  #发布点评 H5 url
        "recommended_video": [  #推荐视频，固定展示10条，已按照点赞数排序*/
}
