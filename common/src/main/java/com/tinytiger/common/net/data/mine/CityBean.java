package com.tinytiger.common.net.data.mine;

import com.tinytiger.common.net.data.BaseBean;

import java.util.List;

/*
 * @author Tamas
 * create at 2019/12/11 0011
 * Email: ljw_163mail@163.com
 * description:
 */
public class CityBean extends BaseBean {



    private List<ProvinceBean> data;



    public List<ProvinceBean> getData() {
        return data;
    }

    public void setData(List<ProvinceBean> data) {
        this.data = data;
    }

    public static class ProvinceBean {
        /**
         * id : 1
         * province_code : 110000
         * province_name : 北京
         * short_name : 北京
         * city_list : [{"id":1,"city_id":"110100","short_name":"北京"}]
         */

        private int id;
        private String province_code;
        private String province_name;
        private String short_name;
        private List<CityListBean> city_list;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getProvince_code() {
            return province_code;
        }

        public void setProvince_code(String province_code) {
            this.province_code = province_code;
        }

        public String getProvince_name() {
            return province_name;
        }

        public void setProvince_name(String province_name) {
            this.province_name = province_name;
        }

        public String getShort_name() {
            return short_name;
        }

        public void setShort_name(String short_name) {
            this.short_name = short_name;
        }

        public List<CityListBean> getCity_list() {
            return city_list;
        }

        public void setCity_list(List<CityListBean> city_list) {
            this.city_list = city_list;
        }

        public static class CityListBean {
            /**
             * id : 1
             * city_id : 110100
             * short_name : 北京
             */

            private int id;
            private String city_id;
            private String short_name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCity_id() {
                return city_id;
            }

            public void setCity_id(String city_id) {
                this.city_id = city_id;
            }

            public String getShort_name() {
                return short_name;
            }

            public void setShort_name(String short_name) {
                this.short_name = short_name;
            }
        }
    }
}
