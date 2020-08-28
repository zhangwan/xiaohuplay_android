package com.tinytiger.common.net.data.video;

import com.tinytiger.common.net.data.BaseBean;

import java.util.List;

/*
 * @author Tamas
 * create at 2019/11/24 0024
 * Email: ljw_163mail@163.com
 * description:
 */
public class CommentDetailListBean extends BaseBean {

    public DataBean data;



    public static class DataBean {


        public int total;
        public int per_page;
        public int current_page;
        public int last_page;
        public List<ReplyDetailBean> data;

        public String author_user_id;
         /*"author_user_id": 3451,     #内容作者user_id
        "total": 3,				#总条数
        "per_page": 2,			#每页显示条数
        "current_page": 2,		#当前页码
        "last_page": 2,			#最后一页*/
    }
}
