package com.tinytiger.titi.mvp.model.game


import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.home.CategoryListBean
import com.tinytiger.common.net.data.home.GameListBean
import com.tinytiger.common.net.scheduler.SchedulerUtils
import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.gametools.category.GameCategoryBannerBean
import com.tinytiger.common.net.data.gametools.category.GameCategoryDetailListBean
import com.tinytiger.common.net.data.gametools.category.GameCategoryListBean

import io.reactivex.Observable


class GameCategoryModel : BaseModel() {


    fun getGameCategory():Observable<GameCategoryListBean>{
        return RetrofitManager.serviceGame.getGameCategory()
            .compose(SchedulerUtils.ioToMain())
    }


    fun getGameCategoryDetailList(cate_id:Int,type:Int,page:Int):Observable<GameCategoryDetailListBean>{
        return RetrofitManager.serviceGame.getGameCategoryDetailList(cate_id,type,page)
            .compose(SchedulerUtils.ioToMain())
    }


    fun getGameCategoryBanner(cate_id:Int):Observable<GameCategoryBannerBean>{
        return RetrofitManager.serviceGame.getGameCategoryBanner(cate_id)
            .compose(SchedulerUtils.ioToMain())
    }



}