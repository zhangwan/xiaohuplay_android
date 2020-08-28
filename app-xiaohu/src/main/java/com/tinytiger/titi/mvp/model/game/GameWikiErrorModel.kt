package com.tinytiger.titi.mvp.model.game

import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.QiniuBean
import com.tinytiger.common.net.data.circle.postsend.*
import com.tinytiger.common.net.scheduler.SchedulerUtils
import io.reactivex.Observable
import retrofit2.http.Field

class GameWikiErrorModel : BaseModel() {

    fun getQiniuToken(): Observable<QiniuBean> {
        return RetrofitManager.service.getQiniuToken()
            .compose(SchedulerUtils.ioToMain())
    }

    fun commitEntryError(content_id: String, error_desc: String, images: String): Observable<BaseBean> {
        return RetrofitManager.serviceGame.commitEntryError(content_id, error_desc, images)
            .compose(SchedulerUtils.ioToMain())
    }

}


