package com.tinytiger.common.net.data.mine;


import com.tinytiger.common.net.data.BaseBean;
import com.tinytiger.common.net.data.user.UserInfoData;

/*
 * @author Tamas
 * create at 2019/11/17 0017
 * Email: ljw_163mail@163.com
 * description: 个人中心获取数据
 */
public class UserCenterBean extends BaseBean {

    public DataBean data;

    public static class DataBean {
        /**
         * like_num : 0
         * follow_and_fansNum  : {"follow_num":8,"fans_num":5}
         * userinfo : {"avatar":"https://cdn.tinytiger.cn/img1561016441752.png","background_img":"","nickname":"小姐姐爱吃鸡","gender":1,"citycn":null,"districtcn":null,"provcn":null,"constellation":""}
         */

        public int is_mutual;  	//#关注状态-1:未关注,0:已关注,1:互相关注 [查看他人主页才有此字段]

        public int like_num;
        public int follow_num;
        public int fans_num;
        public String user_id;
        public String share_url;
        public UserInfoData userinfo;
    }
}
