package com.tinytiger.titi.mvp.model.gametools

import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.gametools.GameLibListBean
import com.tinytiger.common.net.scheduler.SchedulerUtils
import io.reactivex.Observable

class  GameLibModel : BaseModel(){

    fun getGameList(type: Int): Observable<GameLibListBean> {
        return RetrofitManager.service.getGameRankingList(type)
            .compose(SchedulerUtils.ioToMain())
    }
}