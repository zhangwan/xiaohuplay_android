package com.tinytiger.common.net.data.msg;

import com.tinytiger.common.net.data.BaseBean;

/*
 * @author zwy
 * create at 2020/6/1 0001
 * description:
 */
public class ReplyAdoptionBean extends BaseBean {
    public RePlyDataBean data;

    public static class RePlyDataBean {
        public String participate_num;
    }

}
