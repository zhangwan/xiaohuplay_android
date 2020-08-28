package com.tinytiger.titi.im.session.extension;

import com.alibaba.fastjson.JSONObject;
import com.orhanobut.logger.Logger;


/**
 *
 * @Author luke
 * @Date 2020-05-07 14:04
 * @Copyright 小虎互联科技
 * @since 1.1.0
 * @Des 帖子消息
 *
 */
public class PostAttachment extends CustomAttachment {

   // private int type = 0;
    private String msg = "";
    private String logo = "";
    private String post_id = "";


    public PostAttachment() {
        super(CustomAttachmentType.PostInfo);
    }

    /**
     * 解析具体数据
     *
     * @param data 消息数据
     */
    @Override
    protected void parseData(JSONObject data) {

        //Logger.d(data);
       // type = data.getInteger("type");
        msg = data.getString("msg");
        logo = data.getString("logo");
        post_id = data.getString("post_id");

    }


    /**
     * 数据打包
     *
     * @return
     */
    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
     //   data.put("type", type);
        data.put("msg", msg);
        data.put("logo", logo);
        data.put("post_id", post_id);

        return data;
    }


    @Override
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }


}
