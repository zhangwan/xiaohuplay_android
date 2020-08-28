package com.tinytiger.titi.im.session;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.tinytiger.titi.R;
import com.tinytiger.titi.im.session.extension.AssessAttachment;
import com.tinytiger.titi.im.session.extension.CircleAttachment;
import com.tinytiger.titi.im.session.extension.CustomAttachParser;
import com.tinytiger.titi.im.session.extension.CustomAttachment;
import com.tinytiger.titi.im.session.extension.MedalAttachment;
import com.tinytiger.titi.im.session.extension.PostAttachment;
import com.tinytiger.titi.im.session.extension.StickerAttachment;
import com.tinytiger.titi.im.session.extension.SystemAttachment;
import com.tinytiger.titi.im.session.extension.WikiAttachment;
import com.tinytiger.titi.im.session.viewholder.MsgViewHolderAssess;
import com.tinytiger.titi.im.session.viewholder.MsgViewHolderCircle;
import com.tinytiger.titi.im.session.viewholder.MsgViewHolderDefCustom;
import com.tinytiger.titi.im.session.viewholder.MsgViewHolderMedal;
import com.tinytiger.titi.im.session.viewholder.MsgViewHolderPost;
import com.tinytiger.titi.im.session.viewholder.MsgViewHolderSticker;
import com.tinytiger.titi.im.session.viewholder.MsgViewHolderSystem;
import com.tinytiger.titi.im.session.viewholder.MsgViewHolderTip;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.recent.RecentCustomization;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.api.model.session.SessionEventListener;
import com.netease.nim.uikit.api.wrapper.NimMessageRevokeObserver;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.impl.customization.DefaultRecentCustomization;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.LocalAntiSpamResult;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.tinytiger.titi.im.session.viewholder.MsgViewHolderWiki;

import java.util.ArrayList;


/**
 * UIKit自定义消息界面用法展示类
 */
public class SessionHelper {


    private static SessionCustomization p2pCustomization;
    private static RecentCustomization recentCustomization;

    public static final boolean USE_LOCAL_ANTISPAM = true;


    public static void init() {
        // 注册自定义消息附件解析器
        NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new CustomAttachParser());
        // 注册各种扩展消息类型的显示ViewHolder
        registerViewHolders();
        // 设置会话中点击事件响应处理
        setSessionListener();

        // 注册消息撤回监听器
        registerMsgRevokeObserver();
        NimUIKit.setCommonP2PSessionCustomization(getP2pCustomization());
        NimUIKit.setRecentCustomization(getRecentCustomization());
    }

    public static void startP2PSession(Context context, String account) {
        startP2PSession(context, account, null);
    }

    public static void startP2PSession(Context context, String account, IMMessage anchor) {
        NimUIKit.startP2PSession(context, account, anchor);
    }


    /**
     *     定制化单聊界面。如果使用默认界面，返回null即可
      */
    private static SessionCustomization getP2pCustomization() {
        if (p2pCustomization == null) {
            p2pCustomization = new SessionCustomization() {

                // 由于需要Activity Result， 所以重载该函数。
                @Override
                public void onActivityResult(final Activity activity, int requestCode, int resultCode, Intent data) {
                    super.onActivityResult(activity, requestCode, resultCode, data);
                }

                @Override
                public boolean isAllowSendMessage(IMMessage message) {
                    return checkLocalAntiSpam(message);
                }

                @Override
                public MsgAttachment createStickerAttachment(String category, String item) {
                    return new StickerAttachment(category, item);
                }
            };

            // 定制加号点开后可以包含的操作， 默认已经有图片，视频等消息了
            ArrayList<BaseAction> actions = new ArrayList<>();
            p2pCustomization.actions = actions;
            p2pCustomization.withSticker = true;
            // 定制ActionBar右边的按钮，可以加多个
            ArrayList<SessionCustomization.OptionsButton> buttons = new ArrayList<>();
            SessionCustomization.OptionsButton cloudMsgButton = new SessionCustomization.OptionsButton() {
                @Override
                public void onClick(Context context, View view, String sessionId) {
                 //   Intent intent =new Intent(context, ImInfoActivity.class);
               //     intent.putExtra("neteaseId", sessionId);
                //    context.startActivity(intent);
                }
            };
            cloudMsgButton.iconId = R.mipmap.icon_more_b;
            buttons.add(cloudMsgButton);
            p2pCustomization.buttons = buttons;

        }
        return p2pCustomization;
    }

    /**
     * 敏感词过滤
     * @param message
     * @return
     */
    private static boolean checkLocalAntiSpam(IMMessage message) {
        if (!USE_LOCAL_ANTISPAM) {
            return true;
        }
        LocalAntiSpamResult result = NIMClient.getService(MsgService.class).checkLocalAntiSpam(message.getContent(),"**");
        int operator = result == null ? 0 : result.getOperator();
        switch (operator) {
            case 1: // 替换，允许发送
                message.setContent(result.getContent());
                return true;
            case 2: // 拦截，不允许发送
                return false;
            case 3: // 允许发送，交给服务器
                message.setClientAntiSpam(true);
                return true;
            case 0:
            default:
                break;
        }
        return true;
    }



    private static RecentCustomization getRecentCustomization() {
        if (recentCustomization == null) {
            recentCustomization = new DefaultRecentCustomization() {

                @Override
                public String getDefaultDigest(RecentContact recent) {
                    return super.getDefaultDigest(recent);
                }
            };
        }
        return recentCustomization;
    }


    private static void registerViewHolders() {
        NimUIKit.registerMsgItemViewHolder(CustomAttachment.class, MsgViewHolderDefCustom.class);
        NimUIKit.registerMsgItemViewHolder(StickerAttachment.class, MsgViewHolderSticker.class);

        NimUIKit.registerMsgItemViewHolder(SystemAttachment.class, MsgViewHolderSystem.class);
        NimUIKit.registerMsgItemViewHolder(PostAttachment.class, MsgViewHolderPost.class);

        NimUIKit.registerMsgItemViewHolder(AssessAttachment.class, MsgViewHolderAssess.class);
        NimUIKit.registerMsgItemViewHolder(WikiAttachment.class, MsgViewHolderWiki.class);
        NimUIKit.registerMsgItemViewHolder(CircleAttachment.class, MsgViewHolderCircle.class);
        NimUIKit.registerMsgItemViewHolder(MedalAttachment.class, MsgViewHolderMedal.class);
        NimUIKit.registerTipMsgViewHolder(MsgViewHolderTip.class);

    }


    private static void setSessionListener() {
        SessionEventListener listener = new SessionEventListener() {

            @Override
            public void onAvatarClicked(Context context, IMMessage message) {
                // 一般用于打开用户资料页面

            }

            @Override
            public void onAvatarLongClicked(Context context, IMMessage message) {
                // 一般用于群组@功能，或者弹出菜单，做拉黑，加好友等功能
            }

            @Override
            public void onAckMsgClicked(Context context, IMMessage message) {
                // 已读回执事件处理，用于群组的已读回执事件的响应，弹出消息已读详情
            }
        };
        NimUIKit.setSessionListener(listener);
    }


    private static void registerMsgRevokeObserver() {
        NIMClient.getService(MsgServiceObserve.class).observeRevokeMessage(new NimMessageRevokeObserver(), true);
    }


}
