package com.tinytiger.titi.mvp.contract.news

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.home.RecommendBean
import com.tinytiger.common.net.data.video.*


/**
 *
 * @author zhw_luke
 * @date 2020/3/11 0011 下午 2:51
 * @Copyright 小虎互联科技
 * @since 2.0.0
 * @doc
 */
interface CommentTwoContract {
    interface View : IBaseView {

        fun showCommentDetail(bean: CommentListBean)
        fun delComment(comment_id: Int)
        fun  addCommentTxt(reply: CommentDetailBean)
    }

    interface Presenter : IPresenter<View> {

        fun indexComment(content_id:String,comment_id: Int,page:Int,type:Int)


    }
}