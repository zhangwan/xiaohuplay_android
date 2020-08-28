package com.tinytiger.common.net.data.props;

import com.tinytiger.common.net.data.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 道具兑换
 */
public class PropsExchangeBean extends BaseBean {

    public DataBean data;

    public static class DataBean implements Serializable {
        public String id;
        public String tool_id;
        public String cate_name;
        public String name;
        public String image;
        public int tool_number;
        public String exchange_img;
        public List<String> exchange_code;

    }



}
