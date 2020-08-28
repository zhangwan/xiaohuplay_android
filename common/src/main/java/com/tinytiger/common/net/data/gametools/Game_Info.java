package com.tinytiger.common.net.data.gametools;

import java.util.List;

/*
 * @author Tamas
 * create at 2020/3/2 0002
 * Email: ljw_163mail@163.com
 * description:
 */
public class Game_Info {


    /**
     * code : 200
     * status : true
     * msg : 请求成功
     * data : {"total":1,"per_page":10,"current_page":1,"last_page":1,"data":[{"id":17,"game_id":49,"game_name":"陈子婷-英雄联盟","thumbnail":"https://cdn.tinytiger.cn/FhGehOm9Rq4zIRpRNjl5qKj_gKgH","amway_assess_num":0,"title":"","content":"o(´^｀)o\\uff0c🍎为了更好地增加用户体验\\uff0c服务器维护时间内用户将不能正常登录APP\\uff0c维护完毕后\\uff0c用户无需重新下载客户端\\uff0c直接登录即可正常使用APP,哈哈哈哈O(∩_∩)O哈哈~狗苹果<p>OGN Entus Force在艾伦格和米拉玛地图中展现了自己的绝对统治力，场均排名6.4的他们提前锁定了一张PGC门票。<br><\/p><p><img src=\"https://cdn.tin","like_num":0,"comment_num":0,"share_num":0,"tag_list":[],"comment_list":[],"is_like":0}]}
     */

    private int code;
    private boolean status;
    private String msg;
    private DataBeanX data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * total : 1
         * per_page : 10
         * current_page : 1
         * last_page : 1
         * data : [{"id":17,"game_id":49,"game_name":"陈子婷-英雄联盟","thumbnail":"https://cdn.tinytiger.cn/FhGehOm9Rq4zIRpRNjl5qKj_gKgH","amway_assess_num":0,"title":"","content":"o(´^｀)o\\uff0c🍎为了更好地增加用户体验\\uff0c服务器维护时间内用户将不能正常登录APP\\uff0c维护完毕后\\uff0c用户无需重新下载客户端\\uff0c直接登录即可正常使用APP,哈哈哈哈O(∩_∩)O哈哈~狗苹果<p>OGN Entus Force在艾伦格和米拉玛地图中展现了自己的绝对统治力，场均排名6.4的他们提前锁定了一张PGC门票。<br><\/p><p><img src=\"https://cdn.tin","like_num":0,"comment_num":0,"share_num":0,"tag_list":[],"comment_list":[],"is_like":0}]
         */

        private int total;
        private int per_page;
        private int current_page;
        private int last_page;
        private List<DataBean> data;

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

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {

        }
    }
}
