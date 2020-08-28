package com.tinytiger.common.net.data.center;

/*
 * @author Tamas
 * create at 2020/3/4 0004
 * Email: ljw_163mail@163.com
 * description:
 */
public class MineGameBean {
    /**
     * id : 49
     * name : 陈子婷-英雄联盟
     * logo : https://cdn.tinytiger.cn/FhGehOm9Rq4zIRpRNjl5qKj_gKgH
     * download_url :
     */

    public String id;
    public String name;
    public String logo;

    public String download_url;
    public String package_name_android;
    public String package_size_android;

    public String version;
    public String one_introduce;

    public boolean has_application ;

    //二级分类
    public String cate_name;

    //游戏分类详情
    public double score;
    public String thumbnail;

    //收藏、关注时间
    public String create_time;

    public boolean isSelected=false;
    public long time;
    public int is_cloud_game;
}
