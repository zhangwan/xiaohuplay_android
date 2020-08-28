package com.tinytiger.common.net.data.mine;

/*
 * @author Tamas
 * create at 2019/11/22 0022
 * Email: ljw_163mail@163.com
 * description:
 */
public class ContentBean {

    /**
     * id : 971
     * type : 2
     *
     * content_id : 44
     * title : 张张张张
     * jump_url : https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2018939532,1617516463&fm=26&gp=0.jpg
     * nickname : 熊哥
     * create_time : 2019-11-22 14:01:32
     */

    private int id;
    private int type;
    private int content_id;
    private String user_id;
    private String avatar;
    private String title;
    private String cover;
    private String video_url;
    private String nickname;
    private String create_time;
    private String introduce;
    private int view_num;
    private int like_num;
    public int comment_num;
    private int share_num;
    private int is_mutual=-1;
    public int is_like=-1;
    public int is_collect=-1;
    public String game_name;



    public boolean isSelected;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
  //#类型1为图文 2为视频
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getContent_id() {
        return content_id;
    }

    public void setContent_id(int content_id) {
        this.content_id = content_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getView_num() {
        return view_num;
    }

    public void setView_num(int view_num) {
        this.view_num = view_num;
    }

    public int getLike_num() {
        return like_num;
    }

    public void setLike_num(int like_num) {
        this.like_num = like_num;
    }

    public int getComment_num() {
        return comment_num;
    }

    public void setComment_num(int comment_num) {
        this.comment_num = comment_num;
    }

    public int getShare_num() {
        return share_num;
    }

    public void setShare_num(int share_num) {
        this.share_num = share_num;
    }

    public int getIs_mutual() {
        return is_mutual;
    }

    public void setIs_mutual(int is_mutual) {
        this.is_mutual = is_mutual;
    }
}
