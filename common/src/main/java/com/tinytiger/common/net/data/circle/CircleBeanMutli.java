package com.tinytiger.common.net.data.circle;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tinytiger.common.net.data.wiki.MainWikiBean;

/**
 *
 * @author zhw_luke
 * @date 2020/4/28 0028 上午 10:45
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc
 */
public class CircleBeanMutli implements MultiItemEntity {

    /**
     * 0 标题
     * 1 内容
     */
    public int itemType=0;
    public int spanSize=1;

    @Override
    public int getItemType() {
        return itemType;
    }


    public String title;
    public CircleBean mBean;

    public CircleBeanMutli(String title) {
        itemType = 1;
        spanSize=2;
        this.title = title;
    }

    public CircleBeanMutli(CircleBean bean) {
        itemType = 2;
        spanSize=1;
        this.mBean = bean;
    }

}
