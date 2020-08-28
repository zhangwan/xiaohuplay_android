package com.tinytiger.titi.event;


import com.tinytiger.common.net.data.circle.PostBean;
import com.tinytiger.common.net.data.circle.postsend.DraftDetailBean;

/**
 *
 * @author zhw_luke
 * @date 2020/5/25 0025 上午 9:42
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 发布帖子
 */
public class PostEvent {
    //帖子类型 -1:普通(动态)帖 1:问答帖 (必填)
    public int type;

    public PostBean data;

    public PostEvent(int type) {
        this.type = type;
    }

    public PostEvent(int type, PostBean data) {
        this.type = type;
        this.data = data;
    }
}
