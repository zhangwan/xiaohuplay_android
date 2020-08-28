package com.tinytiger.titi.mvp.model.video


import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.msg.AddCommentBean
import com.tinytiger.common.net.data.user.FollowUserBean
import com.tinytiger.common.net.data.video.CommentDetailListBean
import com.tinytiger.common.net.scheduler.SchedulerUtils
import io.reactivex.Observable


class CommentModel : BaseModel() {


    fun indexCommentDetail(content_id: String, comment_id: Int, page: Int): Observable<CommentDetailListBean> {
        return RetrofitManager.service.indexCommentDetail(content_id, comment_id, page, 10)
            .compose(SchedulerUtils.ioToMain())
    }

    fun addComment(content_id: String, comment_id: Int, content: String): Observable<AddCommentBean> {
        return RetrofitManager.service.addComment(content_id, comment_id, content)
            .compose(SchedulerUtils.ioToMain())
    }


    fun addContentLike(content_id: String): Observable<BaseBean> {
        return RetrofitManager.service.addContentLike(content_id)
            .compose(SchedulerUtils.ioToMain())
    }


    fun addContentCollect(content_id: String): Observable<BaseBean> {
        return RetrofitManager.service.addContentCollect(content_id)
            .compose(SchedulerUtils.ioToMain())
    }

    fun cancelContentCollect(content_id: String): Observable<BaseBean> {
        return RetrofitManager.service.cancelContentCollect(content_id)
            .compose(SchedulerUtils.ioToMain())
    }

    fun delComment(content_id: String, comment_id: Int): Observable<BaseBean> {
        return RetrofitManager.service.delComment(content_id, comment_id)
            .compose(SchedulerUtils.ioToMain())
    }


    fun addBlack(is_black: Int, user_id: String): Observable<BaseBean> {
        return if (is_black == 0) {
            RetrofitManager.service.addBlack(user_id)
                .compose(SchedulerUtils.ioToMain())
        } else {
            RetrofitManager.service.cancelBlack(user_id)
                .compose(SchedulerUtils.ioToMain())
        }

    }

    fun addCommentLike(is_like: Int, content_id: String, comment_id: Int): Observable<BaseBean> {
        return if (is_like == -1) {
            RetrofitManager.service.addCommentLike(content_id, comment_id)
                .compose(SchedulerUtils.ioToMain())
        } else {
            RetrofitManager.service.cancelCommentLike(content_id, comment_id)
                .compose(SchedulerUtils.ioToMain())
        }
    }


    fun doFollow(is_mutual: Int, user_id: String): Observable<FollowUserBean> {
        return if (is_mutual < 0) {
            RetrofitManager.service.doFollow(user_id)
                .compose(SchedulerUtils.ioToMain())
        } else {
            RetrofitManager.service.cancelFans(user_id)
                .compose(SchedulerUtils.ioToMain())
        }
    }


}