package com.tinytiger.titi.mvp.model.circle

import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.center.HomeDynamicList
import com.tinytiger.common.net.data.center.MineGameListBean
import com.tinytiger.common.net.data.circle.CircleInfoBean
import com.tinytiger.common.net.data.circle.CircleMeBean
import com.tinytiger.common.net.data.circle.CircleRecommendedBean
import com.tinytiger.common.net.data.circle.PostInfoBean
import com.tinytiger.common.net.data.user.FollowUserBean
import com.tinytiger.common.net.scheduler.SchedulerUtils
import io.reactivex.Observable

class  CirclesInfoModel : BaseModel(){


    fun getGameCircleInfo(game_id: String,circle_id:String): Observable<CircleInfoBean> {
        return RetrofitManager.serviceMoment.getGameCircleInfo(game_id,circle_id)
            .compose(SchedulerUtils.ioToMain())
    }

    fun getCircleTagModularPostList(circle_id:String,tag_modular_id: String,fixed_modular_type: String,page: Int): Observable<PostInfoBean> {
        return RetrofitManager.serviceMoment.getCircleTagModularPostList(circle_id,tag_modular_id,fixed_modular_type,page)
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

    fun joinCircle(circle_id: String): Observable<BaseBean> {
        return RetrofitManager.serviceMoment.joinCircle(circle_id)
            .compose(SchedulerUtils.ioToMain())
    }
}


