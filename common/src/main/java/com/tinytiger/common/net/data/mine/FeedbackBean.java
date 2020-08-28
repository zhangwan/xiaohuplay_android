package com.tinytiger.common.net.data.mine;

/*
 * @author Tamas
 * create at 2020/1/7 0007
 * Email: ljw_163mail@163.com
 * description:
 */
public class FeedbackBean {

    private int id;
    private String problem_desc;
    private String type_name;
    private String replay_content;
    private String avatar;
    private String nickname;
    private String medal_image;
    private String medal_name;


    public String getMedal_image() {
        return medal_image;
    }

    public void setMedal_image(String medal_image) {
        this.medal_image = medal_image;
    }

    public String getMedal_name() {
        return medal_name;
    }

    public void setMedal_name(String medal_name) {
        this.medal_name = medal_name;
    }

    public FeedbackBean() {
    }

    public String getProblem_desc() {
        return problem_desc;
    }

    public void setProblem_desc(String problem_desc) {
        this.problem_desc = problem_desc;
    }

    public String getReplay_content() {
        return replay_content;
    }

    public void setReplay_content(String replay_content) {
        this.replay_content = replay_content;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
