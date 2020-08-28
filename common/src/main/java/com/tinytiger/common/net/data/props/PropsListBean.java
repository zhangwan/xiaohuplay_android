package com.tinytiger.common.net.data.props;


import com.tinytiger.common.net.data.BaseBean;

import java.util.ArrayList;


/**
 *
 * @Author luke
 * @Date 2020-02-04 13:06
 * @Copyright 小虎互联科技
 * @since 1.1.0
 * @Des 道具商城 分类
 *
 */
public class PropsListBean extends BaseBean {
    public DataBean data;

    public static class DataBean {
        public ArrayList<PropsBean> data;

        public int current_page;
        public int last_page;

    }


}
