package com.tinytiger.common.net.data.circle.home;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tinytiger.common.net.data.UserInfo;
import com.tinytiger.common.net.data.circle.PostBean;

/**
 *
 * @author zhw_luke
 * @date 2020/4/28 0028 上午 10:45
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc
 */
public class AttentionMutli implements MultiItemEntity {

    /**
     * 0 标题
     * 1 内容
     */
    public int itemType=0;

    @Override
    public int getItemType() {
        return itemType;
    }


    public String title;
    public PostBean mBean;
    public UserInfo mUserBean;
    public AttentionMutli(int itemType) {
        this.itemType =itemType;
    }


    public AttentionMutli(UserInfo bean) {
        itemType = 2;
        this.mUserBean = bean;
    }

    public AttentionMutli(PostBean bean) {
        itemType = 3;
        this.mBean = bean;
    }

}
