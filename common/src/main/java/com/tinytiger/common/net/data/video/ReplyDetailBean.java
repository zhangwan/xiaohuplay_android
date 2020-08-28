package com.tinytiger.common.net.data.video;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/*
 * @author Tamas
 * create at 2019/11/24 0024
 * Email: ljw_163mail@163.com
 * description:
 */
public class ReplyDetailBean implements MultiItemEntity {


    public int id;
    public int parent_id;
    public int top_parent_id;
    public int parent_user_id;
    public String user_id;
    public String nickname;
    public String netease_id;
    public String avatar;
    public String content;
    public String medal_image;
    public String medal_name;
    public int like_num;
    public int is_like;
    public int is_black;
    public int comment_user_num;
    public int master_type;

    public String create_time;
    public ReplysUserinfoBean commentUserinfo;
    public ReplysUserinfoBean replysUserinfo;


    @Override
    public int getItemType() {
        return 1;
    }
}
