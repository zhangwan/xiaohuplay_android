package com.tinytiger.common.net.data.circle;

import com.tinytiger.common.net.data.AdBean;
import com.tinytiger.common.net.data.BaseBean;
import com.tinytiger.common.net.data.UserInfo;
import com.tinytiger.common.net.data.wiki.BannerBean;

import java.util.ArrayList;

/**
 * @author zhw_luke
 * @date 2019/11/27 0027 下午 6:19
 * @Copyright 小虎互联科技
 * @doc
 * @since 5.0.0
 */

public class CircleInfoBean extends BaseBean {

    public Data data;

    public class Data {

        public gameInfo game_info;
        public ArrayList<AdBean> banner;
        public circleInfo circle_info;

        public Notice notice;
        public ArrayList<Notice> top_post_list;
        public ArrayList<tabModular> tab_modular;
        public ArrayList<tabModular> tag_modular;

        public ArrayList<UserInfo> circle_admin_list;


        public class gameInfo {
            //游戏id
            public String id;
            public String logo;
            public String background;
            public String name;
            //游戏一句话的简介
            public String one_introduce;
            //#圈子模块状态 0关闭 1开启 后续可能废弃
            public int circle_status;
            // #百科模块状态 0关闭 1开启
            public String wiki_status;
            //资讯模块状态 0关闭 1开启
            public String information_status;
            //百科页面状态 1.百科详情页界面 2.招募管理员界面 3.默认点亮百科
            public String modular_id;
            //申请管理员状态 -2：拒绝(可以重新发起申请) -1：拒绝(不可继续申请) 0：未申请 1：审核通过 2：审核中
            public int apply_status;
        }

        public class circleInfo {
            public String id;
            public int add_circle_num;
            public int circle_post_num;
            public String share_url;
            public int is_join;

            public String name;
            public String background;
            public String logo;
            //#申请圈子管理员  1=可申请  2=不可申请
            public String apply_status;
            //  #0默认 1开启申请管理员招募模块，优先判断该字段，再判断apply_status字段
            public String is_opened_admin;
            //申请管理员地址
            public String apply_admin_url;
        }

        public class Notice {
            public String id;
            public String content;
        }

        public class tabModular {
            public String id;
            public String name;
            public int is_show;
            public String tag_type;

            public int is_type;
             /*"id": 5,                #分类id
                "name": "banner模块",     #分类名称
                "is_show": 1            #是否显示 0隐藏 1 显示*/
        }

/*"game_info": {      #游戏详情数据集合
            "logo": "https://cdn.tinytiger.cn/FhGqoYuPzmYY3lq4uCfkF1gsEYUN",            #游戏logo
            "background": "https://cdn.tinytiger.cn/FhGehOm9Rq4zIRpRNjl5qKj_gKgH",      #游戏背景
            "name": "lxl-绝地求生啦",                                                    #游戏名称
            "circle_status": 0,                                                         #圈子模块状态 0未开启 1已开启
            "one_introduce": "一句话要超过10 个字一句话要超过10 个字符一句话要超"               #游戏一句话简介
        },
                "banner": [             #banner数据集合
        {
            "id": 5,                #banner id
            "circle_id": 3,         #圈子id
            "jump_type": 0,         #跳转方式  0=无跳转 1=外部 2=功能页面 3=游戏详情 4=百科详情 5=词条 6=文章 7=视频
            "jump_url": "",         #跳转地址 当jump_type=3|4时为游戏id  5时为词条id 6|7时为资讯id
            "img_url": "https://www.baidu.com/img/dong_f6764cd1911fae7d460b25e31c7e342c.gif"  #banner 图片
        },
        {
            "id": 4,
                "circle_id": 3,
                "jump_type": 7,
                "jump_url": "156",
                "img_url": "http://www.baidu.com"
        },
        {
            "id": 3,
                "circle_id": 3,
                "jump_type": 6,
                "jump_url": "160",
                "img_url": "http://www.baidu.com"
        }
        ],
                "circle_info": {        #圈子基础信息集合
            "id": 3,            #圈子id
            "add_circle_num": 0,        #加入该圈子人数
            "circle_post_num": 13       #圈子内容数
        },
                "notice": {     #公告数据集合
            "id": 3,        #公告id
            "content": "这是内容2"  #公告内容
        },
                "top_post_list": [      #置顶帖子
        {
            "id": 5,        #帖子id
            "content": "再来再来3🍎😔❤🍎😔❤🍎😔❤"  #帖子内容
        },
        {
            "id": 7,
                "content": "发一个正式帖 不发草稿啦再来再来3🍎😔❤🍎😔❤🍎😔❤"
        }
        ],
                "tab_modular": [        #圈子详情页模块数据集合
        {
            "id": 5,                #分类id
            "name": "banner模块",     #分类名称
            "is_show": 1            #是否显示 0隐藏 1 显示
        },
        {
            "id": 6,
                "name": "公告模块",
                "is_show": 1
        },
        {
            "id": 7,
                "name": "置顶模块",
                "is_show": 1
        },
        {
            "id": 8,
                "name": "tab模块",
                "is_show": 1
        }
        ],
                "tag_modular": [        #内容tag模块
        {
            "id": 11,           #内容模块id
            "name": "精华",       #模块名称
            "is_show": 1       #帖子分类模块 0隐藏 1显示
        },
        {
            "id": 13,
                "name": "达人"
        },
        {
            "id": 12,
                "name": "问答"
        },
        {
            "id": 10,
                "name": "test-模块"
        }
        ],
                "circle_admin_list": [      #圈子管理员用户数据集合
        {
            "nickname": "Justin Bieber 03 01 生日快乐💍🌸",       #圈子管理员用户昵称
            "avatar": "http://cdn.tinytiger.cn/user/user4.jpg", #圈子管理员用户头像
            "user_id": "13"  #圈子管理员用户id
        },
        {
            "nickname": "Ryan你仿佛就能发你发你发你发",
                "avatar": "http://cdn.tinytiger.cn/user/user4.jpg",
                "user_id": "983"
        }
        ]*/

    }

}
