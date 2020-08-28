package com.tinytiger.titi.mvp.model.circle

import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.center.HomeDynamicList
import com.tinytiger.common.net.data.center.MineGameListBean
import com.tinytiger.common.net.data.circle.CircleMeBean
import com.tinytiger.common.net.data.circle.CircleRecommendedBean
import com.tinytiger.common.net.scheduler.SchedulerUtils
import io.reactivex.Observable

class  CirclesModel : BaseModel(){


    fun getGame(user_id:String,page:Int): Observable<MineGameListBean> {
        return RetrofitManager.service.getGame(user_id,page)
            .compose(SchedulerUtils.ioToMain())
    }


    fun myCircleList(page: Int): Observable<CircleMeBean> {
        return RetrofitManager.serviceMoment.myCircleList(page)
            .compose(SchedulerUtils.ioToMain())
    }


    fun joinCircle(circle_id: String): Observable<BaseBean> {
        return RetrofitManager.serviceMoment.joinCircle(circle_id)
            .compose(SchedulerUtils.ioToMain())
    }


    fun recommendedCircleList(): Observable<CircleRecommendedBean> {
        return RetrofitManager.serviceMoment.recommendedCircleList()
            .compose(SchedulerUtils.ioToMain())
    }

    fun allCircleList(page: Int): Observable<CircleMeBean> {
        return RetrofitManager.serviceMoment.allCircleList(page)
            .compose(SchedulerUtils.ioToMain())
    }
}


