package com.tinytiger.titi.mvp.contract.mine

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.user.FriendListBean


/**
 *
 * @author zhw_luke
 * @date 2019/11/23 0023 上午 10:55
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc
 */
interface FriendContract {
    interface View : IBaseView {

        fun getUserFollow(position:Int ,uid:String,is_mutual:Int)

        fun getFriendList(bean: FriendListBean)
    }

    interface Presenter : IPresenter<View> {

        fun indexFollow(page: Int,keyWords:String)

        fun indexFans(page: Int,keyWords:String)
    }
}