package com.tinytiger.titi.data.comment;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tinytiger.common.net.data.video.CommentDetailBean;
import com.tinytiger.common.net.data.video.ReplyDetailBean;

/**
 *
 * @author zhw_luke
 * @date 2020/6/28 0028 上午 11:00
 * @Copyright 小虎互联科技
 * @since 3.3.0
 * @doc 资讯二级列表
 */
public class NewsTwoMulti implements MultiItemEntity {


    public int itemType;

    @Override
    public int getItemType() {
        return itemType;
    }

    public NewsTwoMulti(CommentDetailBean bean) {
        this.itemType = 2;
        this.recommendBean = bean;
    }


    public NewsTwoMulti(CommentDetailBean bean, int type) {
        this.itemType = type;
        this.recommendBean = bean;
    }

    /**
     * q缺省文字
     */
    public String defaulttxt = "";

    public CommentDetailBean recommendBean;

}
