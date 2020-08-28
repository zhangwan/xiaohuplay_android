package com.tinytiger.titi.mvp.model.circle

import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.QiniuBean
import com.tinytiger.common.net.data.circle.postsend.DraftDetailBean
import com.tinytiger.common.net.data.circle.postsend.DraftListBean
import com.tinytiger.common.net.scheduler.SchedulerUtils
import io.reactivex.Observable

class CirclesPost1Model : BaseModel() {


    fun getQiniuToken(): Observable<QiniuBean> {
        return RetrofitManager.service.getQiniuToken().compose(SchedulerUtils.ioToMain())
    }


    fun addPost(type: Int, is_draft: Int, post_id: Int, user_ids: String, circle_ids: String,
        circle_names: String, img_url: String, circle_id: String, modular_id: String,
        content: String, cover_url: String, video_url: String, video_length: String,
        post_title: String): Observable<DraftDetailBean> {
        return RetrofitManager.serviceMoment.addPost(type, is_draft, post_id, user_ids, circle_ids,
            circle_names, img_url, circle_id, modular_id, content, cover_url, video_url,
            video_length, post_title).compose(SchedulerUtils.ioToMain())
    }

    fun indexDrafts(page: Int): Observable<DraftListBean> {
        return RetrofitManager.serviceMoment.indexDrafts(page).compose(SchedulerUtils.ioToMain())
    }

    fun delPost(post_ids: String, is_draft: String): Observable<BaseBean> {
        return RetrofitManager.serviceMoment.delPost(post_ids, is_draft)
            .compose(SchedulerUtils.ioToMain())
    }

    fun draftDetail(post_ids: String): Observable<DraftDetailBean> {
        return RetrofitManager.serviceMoment.draftDetail(post_ids)
            .compose(SchedulerUtils.ioToMain())
    }
}


