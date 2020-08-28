package com.tinytiger.titi.mvp.contract.gametools

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.gametools.comment.CommentAssessInfo


/**
 *
 * @author zhw_luke
 * @date 2020/2/28 0028 下午 2:18
 * @Copyright 小虎互联科技
 * @since 2.0.0
 * @doc
 */
interface GameCommentContract {
    interface View : IBaseView {
        fun showAssessList(bean: CommentAssessInfo)

        fun delComment(comment_id: Int)

        fun isLike(islike:Int,comment_id: Int)

        fun addCommentTxt(txt:String,comment_id: Int)
    }

    interface Presenter : IPresenter<View> {
        fun indexAssessComment(game_id: String, assess_id: String, comment_id: String, page: Int)
    }


}