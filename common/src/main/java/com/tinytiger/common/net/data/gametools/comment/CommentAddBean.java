package com.tinytiger.common.net.data.gametools.comment;


import com.tinytiger.common.net.data.BaseBean;

import java.util.ArrayList;

/**
 *
 * @author zhw_luke
 * @date 2020/3/2 0002 下午 2:35
 * @Copyright 小虎互联科技
 * @since 2.0.0
 * @doc 評論
 */
public class CommentAddBean extends BaseBean {

        public Data data;

        public class Data{
                public int last_insert_comment_id;

            // "last_insert_comment_id": 10221 #当前评论的id

        }

}
