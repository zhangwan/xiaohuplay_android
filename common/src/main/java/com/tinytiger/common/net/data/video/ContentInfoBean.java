package com.tinytiger.common.net.data.video;

import com.tinytiger.common.net.data.BaseBean;

/*
 * @author Tamas
 * create at 2019/11/21 0021
 * Email: ljw_163mail@163.com
 * description:
 */
public class ContentInfoBean extends BaseBean {


    public DataBean data;



    public static class DataBean {
        /**
         * id : 65
         * user_id : 3192
         * title : 原视频造梗联盟第四期
         * cover : https://cdn.tinytiger.cn/FhNkavAQCHi9FRT1lBXPxrcaeJ4O
         * type : 2
         * video_url : https://cdn.tinytiger.cn/ltlvl-LHNYhLiJBCkYeGjgUCxqKJ
         * introduce : 这是一个lol的视频
         * view_num : 0
         * like_num : 0
         * collect_num : 0
         * share_num : 0
         * comment_num : 0
         * create_time : 2019-11-21 15:41:55
         * nickname : 66
         * avatar : https://cdn.tinytiger.cn/Fsj-gPvABnFumKDV7k4CBhjdN7mp
         * is_mutual : -1
         * is_like : -1
         * is_collect : -1
         * view_log_id : 689
         * share_url : http://192.168.1.241/web_app/new_information/share_video.html
         */

        private String id;
        private String user_id;
        private String title = "";
        private String cover;
        private int type;
        private String video_url;
        private String introduce = "";
        private int view_num;
        private int like_num;
        private int collect_num;
        private int share_num;
        private int comment_num;
        private String create_time;
        private String nickname="";
        private String avatar;
        // 1:互相关注 0:已关注 -1:未关注 -2:自己
        private int is_mutual=-1;
        private int is_like;
        private int is_collect;
        private int is_black = -1;// #(登录才有此字段)内容作者是否在登录用户的黑名单里1:是,-1:否
        private String view_log_id;
        private String share_url;
        private String netease_id;
        private String medal_image;
        private String medal_name;

        public int master_type;

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

        public String getNetease_id() {
            return netease_id;
        }

        public void setNetease_id(String netease_id) {
            this.netease_id = netease_id;
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

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getVideo_url() {
            return video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
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

        public int getCollect_num() {
            return collect_num;
        }

        public void setCollect_num(int collect_num) {
            this.collect_num = collect_num;
        }

        public int getShare_num() {
            return share_num;
        }

        public void setShare_num(int share_num) {
            this.share_num = share_num;
        }

        public int getComment_num() {
            return comment_num;
        }

        public void setComment_num(int comment_num) {
            this.comment_num = comment_num;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
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

        public int getIs_black() {
            return is_black;
        }

        public void setIs_black(int is_black) {
            this.is_black = is_black;
        }

        public void setIs_like(int is_like) {
            this.is_like = is_like;
        }

        public int getIs_collect() {
            return is_collect;
        }

        public void setIs_collect(int is_collect) {
            this.is_collect = is_collect;
        }

        public String getView_log_id() {
            return view_log_id;
        }

        public void setView_log_id(String view_log_id) {
            this.view_log_id = view_log_id;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }
    }
}
