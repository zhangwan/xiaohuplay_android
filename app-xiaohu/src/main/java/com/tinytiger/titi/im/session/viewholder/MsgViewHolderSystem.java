package com.tinytiger.titi.im.session.viewholder;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.orhanobut.logger.Logger;
import com.tinytiger.common.event.ClassEvent;
import com.tinytiger.common.utils.image.GlideUtil;
import com.tinytiger.titi.R;
import com.tinytiger.titi.im.session.extension.SystemAttachment;
import com.tinytiger.titi.ui.web.WebActivity;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;

import org.greenrobot.eventbus.EventBus;


/**
 * @author zhw_luke
 * @date 2019/11/19 0019 下午 7:39
 * @Copyright 小虎互联科技
 * @doc 系统消息
 * @since 5.0.0
 */
public class MsgViewHolderSystem extends MsgViewHolderBase {

    private SystemAttachment guessAttachment;

    private ImageView ivDoings;
    private TextView tvDoings;
    private TextView tvMsg;

    public MsgViewHolderSystem(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_system;
    }

    @Override
    protected void inflateContentView() {
        ivDoings = view.findViewById(R.id.ivDoings);
        tvDoings = view.findViewById(R.id.tvDoings);
        tvMsg = view.findViewById(R.id.tvMsg);

        view.findViewById(R.id.llDoing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (guessAttachment != null) {
                    Logger.d(JSON.toJSON(guessAttachment));

                    switch (guessAttachment.getType()) {
                        case 1:
                            if (!guessAttachment.getRelation_url().isEmpty()) {
                                Intent intent = new Intent(context, WebActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("url", guessAttachment.getRelation_url());
                                context.startActivity(intent);
                            }

                            break;
                        case 2://活动

                            if (!guessAttachment.getRelation_url().isEmpty()) {
                                Intent intent = new Intent(context, WebActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("url", guessAttachment.getRelation_url());
                                context.startActivity(intent);
                            }
                            break;
                        case 3:
                            //图文
                            EventBus.getDefault().post(new ClassEvent("NewsDetailActivity", guessAttachment.getContent_id()));
                            break;
                        case 4:
                            //视频
                            EventBus.getDefault().post(new ClassEvent("VideoDetailActivity", guessAttachment.getContent_id()));
                            break;
                        case 5:
                            //百科词条
                            EventBus.getDefault().post(new ClassEvent("GameWikiDetailActivity", guessAttachment.getContent_id()));
                            break;
                        default:
                    }
                }
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
        guessAttachment = (SystemAttachment) message.getAttachment();

        ivDoings.setVisibility(View.GONE);
        tvDoings.setVisibility(View.GONE);
        tvMsg.setVisibility(View.GONE);
        switch (guessAttachment.getType()) {
            case 1:
                //文字
                tvMsg.setVisibility(View.VISIBLE);
                tvMsg.setText(guessAttachment.getMsg());
                break;
            case 2:
                //活动
                ivDoings.setVisibility(View.VISIBLE);
                tvDoings.setVisibility(View.VISIBLE);
                tvDoings.setText("活动");
                GlideUtil.loadImg(ivDoings, guessAttachment.getImg_url());
                break;
            case 3:
                //图文
                ivDoings.setVisibility(View.VISIBLE);
                tvDoings.setVisibility(View.VISIBLE);
                tvDoings.setText("作品");
                GlideUtil.loadImg(ivDoings, guessAttachment.getImg_url());
                break;
            case 4:
                //视频
                ivDoings.setVisibility(View.VISIBLE);
                tvDoings.setVisibility(View.VISIBLE);
                tvDoings.setText("作品");
                GlideUtil.loadImg(ivDoings, guessAttachment.getImg_url());
                break;
            case 5:
                tvMsg.setVisibility(View.VISIBLE);
                tvMsg.setText(guessAttachment.getMsg());
                break;
            default:
        }
    }

}
