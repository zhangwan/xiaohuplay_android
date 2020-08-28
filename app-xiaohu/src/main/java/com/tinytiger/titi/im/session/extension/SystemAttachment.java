package com.tinytiger.titi.im.session.extension;

import com.alibaba.fastjson.JSONObject;


/**
 * Created by zhoujianghua on 2015/4/9.
 * 实现自定义消息的附件解析器
 * 红包 json数据bean
 */
public class SystemAttachment extends CustomAttachment {

    private int type = 0;
    private String msg = "";
    private String relation_url = "";
    private String content_id = "";
    private String img_url = "";

    public SystemAttachment() {
        super(CustomAttachmentType.SystemInfo);
    }

    /**
     * 解析具体数据
     *
     * @param data 消息数据
     */
    @Override
    protected void parseData(JSONObject data) {
        type = data.getInteger("type");
        msg = data.getString("msg");
        relation_url = data.getString("relation_url");
        content_id = data.getString("content_id");
        img_url = data.getString("img_url");
    }

   /* type	int
    消息类型 (必填) 1=文字 2=图文 3=内容推荐
    title	string
    消息标题 (必填)
    msg	int
    文字内容(选填) 当type=1时,必填
    relation_url	json
    关联url (选填) 当type=2时,必填
    content_id	int
    关联作品id (选填) 当type=3时,必填*/
   /* 'type'          => $data['type'],
            'title'         => $data['title'],
            'msg'           => $data['msg'],
            'relation_url'  => $data['relation_url'],
            'content_id'    => $data['content_id'],
            'img_url'       => $data['img_url']*/



    /**
     * 数据打包
     *
     * @return
     */
    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put("type", type);
        data.put("msg", msg);
        data.put("relation_url", relation_url);
        data.put("content_id", content_id);
        data.put("img_url", img_url);

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

    public String getRelation_url() {
        return relation_url;
    }

    public void setRelation_url(String relation_url) {
        this.relation_url = relation_url;
    }

    public String getContent_id() {
        return content_id;
    }

    public void setContent_id(String content_id) {
        this.content_id = content_id;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
