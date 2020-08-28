package com.tinytiger.titi.mvp.model.circle

import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.circle.postsend.*
import com.tinytiger.common.net.scheduler.SchedulerUtils
import io.reactivex.Observable

class CirclesPostModel : BaseModel() {



    fun indexCircleByCate(keywords: String, page: Int): Observable<SelectCircler2Bean> {
        return RetrofitManager.serviceMoment.indexCircleByCate(keywords, page)
            .compose(SchedulerUtils.ioToMain())
    }

    fun indexCircleByCate(page: Int): Observable<SelectCircler1Bean> {
        return RetrofitManager.serviceMoment.indexCircleByCate(page)
            .compose(SchedulerUtils.ioToMain())
    }



    fun indexCircleByModular(keywords: String, page: Int): Observable<SelectCirclerName2Bean> {
        return RetrofitManager.serviceMoment.indexCircleByModular(keywords, page,20)
            .compose(SchedulerUtils.ioToMain())
    }


    fun indexCircleByModular1(circle_id: String, page: Int): Observable<SelectCirclerNameBean> {
        return RetrofitManager.serviceMoment.indexCircleByModular(circle_id, page)
            .compose(SchedulerUtils.ioToMain())
    }


    fun getUserList(post_id: String): Observable<SelectFriendBean> {
        return RetrofitManager.serviceMoment.getUserList(post_id)
            .compose(SchedulerUtils.ioToMain())
    }


    fun getFollowUserList(page: Int): Observable<SelectFriend2Bean> {
        return RetrofitManager.serviceMoment.getFollowUserList(page)
            .compose(SchedulerUtils.ioToMain())
    }

    fun inviteUser(post_id: String,user_ids: String): Observable<BaseBean> {
        return RetrofitManager.serviceMoment.inviteUser(post_id,user_ids)
            .compose(SchedulerUtils.ioToMain())
    }

}


