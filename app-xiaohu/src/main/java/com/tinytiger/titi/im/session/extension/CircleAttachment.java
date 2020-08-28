package com.tinytiger.titi.im.session.extension;

import com.alibaba.fastjson.JSONObject;


/**
 *
 * @Author luke
 * @Date 2020-05-07 14:04
 * @Copyright 小虎互联科技
 * @since 1.1.0
 * @Des 圈子消息
 *
 */
public class CircleAttachment extends CustomAttachment {

   // private int type = 0;
    private String msg = "";
    private String logo = "";
    private String circle_id = "";


    public CircleAttachment() {
        super(CustomAttachmentType.CircleInfo);
    }

    /**
     * 解析具体数据
     *
     * @param data 消息数据
     */
    @Override
    protected void parseData(JSONObject data) {
        msg = data.getString("msg");
        logo = data.getString("logo");
        circle_id = data.getString("circle_id");

    }


    /**
     * 数据打包
     *
     * @return
     */
    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put("msg", msg);
        data.put("logo", logo);
        data.put("circle_id", circle_id);

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


    public String getCircle_id() {
        return circle_id;
    }

    public void setCircle_id(String circle_id) {
        this.circle_id = circle_id;
    }
}
