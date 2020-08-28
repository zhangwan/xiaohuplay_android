package com.tinytiger.common.net.data.user;


/**
 *关注/取消用户
 */
public class FriendBean {

    public String user_id;
    public String avatar;
    public String nickname;
    public int is_mutual=-1;
    public String medal_image;
    public String medal_name;
    public int  master_type;

    /* "user_id": 13,			#粉丝的id
                "is_mutual": 1,			#关注状态 1:互相关注 0:已关注 -1:未关注 -2:自己
                "nickname": "神仙姐姐1234",#粉丝的昵称
                "avatar": "https://cdn.tinytiger.cn/img1538118733914.png"	#粉丝的头像*/

}
