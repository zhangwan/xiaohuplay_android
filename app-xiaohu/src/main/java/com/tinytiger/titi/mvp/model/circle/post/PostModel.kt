package com.tinytiger.titi.mvp.model.circle.post



import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.scheduler.SchedulerUtils
import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.CollectBean
import com.tinytiger.common.net.data.circle.post.PostDetailsBean
import com.tinytiger.common.net.data.circle.post.PostListBean
import com.tinytiger.common.net.data.msg.AddCommentBean
import com.tinytiger.common.net.data.msg.ReplyAdoptionBean
import com.tinytiger.common.net.data.search.*
import com.tinytiger.common.net.data.user.FollowUserBean

import io.reactivex.Observable
import retrofit2.http.Field


class PostModel : BaseModel() {

    fun getPostInfo(post_id:String):Observable<PostDetailsBean>{
        return RetrofitManager.serviceMoment.getPostInfo(post_id)
                .compose(SchedulerUtils.ioToMain())
    }

    fun allComments(post_id:String,comment_type: Int,page: Int):Observable<PostListBean>{
        return RetrofitManager.serviceMoment.allComments(post_id,comment_type,page)
            .compose(SchedulerUtils.ioToMain())
    }

    fun doFollow(is_mutual:Int,user_id:String): Observable<FollowUserBean> {
        //1:互相关注 0:登录者关注了对方 -1:未关注 -2:自己
        return if(is_mutual<0){
            RetrofitManager.serviceUser.doFollow(user_id)
                .compose(SchedulerUtils.ioToMain())
        }else{
            RetrofitManager.serviceUser.cancelFans(user_id)
                .compose(SchedulerUtils.ioToMain())
        }
    }

    fun likePostComment(comment_id:String):Observable<BaseBean>{
        return RetrofitManager.serviceMoment.likePostComment(comment_id)
            .compose(SchedulerUtils.ioToMain())
    }
    fun likePost(post_id:String):Observable<BaseBean>{
        return RetrofitManager.serviceMoment.likePost(post_id)
            .compose(SchedulerUtils.ioToMain())
    }

    fun addComment(post_id:String,comment_id: String?, content: String):Observable<AddCommentBean>{
        return RetrofitManager.serviceMoment.addComment(post_id,comment_id,content)
            .compose(SchedulerUtils.ioToMain())
    }

    fun delComment(comment_id: String):Observable<BaseBean>{
        return RetrofitManager.serviceMoment.delComment(comment_id)
            .compose(SchedulerUtils.ioToMain())
    }


    fun replyAdoption(post_id:String,comment_id: String):Observable<ReplyAdoptionBean>{
        return RetrofitManager.serviceMoment.replyAdoption(post_id,comment_id)
            .compose(SchedulerUtils.ioToMain())
    }

    fun subCommentList(comment_id:String,page: Int):Observable<PostListBean>{
        return RetrofitManager.serviceMoment.subCommentList(comment_id,2,page)
            .compose(SchedulerUtils.ioToMain())
    }

    fun collectPost(post_id:String):Observable<CollectBean>{
        return RetrofitManager.serviceMoment.collectPost(post_id)
            .compose(SchedulerUtils.ioToMain())
    }

}