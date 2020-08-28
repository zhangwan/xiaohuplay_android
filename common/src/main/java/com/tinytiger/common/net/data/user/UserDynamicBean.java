package com.tinytiger.common.net.data.user;


import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tinytiger.common.net.data.circle.CircleBean;

import java.util.ArrayList;

/**
 * @author zhw_luke
 * @date 2020/5/19 0019 下午 1:53
 * @Copyright 小虎互联科技
 * @doc 用户主页-动态
 * @since 3.0.0
 */
public class UserDynamicBean implements MultiItemEntity {

    public String id;
    public String create_time;
    public int type;
    public String game_id;

    public String game_name;
    public String logo;
    public String title;
    public String content;
    public int score;

    public int answer_id;

    public ArrayList<String> img_url;
    public ArrayList<CircleBean> circle_list;
    public String modular_name;
    public String circle_id;
    public String modular_id;
    public String circle_name;
    public int participate_num;

    public int is_like;
    public int like_num;
    public int comment_num;
    public String share_url;
/* "share_url": "http://192.168.1.241/web_app/commentSharing/commentDetail.html", #分享链接
                "like_num": 2, #点赞数
                "comment_num": 1 #评论数
                "is_like": -1, #是否点赞 1:是 -1:否*/

    // #该帖子属于的模块类型 1精华 2问答 3综合 （用于跳转）
    public String fixed_modular_type;
    public String video_url="";
    public String cover_url="";

    //帖子状态 1:通过 0:待审核 -1:未通过
    public String status;
    public Long video_length;
    @Override
    public int getItemType() {
        return type;
    }

    /*"id": 122, #帖子ID
                "create_time": "2020-05-12 14:32:19", #发布时间
                "type": 2, #类型:1=评价 2=帖子
                "answer_id": 0, #-1=动态贴 0=问答贴(未采纳) 大于0=问答贴(已采纳的id)
                "content": "测试帖子置顶", #帖子内容
                "img_url": [ #帖子图片(最多9张)
                    "https://cdn.tinytiger.cn/FoNlqfZCXqupLIldN8DtZssJztdT"
                            ],
                            "circle_list": [ #圈子列表
    {
        "circle_id": 25,
            "name": "与偶像团字体V6 的秘"
    },
    {
        "circle_id": 26,
            "name": "古龍群俠傳《遊戲酒吧"
    }
                ]*/

    /* "id": 2642134, #评价ID
                "create_time": "2020-05-12 16:13:39", #发布时间
                "type": 1, #类型:1=评价 2=帖子
                "game_id": 2, #游戏ID

                "game_name": "张晓伟永恒钻石", #游戏名称
                "logo": "https://cdn.tinytiger.cn/FhGqoYuPzmYY3lq4uCfkF1gsEYUN", #游戏logo
                "title": "合并",  #标题
                "content": "萨达是打发点撒范德萨第三方的撒范德萨啊啊第三方的说法", #内容
                "score": 3 #评分*/

}
