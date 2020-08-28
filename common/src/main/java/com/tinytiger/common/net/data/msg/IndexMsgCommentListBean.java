package com.tinytiger.common.net.data.msg;


import com.tinytiger.common.net.data.BaseBean;
import java.util.ArrayList;

/**
 *
 * @author zhw_luke
 * @date 2019/12/15 0015 下午 5:14
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc
 */
public class IndexMsgCommentListBean extends BaseBean {
    public DataBean data;

    public static class DataBean {
        public ArrayList<MsgCommentBean> data;

        public int total;
        public int current_page;
        public int last_page;
        public String  author_user_id;

        public MsgCommentBean top_comment_info;

        public MsgCommentBean current_comment_info;
    }
}
