package com.tinytiger.titi.mvp.contract.game



import com.tinytiger.common.net.data.home.CategoryListBean
import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.home.GameListBean

/**
 *
 * @author zhw_luke
 * @date 2019/10/22 0022 下午 2:12
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc
 */
interface GameContract {

    interface View : IBaseView {

        fun getCategoryList(itemInfo: CategoryListBean)

        fun getGameContentList(itemInfo: GameListBean)
    }

    interface Presenter : IPresenter<View> {

        fun loadCategoryList()
        fun loadGameContentList(category_id:Int)
    }


}