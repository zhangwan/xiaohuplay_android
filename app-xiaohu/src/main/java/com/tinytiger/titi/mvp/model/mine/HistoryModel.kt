package com.tinytiger.titi.mvp.model.mine



import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.scheduler.SchedulerUtils
import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.center.UserGameAmwayList
import com.tinytiger.common.net.data.mine.MyContentListBean
import com.tinytiger.common.net.data.user.FollowUserBean
import io.reactivex.Observable


class HistoryModel : BaseModel() {


    fun getViewHistory(page:Int): Observable<MyContentListBean> {
        return RetrofitManager.service.viewHistory(page)
            .compose(SchedulerUtils.ioToMain())
    }
    fun getUserGameAmwayViewHistory(page:Int): Observable<UserGameAmwayList> {
        return RetrofitManager.service.getUserGameAmwayViewHistory(page)
            .compose(SchedulerUtils.ioToMain())
    }

    fun delViewHistory(id:Int): Observable<BaseBean> {
        return RetrofitManager.service.delViewHistory(id)
            .compose(SchedulerUtils.ioToMain())
    }


    fun clearViewHistory(type:Int): Observable<BaseBean> {
        return RetrofitManager.service.clearViewHistory(type)
            .compose(SchedulerUtils.ioToMain())
    }

    fun likeAssessOrTag(game_id:String,assess_id:String,tag_id: String):Observable<BaseBean>{
        return RetrofitManager.service.likeAssessOrTag(game_id,assess_id,tag_id)
            .compose(SchedulerUtils.ioToMain())
    }


    fun doFollow(uid:String):Observable<FollowUserBean>{
        return RetrofitManager.service.doFollow(uid)
            .compose(SchedulerUtils.ioToMain())
    }

    fun cancelFans(uid:String):Observable<FollowUserBean>{
        return RetrofitManager.service.cancelFans(uid)
            .compose(SchedulerUtils.ioToMain())
    }


}