package com.tinytiger.titi.mvp.contract.news

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.home.RecommendBean
import com.tinytiger.common.net.data.video.CommentListBean
import com.tinytiger.common.net.data.video.ContentInfoBean


/**
 *
 * @author zhw_luke
 * @date 2020/3/11 0011 下午 2:51
 * @Copyright 小虎互联科技
 * @since 2.0.0
 * @doc
 */
interface CommentInfoContract {
    interface View : IBaseView {

        fun showContentInfo(bean: ContentInfoBean.DataBean)

        fun showComment(bean: CommentListBean.DataBean)

        fun  showLikeStatus(is_like: Int,comment_id: Int)

        fun showIntroContentList(bean: ArrayList<RecommendBean>)

        fun showResult()

        fun  showMutual(is_mutual:Int)

        fun  showBlackStatus(is_black: Int, userId: String)

        fun  showDelComment(comment_id:Int)
    }

    interface Presenter : IPresenter<View> {

        fun getContentInfo(content_id: String)

        fun indexComment(content_id:String,page:Int)

        fun indexCommentDetail(content_id:String,comment_id: Int,page:Int)

        fun addComment(content_id: String,comment_id: Int, content: String)

        fun getIntroContentList(content_id: String)

        fun addLike(content_id: String)

        fun cancelLike(content_id: String)

        fun addCollect(content_id: String)

        fun cancelCollect(content_id: String)

//          fun addCommentLike(content_id: String,comment_id: Int)
//
//        fun cancelCommentLike(content_id: String,comment_id: Int)

        fun addCommentLike(is_like:Int,content_id: String,comment_id: Int)

        fun doFollow(is_mutual:Int,user_id:String)

        fun delComment(content_id: String,comment_id: Int)

        fun addBlack(is_black:Int,user_id: String,netease_id: String?)

    }
}