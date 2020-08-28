package com.tinytiger.titi.mvp.contract.circle

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.center.*
import com.tinytiger.common.net.data.circle.CircleInfoBean
import com.tinytiger.common.net.data.circle.CircleMeBean
import com.tinytiger.common.net.data.circle.CircleRecommendedBean
import com.tinytiger.common.net.data.circle.PostInfoBean


/**
 *
 * @author zhw_luke
 * @date 2020/5/15 0015 下午 5:11
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc
 */
interface CirclesInfoContract {
    interface View : IBaseView {

        fun showGameCircleInfo(bean: CircleInfoBean)
        fun showCircleList(bean: PostInfoBean)
        fun showJoin(join: Int)
    }

    interface Presenter : IPresenter<View> {
        fun getGameCircleInfo(game_id: String,circle_id:String)

    }
}