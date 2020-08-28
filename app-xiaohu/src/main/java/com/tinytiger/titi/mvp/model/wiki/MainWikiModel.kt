package com.tinytiger.titi.mvp.model.wiki

import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.wiki.MainWikiListBean
import com.tinytiger.common.net.data.wiki.WikiSearchListBean
import com.tinytiger.common.net.scheduler.SchedulerUtils
import io.reactivex.Observable

class  MainWikiModel : BaseModel(){
    fun getGameWiki(): Observable<MainWikiListBean> {
        return RetrofitManager.serviceGame.getGameWiki()
            .compose(SchedulerUtils.ioToMain())
    }


    fun searchWiki(keyword:String,game_id:String,page:Int): Observable<WikiSearchListBean> {
        return RetrofitManager.serviceGame.searchGameWiki(keyword,game_id,page)
            .compose(SchedulerUtils.ioToMain())
    }





}