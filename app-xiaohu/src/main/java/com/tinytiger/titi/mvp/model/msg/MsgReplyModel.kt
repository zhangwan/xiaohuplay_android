package com.tinytiger.titi.mvp.model.msg


import com.tinytiger.common.net.data.msg.ReplyListBean
import com.tinytiger.common.base.BaseModel

import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.msg.IndexMsgCommentListBean
import com.tinytiger.common.net.scheduler.SchedulerUtils
import io.reactivex.Observable


class MsgReplyModel : BaseModel() {

    fun getReplyList(page:Int):Observable<ReplyListBean>{
        return RetrofitManager.service.getReplyList(page)
            .compose(SchedulerUtils.ioToMain())
    }
    fun getLike(page:Int):Observable<ReplyListBean>{
        return RetrofitManager.service.getLike(page)
            .compose(SchedulerUtils.ioToMain())
    }
    fun indexMsgComment(content_id:String,comment_id: Int,type:Int,game_id:String,page:Int): Observable<IndexMsgCommentListBean> {
        return RetrofitManager.service.indexMsgComment(content_id,comment_id,type,game_id,page,0)
            .compose(SchedulerUtils.ioToMain())
    }

    fun editReplyRead(comment_id: Int):Observable<BaseBean>{
        return RetrofitManager.service.editReplyRead(comment_id)
            .compose(SchedulerUtils.ioToMain())
    }

    fun editLikeRead(comment_id: Int,read_type: Int):Observable<BaseBean>{
        return RetrofitManager.service.editLikeRead(comment_id,read_type)
            .compose(SchedulerUtils.ioToMain())
    }

}