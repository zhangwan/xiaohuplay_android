package com.tinytiger.titi.mvp.contract.msg


import com.tinytiger.common.net.data.msg.ReplyListBean
import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.msg.IndexMsgCommentListBean


/**
 *
 * @author zhw_luke
 * @date 2019/10/22 0022 下午 2:12
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc
 */
interface MsgReplyContract {

    interface View : IBaseView {

        /**
         *
         */
        fun getReplyList(itemInfo: ReplyListBean)
        fun showComment(itemInfo: IndexMsgCommentListBean)

    }

    interface Presenter : IPresenter<View> {

        fun loadReplyList(page:Int)
        fun loadLikeList(page:Int)

    }


}