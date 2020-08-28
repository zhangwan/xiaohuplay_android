package com.tinytiger.titi.data.game;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tinytiger.common.net.data.circle.CircleBean;
import com.tinytiger.common.net.data.gametools.GameAssessInfo;
import com.tinytiger.common.net.data.gametools.GameInfoBean;
import com.tinytiger.common.net.data.gametools.GameInfoDetailBean;
import com.tinytiger.common.net.data.gametools.RecommendVideoBean;

import java.util.List;

public class MultiGameDetailBean implements MultiItemEntity {


    public int itemType;
    public String tag_cloud_url;
    public GameAssessInfo assessInfo;

    public List<RecommendVideoBean> recommended_video;

    public GameInfoBean mGameInfoBean = null;

    public GameInfoDetailBean.Data mGameBean = null;


    public CircleBean circleInfo;


    @Override
    public int getItemType() {
        return itemType;
    }


    public MultiGameDetailBean(GameInfoDetailBean.Data bean) {
        mGameBean = bean;
        itemType = 1;
    }

    public MultiGameDetailBean(GameInfoBean info, int type) {
        mGameInfoBean = info;
        itemType = type;
    }

    public MultiGameDetailBean(String url, int type) {
        this.tag_cloud_url = url;
        //2,6
        itemType = type;
    }


    public MultiGameDetailBean(List<RecommendVideoBean> recommended_video) {
        this.recommended_video = recommended_video;
        itemType = 4;
    }


    public MultiGameDetailBean(GameAssessInfo info) {
        assessInfo = info;
        itemType = 5;
    }

    public MultiGameDetailBean(CircleBean info) {
        circleInfo = info;
        itemType = 7;
    }

    public MultiGameDetailBean(CircleBean info, int type) {
        circleInfo = info;
        itemType = type;
    }
}
