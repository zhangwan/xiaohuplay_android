package com.tinytiger.common.net.data.video;

import com.tinytiger.common.net.data.BaseBean;

import java.util.List;

/*
 * @author Tamas
 * create at 2019/11/24 0024
 * Email: ljw_163mail@163.com
 * description:
 */
public class CommentListBean extends BaseBean {

    public DataBean data;
    public static class DataBean {
        public String author_user_id;
        public int total;
        public int per_page;
        public int current_page;
        public int last_page;
        public List<CommentDetailBean> data;
    }
}
