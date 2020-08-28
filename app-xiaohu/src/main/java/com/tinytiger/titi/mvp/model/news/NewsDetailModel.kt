package com.tinytiger.titi.mvp.model.news



import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.gametools.GameAssessBean
import com.tinytiger.common.net.data.gametools.comment.CommentAddBean
import com.tinytiger.common.net.data.gametools.comment.CommentAssessInfo
import com.tinytiger.common.net.data.home.RecommendListBean
import com.tinytiger.common.net.data.user.FollowUserBean
import com.tinytiger.common.net.data.user.LikeBean
import com.tinytiger.common.net.data.video.CommentDetailListBean
import com.tinytiger.common.net.data.video.CommentListBean
import com.tinytiger.common.net.data.video.ContentInfoBean
import com.tinytiger.common.net.scheduler.SchedulerUtils
import io.reactivex.Observable


class NewsDetailModel : BaseModel() {

    fun getContentInfo(content_id:String): Observable<ContentInfoBean> {
        return RetrofitManager.service.getContent(content_id)
            .compose(SchedulerUtils.ioToMain())
    }


    fun indexComment(content_id:String,page:Int,comment_type: Int): Observable<CommentListBean> {
        return RetrofitManager.service.indexComment(content_id,page,10,comment_type)
            .compose(SchedulerUtils.ioToMain())
    }

    fun indexCommentDetail(content_id:String,comment_id: Int,page:Int): Observable<CommentDetailListBean> {
        return RetrofitManager.service.indexCommentDetail(content_id,comment_id,page,10)
            .compose(SchedulerUtils.ioToMain())
    }
    fun addComment(content_id: String,comment_id: Int, content: String) : Observable<BaseBean> {
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
    fun addContentLike(content_id: String) : Observable<BaseBean> {
        return RetrofitManager.service.addContentLike(content_id)
            .compose(SchedulerUtils.ioToMain())
    }

    fun cancelContentLike(content_id:String): Observable<BaseBean> {
        return RetrofitManager.service.cancelContentLike(content_id)
            .compose(SchedulerUtils.ioToMain())
    }

    fun addContentCollect(content_id: String) : Observable<BaseBean> {
        return RetrofitManager.service.addContentCollect(content_id)
            .compose(SchedulerUtils.ioToMain())
    }

    fun cancelContentCollect(content_id:String): Observable<BaseBean> {
        return RetrofitManager.service.cancelContentCollect(content_id)
            .compose(SchedulerUtils.ioToMain())
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

    fun getAssessInfo(game_id:String,assess_id:String):Observable<GameAssessBean>{
        return RetrofitManager.service.getAssessInfo(game_id,assess_id)
            .compose(SchedulerUtils.ioToMain())
    }

    fun indexAssessComment(game_id: String,assess_id:String,page:Int,type:Int):Observable<CommentAssessInfo>{
        return RetrofitManager.service.indexAssessComment(game_id,assess_id,page,type)
            .compose(SchedulerUtils.ioToMain())
    }


    fun doFollow(uid:String):Observable<FollowUserBean>{
        return RetrofitManager.service.doFollow(uid)
            .compose(SchedulerUtils.ioToMain())
    }

    fun cancelFans(uid:String):Observable<FollowUserBean>{
        return RetrofitManager.service.cancelFans(uid)
            .compose(SchedulerUtils.ioToMain())
    }

    fun likeAssessOrTag(game_id:String,assess_id:String,tag_id: String):Observable<LikeBean>{
        return RetrofitManager.service.likeAssessOrTag(game_id,assess_id,tag_id)
            .compose(SchedulerUtils.ioToMain())
    }

    fun handleLike(game_id:String,assess_id:String,comment_id: String):Observable<LikeBean>{
        return RetrofitManager.service.handleLike(game_id,assess_id,comment_id)
            .compose(SchedulerUtils.ioToMain())
    }

    fun collectAssess(game_id:String,assess_id:String):Observable<LikeBean>{
        return RetrofitManager.service.collectAssess(game_id,assess_id)
            .compose(SchedulerUtils.ioToMain())
    }


    fun addComment(game_id: String, assess_id: String, comment_id: String, content: String): Observable<CommentAddBean> {
        return RetrofitManager.service.assessaddComment(game_id,assess_id, comment_id, content)
            .compose(SchedulerUtils.ioToMain())
    }


    fun delComment( comment_id: String): Observable<BaseBean> {
        return RetrofitManager.service.assessDelComment(comment_id)
            .compose(SchedulerUtils.ioToMain())
    }

}