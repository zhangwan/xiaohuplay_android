package com.tinytiger.common.net.data;


/**
 *
 */
public class AdBean {

    public String id;
    public String title;
    public String image;
    public String sort;

    public int jump_type;
    public String jump_url;
    public String jump_view;

    public String position_id;
    public String content_type;

    public String start_time;
    public String end_time;
    public Extend extend;

    public class Extend {
        public int duration;
        public String button;
        public String button_color;
        public String text_color;
    }

  /*   "extend": { # 开屏配置 无数据返回空字符串
        "duration": 5 # 开屏停留时长(秒)
    },*/
/*     "id": 147,  #广告ID
                "title": "2020619修改弹窗2", #广告名称
                "image": "https://cdn.tinytiger.cn/Fu_mFAgA_m6mw1jVVcd92OmI5vLc", # 广告图片
                "jump_url": "", #广告跳转链接
                "jump_type": 4, #事件类型:0无事件1外部url跳转 2内部url跳转 3跳转文章/视频 4跳转游戏 5跳转百科 6跳转词条 7跳转帖子
                "jump_view": "786731", #功能页面标识:事件类型为3||4||5||6||7时有返回(大于0为对应的详情ID)
                "start_time": "2020-06-19 00:00:00", # 广告开始时间
                "end_time": "2020-06-29 00:00:00", # 广告结束时间
                "extend": {  # 弹窗配置 无数据返回空字符串
        "button": "打开", # 按钮名称
        "button_color": "FF0000", #按钮颜色
        "text_color": "FFFFFF" # 按钮字体颜色
    },
            "content_type": 0 #内容类型:jump_view大于0时返回(1为图文,2为视频) 目前只有jump_type=3返回1或2*/


                 /*"jump_type": 3, //事件类型:0无事件1外部url跳转 2内部url跳转 3内部功能页面跳转
                         "jump_view": "6" //功能页面标识:事件类型为3时有返回(大于0为内容详情ID,0为游戏头条)
                         "content_type":2 //内容类型:jump_view大于0时返回(1为图文,2为视频)*/


         /*"jump_url": null, //广告跳转链接
                 "jump_type": 3, //事件类型:0无事件1外部url跳转 2内部url跳转 3内部功能页面跳转
                 "jump_view": "game_headline" //功能页面标识:事件类型为3时有返回(现在暂时只有:content内容详情页,game_headline游戏头条)*/



/* "id": 4,     //广告ID
         "title": "六哥,K神,整活哥你最喜欢谁?",     //广告标题
         "image": "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2018939532,1617516463&fm=26&gp=0.jpg",  //广告图片
         "sort": 4,  //广告排序
         "jump_url": "http://www.baidu.com"  //广告跳转链接*/


}
