package com.tinytiger.titi.mvp.contract.gametools

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.gametools.category.GameCategoryBannerBean
import com.tinytiger.common.net.data.gametools.category.GameCategoryBean
import com.tinytiger.common.net.data.gametools.category.GameCategoryDetailListBean
import com.tinytiger.common.net.data.gametools.category.GameCategoryListBean


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description:
*/
interface  GameCategoryContract{
    interface View : IBaseView {

        fun showGameCategory(data: GameCategoryListBean)

        fun showGameCategoryDetailList(data: GameCategoryDetailListBean.DataBean)

        fun showGameCategoryBanner(data: ArrayList<GameCategoryBannerBean.DataBean>)
    }

    interface Presenter : IPresenter<View> {
        fun getGameCategory()


        fun getGameCategoryDetailList(cate_id:Int,type:Int,page:Int)

        fun getGameCategoryBanner(cate_id:Int)
    }
}