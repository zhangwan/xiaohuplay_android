package com.tinytiger.titi.im.session.extension;

import com.alibaba.fastjson.JSONObject;


/**
 *
 * @Author luke
 * @Date 2020-05-07 14:04
 * @Copyright 小虎互联科技
 * @since 1.1.0
 * @Des 百科消息
 *
 */
public class WikiAttachment extends CustomAttachment {

    private String msg = "";
    private String logo = "";
    private String game_id = "";


    public WikiAttachment() {
        super(CustomAttachmentType.WikiInfo);
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
        game_id = data.getString("game_id");

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
        data.put("game_id", game_id);

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

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }
}
