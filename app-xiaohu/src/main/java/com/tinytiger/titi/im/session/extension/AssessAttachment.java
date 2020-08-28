package com.tinytiger.titi.im.session.extension;

import com.alibaba.fastjson.JSONObject;


/**
 *
 * @Author luke
 * @Date 2020-05-07 14:04
 * @Copyright 小虎互联科技
 * @since 1.1.0
 * @Des 帖子消息
 *
 */
public class AssessAttachment extends CustomAttachment {

   // private int type = 0;
    private String msg = "";
    private String logo = "";
    private String game_id = "";
    private String assess_id = "";

    public AssessAttachment() {
        super(CustomAttachmentType.AssessInfo);
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
        game_id = data.getString("game_id");
        assess_id = data.getString("assess_id");
    }


  /*  'msg' => '你的点评写的也忒好了，已被列为精选上安利墙，恭喜上墙～',
            'assess_id' => 3123,
            'game_id' => 34,
            'logo' => "http://www.baidu.com/a.jpg"*/


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
        data.put("game_id", game_id);
        data.put("assess_id", assess_id);
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

    public String getAssess_id() {
        return assess_id;
    }

    public void setAssess_id(String assess_id) {
        this.assess_id = assess_id;
    }
}
