package com.tinytiger.common.net.data.gametools;


import com.tinytiger.common.net.data.gametools.comment.CommentAssessBean;

import java.util.ArrayList;

public class GameCommentBean {

        /**
         * id : 3
         * user_id : 983
         * title :
         * content : null
         * like_num : 11
         * comment_num : 22
         * share_num : 0
         * create_time : 2020-02-27 16:58:13
         * avatar : http://cdn.tinytiger.cn/user/user4.jpg
         * username : 13521157479
         * is_mutual : 0
         * user_follow_status : 0
         */

        private String id;
        private String user_id;
        private String title;
        private String content;
        private int score;
        private int like_num;
        private int comment_num;
        private int share_num;
        private String create_time;
        private String avatar;
        private String nickname;
        private int is_mutual;
        private int is_like;
        private int user_follow_status;

        private String medal_image;
        private String medal_name;
    public int master_type;

    public ArrayList<CommentAssessBean> comment_list;

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

    public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
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

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
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

    public int getIs_mutual() {
            return is_mutual;
        }

        public void setIs_mutual(int is_mutual) {
            this.is_mutual = is_mutual;
        }

    public int getIs_like() {
        return is_like;
    }

    public void setIs_like(int is_like) {
        this.is_like = is_like;
    }

    public int getUser_follow_status() {
            return user_follow_status;
        }

        public void setUser_follow_status(int user_follow_status) {
            this.user_follow_status = user_follow_status;
        }


}
