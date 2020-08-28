package com.tinytiger.common.event;


/**
 * 搜索
 */
public class SearchEvent {
    /**
     * 1 页面跳转
     */
    public int type;

    public int page;

    public String uid;
    public int is_mutual=-1;

    public SearchEvent(int type,int page) {
        this.page = page;
        this.type = type;
    }


    /**
     *
     * @param uid
     * @param page item位置
     */
    public SearchEvent(String uid,int page,int is_mutual) {
        this.page = page;
        this.type = 2;
        this.uid = uid;
        this.is_mutual = is_mutual;
    }
}
