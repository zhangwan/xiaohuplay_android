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
public class CommentAssessInfo extends BaseBean {

        public Data data;

        public class Data{
                public String author_user_id;

                public int current_page;
                public int last_page;
                public ArrayList<CommentAssessBean> data;
/*                 "author_user_id": 3451,     #内容作者user_id
    "total": 3,				#总条数
    "per_page": 2,			#每页显示条数
    "current_page": 2,		#当前页码
    "last_page": 2,			#最后一页*/


        }

}
