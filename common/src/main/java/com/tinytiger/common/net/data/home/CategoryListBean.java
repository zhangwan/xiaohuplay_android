package com.tinytiger.common.net.data.home;



import com.tinytiger.common.net.data.BaseBean;

import java.util.ArrayList;

/**
 * 游戏分类
 */
public class CategoryListBean extends BaseBean {
    public ArrayList<CategoryBean> data;

    public class CategoryBean {

            public int id;
            public String cate_name;

       /* "id": 1,  //分类ID
                "cate_name": "英雄联盟"  //分类名字*/

    }
}
