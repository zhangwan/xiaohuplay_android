package com.tinytiger.titi.mvp.model.gametools


import com.tinytiger.common.base.BaseModel

import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.gametools.GameAssessBean
import com.tinytiger.common.net.data.gametools.comment.CommentAddBean
import com.tinytiger.common.net.data.gametools.comment.CommentAssessInfo
import com.tinytiger.common.net.data.msg.AddCommentBean
import com.tinytiger.common.net.data.user.FollowUserBean
import com.tinytiger.common.net.data.user.LikeBean
import com.tinytiger.common.net.scheduler.SchedulerUtils
import io.reactivex.Observable


class GameCommentModel : BaseModel() {

    fun indexAssessComment(game_id: String, assess_id: String, comment_id: String,page: Int):Observable<CommentAssessInfo>{
        return RetrofitManager.service.indexAssessComment2(game_id,assess_id,page,comment_id)
            .compose(SchedulerUtils.ioToMain())
    }


    fun delComment( comment_id: String): Observable<BaseBean> {
        return RetrofitManager.service.assessDelComment(comment_id)
            .compose(SchedulerUtils.ioToMain())
    }

    fun handleLike(game_id: String, assess_id: String, comment_id: String): Observable<LikeBean> {
        return RetrofitManager.service.handleLike(game_id, assess_id,comment_id)
            .compose(SchedulerUtils.ioToMain())
    }

    fun addComment(game_id: String, assess_id: String, comment_id: String, content: String): Observable<CommentAddBean> {
        return RetrofitManager.service.assessaddComment(game_id,assess_id, comment_id, content)
            .compose(SchedulerUtils.ioToMain())
    }
}