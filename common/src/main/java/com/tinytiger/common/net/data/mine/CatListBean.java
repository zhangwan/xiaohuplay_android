package com.tinytiger.common.net.data.mine;

import com.tinytiger.common.net.data.BaseBean;

import java.util.List;

/*
 * @author Tamas
 * create at 2020/3/26 0026
 * Email: ljw_163mail@163.com
 * description:
 */
public class CatListBean  extends BaseBean {

    private List<CateBean> data;

    public List<CateBean> getData() {
        return data;
    }

    public void setData(List<CateBean> data) {
        this.data = data;
    }

 public    class  CateBean {
        private int id;
        private String type_name;
        private boolean is_selected;

     public boolean getIs_selected() {
         return is_selected;
     }

     public void setIs_selected(boolean is_selected) {
         this.is_selected = is_selected;
     }

     public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }
    }
}
