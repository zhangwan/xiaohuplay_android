package com.tinytiger.common.net.data.search;

/**
 * 搜索用户
 */
public class SearchUserInfo {

        public String user_id;
        public String nickname="";
        public int gender;
        public String avatar;
        public String medal_image;
        public String medal_name;

        public int fans_num;
        public int number;
        public int is_mutual=-1;

        public int age;
        public String districtcn="";
        public String provcn="";
        public int master_type;
        /*"user_id": 13,                          #用户id
        "nickname": "神仙姐姐1234",              #用户昵称
        "gender": 1,                            #用户性别 1男 2女 0未知
        "avatar": "https://cdn.tinytiger.cn/img1538118733914.png",  #用户头像
        "fans_num": 42,         #用户粉丝数
        "number": 0,            #用户作品数
        "is_mutual": -1          #用户关注关系 0为已关注 1为相互关注 -1为未关注*/

              /*  "age": 20,               #年龄
            "districtcn": "深圳",    #地区市
            "provcn": "广东",         #省份*/


}
