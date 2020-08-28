package com.tinytiger.common.net.data.props;


import com.tinytiger.common.net.data.BaseBean;

import java.util.ArrayList;


/**
 *
 * @Author tamas
 * @Date 2020-02-04 13:06
 * @Copyright 小虎互联科技
 * @since 1.1.0
 * @Des 道具詳情 detail
 *
 */
public class PropsInfoListBean extends BaseBean {
    public DataBean data;

    public static class DataBean {
        public PropsInfoBean info;

        public ArrayList<PropsBean> indexToolsRecommend;


    }


}
