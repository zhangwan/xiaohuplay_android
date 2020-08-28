
package com.tinytiger.common.net.data.video;

/*
 * @author Tamas
 * create at 2019/11/24 0024
 * Email: ljw_163mail@163.com
 * description:
 */
public class ReplysUserinfoBean {

    /**
     * user_id : 2780
     * nickname : Wesley
     */

    public String user_id;
    public String nickname;
    public String netease_id;
    public String avatar;

    public String medal_image;
    public int master_type;

    public ReplysUserinfoBean(String nickname) {
        this.nickname = nickname;
    }

    public ReplysUserinfoBean() {

    }
}

