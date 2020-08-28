package com.tinytiger.common.net.data.center;

import com.tinytiger.common.net.data.BaseBean;
import com.tinytiger.common.net.data.home2.AmwayBean;

import java.util.List;

/*
 * @author Tamas
 * create at 2020/3/4 0004
 * Email: ljw_163mail@163.com
 * description:
 */
public class UserGameAmwayList extends BaseBean {


    /**
     * code : 200
     * status : true
     * msg : 请求成功
     * data : {"total":2,"per_page":10,"current_page":1,"last_page":1,"data":[{"id":49,"name":"陈子婷-英雄联盟","logo":"https://cdn.tinytiger.cn/FhGehOm9Rq4zIRpRNjl5qKj_gKgH","download_url":""},{"id":2,"name":"lxl-绝地求生啦","logo":"https://cdn.tinytiger.cn/FhGehOm9Rq4zIRpRNjl5qKj_gKgH","download_url":""}]}
     */

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        /**
         * total : 2
         * per_page : 10
         * current_page : 1
         * last_page : 1
         * data : [{"id":49,"name":"陈子婷-英雄联盟","logo":"https://cdn.tinytiger.cn/FhGehOm9Rq4zIRpRNjl5qKj_gKgH","download_url":""},{"id":2,"name":"lxl-绝地求生啦","logo":"https://cdn.tinytiger.cn/FhGehOm9Rq4zIRpRNjl5qKj_gKgH","download_url":""}]
         */

        private int total;
        private int per_page;
        private int current_page;
        private int last_page;
     //   private List<UserGameAmwayBean> data;
        private List<AmwayBean> data;

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

        public List<AmwayBean> getData() {
            return data;
        }

        public void setData(List<AmwayBean> data) {
            this.data = data;
        }


    }
}
