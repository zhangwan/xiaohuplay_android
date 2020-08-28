package com.tinytiger.titi.mvp.contract.gametools

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.home2.AmwayWallListBean
import com.tinytiger.common.net.data.home2.ContentListBean
import com.tinytiger.common.net.data.home2.GameCategoryBean
import com.tinytiger.common.net.data.home2.GameListBean
import java.util.ArrayList


/**
 *
 * @author zhw_luke
 * @date 2020/2/28 0028 下午 2:18
 * @Copyright 小虎互联科技
 * @since 2.0.0
 * @doc
 */
interface GameHomeContract {
    interface View : IBaseView {
        fun loadCategoryList(bean: GameCategoryBean)
        fun loadContentList(bean: ContentListBean)

        fun getGameList(bean: GameListBean, type:Int)

        fun loadAmwayWall(bean: AmwayWallListBean)

        fun getUserFollow(uid:String, is_mutual:Int)
    }

    interface Presenter : IPresenter<View> {
        fun getUserViewCategoryList()
    }


}