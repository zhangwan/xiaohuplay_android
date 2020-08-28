package com.tinytiger.common.net.data.home2;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tinytiger.common.net.data.gametools.GameInfoBean;

/**
 * @author luke
 * @date 2018/9/1 13:54
 * @doc
 */
public class NewsBeanMulti implements MultiItemEntity {

    /**
     * 1 图文
     * 2 视频
     */
    private int itemType;

    @Override
    public int getItemType() {
        return itemType;
    }

    public NewsBeanMulti(NewsBean mGameBean) {
        this.itemType = mGameBean.type;
        if (itemType==0){
            this.itemType=1;
        }
        this.mGameBean = mGameBean;
    }

    public NewsBeanMulti(GameInfoBean mGameBean) {
        this.itemType = 3;
        this.mGameInfoBean = mGameBean;
    }

    public NewsBeanMulti(int itemType) {
        this.itemType =itemType;
    }
    public NewsBeanMulti() {
        this.itemType =4;
    }

    private GameInfoBean mGameInfoBean = null;

    public GameInfoBean getGameInfoBean() {
        return mGameInfoBean;
    }

    /**
     * q缺省文字
     */
    public String defaulttxt = "";

    public NewsBean mGameBean;

}	
