package com.tinytiger.common.net.data.center;

import com.tinytiger.common.net.data.BaseBean;

import java.util.List;

/*
 * @author Tamas
 * create at 2020/3/30 0030
 * Email: ljw_163mail@163.com
 * description:
 */
public class UserMedalList extends BaseBean {

    public DataBean data;

    public static class DataBean {

        public int total;
        public int per_page;
        public int current_page;
        public int last_page;
        public String medal_introduce_url;

        public List<UserMedalBean> list;


    }

}
