package com.tinytiger.common.net.data.home2;


/**
 * 游戏
 */
public class GameBean {


    public String id;
    public String logo;
    public String thumbnail;


    public String name;
    public float score;
    public String one_introduce;
    public int amway_count;
    public int show_score;

    //"show_score": 1          #是否显示评分 0否 1是
    /**
     * true 添加
     * false 删除
     */
    public boolean type;

    public GameBean() {

    }

    public GameBean(String id, String logo, String name, boolean type) {
        this.id = id;
        this.logo = logo;
        this.name = name;
        this.type = type;
    }


    /*"id": 2, #游戏ID
            "logo": "https://cdn.tinytiger.cn/FhGehOm9Rq4zIRpRNjl5qKj_gKgH", #游戏LOGO
            "thumbnail": "http://cdn.tinytiger.cn/user/user4.jpg", #游戏缩略图

            "name": "测试游戏名称", #游戏名称
            "score": "0.0", #游戏评分
            "one_introduce": "英雄联盟又称LOL" #游戏简介*/

}
