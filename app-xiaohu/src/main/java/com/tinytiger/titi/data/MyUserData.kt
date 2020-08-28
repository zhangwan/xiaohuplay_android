package com.tinytiger.titi.data


import com.tinytiger.common.net.data.login.UserInfoBean
import com.tinytiger.common.utils.ConstantsUtils
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.titi.R
import com.tinytiger.titi.im.config.LogoutHelper

import com.tinytiger.titi.im.config.preference.Preferences


/*
* @author Tamas
* create at 2019/11/14 0014
* Email: ljw_163mail@163.com
* description: 
*/
object MyUserData {


    fun setUserInfo(user: UserInfoBean) {
        SpUtils.saveSP(R.string.token, user.token)
        SpUtils.saveSP(R.string.user_id, user.user_id)
        SpUtils.saveSP(R.string.username, user.username)
        SpUtils.saveSP(R.string.nickname, user.nickname)
        SpUtils.saveSP(R.string.netease_id, user.netease_id)
        SpUtils.saveSP(R.string.netease_token, user.netease_token)
        Preferences.saveUserAccount(user.netease_id)
        Preferences.saveUserToken(user.netease_token)
        SpUtils.saveSP(R.string.master_type, user.master_type)
        SpUtils.saveSP(R.string.medal_image, user.medal_image)
    }

    fun setUserClear() {
        SpUtils.saveSP(R.string.token, "")
        SpUtils.saveSP(R.string.user_id, "")
        SpUtils.saveSP(R.string.username, "")
        SpUtils.saveSP(R.string.nickname, "")
        SpUtils.saveSP(R.string.netease_id, "")
        SpUtils.saveSP(R.string.netease_token, "")
        SpUtils.saveSP(R.string.master_type, 0)
        SpUtils.saveSP(R.string.medal_image, "")

        SpUtils.saveSP(R.string.show_fans, false)
        SpUtils.saveSP(R.string.fans_num, 0)
        SpUtils.saveSP(ConstantsUtils.clarityIndex,-1)
        Preferences.saveUserToken("")
        LogoutHelper.logout()
    }

    fun setOtherInfo(bind_type: Int, unionid: String, nickname: String, avatar: String, gender: String) {
        SpUtils.saveSP(com.tinytiger.titi.R.string.bind_type, bind_type)
        SpUtils.saveSP(R.string.bind_unionid, unionid)
        SpUtils.saveSP(R.string.nickname, nickname)
        SpUtils.saveSP(R.string.avatar, avatar)
        SpUtils.saveSP(R.string.gender, gender)
    }

    fun  getUserId():String{
       return SpUtils.getString(R.string.user_id, "0")
    }

    fun isEmptyToken():Boolean{
        val token = SpUtils.getString(R.string.token,"")
        return token.isEmpty()
    }

    fun setToken(token:String){
          SpUtils.saveSP(R.string.token,token)
    }

    fun getToken():String{
        return  SpUtils.getString(R.string.token,"")
    }



}