package com.tinytiger.titi.mvp.model.mine




import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.mine.MyAccountInfoBean
import com.tinytiger.common.net.data.mine.MyUserInfoBean
import com.tinytiger.common.base.BaseModel



import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.QiniuBean
import com.tinytiger.common.net.data.mine.CityBean
import com.tinytiger.common.net.scheduler.SchedulerUtils
import io.reactivex.Observable


class OperationModel : BaseModel() {


    fun getAccountSecurityData(): Observable<MyAccountInfoBean> {
        return RetrofitManager.service.getAccountSecurityData()
            .compose(SchedulerUtils.ioToMain())
    }

    fun getUserInfo(): Observable<MyUserInfoBean> {
        return RetrofitManager.service.getUserInfo()
            .compose(SchedulerUtils.ioToMain())
    }


    fun setNoviceUserProfile(avatar: String?,nickname:String?,gender:String?): Observable<BaseBean> {
        return RetrofitManager.service.setNoviceUserProfile(avatar,nickname,gender)
            .compose(SchedulerUtils.ioToMain())
    }

    fun modifyAvatar(avatar: String): Observable<BaseBean> {
        return RetrofitManager.service.modifyAvatar(avatar)
            .compose(SchedulerUtils.ioToMain())
    }

    fun modifyGender(gender: String): Observable<BaseBean> {
        return RetrofitManager.service.modifyGender(gender)
            .compose(SchedulerUtils.ioToMain())
    }

    fun modifyNickname(nickname: String): Observable<BaseBean> {
        return RetrofitManager.service.modifyNickname(nickname)
            .compose(SchedulerUtils.ioToMain())
    }

    fun modifyBirthday(birthday: String): Observable<BaseBean> {
        return RetrofitManager.service.modifyBirthday(birthday)
            .compose(SchedulerUtils.ioToMain())
    }

    fun modifyCity(district: String): Observable<BaseBean> {
        return RetrofitManager.service.modifyCity(district)
            .compose(SchedulerUtils.ioToMain())
    }

    fun modifyResume(desc: String): Observable<BaseBean> {
        return RetrofitManager.service.modifyResume(desc)
            .compose(SchedulerUtils.ioToMain())
    }
    fun bindSocialPlatform(type:Int,unionid: String,nickname: String): Observable<BaseBean> {
        return RetrofitManager.service.bindSocialPlatform(type.toString(),unionid,nickname)
            .compose(SchedulerUtils.ioToMain())
    }

    fun getQiniuToken(): Observable<QiniuBean> {
        return RetrofitManager.service.getQiniuToken()
            .compose(SchedulerUtils.ioToMain())
    }

    fun getAreaList(): Observable<CityBean> {
        return RetrofitManager.service.getAreaList()
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

    fun unBindSocialPlatform(platform_type: Int): Observable<BaseBean> {
        return RetrofitManager.service.unBindSocialPlatform(platform_type)
            .compose(SchedulerUtils.ioToMain())
    }


}