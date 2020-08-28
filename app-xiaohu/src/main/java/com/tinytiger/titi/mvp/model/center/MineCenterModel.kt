package com.tinytiger.titi.mvp.model.center

import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.center.MineGameListBean
import com.tinytiger.common.net.data.center.UserCollectWikiList
import com.tinytiger.common.net.data.center.UserGameAmwayList
import com.tinytiger.common.net.data.center.UserMedalList
import com.tinytiger.common.net.data.circle.PostInfoBean
import com.tinytiger.common.net.data.mine.MyContentListBean
import com.tinytiger.common.net.data.user.FollowUserBean
import com.tinytiger.common.net.scheduler.SchedulerUtils
import io.reactivex.Observable

class  MineCenterModel : BaseModel(){
    fun getUserFollowGameList(page:Int): Observable<MineGameListBean> {
        return RetrofitManager.service.getUserFollowGameList(page)
            .compose(SchedulerUtils.ioToMain())
    }
    fun GameFollow(game_id:String,cancel:Int): Observable<BaseBean> {
        return RetrofitManager.service.GameFollow(game_id,cancel.toString())
            .compose(SchedulerUtils.ioToMain())
    }

    fun getUserGameAmwayList(page:Int): Observable<UserGameAmwayList> {
        return RetrofitManager.service.getMineCommentList(page)
            .compose(SchedulerUtils.ioToMain())
    }

    fun delUserGameAmwayList(ids:String): Observable<BaseBean> {
        return RetrofitManager.service.delUserGameAmwayList(ids)
            .compose(SchedulerUtils.ioToMain())
    }


    fun getUserWikiCollectList(page:Int): Observable<UserCollectWikiList> {
        return RetrofitManager.service.getUserWikiCollectList(page)
            .compose(SchedulerUtils.ioToMain())
    }
    fun getUserTemContentCollectList(page:Int): Observable<UserCollectWikiList> {
        return RetrofitManager.serviceGame.getUserTemContentCollectList(page)
            .compose(SchedulerUtils.ioToMain())
    }



    fun collectList(page:Int): Observable<MyContentListBean> {
        return RetrofitManager.service.collectList(page)
            .compose(SchedulerUtils.ioToMain())
    }

    fun getUserGameAmwayCollectList(page:Int): Observable<UserGameAmwayList> {
        return RetrofitManager.service.getUserGameAmwayCollectList(page)
            .compose(SchedulerUtils.ioToMain())
    }
    fun likeAssessOrTag(game_id:String,assess_id:String,tag_id: String):Observable<BaseBean>{
        return RetrofitManager.service.likeAssessOrTag(game_id,assess_id,tag_id)
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


    fun batchCancelCollectGameAssess(ids:String):Observable<BaseBean>{
        return RetrofitManager.service.batchCancelCollectGameAssess(ids)
            .compose(SchedulerUtils.ioToMain())
    }
    fun batchCancelCollectNews(ids:String):Observable<BaseBean>{
        return RetrofitManager.service.batchCancelCollectNews(ids)
            .compose(SchedulerUtils.ioToMain())
    }
    fun batchCancelCollectGameWiki(ids:String):Observable<BaseBean>{
        return RetrofitManager.service.batchCancelCollectGameWiki(ids)
            .compose(SchedulerUtils.ioToMain())
    }
    fun batchCancelCollectNode(ids:String):Observable<BaseBean>{
        return RetrofitManager.service.batchCancelCollectNode(ids)
            .compose(SchedulerUtils.ioToMain())
    }

    fun getMedalList(page:Int):Observable<UserMedalList>{
        return RetrofitManager.service.getMedalList(page,1)
            .compose(SchedulerUtils.ioToMain())
    }

    fun wearMedal(medal_id:String):Observable<BaseBean>{
        return RetrofitManager.service.wearMedal(medal_id)
            .compose(SchedulerUtils.ioToMain())
    }



    fun collectWiki(wiki_id:String,is_collect:Int) :  Observable<BaseBean>{
        if (is_collect==1){
            return RetrofitManager.serviceGame.cancelCollectTemContent("[$wiki_id]")
                .compose(SchedulerUtils.ioToMain())
        }else{
            return RetrofitManager.serviceGame.collectTemContent(wiki_id)
                .compose(SchedulerUtils.ioToMain())
        }
    }
    fun getPostList(page: Int):Observable<PostInfoBean>{
        return RetrofitManager.service.getPostList(page)
            .compose(SchedulerUtils.ioToMain())
    }
    fun likePost(post_id:String):Observable<BaseBean>{
        return RetrofitManager.serviceMoment.likePost(post_id)
            .compose(SchedulerUtils.ioToMain())
    }
    fun getDynamicList(page: Int):Observable<PostInfoBean>{
        return RetrofitManager.service.getDynamicList(page)
            .compose(SchedulerUtils.ioToMain())
    }
    fun getAnswerList(page: Int):Observable<PostInfoBean>{
        return RetrofitManager.service.getAnswerList(page)
            .compose(SchedulerUtils.ioToMain())
    }
    fun delPost(post_ids: String,is_draft: String):Observable<BaseBean>{
        return RetrofitManager.serviceMoment.delPost(post_ids,is_draft)
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
}


