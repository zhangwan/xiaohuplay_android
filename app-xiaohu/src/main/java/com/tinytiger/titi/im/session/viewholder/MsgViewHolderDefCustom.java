package com.tinytiger.titi.im.session.viewholder;

import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderText;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;


/**
 *
 * @author zhw_luke
 * @date 2019/10/21 0021 下午 2:19
 * @Copyright 小虎互联科技
 * @since 1.0.0
 * @doc 未知消息类型
 */
public class MsgViewHolderDefCustom extends MsgViewHolderText {

    public MsgViewHolderDefCustom(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected String getDisplayText() {
    //    DefaultCustomAttachment attachment = (DefaultCustomAttachment) message.getAttachment();
        //return "type: " + attachment.getType() + ", data: " + attachment.getContent();
        return "当前版本不支持此消息类型";
    }
}
