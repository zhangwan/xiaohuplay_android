package com.tinytiger.common.event;


/**
 * 用户状态
 */
public class UserStatusEvent {
    //用户id
    public String user_id;

    /**
     * 1解除拉黑
     * -1 拉黑
     * 拉黑状态
     * 1:是 ,-1:否
     */
    public int type = 0;

    public UserStatusEvent(String user_id, int type) {
        this.user_id = user_id;
        this.type = type;
    }
}
