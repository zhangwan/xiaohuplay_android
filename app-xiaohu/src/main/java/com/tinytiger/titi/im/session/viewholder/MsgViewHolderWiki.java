package com.tinytiger.titi.im.session.viewholder;


import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;

import com.tinytiger.common.utils.ConstantsUtils;
import com.tinytiger.titi.R;

import com.tinytiger.titi.im.session.extension.WikiAttachment;
import com.tinytiger.titi.ui.circle.detail.CirclesDetailsActivity;




/**
 *
 * @author zhw_luke
 * @date 2019/11/19 0019 下午 7:39
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 百科消息
 */
public class MsgViewHolderWiki extends MsgViewHolderBase {

    private WikiAttachment mAttachment;

    private TextView tvMsg;

    public MsgViewHolderWiki(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_txt;
    }

    @Override
    protected void inflateContentView() {
        tvMsg = view.findViewById(R.id.tvMsg);

        view.findViewById(R.id.llDoing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CirclesDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra("circleId", "");
                intent.putExtra("modularId", "");
                intent.putExtra("pageIndex", ConstantsUtils.Page.PAGE_WIKI);
                intent.putExtra("gameId",mAttachment.getGame_id());

                context.startActivity(intent);
            }
        });

    }

    @Override
    protected void bindContentView() {
        if (message.getAttachment() == null) {
            return;
        }
        mAttachment = (WikiAttachment) message.getAttachment();
        tvMsg.setText(mAttachment.getMsg());

    }

}
