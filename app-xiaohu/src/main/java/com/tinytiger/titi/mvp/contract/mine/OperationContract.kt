package com.tinytiger.titi.mvp.contract.mine

import com.tinytiger.common.net.data.user.UserInfoData
import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.mine.CityBean


/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description:
*/
interface  OperationContract{
    interface View : IBaseView {



        /**
         * 显示个人资料页
         */
        fun showUserInfo( bean: UserInfoData)

        fun showResult()

        fun getQiniuToken(qiniu: String)

        fun showCityList(data: List<CityBean.ProvinceBean>)

    }

    interface Presenter : IPresenter<View> {

        /**
         * 账号与安全数据
         */
        fun getAccountSecurityData()

        /**
         *  个人中心-个人资料页
         */
        fun getUserInfo()


        fun onModifyResume(desc: String)

        /**
         *   设置新手资料
         */
         fun setNoviceUserProfile(avatar: String?,nickname:String?,gender:String?)

        /**
         *   修改头像
         */
        fun onModifyAvatar(avatar:String)

        /**
         *   修改性别
         */
        fun onModifyGender(gender:String)

        /**
         *  修改昵称
         */
        fun onModifyNickname(nickname:String)


        /**
         *   修改破蛋日
         */
        fun onModifyBirthday(birthday:String)

        /**
         *  修改破蛋日
         */
        fun onModifyCity(district:String)

        fun bindSocialPlatform( type:Int,unionid: String,nickname: String)


        fun loadQiniuToken()


    }
}