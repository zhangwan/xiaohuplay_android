package com.tinytiger.common.net.data.center;

import java.util.List;

/*
 * @author Tamas
 * create at 2020/3/4 0004
 * Email: ljw_163mail@163.com
 * description:
 */
public class UserGameAmwayBean {
    /**
     * id : 17
     * game_id : 49
     * game_name : 陈子婷-英雄联盟
     * thumbnail : https://cdn.tinytiger.cn/FhGehOm9Rq4zIRpRNjl5qKj_gKgH
     * amway_assess_num : 0
     * title :
     * content : o(´^｀)o\uff0c🍎为了更好地增加用户体验\uff0c服务器维护时间内用户将不能正常登录APP\uff0c维护完毕后\uff0c用户无需重新下载客户端\uff0c直接登录即可正常使用APP,哈哈哈哈O(∩_∩)O哈哈~狗苹果<p>OGN Entus Force在艾伦格和米拉玛地图中展现了自己的绝对统治力，场均排名6.4的他们提前锁定了一张PGC门票。<br></p><p><img src="https://cdn.tin
     * like_num : 0
     * comment_num : 0
     * share_num : 0
     * tag_list : []
     * comment_list : []
     * is_like : 0
     */

    private int id;
    private int game_id;
    private String game_name;
    private String thumbnail;
    private int amway_assess_num;
    private String title;
    private String content;
    private int like_num;
    private int comment_num;
    private int share_num;
    private int is_like;
    private List<String> tag_list;
    private List<String> comment_list;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getAmway_assess_num() {
        return amway_assess_num;
    }

    public void setAmway_assess_num(int amway_assess_num) {
        this.amway_assess_num = amway_assess_num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public int getIs_like() {
        return is_like;
    }

    public void setIs_like(int is_like) {
        this.is_like = is_like;
    }

    public List<String> getTag_list() {
        return tag_list;
    }

    public void setTag_list(List<String> tag_list) {
        this.tag_list = tag_list;
    }

    public List<String> getComment_list() {
        return comment_list;
    }

    public void setComment_list(List<String> comment_list) {
        this.comment_list = comment_list;
    }
}
