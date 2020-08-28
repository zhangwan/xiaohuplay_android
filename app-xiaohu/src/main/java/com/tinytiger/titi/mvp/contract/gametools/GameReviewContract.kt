package com.tinytiger.titi.mvp.contract.gametools

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.gametools.GameAssessBean
import com.tinytiger.common.net.data.gametools.comment.CommentAssessInfo


/**
 *
 * @author zhw_luke
 * @date 2020/2/28 0028 下午 2:18
 * @Copyright 小虎互联科技
 * @since 2.0.0
 * @doc
 */
interface  GameReviewContract{
    interface View : IBaseView {
        fun showAssessInfo(bean: GameAssessBean)
        fun showAssessList(bean: CommentAssessInfo)

        fun getUserFollow(is_mutual:Int)

        fun addCommentTxt(txt:String,comment_id: Int)

        fun delComment(comment_id: Int)

        fun showLike(is_like:Int,id:Int)
        fun showCollect(is_collect:Int)
    }

    interface Presenter : IPresenter<View> {
        fun getAssessInfo(game_id:String,assess_id:String)
    }
}