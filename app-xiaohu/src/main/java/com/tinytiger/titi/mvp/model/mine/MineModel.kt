package com.tinytiger.titi.mvp.model.mine



import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.mine.UserCenterBean
import com.tinytiger.common.net.scheduler.SchedulerUtils
import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.data.mine.ShareAppBean
import io.reactivex.Observable


class MineModel : BaseModel() {


    fun getUserCenter(): Observable<UserCenterBean> {
        return RetrofitManager.service.getUserCenter()
            .compose(SchedulerUtils.ioToMain())
    }
    fun shareApp(): Observable<ShareAppBean> {
        return RetrofitManager.service.shareApp()
            .compose(SchedulerUtils.ioToMain())
    }

}