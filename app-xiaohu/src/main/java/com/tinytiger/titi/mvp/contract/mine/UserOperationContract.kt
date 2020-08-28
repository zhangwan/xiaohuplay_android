package com.tinytiger.titi.mvp.contract.mine

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.user.UserInfoData


/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description: 
*/
interface UserOperationContract {
    interface View : IBaseView {



        /**
         * 显示个人资料页
         */
        fun showUserInfo( bean: UserInfoData)

        fun showResult()


        /**
         * 倒计时
         */
        fun showCountdown()
    }

    interface Presenter : IPresenter<View> {



        /**
         * 账号与安全数据
         */
        fun getAccountSecurityData()

        /**
         *  重置/设置 密码
         */
        fun onResetPassword(password:String,confirm_password:String)


        fun sendCode(phone: String)


        fun verificationPhone(verificationCode: String, password: String)

        fun changePhone( phone: String,verificationCode: String)

        fun recoverPassword(verificationCode: String, password: String)
    }
}