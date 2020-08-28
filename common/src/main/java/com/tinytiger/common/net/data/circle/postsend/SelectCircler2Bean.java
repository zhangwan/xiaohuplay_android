package com.tinytiger.common.net.data.circle.postsend;

import com.tinytiger.common.net.data.BaseBean;

import java.util.ArrayList;

/**
 * @author zhw_luke
 * @date 2019/11/27 0027 下午 6:19
 * @Copyright 小虎互联科技
 * @doc
 * @since 5.0.0
 */
public class SelectCircler2Bean extends BaseBean {

    public Data data;

    public class Data{
        public int total;
        public int current_page;
        public int last_page;

        public ArrayList<SelectCirclerInfoBean> data;
    }

}
