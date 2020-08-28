package com.tinytiger.common.net.data.circle.postsend;

import com.chad.library.adapter.base.entity.MultiItemEntity;


/**
 * @author luke
 * @date 2018/9/1 13:54
 * @doc
 */
public class MultiFriendBean implements MultiItemEntity {

    public int itemType;

    @Override
    public int getItemType() {
        return itemType;
    }

    public MultiFriendBean(FriendBean bean) {
        this.itemType = 2;
        this.mBean = bean;
    }

    public MultiFriendBean(String defaulttxt) {
        this.itemType = 1;
        this.defaulttxt = defaulttxt;
    }

    /**
     * q缺省文字
     */
    public String defaulttxt = "";
    public FriendBean mBean;

}	
