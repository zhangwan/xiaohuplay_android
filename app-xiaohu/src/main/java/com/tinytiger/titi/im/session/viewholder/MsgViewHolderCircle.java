package com.tinytiger.titi.im.session.viewholder;


import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.tinytiger.common.utils.ConstantsUtils;
import com.tinytiger.titi.R;
import com.tinytiger.titi.im.session.extension.CircleAttachment;
import com.tinytiger.titi.ui.circle.detail.CirclesDetailsActivity;



/**
 *
 * @author zhw_luke
 * @date 2019/11/19 0019 下午 7:39
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 帖子消息
 */
public class MsgViewHolderCircle extends MsgViewHolderBase {

    private CircleAttachment mAttachment;

    private TextView tvMsg;

    public MsgViewHolderCircle(BaseMultiItemFetchLoadAdapter adapter) {
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
                intent.putExtra("circleId", mAttachment.getCircle_id());
                intent.putExtra("modularId","");
                intent.putExtra("pageIndex", ConstantsUtils.Page.PAGE_CIRCLE);
                intent.putExtra("gameId","");
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
        mAttachment = (CircleAttachment) message.getAttachment();
        tvMsg.setText(mAttachment.getMsg());

    }

}
