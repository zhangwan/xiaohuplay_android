package com.tinytiger.common.event;


/**
 *
 * @author zhw_luke
 * @date 2018/9/19 0019 下午 1:52
 * @doc 首页通知Event
 */
public class GameEvent {

    public int type;

    public String msg;

    public GameEvent() {
    }
    public GameEvent(int type) {
        this.type = type;
    }
}
