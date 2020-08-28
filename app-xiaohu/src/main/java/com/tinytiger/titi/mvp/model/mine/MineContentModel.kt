package com.tinytiger.titi.mvp.model.mine



import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.scheduler.SchedulerUtils
import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.mine.MyContentListBean
import io.reactivex.Observable


class MineContentModel : BaseModel() {


    fun getFocusUserWorks(page:Int): Observable<MyContentListBean> {
        return RetrofitManager.service.getFocusUserWorks(page)
            .compose(SchedulerUtils.ioToMain())
    }

    fun cancelLike(content_id:Int): Observable<BaseBean> {
        return RetrofitManager.service.cancelContentLike(content_id.toString())
            .compose(SchedulerUtils.ioToMain())
    }


    fun addLike(content_id:Int): Observable<BaseBean> {
        return RetrofitManager.service.addContentLike(content_id.toString())
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
}