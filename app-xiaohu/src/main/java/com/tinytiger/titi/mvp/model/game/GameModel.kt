package com.tinytiger.titi.mvp.model.game


import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.home.CategoryListBean
import com.tinytiger.common.net.data.home.GameListBean
import com.tinytiger.common.net.scheduler.SchedulerUtils
import com.tinytiger.common.base.BaseModel

import io.reactivex.Observable


class GameModel : BaseModel() {


    fun getCategoryList():Observable<CategoryListBean>{
        return RetrofitManager.service.getCategoryList()
            .compose(SchedulerUtils.ioToMain())
    }


    fun getGameContentList(category_id:Int):Observable<GameListBean>{
        return RetrofitManager.service.getGameContentList(category_id)
            .compose(SchedulerUtils.ioToMain())
    }
}