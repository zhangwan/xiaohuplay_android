package com.tinytiger.titi.mvp.model.home



import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.scheduler.SchedulerUtils
import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.ad.AdListBean
import com.tinytiger.common.net.data.home2.AmwayWallListBean
import com.tinytiger.common.net.data.home2.PushIndexBean
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.titi.R


import io.reactivex.Observable


class HomeModel : BaseModel() {



    fun getAdList():Observable<AdListBean>{
        return RetrofitManager.service.getAdList()
            .compose(SchedulerUtils.ioToMain())
    }
    fun clickAdRecord(ad_id: String):Observable<BaseBean>{
        val user_id=SpUtils.getString(R.string.user_id,"0")
        return RetrofitManager.service.clickAdRecord(ad_id,user_id,"2")
            .compose(SchedulerUtils.ioToMain())
    }
    fun amwayWallRecommend(source:String):Observable<AmwayWallListBean>{
        return RetrofitManager.serviceGame.amwayWallRecommend(source)
            .compose(SchedulerUtils.ioToMain())
    }


    fun indexGame():Observable<PushIndexBean>{
        return RetrofitManager.serviceGame.indexGame()
            .compose(SchedulerUtils.ioToMain())
    }
}