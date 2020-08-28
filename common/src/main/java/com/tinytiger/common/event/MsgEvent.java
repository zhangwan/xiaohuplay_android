package com.tinytiger.common.event;


/**
 *
 * @author zhw_luke
 * @date 2019/12/15 0015 下午 4:00
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc msg 消息传递
 */
public class MsgEvent {


    public int msgSize;

    public String sessionId;

    public MsgEvent() {
    }

    public MsgEvent(int msgSize, String sessionId) {
        this.msgSize = msgSize;
        this.sessionId = sessionId;
    }
}
