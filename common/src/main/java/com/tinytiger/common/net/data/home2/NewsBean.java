package com.tinytiger.common.net.data.home2;


/**
 * 游戏分类
 */
public class NewsBean {


    public String id;
    public int type=1;
    public String title;
    public String cover;

    public int view_num;
    public String video_url;
    public long video_length;
    public String create_time;

    public String game_id="";
    public int comment_count;
    public int comment_user_num;
    public int comment_num;
    public String game_name;
    public String name;

    public String user_id;


           /*"id": 124,  #内容ID
            "type": 1, #类型:1为图文,2为视频
            "title": "张晓伟666666", #内容标题
            "cover": "https://cdn.tinytiger.cn/FnMRUJUaLJYRKBXz2Gxm3FCUePqX", #内容封面

            "view_num": 12, #浏览数
            "video_url": null,  #视频URL(type=2时有返回值)
            "video_length": 0,  #视频时长(单位秒,type=2时有返回值)
            "create_time": "2020-01-17 16:34:27", #内容创建时间

            "game_id": 0, #游戏ID
            "comment_count": 0, #评论总数
            "game_name": 0 #游戏名*/

}
