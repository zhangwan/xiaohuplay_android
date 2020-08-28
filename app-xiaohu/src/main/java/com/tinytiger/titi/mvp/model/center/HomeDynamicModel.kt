package com.tinytiger.titi.mvp.model.center

import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.center.HomeDynamicList
import com.tinytiger.common.net.data.center.MineGameListBean
import com.tinytiger.common.net.data.user.UserDynamicBean
import com.tinytiger.common.net.data.user.UserDynamicList
import com.tinytiger.common.net.scheduler.SchedulerUtils
import io.reactivex.Observable

class  HomeDynamicModel : BaseModel(){
    fun getDynamic(user_id:String,page:Int): Observable<HomeDynamicList> {
        return RetrofitManager.service.getDynamic(user_id,page)
            .compose(SchedulerUtils.ioToMain())
    }

    fun getGame(user_id:String,page:Int): Observable<MineGameListBean> {
        return RetrofitManager.service.getGame(user_id,page)
            .compose(SchedulerUtils.ioToMain())
    }


    fun getUserDynamic(user_id:String,page:Int): Observable<UserDynamicList> {
        return RetrofitManager.serviceUser.getUserDynamic(user_id,page)
            .compose(SchedulerUtils.ioToMain())
    }

    fun likePost(post_id:String):Observable<BaseBean>{
        return RetrofitManager.serviceMoment.likePost(post_id)
            .compose(SchedulerUtils.ioToMain())
    }
}


