package com.tinytiger.common.net.data.gametools.category;

/*
 * @author Tamas
 * create at 2020/4/16 0016
 * Email: ljw_163mail@163.com
 * description: 游戏分类详情 bean
 */
public class GameCategoryDetailBean {
    /**
     * game_id : 147
     * name : kkkkk
     * logo : http://www.baidu.cn
     * one_introduce : xxxx111111111111111
     * download_url : http://www.baidu.cn
     * score : 0.0
     */

    private String game_id;
    private String name;
    private String logo;
    private String one_introduce;
    private String download_url;
    private String score;

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getOne_introduce() {
        return one_introduce;
    }

    public void setOne_introduce(String one_introduce) {
        this.one_introduce = one_introduce;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
