package com.tinytiger.titi.mvp.model.circle

import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.circle.CircleMeBean
import com.tinytiger.common.net.data.circle.CircleRecommendedBean
import com.tinytiger.common.net.data.circle.home.CircleAttentionBean
import com.tinytiger.common.net.data.circle.home.CircleAttentionUserBean
import com.tinytiger.common.net.data.user.FollowUserBean
import com.tinytiger.common.net.scheduler.SchedulerUtils
import io.reactivex.Observable

class CirclesHomeModel : BaseModel() {

    fun focusList(page: Int): Observable<CircleAttentionBean> {
        return RetrofitManager.serviceMoment.focusList(page)
            .compose(SchedulerUtils.ioToMain())
    }
    fun recommendedUserList(page: Int): Observable<CircleAttentionUserBean> {
        return RetrofitManager.serviceMoment.recommendedUserList(page,10000)
            .compose(SchedulerUtils.ioToMain())
    }

    fun myCircleList(page: Int): Observable<CircleMeBean> {
        return RetrofitManager.serviceMoment.myCircleList(page)
            .compose(SchedulerUtils.ioToMain())
    }

    fun recommendedCircleList(): Observable<CircleRecommendedBean> {
        return RetrofitManager.serviceMoment.recommendedCircleList()
            .compose(SchedulerUtils.ioToMain())
    }


    fun recommendedPost(page: Int): Observable<CircleAttentionBean> {
        return RetrofitManager.serviceMoment.recommendedPost(page)
            .compose(SchedulerUtils.ioToMain())
    }


    fun doFollow(is_mutual:Int,user_id:String): Observable<FollowUserBean> {
        //1:互相关注 0:登录者关注了对方 -1:未关注 -2:自己
        return if(is_mutual<0){
            RetrofitManager.serviceUser.doFollow(user_id)
                .compose(SchedulerUtils.ioToMain())
        }else{
            RetrofitManager.serviceUser.cancelFans(user_id)
                .compose(SchedulerUtils.ioToMain())
        }
    }
    fun likePost(post_id:String):Observable<BaseBean>{
        return RetrofitManager.serviceMoment.likePost(post_id)
            .compose(SchedulerUtils.ioToMain())
    }
}


