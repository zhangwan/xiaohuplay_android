package com.tinytiger.common.net.data.wiki;

import com.tinytiger.common.net.data.BaseBean;

import java.util.List;

/*
 * @author Tamas
 * create at 2020/4/15 0015
 * Email: ljw_163mail@163.com
 * description:
 */
public class WikiSearchListBean extends BaseBean {



    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * total : 1
         * per_page : 20
         * current_page : 1
         * last_page : 1
         * data : [{"id":170,"game_id":2,"name":"陈子婷3-content","is_type":2}]
         */

        private int total;
        private int per_page;
        private int current_page;
        private int last_page;
        private List<WikiSearchBean> data;

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

        public List<WikiSearchBean> getData() {
            return data;
        }

        public void setData(List<WikiSearchBean> data) {
            this.data = data;
        }
    }
}
