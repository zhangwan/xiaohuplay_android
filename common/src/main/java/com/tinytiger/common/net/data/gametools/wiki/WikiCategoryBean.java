package com.tinytiger.common.net.data.gametools.wiki;


import java.util.ArrayList;

/**
 *
 * @author zhw_luke
 * @date 2020/2/28 0028 下午 2:27
 * @Copyright 小虎互联科技
 * @since 2.0.0
 * @doc WIKIL 一级,二级分类
 */
public class WikiCategoryBean {

    public String id;
    public String cate_name;
    //一级分类
    public ArrayList<WikiCategoryBean> son_category;

    public String pid;
    //详情
    public ArrayList<WikiCategoryInfoBean> content_list;



    public int position;
   /* "id": 83,
            "cate_name": "陈子婷6",
            "son_category": [
    {
        "id": 87,
            "cate_name": "已经提开关与克玫瑰",
            "pid": 83,
            "content_list": []
    }
            ]*/


}
