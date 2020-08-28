package com.tinytiger.titi.mvp.model



import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.login.AgreementBean
import com.tinytiger.common.net.data.login.ForgetBean
import com.tinytiger.common.net.data.login.LoginBean
import com.tinytiger.common.net.scheduler.SchedulerUtils
import io.reactivex.Observable


class LoginModel : BaseModel() {


    fun sendVerificationCode(phone: String): Observable<BaseBean> {
        return RetrofitManager.service.sendVerificationCode(phone)
            .compose(SchedulerUtils.ioToMain())
    }
    fun verificationCodeLogin(phone: String,verification_code:String): Observable<LoginBean> {
        return RetrofitManager.service.verificationCodeLogin(phone,verification_code)
            .compose(SchedulerUtils.ioToMain())
    }
    fun onPasswordLogin(phone: String,password:String): Observable<LoginBean> {
        return RetrofitManager.service.passwordLogin(phone,password)
            .compose(SchedulerUtils.ioToMain())
    }

    fun recoverPassword(phone: String, code: String) : Observable<ForgetBean> {
        return RetrofitManager.service.recoverPassword(phone,code)
            .compose(SchedulerUtils.ioToMain())
    }



    fun otherLogin(type:Int,unionid: String,nickname:String,avatar: String,gender:String): Observable<LoginBean> {
        return if(type==1){
            RetrofitManager.service.wxLogin(unionid,nickname,avatar,gender)
                .compose(SchedulerUtils.ioToMain())
        }else{
            RetrofitManager.service.qqLogin(unionid,nickname,avatar,gender)
                .compose(SchedulerUtils.ioToMain())
        }
    }

    fun bindPhone(bind_type: Int, bind_unionid: String, nickname: String, avatar: String, gender: String, phone: String, verificationCode: String): Observable<LoginBean> {
        return   RetrofitManager.service.bindPhone(bind_type.toString(),bind_unionid,nickname,avatar,gender,phone,verificationCode)
            .compose(SchedulerUtils.ioToMain())
    }

    fun getAgreement(): Observable<AgreementBean> {
        return RetrofitManager.service.getAgreement()
            .compose(SchedulerUtils.ioToMain())
    }

    fun verificationPhone(verificationCode: String): Observable<BaseBean> {
        return RetrofitManager.service.verificationPhone(verificationCode)
            .compose(SchedulerUtils.ioToMain())
    }

    fun changePhone( phone: String,verificationCode: String): Observable<BaseBean> {
        return RetrofitManager.service.changePhone(phone,verificationCode)
            .compose(SchedulerUtils.ioToMain())
    }

}