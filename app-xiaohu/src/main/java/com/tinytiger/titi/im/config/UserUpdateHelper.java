package com.tinytiger.titi.im.config;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zhw_luke
 * @date 2019/11/13 0013 下午 2:32
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 云信更新用户资料
 */
public class UserUpdateHelper {

    /**
     * 更新用户资料
     */
    public static void update(final UserInfoFieldEnum field, final Object value, RequestCallbackWrapper<Void> callback) {
        Map<UserInfoFieldEnum, Object> fields = new HashMap<>(1);
        fields.put(field, value);
        update(fields, callback);
    }

    /**
     * 批量更新
     * @param fields
     * @param callback
     */
    public static void update(final Map<UserInfoFieldEnum, Object> fields, final RequestCallbackWrapper<Void> callback) {
        NIMClient.getService(UserService.class).updateUserInfo(fields).setCallback(new RequestCallbackWrapper<Void>() {
            @Override
            public void onResult(int code, Void result, Throwable exception) {
                if (callback != null) {
                    callback.onResult(code, result, exception);
                }
            }
        });
    }
}
