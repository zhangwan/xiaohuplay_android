package com.tinytiger.titi.mvp.model.mine


import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.mine.UserPrivacyBean
import com.tinytiger.common.base.BaseModel




import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.scheduler.SchedulerUtils
import io.reactivex.Observable


class SettingPrivacyModel : BaseModel() {

    fun modifyPrivateConfig(private_type: Int, modify_val: Int): Observable<BaseBean> {
        return RetrofitManager.service.modifyPrivateConfig(private_type, modify_val)
            .compose(SchedulerUtils.ioToMain())
    }
    fun getUserPrivateConfig(): Observable<UserPrivacyBean> {
        return RetrofitManager.service.getUserPrivateConfig()
            .compose(SchedulerUtils.ioToMain())
    }

}