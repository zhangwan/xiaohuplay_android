package com.tinytiger.titi.mvp.contract.game


import com.tinytiger.common.net.data.home.RecommendBean
import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.ad.AdListBean

import java.util.ArrayList

/**
 *
 * @author zhw_luke
 * @date 2019/10/22 0022 下午 2:12
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc
 */
interface HomeGameContract {

    interface View : IBaseView {

        fun getAdList(itemInfo: AdListBean)

        fun getRecommendList(itemInfo: ArrayList<RecommendBean>)

    }

    interface Presenter : IPresenter<View> {

        /**
         *banner
         */
        fun loadAdList()

        fun loadRecommendContentList()
    }


}