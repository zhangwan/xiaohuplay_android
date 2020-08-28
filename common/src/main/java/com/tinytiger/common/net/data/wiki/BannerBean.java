package com.tinytiger.common.net.data.wiki;

/*
 * @author Tamas
 * create at 2020/4/15 0015
 * Email: ljw_163mail@163.com
 * description:
 */
public class BannerBean {
    /**
     * id : 51
     * title : 百科banner名称修改
     * position_id : 7
     * image : https://cdn.tinytiger.cn/FuBe10WYTGHS8M_RaNfZJY-s7p_6
     * sort : 1
     * jump_url : http://www.baidu.com
     * jump_type : 1
     * jump_view : 0
     * content_type : 0
     */

       // "jump_type": 0, #跳转方式  0=无跳转 1=外部跳转 2=功能页面跳转 3=游戏跳转
         // "jump_url": "" #跳转目标 jump_type=1时为外部链接,jump_type=2时为功能页面标识,jump_type=3时为游戏id

    private int id;
    private String title;
    private int position_id;
    private String image;

    private int sort;
    private String jump_url;
    private int jump_type;
    private String jump_view;
    private int content_type;



    public BannerBean() {
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPosition_id() {
        return position_id;
    }

    public void setPosition_id(int position_id) {
        this.position_id = position_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getJump_url() {
        return jump_url;
    }

    public void setJump_url(String jump_url) {
        this.jump_url = jump_url;
    }

    public int getJump_type() {
        return jump_type;
    }

    public void setJump_type(int jump_type) {
        this.jump_type = jump_type;
    }

    public String getJump_view() {
        return jump_view;
    }

    public void setJump_view(String jump_view) {
        this.jump_view = jump_view;
    }

    public int getContent_type() {
        return content_type;
    }

    public void setContent_type(int content_type) {
        this.content_type = content_type;
    }
}
