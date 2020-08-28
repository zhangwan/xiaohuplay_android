package com.tinytiger.titi.mvp.model.detail



import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.home.RecommendListBean
import com.tinytiger.common.net.data.msg.AddCommentBean
import com.tinytiger.common.net.data.user.FollowUserBean
import com.tinytiger.common.net.data.video.CommentDetailListBean
import com.tinytiger.common.net.data.video.CommentListBean
import com.tinytiger.common.net.data.video.ContentInfoBean
import com.tinytiger.common.net.scheduler.SchedulerUtils
import io.reactivex.Observable


class PlayModel : BaseModel() {


    fun getContentInfo(content_id:String): Observable<ContentInfoBean> {
        return RetrofitManager.service.getContent(content_id)
            .compose(SchedulerUtils.ioToMain())
    }


    fun indexComment(content_id:String,page:Int): Observable<CommentListBean> {
        return RetrofitManager.service.indexComment(content_id,page,10)
            .compose(SchedulerUtils.ioToMain())
    }

    fun indexCommentDetail(content_id:String,comment_id: Int,page:Int): Observable<CommentDetailListBean> {
        return RetrofitManager.service.indexCommentDetail(content_id,comment_id,page,10)
            .compose(SchedulerUtils.ioToMain())
    }
    fun addComment(content_id: String,comment_id: Int, content: String) : Observable<AddCommentBean> {
        return RetrofitManager.service.addComment(content_id,comment_id,content)
            .compose(SchedulerUtils.ioToMain())
    }

    fun getIntroContentList(content_id: String) : Observable<RecommendListBean> {
        return RetrofitManager.service.getIntroContentList(content_id)
            .compose(SchedulerUtils.ioToMain())
    }


    fun addCommentLike(is_like:Int,content_id: String,comment_id: Int) : Observable<BaseBean> {
        return if(is_like==-1) {
            RetrofitManager.service.addCommentLike(content_id, comment_id)
                .compose(SchedulerUtils.ioToMain())
        }else{
            RetrofitManager.service.cancelCommentLike(content_id,comment_id)
                .compose(SchedulerUtils.ioToMain())
        }
    }


    fun addContentLike(is_like:Int,content_id: String) : Observable<BaseBean> {
        return if(is_like==-1) {
            RetrofitManager.service.addContentLike(content_id)
                .compose(SchedulerUtils.ioToMain())
        }else{
            RetrofitManager.service.cancelContentLike(content_id)
                .compose(SchedulerUtils.ioToMain())
        }

    }



    fun addContentCollect(is_collect:Int,content_id: String) : Observable<BaseBean> {
        return if(is_collect==-1) {
            RetrofitManager.service.addContentCollect(content_id)
                .compose(SchedulerUtils.ioToMain())
        }else{
            RetrofitManager.service.cancelContentCollect(content_id)
                .compose(SchedulerUtils.ioToMain())
        }

    }



    fun delComment(content_id: String,comment_id: Int) : Observable<BaseBean> {
        return RetrofitManager.service.delComment(content_id,comment_id)
            .compose(SchedulerUtils.ioToMain())
    }


    fun addBlack(is_black:Int,user_id: String)  : Observable<BaseBean> {
        return if(is_black!=1){
            RetrofitManager.service.addBlack(user_id)
                .compose(SchedulerUtils.ioToMain())
        }else{
            RetrofitManager.service.cancelBlack(user_id)
                .compose(SchedulerUtils.ioToMain())
        }

    }

//    fun addCommentLike(content_id: String,comment_id: Int) : Observable<BaseBean> {
//        return RetrofitManager.service.addCommentLike(content_id,comment_id)
//            .compose(SchedulerUtils.ioToMain())
//    }

//    fun cancelCommentLike(content_id:String,comment_id: Int): Observable<BaseBean> {
//        return RetrofitManager.service.cancelCommentLike(content_id,comment_id)
//            .compose(SchedulerUtils.ioToMain())
//    }


    fun doFollow(is_mutual:Int,user_id:String): Observable<FollowUserBean> {
        return if(is_mutual<0){
            RetrofitManager.service.doFollow(user_id)
                .compose(SchedulerUtils.ioToMain())
        }else{
            RetrofitManager.service.cancelFans(user_id)
                .compose(SchedulerUtils.ioToMain())
        }
    }


}