package com.tinytiger.common.net.data.gametools.category;

import com.tinytiger.common.net.data.BaseBean;
import com.tinytiger.common.net.data.center.MineGameBean;

import java.util.ArrayList;
import java.util.List;

/*
 * @author Tamas
 * create at 2020/4/16 0016
 * Email: ljw_163mail@163.com
 * description: 游戏分类详情 list
 */
public class GameCategoryDetailListBean extends BaseBean {


    /**
     * status : true
     * data : {"total":1,"per_page":20,"current_page":1,"last_page":1,"data":[{"game_id":147,"name":"kkkkk","logo":"http://www.baidu.cn","one_introduce":"xxxx111111111111111","download_url":"http://www.baidu.cn","score":"0.0"}]}
     */

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
         * data : [{"game_id":147,"name":"kkkkk","logo":"http://www.baidu.cn","one_introduce":"xxxx111111111111111","download_url":"http://www.baidu.cn","score":"0.0"}]
         */

        private int total;
        private int per_page;
        private int current_page;
        private int last_page;
        private ArrayList<MineGameBean> data;

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

        public ArrayList<MineGameBean> getData() {
            return data;
        }

        public void setData(ArrayList<MineGameBean> data) {
            this.data = data;
        }


    }
}
