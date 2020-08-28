package com.tinytiger.common.net.data.msg;


import com.tinytiger.common.net.data.BaseBean;


import java.util.ArrayList;

/**
 *
 */
public class ReplyListBean extends BaseBean {
    public DataBean data;

    public static class DataBean {
        public ArrayList<ReplyBean> data;

        public int total;
        public int current_page;
        public int last_page;
    }




/* "id": 4,     //广告ID
         "title": "六哥,K神,整活哥你最喜欢谁?",     //广告标题
         "image": "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2018939532,1617516463&fm=26&gp=0.jpg",  //广告图片
         "sort": 4,  //广告排序
         "jump_url": "http://www.baidu.com"  //广告跳转链接*/


}
