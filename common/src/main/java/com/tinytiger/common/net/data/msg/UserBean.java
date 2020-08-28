package com.tinytiger.common.net.data.msg;


/**
 *
 */
public class UserBean {


    public String user_id;
    public String nickname;
    public String netease_id;
    public String avatar;
    public int master_type;
    public String medal_image;

    public UserBean() {

    }
    public UserBean(String user_id, String nickname, String netease_id, String avatar) {
        this.user_id = user_id;
        this.nickname = nickname;
        this.netease_id = netease_id;
        this.avatar = avatar;
    }



    /*"user_id": 3328,
           "nickname": "洗衣液",   #评论人昵称
                "netease_id": "084e491c1f29138a3585fb6599dd4309",   #评论人云信id
                "avatar": "https://cdn.tinytiger.cn/FrjIGvkcAYz__jlBv_dKR0MxHOwp",  #评论人头像*/


}
