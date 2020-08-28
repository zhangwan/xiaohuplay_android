package com.netease.nim.uikit.common;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;

/**
 *
 * @author zhw_luke
 * @date 2019/12/5 0005 下午 2:28
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 云信拉黑与取消操作
 */
public class UserUtil {


    /**
     * 加入黑名单
     * @param account
     */
    public static void addFromBlack(String account) {
        if(account==null ||account.isEmpty()){
            return;
        }

        NIMClient.getService(FriendService.class).addToBlackList(account).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }

            @Override
            public void onFailed(int i) {
            }

            @Override
            public void onException(Throwable throwable) {
            }
        });
    }

    /**
     * 移出黑名单
     * @param account
     */
    public static void removeFromBlack(String account) {


        if(account==null ||account.isEmpty()){
            return;
        }
        NIMClient.getService(FriendService.class).removeFromBlackList(account).setCallback(new RequestCallback<Void>() {

            @Override
            public void onSuccess(Void aVoid) {

            }

            @Override
            public void onFailed(int i) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }

    /**
     * 用户通知操作
     * @param account 云信id
     * @param type 是否屏蔽 true 开启 false 关闭
     */
    public static void setMessageNotify(String account,boolean type) {
        if(account==null ||account.isEmpty()){
            return;
        }

        NIMClient.getService(FriendService.class).setMessageNotify(account,type).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }

            @Override
            public void onFailed(int i) {

            }

            @Override
            public void onException(Throwable throwable) {
            }
        });

    }

}
