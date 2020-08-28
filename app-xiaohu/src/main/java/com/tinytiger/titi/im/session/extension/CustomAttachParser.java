package com.tinytiger.titi.im.session.extension;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser;

/**
 *
 * @author zhw_luke
 * @date 2020/6/1 0001 上午 9:54
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc im消息解析及全局自定义消息分发入口
 */
public class CustomAttachParser implements MsgAttachmentParser {

    private static final String KEY_TYPE = "type";
    private static final String KEY_DATA = "data";

    @Override
    public MsgAttachment parse(String json) {

        CustomAttachment attachment = null;
        try {
            JSONObject object = JSON.parseObject(json);
            int type = object.getInteger(KEY_TYPE);
            JSONObject data = object.getJSONObject(KEY_DATA);
            switch (type) {
                case CustomAttachmentType.SystemInfo:
                    attachment = new SystemAttachment();
                    break;
                case CustomAttachmentType.PostInfo:
                    attachment = new PostAttachment();
                    break;
                case CustomAttachmentType.AssessInfo:
                    attachment = new AssessAttachment();
                    break;
                case CustomAttachmentType.WikiInfo:
                    attachment = new WikiAttachment();
                    break;
                case CustomAttachmentType.CircleInfo:
                    attachment = new CircleAttachment();
                    break;
                case CustomAttachmentType.MedalInfo:
                    attachment = new MedalAttachment();
                    break;
                default:
                    attachment = new DefaultCustomAttachment();
                    break;
            }

            if (attachment != null) {
                attachment.fromJson(data);
            }
        } catch (Exception e) {

        }

        return attachment;
    }

    public static String packData(int type, JSONObject data) {
        JSONObject object = new JSONObject();
        object.put(KEY_TYPE, type);
        if (data != null) {
            object.put(KEY_DATA, data);
        }

        return object.toJSONString();
    }
}
