package com.tinytiger.titi.event;


/**
 *
 * @author zhw_luke
 * @date 2020/5/15 0015 上午 9:47
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 关注
 */
public class AttentionEvent {

    public String userId;
    public int isMutual;

    public AttentionEvent(String userId, int isMutual) {
        this.userId = userId;
        this.isMutual = isMutual;
    }
}
