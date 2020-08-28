package com.tinytiger.titi.mvp.model.mine




import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.mine.MyAccountInfoBean
import com.tinytiger.common.base.BaseModel

import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.login.ForgetBean
import com.tinytiger.common.net.scheduler.SchedulerUtils
import io.reactivex.Observable


class UserOperationModel : BaseModel() {


    fun getAccountSecurityData(): Observable<MyAccountInfoBean> {
        return RetrofitManager.service.getAccountSecurityData()
            .compose(SchedulerUtils.ioToMain())
    }

    fun resetPassword(password: String, confirmPassword: String): Observable<BaseBean> {
        return RetrofitManager.service.resetPassword(password,confirmPassword)
            .compose(SchedulerUtils.ioToMain())
    }

    fun sendVerificationCode(phone: String): Observable<BaseBean> {
        return RetrofitManager.service.sendVerificationCode(phone)
            .compose(SchedulerUtils.ioToMain())
    }

    fun verificationPhone(verificationCode: String, password: String): Observable<BaseBean> {
        return RetrofitManager.service.verificationPhone(verificationCode,password)
            .compose(SchedulerUtils.ioToMain())
    }
    fun changePhone( phone: String,verificationCode: String): Observable<BaseBean> {
        return RetrofitManager.service.changePhone(phone,verificationCode)
            .compose(SchedulerUtils.ioToMain())
    }
    fun modifyPassword( password: String): Observable<BaseBean> {
        return RetrofitManager.service.modifyPassword(password)
            .compose(SchedulerUtils.ioToMain())
    }

    fun recoverPassword( phone: String,verificationCode: String): Observable<ForgetBean> {
        return RetrofitManager.service.recoverPassword(phone,verificationCode)
            .compose(SchedulerUtils.ioToMain())
    }

    fun verificationPhone( phone: String): Observable<BaseBean> {
        return RetrofitManager.service.verificationPhone(phone)
            .compose(SchedulerUtils.ioToMain())
    }

}