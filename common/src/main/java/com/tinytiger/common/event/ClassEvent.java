package com.tinytiger.common.event;


/**
 * class页面跳转
 */
public class ClassEvent {
    //页面标记
    public String mclass;
    //用户id
    public String user_id;

    public int type=0;
    public int index;
    //参数1
    public String  element1;


    public ClassEvent(String mclass) {
        this.mclass = mclass;
    }

    public ClassEvent(String mclass, int type) {
        this.mclass = mclass;
        this.type = type;
    }


    public ClassEvent(String mclass,  String element1) {
        this.mclass = mclass;
        this.element1 = element1;
    }

    public ClassEvent(String mclass, int type, String element1) {
        this.mclass = mclass;
        this.type = type;
        this.element1 = element1;
    }

    public ClassEvent(String mclass,int type, String element1,String user_id) {
        this.mclass = mclass;
        this.user_id = user_id;
        this.type = type;
        this.element1 = element1;
    }
}
