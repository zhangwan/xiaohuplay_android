package com.tinytiger.common.net.data.circle;

import java.util.ArrayList;

/**
 * @author zhw_luke
 * @date 2019/11/27 0027 下午 6:19
 * @Copyright 小虎互联科技
 * @doc 帖子bean
 * @since 5.0.0
 */
public class PostBean {


    public String id;
    // # 回答id -1:动态贴, 0:问答帖(未采纳) 大于0:问答帖(已采纳的id)
    public int answer_id;
    // #该帖子属于的模块id （用于跳转）
    public String modular_id;
    public String create_time;

    public String content;
    public int view_num;
    public int is_like;
    public ArrayList<String> img_url;

    public int view_user_num;
    public int like_num;
    public int comment_num;
    public String comment_user_num;

    public String share_num;
    public String collect_num;
    public String user_id;
    public String avatar = "";
    public int master_type ;

    public String nickname;
    public String medal_image;


    public int is_mutual;
    public String modular_name;
    public String is_post_collect;

    public String is_post_like;
    public ArrayList<CircleBean> circle_list;

    public ArrayList<CircleBean> circle_post;
    //主圈名称
    public String circle_name;

    public int participate_num;
    public boolean isSelected=false;
    public int total_hots;
    public String share_url;
    public String collect_time;
    public String circle_id;
    public String title;
    public String video_url="";
    public String cover_url="";

    //帖子状态 1:通过 0:待审核 -1:未通过
    public String status;

    // #该帖子属于的模块类型 1精华 2问答 3综合 （用于跳转）
    public String fixed_modular_type;

    public int state=1 ;//播放状态   0播放，1停止

    //播放时长
    public Long video_length;
    public int type;

    //true 从圈子详情页面进来 false 首页
    public Boolean circleType;



    public String getCircle_id() {
        if (circle_id==null){
            circle_id="0";
        }
        return circle_id;
    }

    public void setCircle_id(String circle_id) {
        this.circle_id = circle_id;
    }

/* "id": 17,
             "answer_id": -1,
             "modular_id": 12,
             "create_time": "2020-05-03 14:34:30",

             "content": "挺好挺好挺好挺好挺好挺好挺好挺好挺好挺好挺好挺好挺好挺好挺好挺好挺好挺好挺好挺好挺好挺好",
             "view_num": 0,
             "img_url": [
             "https://cdn.tinytiger.cn/FsyLWy_h67owmNmCvHcymJdyWTnH",
             "https://cdn.tinytiger.cn/Fj6mJZkap3elfJ1MyElPjoPP3v49"
             ],

             "view_user_num": 0,
             "like_num": 0,
             "comment_num": 0,
             "comment_user_num": 0,

             "share_num": 0,
             "collect_num": 0,
             "user_id": 3624,
             "user_logo": "http://cdn.tinytiger.cn/user/user1.jpg",

             "user_nickname": "大宁次大女子等你刺挠多次点那次到",
             "is_collect": 1,
             "modular_name": "问答",
             "is_post_collect": 0,

             "is_post_like": 0,
             "circle_list": [
    {
        "post_id": 17,
            "circle_id": 4,
            "name": "圈子名称4test-"
    }
                ]*/
}
