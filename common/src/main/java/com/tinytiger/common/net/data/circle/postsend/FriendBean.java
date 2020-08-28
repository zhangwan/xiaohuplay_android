package com.tinytiger.common.net.data.circle.postsend;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author zhw_luke
 * @date 2019/11/27 0027 下午 6:19
 * @Copyright 小虎互联科技
 * @doc
 * @since 5.0.0
 */
public class FriendBean implements Parcelable {



    public int user_id;
    public String nickname;
    public String avatar;
    public int  master_type;
    public boolean select;
    public String medal_image;

    /**
     * 帖子已经邀请人
     */
    public boolean old;
    public FriendBean(int user_id) {
        this.user_id = user_id;
    }
    public FriendBean() {

    }

    //    "data": {
//        "qa_user": [   # 问答团
//        {
//            "user_id": 13,
//                "nickname": "Justin Bieber 03 01 生日快"
//        },
//        {
//            "user_id": 983,
//                "nickname": "Ryan你仿佛就能发你发你发你发"
//        },
//        {
//            "user_id": 3272,
//                "nickname": "用户80014594"
//        }
//        ],
//        "recommend_user": [   # 推荐用户
//        {
//            "user_id": 3603,
//                "nickname": "用户10607064"
//        }
//        ],
//        "follow_user": {   # 关注用户
//            "list": [    #关注列表-默认会返回50条记录
//            {
//                "id": 2801,
//                    "user_id": 3346,
//                    "nickname": "付费恢复恢复好"
//            },
//            {
//                "id": 2149,
//                    "user_id": 3443,
//                    "nickname": "ertrt"
//            },
//            {
//                "id": 2143,
//                    "user_id": 3394,
//                    "nickname": "测试账号测试账号"
//            }
//            ],
//            "next_page_id": 0   # [0没有下一页] [>0下一页 page 值]
//        }
//    }


    protected FriendBean(Parcel in) {
        user_id = in.readInt();
        nickname = in.readString();
        avatar = in.readString();
        select = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(user_id);
        dest.writeString(nickname);
        dest.writeString(avatar);
        dest.writeByte((byte) (select ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FriendBean> CREATOR = new Creator<FriendBean>() {
        @Override
        public FriendBean createFromParcel(Parcel in) {
            return new FriendBean(in);
        }

        @Override
        public FriendBean[] newArray(int size) {
            return new FriendBean[size];
        }
    };
}
