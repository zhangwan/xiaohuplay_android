package com.tinytiger.titi.mvp.model.news



import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.msg.AddCommentBean
import com.tinytiger.common.net.data.video.CommentListBean
import com.tinytiger.common.net.scheduler.SchedulerUtils
import io.reactivex.Observable


class CommentTowModel : BaseModel() {

    fun indexComment(content_id: String, comment_id: Int, page: Int,type:Int): Observable<CommentListBean> {
        return RetrofitManager.service.indexComment(content_id, comment_id, page, 20,type)
            .compose(SchedulerUtils.ioToMain())
    }

    fun delComment(content_id: String, comment_id: Int): Observable<BaseBean> {
        return RetrofitManager.service.delComment(content_id, comment_id)
            .compose(SchedulerUtils.ioToMain())
    }

    fun addCommentLike(is_like: Int, content_id: String, comment_id: Int): Observable<BaseBean> {
        return if (is_like == 1) {
            RetrofitManager.service.addCommentLike(content_id, comment_id)
                .compose(SchedulerUtils.ioToMain())
        } else {
            RetrofitManager.service.cancelCommentLike(content_id, comment_id)
                .compose(SchedulerUtils.ioToMain())
        }
    }


    fun addComment(content_id: String, comment_id: Int, content: String): Observable<AddCommentBean> {
        return RetrofitManager.service.addComment(content_id, comment_id, content)
            .compose(SchedulerUtils.ioToMain())
    }

}