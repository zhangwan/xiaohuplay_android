package com.tinytiger.common.net.data.gametools;

import java.util.List;

public class sds {



    private DataBeanX data;



    public static class DataBeanX {
        /**
         * game_info : {"id":49,"logo":"https://cdn.tinytiger.cn/FhGehOm9Rq4zIRpRNjl5qKj_gKgH","background":"https://cdn.tinytiger.cn/FvaRUgQUZEm3bqhsA05_STnz0iRu","name":"陈子婷-英雄联盟英雄联盟","download_url":"","is_follow":0}
         * content_info : {"total":2,"per_page":10,"current_page":1,"last_page":1,"data":[{"id":114,"cover":"https://cdn.tinytiger.cn/lo-xVgzL5yVELpd2UirvGhQkMVwY","title":"杨鑫test999999999","view_num":11,"comment_num":0,"type":2,"video_length":69,"video_url":"https://cdn.tinytiger.cn/20191216183430.m3u8","name":"陈子婷-英雄联盟英雄联盟"},{"id":115,"cover":"https://cdn.tinytiger.cn/Fp7JegKMFX2TFJMotnxgs8TJwK4h","title":"wangze","view_num":12,"comment_num":0,"type":2,"video_length":13,"video_url":"https://cdn.tinytiger.cn/FsglEVugH5QDDvuZ0BTgtXAz7DRB","name":"陈子婷-英雄联盟英雄联盟"}]}
         */

        private GameInfoBean game_info;
        private ContentInfoBean content_info;


        public static class GameInfoBean {
            /**
             * id : 49
             * logo : https://cdn.tinytiger.cn/FhGehOm9Rq4zIRpRNjl5qKj_gKgH
             * background : https://cdn.tinytiger.cn/FvaRUgQUZEm3bqhsA05_STnz0iRu
             * name : 陈子婷-英雄联盟英雄联盟
             * download_url :
             * is_follow : 0
             */

            private int id;
            private String logo;
            private String background;
            private String name;
            private String download_url;
            private int is_follow;

        }

        public static class ContentInfoBean {
            /**
             * total : 2
             * per_page : 10
             * current_page : 1
             * last_page : 1
             * data : [{"id":114,"cover":"https://cdn.tinytiger.cn/lo-xVgzL5yVELpd2UirvGhQkMVwY","title":"杨鑫test999999999","view_num":11,"comment_num":0,"type":2,"video_length":69,"video_url":"https://cdn.tinytiger.cn/20191216183430.m3u8","name":"陈子婷-英雄联盟英雄联盟"},{"id":115,"cover":"https://cdn.tinytiger.cn/Fp7JegKMFX2TFJMotnxgs8TJwK4h","title":"wangze","view_num":12,"comment_num":0,"type":2,"video_length":13,"video_url":"https://cdn.tinytiger.cn/FsglEVugH5QDDvuZ0BTgtXAz7DRB","name":"陈子婷-英雄联盟英雄联盟"}]
             */

            private int total;
            private int per_page;
            private int current_page;
            private int last_page;
            private List<DataBean> data;

            public static class DataBean {
                /**
                 * id : 114
                 * cover : https://cdn.tinytiger.cn/lo-xVgzL5yVELpd2UirvGhQkMVwY
                 * title : 杨鑫test999999999
                 * view_num : 11
                 * comment_num : 0
                 * type : 2
                 * video_length : 69
                 * video_url : https://cdn.tinytiger.cn/20191216183430.m3u8
                 * name : 陈子婷-英雄联盟英雄联盟
                 */

                private int id;
                private String cover;
                private String title;
                private int view_num;
                private int comment_num;
                private int type;
                private int video_length;
                private String video_url;
                private String name;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getCover() {
                    return cover;
                }

                public void setCover(String cover) {
                    this.cover = cover;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public int getView_num() {
                    return view_num;
                }

                public void setView_num(int view_num) {
                    this.view_num = view_num;
                }

                public int getComment_num() {
                    return comment_num;
                }

                public void setComment_num(int comment_num) {
                    this.comment_num = comment_num;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public int getVideo_length() {
                    return video_length;
                }

                public void setVideo_length(int video_length) {
                    this.video_length = video_length;
                }

                public String getVideo_url() {
                    return video_url;
                }

                public void setVideo_url(String video_url) {
                    this.video_url = video_url;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
        }
    }
}
