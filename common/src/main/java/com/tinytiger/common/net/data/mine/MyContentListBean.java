package com.tinytiger.common.net.data.mine;

import com.tinytiger.common.net.data.BaseBean;

import java.util.List;

/*
 * @author Tamas
 * create at 2019/11/22 0022
 * Email: ljw_163mail@163.com
 * description:
 */
public class MyContentListBean extends BaseBean {
    /**
     * data : {"total":0,"per_page":20,"current_page":1,"last_page":0,"data":[]}
     */

    public DataBean data;




    public static class DataBean {
        /**
         * total : 2
         * per_page : 20
         * current_page : 1
         * last_page : 1
         * data : [{"id":971,"type":2,"content_id":44,"title":"张张张张","jump_url":"https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2018939532,1617516463&fm=26&gp=0.jpg","nickname":"熊哥","create_time":"2019-11-22 14:01:32"},{"id":970,"type":2,"content_id":46,"title":"陈子婷77777","jump_url":"https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2018939532,1617516463&fm=26&gp=0.jpg","nickname":"熊哥","create_time":"2019-11-22 14:00:58"}]
         */

        private int total;
        private int per_page;
        private int current_page;
        private int last_page;
        public String video_share_url;
        public String text_share_url;
        private List<ContentBean> data;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPer_page() {
            return per_page;
        }

        public void setPer_page(int per_page) {
            this.per_page = per_page;
        }

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }

        public int getLast_page() {
            return last_page;
        }

        public void setLast_page(int last_page) {
            this.last_page = last_page;
        }

        public List<ContentBean> getData() {
            return data;
        }

        public void setData(List<ContentBean> data) {
            this.data = data;
        }
    }
}
