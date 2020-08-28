package com.tinytiger.common.net.data.search;

import com.tinytiger.common.net.data.BaseBean;

import java.util.ArrayList;

/**
 *
 * @author zhw_luke
 * @date 2020/3/3 0003 上午 9:47
 * @Copyright 小虎互联科技
 * @since 2.0.0
 * @doc 热搜词
 */
public class HotKeyBean extends BaseBean {
    public ArrayList<HotKeyInfo> data;
    public class HotKeyInfo{

        public String id;
        public String keyword;

/*"id": 1,            #id
            "keyword": "好的",    #名称*/
    }



}
