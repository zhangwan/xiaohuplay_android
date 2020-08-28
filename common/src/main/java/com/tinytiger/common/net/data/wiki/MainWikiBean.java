package com.tinytiger.common.net.data.wiki;

/*
 * @author Tamas
 * create at 2020/4/15 0015
 * Email: ljw_163mail@163.com
 * description:
 */
public class MainWikiBean {

    /**
     * id : 2
     * logo : https://cdn.tinytiger.cn/FhGqoYuPzmYY3lq4uCfkF1gsEYUN
     * name : lxl-绝地求生啦
     * category_id : 1
     * total : 81
     */

    private String id;
    private String logo;
    private String name;
    private int category_id;// # 模块分类  1百科详情界面 2招募管理界面
    private int total;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
