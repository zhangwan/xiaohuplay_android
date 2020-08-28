package com.tinytiger.common.net.data.gametools;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tinytiger.common.net.data.gametools.comment.CommentAssessBean;
import com.tinytiger.common.net.data.home.GameBean;


/**
 * @author luke
 * @date 2018/9/1 13:54
 * @doc
 */
public class AssessBeanMulti implements MultiItemEntity {

    public int itemType;

    @Override
    public int getItemType() {
        return itemType;
    }

    public AssessBeanMulti(GameAssessBean.Data mGameBean) {
        this.itemType = 1;
        this.mGameBean = mGameBean;
    }

    public AssessBeanMulti(int itemType,String defaulttxt) {
        this.itemType = itemType;
        this.defaulttxt = defaulttxt;
    }
    public AssessBeanMulti(CommentAssessBean mCommentAssessBean) {
        this.itemType = 3;
        this.mCommentAssessBean = mCommentAssessBean;
    }

    public AssessBeanMulti(int itemType,int size) {
        this.itemType = itemType;
        this.size = size;
    }

    /**
     * q缺省文字
     */
    public String defaulttxt = "";
    public int size = 0;
    public GameAssessBean.Data mGameBean;
    public CommentAssessBean mCommentAssessBean;
}	
