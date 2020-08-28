package com.tinytiger.common.net.data.gametools;

import java.util.List;

public class GameInfoBean {

    public String id;
    public String logo;
    public String background;
    public String name;
    public String thumbnail;

    public String download_url;
    public String package_size_android;
    public String package_name_android;
    public String package_update_time;

    public int is_cloud_game;
    //是否已经预约 0否 1是
    public int appointment;
    //1-上传链接 2-上传文件 3-预约类型
    public int package_type;

    public String version;
    public String developer;
    public String publisher;

    public int is_follow;
    public int follow_status;
    public int wiki_status;
    public int modular_id;
    //#申请管理员状态 -2：拒绝(可以重新发起申请) -1：拒绝(不可继续申请) 0：未申请 1：审核通过 2：审核中
    public int apply_status;
    public int circle_status;
        //  "modular_id": 1,    #模块id 1.百科详情页界面 2.招募管理员界面
        //    "apply_status": 2,  #申请管理员状态 -1：拒绝 0：未申请 1：审核通过 2：审核中
    public int status;
    public int community_status;
    public int information_status;

    public double score;
    public String one_introduce;
    public String detail_introduce;
    public String video_introduce;
    public String create_time;
    public List<String> images_introduce;
    public int video_introduce_type;
    public int images_introduce_type;


    public List<String> cate_list;
    public int show_score;

    public int radar_chart_status;
    public String radar_chart_url;

           //"radar_chart_status": 0 #是否显示雷达图 0=不显示  1=显示
}
