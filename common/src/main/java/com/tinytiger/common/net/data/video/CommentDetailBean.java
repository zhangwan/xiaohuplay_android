package com.tinytiger.common.net.data.video;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/*
 * @author Tamas
 * create at 2019/11/24 0024
 * Email: ljw_163mail@163.com
 * description:
 */
public class CommentDetailBean  implements MultiItemEntity {


    public int id;
    public String user_id;
    public String netease_id;
    public String nickname;

    public int parent_id;
    public int parent_user_id;
    public String avatar;
    public String create_time;

    public int hots;
    public int has_badge;
    public String content;
    public int comment_user_num;

    public int like_num;
    public int comment_num;
    public int is_like;
    public  int is_black = -1;
    public int master_type;


    public List<ReplyDetailBean> replys;


    public String medal_image;
    public String medal_name;

    public int top_parent_id;

    public String content_id;
    public ReplysUserinfoBean commentUserinfo;
    public ReplysUserinfoBean replysUserinfo;

    @Override
    public int getItemType() {
        return 0;
    }
}
