package com.tinytiger.titi.mvp.contract.game



import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.home.HeadLineListBean

/**
 *
 * @author zhw_luke
 * @date 2019/10/22 0022 下午 2:12
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc
 */
interface GameHeadContract {

    interface View : IBaseView {
        fun getHeadLineList(itemInfo: HeadLineListBean)

        fun getUserFollow(position:Int ,uid:String,is_mutual:Int)

        fun getUserCollect(position:Int ,is_collect:Int)
    }

    interface Presenter : IPresenter<View> {

        fun loadHeadLineList(page:Int)
    }


}