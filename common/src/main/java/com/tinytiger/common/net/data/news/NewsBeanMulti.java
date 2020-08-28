package com.tinytiger.common.net.data.news;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tinytiger.common.net.data.home.RecommendBean;
import com.tinytiger.common.net.data.video.ContentInfoBean;


/**
 * @author luke
 * @date 2018/9/1 13:54
 * @doc
 */
public class NewsBeanMulti implements MultiItemEntity {

    /**
     * 1 图文
     * 2 视频
     * 3 推荐
     */
    private int itemType;

    @Override
    public int getItemType() {
        return itemType;
    }

    public NewsBeanMulti(int itemType) {
        this.itemType = itemType;
    }

    public NewsBeanMulti(int itemType,String text) {
        this.itemType = itemType;
        this.defaultText = text;
    }

    /**
     * q缺省文字
     */
    public String defaultText = "";

    public ContentInfoBean.DataBean mNewsInfoBean;

    public RecommendBean mRecommendBean;

    public NewsBeanMulti(RecommendBean recommend){
        this.mRecommendBean = recommend;
        this.itemType = 2;
    }

}
