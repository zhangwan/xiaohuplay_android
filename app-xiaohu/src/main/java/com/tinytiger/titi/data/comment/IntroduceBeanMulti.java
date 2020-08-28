package com.tinytiger.titi.data.comment;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tinytiger.common.net.data.home.RecommendBean;
import com.tinytiger.common.net.data.video.ContentInfoBean;

/*
 * @author Tamas
 * create at 2019/12/4 0004
 * Email: ljw_163mail@163.com
 * description:
 */
public class IntroduceBeanMulti implements MultiItemEntity {

    /**
     * 1 介绍页
     * 2 推荐
     */
    int itemType;

    @Override
    public int getItemType() {
        return itemType;
    }

    public IntroduceBeanMulti(RecommendBean bean) {
        this.itemType = 2;
        this.recommendBean = bean;
    }

    public IntroduceBeanMulti(String text) {
        this.itemType = 3;
        this.defaulttxt = text;
    }

    public IntroduceBeanMulti( ContentInfoBean.DataBean content) {
        this.itemType = 1;
        this.contentBean = content;
    }


    /**
     * q缺省文字
     */
    public String defaulttxt = "";

    public RecommendBean recommendBean;
    public ContentInfoBean.DataBean contentBean;

}
