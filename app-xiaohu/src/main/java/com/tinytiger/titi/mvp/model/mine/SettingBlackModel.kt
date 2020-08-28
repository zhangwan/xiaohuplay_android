package com.tinytiger.titi.mvp.model.mine


import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.mine.UserBlackBean
import com.tinytiger.common.base.BaseModel




import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.scheduler.SchedulerUtils
import io.reactivex.Observable


class SettingBlackModel : BaseModel() {

    fun getUserBlackList(page: Int): Observable<UserBlackBean> {
        return RetrofitManager.service.getUserBlackList(page)
            .compose(SchedulerUtils.ioToMain())
    }
    fun cancelBlack(user_id:String): Observable<BaseBean> {
        return RetrofitManager.service.cancelBlack(user_id)
            .compose(SchedulerUtils.ioToMain())
    }

}