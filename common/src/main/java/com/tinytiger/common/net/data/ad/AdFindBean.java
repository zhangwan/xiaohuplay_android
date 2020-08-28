package com.tinytiger.common.net.data.ad;

import com.tinytiger.common.net.data.AdBean;
import com.tinytiger.common.net.data.BaseBean;

import java.util.ArrayList;

/**
 * @author lmq001
 * @date 2020/08/04 14:31
 * @copyright 小虎互联科技
 * @doc
 */
public class AdFindBean extends BaseBean {
    public AdBanner data;

    public class AdBanner {
        public ArrayList<AdBean> banner;

        public ArrayList<AdClassify> classify;
    }

}
