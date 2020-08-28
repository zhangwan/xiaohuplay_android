package com.tinytiger.common.net.data.gametools.category;

import com.tinytiger.common.net.data.BaseBean;

import java.util.ArrayList;
import java.util.List;

/*
 * @author Tamas
 * create at 2020/4/16 0016
 * Email: ljw_163mail@163.com
 * description:游戏分类详情 banner
 */
public class GameCategoryBannerBean extends BaseBean {


    /**
     * status : true
     * data : [{"img_url":"https://cdn.tinytiger.cn/82bb0da0f974b408ca77b149b7236399.png1","game_id":49},{"img_url":"https://cdn.tinytiger.cn/82bb0da0f974b408ca77b149b7236399.png123","game_id":49},{"img_url":"https://cdn.tinytiger.cn/82bb0da0f974b408ca77b149b7236399.png1","game_id":49},{"img_url":"https://cdn.tinytiger.cn/82bb0da0f974b408ca77b149b7236399.png1","game_id":49},{"img_url":"https://cdn.tinytiger.cn/FhGqoYuPzmYY3lq4uCfkF1gsEYUN","game_id":0}]
     */

    private ArrayList<DataBean> data;

    public ArrayList<DataBean> getData() {
        return data;
    }

    public void setData(ArrayList<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * img_url : https://cdn.tinytiger.cn/82bb0da0f974b408ca77b149b7236399.png1
         * game_id : 49
         */

        private String img_url;
        private String game_id;

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getGame_id() {
            return game_id;
        }

        public void setGame_id(String game_id) {
            this.game_id = game_id;
        }
    }
}
