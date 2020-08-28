package com.tinytiger.titi.mvp.model.news


import com.tinytiger.common.net.RetrofitManager

import com.tinytiger.common.net.scheduler.SchedulerUtils
import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.home.NewsListBean
import com.tinytiger.common.net.data.user.FollowUserBean


import io.reactivex.Observable


class NewsModel : BaseModel() {

    fun getContentInfo(content_id: String):Observable<NewsListBean>{
        return RetrofitManager.service.getNewsContentInfo(content_id)
                .compose(SchedulerUtils.ioToMain())
    }

    fun addContentLike(content_id:String):Observable<BaseBean>{
        return RetrofitManager.service.addContentLike(content_id)
            .compose(SchedulerUtils.ioToMain())
    }


    fun canceContentLike(content_id:String):Observable<BaseBean>{
        return RetrofitManager.service.cancelContentLike(content_id)
            .compose(SchedulerUtils.ioToMain())
    }


    fun addContentCollect(content_id:String):Observable<BaseBean>{
        return RetrofitManager.service.addContentCollect(content_id)
            .compose(SchedulerUtils.ioToMain())
    }

    fun cancelContentCollect(content_id:String):Observable<BaseBean>{
        return RetrofitManager.service.cancelContentCollect(content_id)
            .compose(SchedulerUtils.ioToMain())
    }


    fun addNewsComment(content_id:String,content:String):Observable<BaseBean>{
        return RetrofitManager.service.addNewsComment(content_id,content)
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