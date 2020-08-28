package com.tinytiger.titi.mvp.contract.center

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.center.*
import com.tinytiger.common.net.data.user.UserDynamicBean
import com.tinytiger.common.net.data.user.UserDynamicList


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description:
*/
interface HomeDynamicContract {
    interface View : IBaseView {

         fun showHomeDynamicList(bean: UserDynamicList)
         fun showHomeGameList(bean: MineGameListBean.Data)
    }

    interface Presenter : IPresenter<View> {
        fun getDynamic(user_id:String,page:Int)

        fun getGame(user_id:String,page:Int)

    }
}