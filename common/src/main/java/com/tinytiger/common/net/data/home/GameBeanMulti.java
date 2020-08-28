package com.tinytiger.common.net.data.home;

import com.chad.library.adapter.base.entity.MultiItemEntity;


/**
 * @author luke
 * @date 2018/9/1 13:54
 * @doc
 */
public class GameBeanMulti implements MultiItemEntity {

    /**
     * 1 图文
     * 2 视频
     */
    private int itemType;

    @Override
    public int getItemType() {
        return itemType;
    }

    public GameBeanMulti(GameBean mGameBean) {
        this.itemType = mGameBean.type;
        this.mGameBean = mGameBean;
    }


    /**
     * q缺省文字
     */
    public String defaulttxt = "";

    public GameBean mGameBean;

}	
