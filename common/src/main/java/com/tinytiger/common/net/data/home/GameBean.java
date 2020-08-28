package com.tinytiger.common.net.data.home;


/**
 * 游戏分类
 */
public class GameBean {


    public String id;
    public int type;
    public String title;
    public String cover;
    public String user_id;
    public String video_url;
    public long video_length;
    public String create_time;
    public String second_tag="";

    public String third_tag="";
    public int view_num;

            /*"id": 1, //文章/视频ID
                    "type": 2, //类型:1为图文,2为视频
                    "title": "文章标题", //标题
                    "cover": "https://ss0.bdstatic.com/9bA1vGfa2gU2pMbfm9GUKT-w/timg?wisealaddin&sec=1573724377&di=a81ee16d3c2d62d4cedc7e41cdc8d447&quality=100&size=f242_182&src=http://vdposter.bdstatic.com/196852f3d58bf9a766e1a6a199e54420.jpeg", //封面图

                    "video_url": "", //视频URL
                    "video_length": 0, //视频时长(秒)
                    "create_time": "2019-11-13 10:18:32", //创建时间
                    "second_tag": "吃鸡", //第二级标签
                    "third_tag": "吃鸡2" //第三级标签*/


            public int comment_num;
            public int comment_user_num;

}
