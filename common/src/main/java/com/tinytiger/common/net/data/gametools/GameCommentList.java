package com.tinytiger.common.net.data.gametools;

import com.tinytiger.common.net.data.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class GameCommentList extends BaseBean {
    public Data data;

    public class Data {


        /**
         * total : 0
         * per_page : 10
         * current_page : 1
         * last_page : 0
         * data : []
         */

        private int total;
        private int per_page;
        private int current_page;
        private int last_page;
        private List<GameCommentBean> data;

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

        public List<GameCommentBean> getData() {
            return data;
        }

        public void setData(List<GameCommentBean> data) {
            this.data = data;
        }
    }

}
