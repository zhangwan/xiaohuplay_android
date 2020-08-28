package com.tinytiger.titi.mvp.model.gametools

import com.orhanobut.logger.Logger
import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.gametools.*
import com.tinytiger.common.net.data.gametools.wiki.WikiDitailList
import com.tinytiger.common.net.data.gametools.wiki.WikiModularList
import com.tinytiger.common.net.data.gametools.wiki.WikiModularOtherList
import com.tinytiger.common.net.data.gametools.wiki.WikiStatusList
import com.tinytiger.common.net.scheduler.SchedulerUtils
import io.reactivex.Observable

class  GameWikiModel : BaseModel(){


    fun modularInfo(game_id: String): Observable<WikiModularList> {
        return RetrofitManager.service.modularInfo(game_id)
            .compose(SchedulerUtils.ioToMain())
    }
    fun otherModularInfo(submod_id: String): Observable<WikiModularOtherList> {
        return RetrofitManager.service.otherModularInfo(submod_id)
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


    fun collectWiki(wiki_id:String,is_collect:Int) :  Observable<BaseBean>{
        if (is_collect==1){
            return RetrofitManager.serviceGame.cancelCollectTemContent("[$wiki_id]")
                .compose(SchedulerUtils.ioToMain())
        }else{
            return RetrofitManager.serviceGame.collectTemContent(wiki_id)
                .compose(SchedulerUtils.ioToMain())
        }
    }
    fun getWikiStatus(game_id: String,page:Int):  Observable<WikiStatusList>  {
        return RetrofitManager.serviceGame.getWikiStatus(game_id,page)
            .compose(SchedulerUtils.ioToMain())
    }


    fun openWiki(game_id: String):  Observable<BaseBean>  {
        return RetrofitManager.serviceGame.openWiki(game_id)
            .compose(SchedulerUtils.ioToMain())
    }
}