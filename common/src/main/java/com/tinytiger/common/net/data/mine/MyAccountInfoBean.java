package com.tinytiger.common.net.data.mine;


import com.tinytiger.common.net.data.BaseBean;
import com.tinytiger.common.net.data.user.UserInfoData;

/*
 * @author Tamas
 * create at 2019/11/19 0019
 * Email: ljw_163mail@163.com
 * description:
 */
public class MyAccountInfoBean extends BaseBean {
    /**
     * data : {"user_info":{"username":"18555555555","is_password":1,"is_bind_wx":0,"is_bind_qq":0}}
     */

    public DataBean data;



    public static class DataBean {
        /**
         * user_info : {"username":"18555555555","is_password":1,"is_bind_wx":0,"is_bind_qq":0}
         */

        public UserInfoData user_info;




    }


//    public static class ProvinceBean {
//        /**
//         * nickname : 狙击兵
//         * avatar : http://cdn.tinytiger.cn/user/user4.jpg
//         * gender : 2
//         * birthday : null
//         * provcn : null
//         * citycn : null
//         */
//
//        private String nickname;
//        private String avatar;
//        private int gender;
//        private String birthday;
//        private String provcn;
//        private String citycn;
//
//        public String getNickname() {
//            return nickname;
//        }
//
//        public void setNickname(String nickname) {
//            this.nickname = nickname;
//        }
//
//        public String getAvatar() {
//            return avatar;
//        }
//
//        public void setAvatar(String avatar) {
//            this.avatar = avatar;
//        }
//
//        public int getGender() {
//            return gender;
//        }
//
//        public void setGender(int gender) {
//            this.gender = gender;
//        }
//
//        public String getBirthday() {
//            return birthday;
//        }
//
//        public void setBirthday(String birthday) {
//            this.birthday = birthday;
//        }
//
//        public String getProvcn() {
//            return provcn;
//        }
//
//        public void setProvcn(String provcn) {
//            this.provcn = provcn;
//        }
//
//        public String getCitycn() {
//            return citycn;
//        }
//
//        public void setCitycn(String citycn) {
//            this.citycn = citycn;
//        }
//    }
}
