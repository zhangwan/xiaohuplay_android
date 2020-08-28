package com.tinytiger.titi.event;


/**
 *
 * @author zhw_luke
 * @date 2020/5/25 0025 上午 9:42
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 点赞
 */
public class LikeEvent {

    public String id;
    public int like_num;
    public int is_like;

    public LikeEvent(String id, int like_num, int is_like) {
        this.id = id;
        this.like_num = like_num;
        this.is_like = is_like;
    }
}
