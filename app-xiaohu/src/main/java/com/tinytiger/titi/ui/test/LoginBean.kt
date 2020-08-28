package com.tinytiger.titi.ui.test

import com.tinytiger.common.http.response.BaseResp


/*
 * @author Tamas
 * create at 2019/11/17 0017
 * Email: ljw_163mail@163.com
 * description:
 */
class LoginResp : BaseResp<LoginBean>()
class LoginBean {
    var action_type: String? = null
    var user_info: UserInfoBean? = null

    class UserInfoBean {
        var user_id: String? = null
        var token: String? = null
        var username: String? = null
        var nickname: String? = null
        var netease_id: String? = null
        var netease_token: String? = null
        var medal_name: String? = null
        var medal_image: String? = null
        var master_id: String? = null
        var master_type: String? = null
    }
}