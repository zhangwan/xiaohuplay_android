package com.netease.nim.uikit.mvp;


/**
 *
 * @author zhw_luke
 * @date 2018/9/19 0019 下午 1:52
 * @doc
 */
public class P2pEvent {

    public int type;

    public String sessionId;

    public P2pEvent() {
    }
    public P2pEvent(int type) {
        this.type = type;
    }

    public P2pEvent(int type,String sessionId) {
        this.type = type;
        this.sessionId = sessionId;
    }
}
