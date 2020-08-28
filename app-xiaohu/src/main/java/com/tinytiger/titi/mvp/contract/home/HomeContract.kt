package com.tinytiger.titi.mvp.contract.home


import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.net.data.search.SearchAllBean
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.ad.AdListBean
import com.tinytiger.common.net.data.home2.AmwayWallListBean
import com.tinytiger.common.net.data.home2.PushIndexBean


/**
 *
 * @author zhw_luke
 * @date 2019/10/22 0022 下午 2:12
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc
 */
interface HomeContract {

    interface View : IBaseView {

        fun getAdList(itemInfo: AdListBean)

        fun loadAmwayWall(bean: AmwayWallListBean)

        fun loadPushIndex(bean: PushIndexBean)
    }

    interface Presenter : IPresenter<View> {

        /**
         *所有搜索数据
         */
        fun loadAllInfo(keyWords:String)

    }


}