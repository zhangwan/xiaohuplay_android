package com.tinytiger.titi.mvp.model.mine



import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.scheduler.SchedulerUtils
import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.mine.MyContentListBean
import io.reactivex.Observable


class CollectionModel : BaseModel() {


    fun collectList(page:Int): Observable<MyContentListBean> {
        return RetrofitManager.service.collectList(page)
            .compose(SchedulerUtils.ioToMain())
    }

    fun cancelCollect(content_id:Int): Observable<BaseBean> {
        return RetrofitManager.service.cancelContentCollect(content_id.toString())
            .compose(SchedulerUtils.ioToMain())
    }



}