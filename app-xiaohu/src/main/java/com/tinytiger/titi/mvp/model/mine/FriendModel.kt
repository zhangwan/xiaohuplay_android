package com.tinytiger.titi.mvp.model.mine



import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.scheduler.SchedulerUtils
import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.data.user.FollowUserBean
import com.tinytiger.common.net.data.user.FriendListBean
import io.reactivex.Observable


class FriendModel : BaseModel() {


    fun indexFollow(page:Int,keyWords:String): Observable<FriendListBean> {
        return RetrofitManager.service.indexFollow(page,keyWords)
            .compose(SchedulerUtils.ioToMain())
    }

    fun indexFollow(user_id: String,page:Int,keyWords:String): Observable<FriendListBean> {
        return RetrofitManager.service.indexFollow(user_id,page,keyWords)
            .compose(SchedulerUtils.ioToMain())
    }

    fun indexFans(page:Int,keyWords:String): Observable<FriendListBean> {
        return RetrofitManager.service.indexFans(page,keyWords)
            .compose(SchedulerUtils.ioToMain())
    }


    fun indexFans(user_id: String,page:Int,keyWords:String): Observable<FriendListBean> {
        return RetrofitManager.service.indexFans(user_id,page,keyWords)
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