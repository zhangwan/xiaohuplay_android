package com.tinytiger.titi.mvp.model.mine



import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.scheduler.SchedulerUtils
import io.reactivex.Observable


class CancelAccountModel : BaseModel() {


    fun cancellationAccount(): Observable<BaseBean> {
        return RetrofitManager.service.cancellationAccount()
            .compose(SchedulerUtils.ioToMain())
    }


}