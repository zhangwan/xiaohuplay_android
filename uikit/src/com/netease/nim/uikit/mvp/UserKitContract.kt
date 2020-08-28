package com.netease.nim.uikit.mvp


import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.msg.UserKitBean


/**
 *
 * @author zhw_luke
 * @date 2019/10/22 0022 下午 2:12
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc
 */
interface UserKitContract {

    interface View : IBaseView {
        fun getBlack(type:Boolean)

        fun getUserRelation(bean: UserKitBean)
    }

    interface Presenter : IPresenter<View> {

        /**
         *
         */
        fun loadCancelBlack(user_id:String)
        fun loadAddBlack(user_id:String)
    }


}