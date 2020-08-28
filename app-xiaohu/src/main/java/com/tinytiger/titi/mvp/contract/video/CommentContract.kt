package com.tinytiger.titi.mvp.contract.video

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.video.CommentDetailListBean
import com.tinytiger.common.net.data.video.CommentListBean
import com.tinytiger.common.net.data.video.ReplyDetailBean


/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description:  视频详情
*/
interface CommentContract {
    interface View : IBaseView {


        fun showComment(bean: CommentListBean.DataBean)

        fun showCommentDetail(bean: CommentDetailListBean.DataBean)


        fun showDelComment(position:Int)

        fun showResult()

        fun  addCommentTxt(reply: ReplyDetailBean)

        fun  showBlackStatus(is_black: Int, userId: String)

        fun  showLikeStatus(is_like: Int,comment_id: Int)
    }

    interface Presenter : IPresenter<View> {




//        fun indexComment(content_id: String, page: Int)

        fun indexCommentDetail(content_id:String,comment_id: Int,page:Int)

        fun addComment(content_id: String,comment_id: Int, content: String,hint:String)


//        fun addContentLike(content_id: String)
//
//        fun cancelLike(content_id: String)
//
//        fun addContentCollect(content_id: String)

//        fun cancelCollect(content_id: String)

          fun addCommentLike(is_like:Int,content_id: String,comment_id: Int)

//        fun cancelCommentLike(content_id: String,comment_id: Int)

//        fun doFollow(is_mutual:Int,user_id:String)

        fun delComment(position:Int,content_id: String,comment_id: Int)

        fun addBlack(is_black:Int,user_id: String,netease_id: String?)
    }
}