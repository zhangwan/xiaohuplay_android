package com.tinytiger.common.net.data.circle.postsend;

import com.tinytiger.common.net.data.BaseBean;

import java.util.ArrayList;

/**
 * @author zhw_luke
 * @date 2019/11/27 0027 下午 6:19
 * @Copyright 小虎互联科技
 * @doc
 * @since 5.0.0
 */
public class DraftDetailBean extends BaseBean {

    public Data data;

    public class Data {
        public int id;
        public int type;
        public int post_id;
        public String content;

        public String circle_id;
        public String circle_name;
        public String modular_id;
        public String modular_name;
        public String title;
        public String cover_url;
        public String video_url;
        public String video_length;

        public ArrayList<String> img_url;
        public ArrayList<SelectTopicBean> circle_ids;
        public ArrayList<String> circle_names;


        public ArrayList<FriendBean> user_ids;

/*"id": 45, #帖子草稿id
    "type": 0, #帖子类型  -1:动态贴, 0:问答帖
    "create_time": "2020-05-09 14:23:00", #草稿保存时间
    "content": "发一222个正式帖 不发草稿啦再来再来3🍎😔❤🍎😔❤🍎😔❤", #草稿内容
    "is_deleted": 0,#是否删除 0=否 1=是
    "user_id": 4405, #发贴人
    "img_url": null, #图片
    "update_time": "2020-05-09 16:25:49", #更新时间
    "modular_id": 0, 所属模块
    "circle_name": "test-", #所属圈子名称
    "modular_name": "攻略", # 所属模块名称
    "circle_ids": [ #关联已有话题圈
        {
            "id": 1, #圈子id
            "name": "圈子名称1" #圈子名称
        }
    ],
            "circle_names": [ #新话题圈
      "啦啦啦", #圈子名称
      "lxl-绝地求生啦22"
              ],
              "user_ids": [ #当type != -1时,被邀请回答用户
        {
            "user_id": 13, #用户id
            "nickname": "Justin Bieber 03 01 生日快乐💍🌸" #用户昵称
        }
    ]*/
    }

}
