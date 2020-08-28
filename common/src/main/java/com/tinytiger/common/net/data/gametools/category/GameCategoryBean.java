package com.tinytiger.common.net.data.gametools.category;


import java.util.ArrayList;


/*
 * @author Tamas
 * create at 2020/4/15 0015
 * Email: ljw_163mail@163.com
 * description:
 */
public class GameCategoryBean {


    /**
     * id : 62
     * cate_name : 王者荣耀1
     * template_id : 4
     * childCate : [{"id":63,"cate_name":"王者荣耀1-1","icon":null,"pid":62}]
     */

    public int id;
    public String cate_name;
    public String icon;
    public int template_id;
    public ArrayList<ChildCateBean> childCate;

    public class ChildCateBean {
        /**
         * id : 63
         * cate_name : 王者荣耀1-1
         * icon : null
         * pid : 62
         */

        public int id;
        public String cate_name;
        public String icon;
        public int pid;
    }

}
