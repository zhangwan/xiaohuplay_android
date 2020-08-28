package com.tinytiger.common.net.data.mine;
import com.tinytiger.common.net.data.BaseBean;


public class ShareAppBean extends BaseBean {

    public DataBean data;

    public static class DataBean {
        public String logo_url;
        public String title;
        public String slogan;
        public String app_download_url;
        public String app_agreement_url;
/*"logo_url": "https://cdn.tinytiger.cn/logo.jpg",
        "title": "小虎Hoo",
        "slogan": "",
        "h5_url": "http://www.baidu.com"*/
 /*"app_download_url": "http://192.168.1.241/web_app/new_information/download.html",   #下载页H5
        "app_agreement_url": "http://192.168.1.241/web_app/new_appAgreement/index.html"      #协议H5*/
    }
}
