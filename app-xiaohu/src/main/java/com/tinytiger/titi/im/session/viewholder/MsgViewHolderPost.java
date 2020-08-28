package com.tinytiger.titi.im.session.viewholder;


import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;

import com.orhanobut.logger.Logger;
import com.tinytiger.common.utils.image.GlideUtil;
import com.tinytiger.titi.R;
import com.tinytiger.titi.im.session.extension.PostAttachment;
import com.tinytiger.titi.ui.circle.PostActivity;


/**
 *
 * @author zhw_luke
 * @date 2019/11/19 0019 下午 7:39
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 帖子消息
 */
public class MsgViewHolderPost extends MsgViewHolderBase {

    private PostAttachment guessAttachment;

    private ImageView ivIcon;
    private TextView tvMsg;

    public MsgViewHolderPost(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_post;
    }

    @Override
    protected void inflateContentView() {
        ivIcon = view.findViewById(R.id.ivIcon);
        tvMsg = view.findViewById(R.id.tvMsg);

        view.findViewById(R.id.llDoing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("postId", guessAttachment.getPost_id());
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
        guessAttachment = (PostAttachment) message.getAttachment();
        if (guessAttachment.getLogo()!=null){
            GlideUtil.loadImg(ivIcon,guessAttachment.getLogo());
        }
        tvMsg.setText(guessAttachment.getMsg());

    }

}
