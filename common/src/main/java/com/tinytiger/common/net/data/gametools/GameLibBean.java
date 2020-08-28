package com.tinytiger.common.net.data.gametools;

import java.util.ArrayList;
import java.util.List;

/*
 * @author Tamas
 * create at 2020/3/2 0002
 * Email: ljw_163mail@163.com
 * description:
 */
public class GameLibBean {

    public int total;
    public int per_page;
    public int current_page;
    public int last_page;
    public ArrayList<GameInfoBean> data;
    public void setTotal(int total) {
        this.total = total;
    }
    public int getTotal() {
        return total;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }
    public int getPer_page() {
        return per_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }
    public int getCurrent_page() {
        return current_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }
    public int getLast_page() {
        return last_page;
    }

    public void setData(ArrayList<GameInfoBean> data) {
        this.data = data;
    }
    public ArrayList<GameInfoBean> getData() {
        return data;
    }
}
