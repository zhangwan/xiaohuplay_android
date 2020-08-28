package com.tinytiger.titi.mvp.contract.video

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.msg.IndexMsgCommentListBean


/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description:  视频详情
*/
interface MsgCommentContract {
    interface View : IBaseView {
        fun showComment(bean: IndexMsgCommentListBean.DataBean)

        fun addCommentTxt(txt:String,comment_id: Int)
        fun isLike(islike:Int,comment_id: Int)

        fun delComment(comment_id: Int)

        fun showBlackStatus(is_black: Int,user_id:String)

    }

    interface Presenter : IPresenter<View> {
        fun indexMsgComment(content_id:String,comment_id: Int,type:Int,game_id:String,page:Int,gt_or_lt:Int)

    }
}