package com.tinytiger.common.net.data.home;


import com.tinytiger.common.net.data.BaseBean;

import java.util.ArrayList;

/**
 *
 */
public class HeadLineListBean extends BaseBean {

    public DataBean data;

    public class DataBean {

        public ArrayList<NewsInfoBean> data;

        public int current_page;
        public int last_page;
        public int  total;
        public String video_share_url;
        public String text_share_url;


         /*"total": 2,
                 "per_page": 15,     #每页显示数量
        "current_page": 1,  ＃当前页
        "last_page": 1,*/
    }
/*
"id": 15, //视频ID
        "title": "视频15", //视频标题
        "cover": "https://ss0.bdstatic.com/9bA1vGfa2gU2pMbfm9GUKT-w/timg?wisealaddin&sec=1573724377&di=a615e71e2d2765792d5bebc00a587deb&quality=100&size=f242_182&src=http://vdposter.bdstatic.com/36a5c39ca9045f0ea56f6db95791d643.jpeg",  //封面图片
        "video_url": "https://haokan.baidu.com/v?vid=7093317942017555807&pd=bjh&fr=bjhauthor&type=video", //视频URL地址
        "video_length": 60, //视频总时长(秒)
        "create_time": "2019-11-16 17:43:42" //视频创建时间
*/


}
