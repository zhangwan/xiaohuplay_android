package com.tinytiger.common.net.data.wiki;

import com.tinytiger.common.net.data.BaseBean;
import com.tinytiger.common.net.data.AdBean;

import java.util.ArrayList;
import java.util.List;

/*
 * @author Tamas
 * create at 2020/4/15 0015
 * Email: ljw_163mail@163.com
 * description:
 */
public class MainWikiListBean extends BaseBean {


    /**
     * code : 200
     * status : true
     * msg : 请求成功
     * data : {"banner":[{"id":51,"title":"百科banner名称修改","position_id":7,"image":"https://cdn.tinytiger.cn/FuBe10WYTGHS8M_RaNfZJY-s7p_6","sort":1,"jump_url":"http://www.baidu.com","jump_type":1,"jump_view":"0","content_type":0}],"default_banner":"http://www.baidu.com","all_game":[{"id":2,"logo":"https://cdn.tinytiger.cn/FhGqoYuPzmYY3lq4uCfkF1gsEYUN","name":"lxl-绝地求生啦","total":81},{"id":142,"logo":"http://www.baidu.cn","name":"测试142","total":0},{"id":143,"logo":"http://www.baidu.cn","name":"测试143","total":0},{"id":144,"logo":"http://www.baidu.cn","name":"测试144","total":0}],"recommend_game":[{"id":2,"logo":"https://cdn.tinytiger.cn/FhGqoYuPzmYY3lq4uCfkF1gsEYUN","name":"lxl-绝地求生啦","category_id":1,"total":81}],"soon_start_open":[]}
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
         * banner : [{"id":51,"title":"百科banner名称修改","position_id":7,"image":"https://cdn.tinytiger.cn/FuBe10WYTGHS8M_RaNfZJY-s7p_6","sort":1,"jump_url":"http://www.baidu.com","jump_type":1,"jump_view":"0","content_type":0}]
         * default_banner : http://www.baidu.com
         * all_game : [{"id":2,"logo":"https://cdn.tinytiger.cn/FhGqoYuPzmYY3lq4uCfkF1gsEYUN","name":"lxl-绝地求生啦","total":81},{"id":142,"logo":"http://www.baidu.cn","name":"测试142","total":0},{"id":143,"logo":"http://www.baidu.cn","name":"测试143","total":0},{"id":144,"logo":"http://www.baidu.cn","name":"测试144","total":0}]
         * recommend_game : [{"id":2,"logo":"https://cdn.tinytiger.cn/FhGqoYuPzmYY3lq4uCfkF1gsEYUN","name":"lxl-绝地求生啦","category_id":1,"total":81}]
         * soon_start_open : []
         */

        public ArrayList<AdBean> banner;
        // # 全部模块
        public List<MainWikiBean> all_game;
        // #热门主推
        public List<MainWikiBean> recommend_game;
        // # 即将开放
        public List<MainWikiBean> soon_start_open;

    }
}
