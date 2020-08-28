package com.tinytiger.titi.im.session.viewholder;


import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.tinytiger.titi.R;
import com.tinytiger.titi.im.session.extension.CircleAttachment;
import com.tinytiger.titi.im.session.extension.MedalAttachment;
import com.tinytiger.titi.ui.game.info.GameDetailActivity;
import com.tinytiger.titi.ui.mine.other.MineMeritActivity;


/**
 *
 * @author zhw_luke
 * @date 2019/11/19 0019 下午 7:39
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 勋章消息
 */
public class MsgViewHolderMedal extends MsgViewHolderBase {

    private MedalAttachment mAttachment;

    private TextView tvMsg;

    public MsgViewHolderMedal(BaseMultiItemFetchLoadAdapter adapter) {
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
                Intent intent = new Intent(context, MineMeritActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("medal_id", mAttachment.getMedal_id());
                context.startActivity(intent);
            }
        });

    }

    /**
     * 显示自定义消息内容
     */
    @Override
    protected void bindContentView() {
        if (message.getAttachment() == null) {
            return;
        }
        mAttachment = (MedalAttachment) message.getAttachment();
        tvMsg.setText(mAttachment.getMsg());

    }

}
