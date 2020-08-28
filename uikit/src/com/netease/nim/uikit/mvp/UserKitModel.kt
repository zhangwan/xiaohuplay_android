package com.netease.nim.uikit.mvp


import com.tinytiger.common.net.RetrofitManager

import com.tinytiger.common.net.scheduler.SchedulerUtils
import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.msg.UserKitBean


import io.reactivex.Observable


class UserKitModel : BaseModel() {

    fun cancelBlack(user_id:String):Observable<BaseBean>{
        return RetrofitManager.service.cancelBlack(user_id)
                .compose(SchedulerUtils.ioToMain())
    }
    fun addBlack(user_id:String):Observable<BaseBean>{
        return RetrofitManager.service.addBlack(user_id)
            .compose(SchedulerUtils.ioToMain())
    }

    fun getUserRelation(netease_id:String):Observable<UserKitBean>{
        return RetrofitManager.service.getUserRelation(netease_id)
            .compose(SchedulerUtils.ioToMain())
    }


}