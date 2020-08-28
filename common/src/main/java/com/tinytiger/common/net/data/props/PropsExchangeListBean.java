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
public class PropsExchangeListBean extends BaseBean {
    public DataBean data;

    public static class DataBean {
        public int totalNum;
        public int totalCate;


        public ArrayList<PropsBean> detail;
//"totalNum": 3,  #总道具数量
  //      "totalCate": 1  #总道具分类数量

    }


}
