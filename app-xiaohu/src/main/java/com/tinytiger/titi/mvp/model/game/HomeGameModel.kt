package com.tinytiger.titi.mvp.model.game


import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.ad.AdListBean
import com.tinytiger.common.net.data.home.RecommendListBean
import com.tinytiger.common.net.scheduler.SchedulerUtils
import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.titi.R


import io.reactivex.Observable


class HomeGameModel : BaseModel() {

    fun getAdList():Observable<AdListBean>{
        return RetrofitManager.service.getAdList()
                .compose(SchedulerUtils.ioToMain())
    }

    fun getRecommendContentList():Observable<RecommendListBean>{
        return RetrofitManager.service.getRecommendContentList()
            .compose(SchedulerUtils.ioToMain())
    }
}