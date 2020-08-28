package com.tinytiger.titi.mvp.contract.login


import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter

/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description: 登录
*/
interface LoginContract {

    interface View : IBaseView {


        /**
         * 倒计时
         */
        fun showCountdown()


        /***
         *  登录成功
         *  1微信
         *  2QQ
         *  3密码
         *  4手机号
         */
        fun showLoginSuccess(type: Int)

        /**
         * 首次注册
         * @param type Int 1 验证码注册 2第三方注册
         */
        fun showLoginRegister(type: Int)


        fun showAgreement(url: String)
    }

    interface Presenter : IPresenter<View> {



        fun sendCode(phone: String)


        fun onCodeLogin(phone: String, code:String)


        fun onPasswordLogin(phone: String, password:String)


        fun onOtherLogin(bind_type:Int,unionid: String,nickname:String,avatar: String,gender:String)


        /**
         *绑定手机
         * @param bind_type Int 要绑定的平台类型 1微信 2QQ
         * @param unionid String
         * @param nickname String
         * @param avatar String
         * @param gender String 性别 1男 2女
         */
        fun onBindPhone(bind_type:Int,unionid: String,nickname:String,avatar: String,gender:String,phone: String,verification_code:String)

        fun recoverPassword(phone: String, code: String)

        fun verificationPhone(verificationCode: String)

        fun changePhone( phone: String,verificationCode: String)
    }


}