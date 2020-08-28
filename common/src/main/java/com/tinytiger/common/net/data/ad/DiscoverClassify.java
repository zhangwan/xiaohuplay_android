package com.tinytiger.common.net.data.ad;

import com.tinytiger.common.net.data.BaseBean;

import java.util.ArrayList;

/**
 * @author lmq001
 * @date 2020/08/04 15:08
 * @copyright 小虎互联科技
 * @doc
 */
public class DiscoverClassify extends BaseBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {

        private int total;
        private int per_page;
        private int current_page;
        private int last_page;
        private ArrayList<AdClassify> data;

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

        public ArrayList<AdClassify> getData() {
            return data;
        }

        public void setData(ArrayList<AdClassify> data) {
            this.data = data;
        }


    }
}
