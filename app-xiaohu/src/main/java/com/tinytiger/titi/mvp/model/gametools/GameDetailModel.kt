package com.tinytiger.titi.mvp.model.gametools

import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.gametools.*
import com.tinytiger.common.net.data.gametools.wiki.WikiDitailList
import com.tinytiger.common.net.data.gametools.wiki.WikiModularList
import com.tinytiger.common.net.data.gametools.wiki.WikiModularOtherList
import com.tinytiger.common.net.scheduler.SchedulerUtils
import io.reactivex.Observable

class  GameDetailModel : BaseModel(){
    fun getGameInfo(game_id: String,user_id:String): Observable<GameInfoDetailBean> {
        return RetrofitManager.service.getGameInfo(game_id,user_id)
            .compose(SchedulerUtils.ioToMain())
    }
    fun getGameInfoCommentList(game_id: String,user_id:String,stars:String,type: Int,page:Int): Observable<GameCommentList> {
        return RetrofitManager.service.getGameInfoCommentList(game_id,user_id,stars,type,page)
            .compose(SchedulerUtils.ioToMain())
    }

    fun modularInfo(game_id: String): Observable<WikiModularList> {
        return RetrofitManager.service.modularInfo(game_id)
            .compose(SchedulerUtils.ioToMain())
    }
    fun otherModularInfo(submod_id: String): Observable<WikiModularOtherList> {
        return RetrofitManager.service.otherModularInfo(submod_id)
            .compose(SchedulerUtils.ioToMain())
    }

    fun getGameWikiList(game_id: String): Observable<GameWikiListBean> {
        return RetrofitManager.service.getGameWikiList(game_id)
            .compose(SchedulerUtils.ioToMain())
    }

    fun getGameWikiDetail(game_id: String, wiki_id: String):  Observable<GameWikiDetailBean>  {
        return RetrofitManager.service.getGameWikiDetail(game_id,wiki_id)
            .compose(SchedulerUtils.ioToMain())
    }

    fun likeAssessOrTag(game_id: String, assess_id: String):  Observable<BaseBean>  {
        return RetrofitManager.service.likeAssessOrTag(game_id,assess_id)
            .compose(SchedulerUtils.ioToMain())
    }



    fun collectGameWiki(wiki_id:String,is_collect:Int):  Observable<BaseBean>  {
        return if(is_collect == 0){
            //收藏
            RetrofitManager.service.collectGameWiki(wiki_id)
                .compose(SchedulerUtils.ioToMain())
        }else{
            //取消收藏
            RetrofitManager.service.cancelCollectGameWiki(wiki_id)
                .compose(SchedulerUtils.ioToMain())
        }
    }



    fun doFollow(user_id:String,is_follow:Int):  Observable<BaseBean>  {
        return if(is_follow == 0){
            //关注
            RetrofitManager.service.doFollow(user_id)
                .compose(SchedulerUtils.ioToMain())
        }else{
            //取消关注
            RetrofitManager.service.cancelFans(user_id)
                .compose(SchedulerUtils.ioToMain())
        }
    }


    fun getGameDetailContentData(game_id: String,page:Int):  Observable<GameContentListBean>  {
        return RetrofitManager.service.getGameDetailContentData(game_id,page)
            .compose(SchedulerUtils.ioToMain())
    }

    fun onGameFollow(game_id: String, is_follow: Int):  Observable<BaseBean> {
        return RetrofitManager.service.GameFollow(game_id,is_follow.toString())
            .compose(SchedulerUtils.ioToMain())
    }

    fun addApplyAdmin(game_id: String, about_game: String,
                      connect_info: String, has_experience: String,
                      play_game_time: String,week_time: String,
                      the_age: String,office_hours: String,
                      other_game: String, other_game_name: String):  Observable<BaseBean> {
        return RetrofitManager.service.addApplyAdmin(game_id,about_game,connect_info,has_experience,play_game_time,week_time,the_age,office_hours,other_game,other_game_name)
            .compose(SchedulerUtils.ioToMain())
    }



    fun bannerClick(banner_id: String):  Observable<BaseBean> {
        return RetrofitManager.service.bannerClick(banner_id)
            .compose(SchedulerUtils.ioToMain())
    }


    fun getTemContentInfo(game_id: String):  Observable<WikiDitailList>  {
        return RetrofitManager.serviceGame.getTemContentInfo(game_id)
            .compose(SchedulerUtils.ioToMain())
    }

    fun addAppointment(game_id: String):  Observable<BaseBean>  {
        return RetrofitManager.service.addAppointment(game_id)
            .compose(SchedulerUtils.ioToMain())
    }
}