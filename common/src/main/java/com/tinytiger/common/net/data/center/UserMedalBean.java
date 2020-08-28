package com.tinytiger.common.net.data.center;

import com.tinytiger.common.net.data.BaseBean;

import java.util.List;

/*
 * @author Tamas
 * create at 2020/3/30 0030
 * Email: ljw_163mail@163.com
 * description:
 */
public class UserMedalBean extends BaseBean {


    /**
     * id : 2
     * name : j
     * image : serserserssfdsfsdf
     * explain : ssssss
     * is_use : 1
     */

    public String id;
    public String name;
    public String image;
    public String explain;
    // #是否佩戴 0 未佩戴 1已佩戴
    public int is_use;
    //#是否拥有 0未拥有 1已拥有
    public int is_have;

    public int jump_type;
    public String jump_url;
    //   "jump_type": 1,           #0=无跳转 1=外部 2=功能页面 3=游戏详情 4=百科详情 5=词条 6=文章 7=视频 8=帖子
    //           "jump_url": "xxx",           #跳转URL或ID

}
